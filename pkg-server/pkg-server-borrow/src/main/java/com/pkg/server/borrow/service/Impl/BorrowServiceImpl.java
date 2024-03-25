package com.pkg.server.borrow.service.Impl;


import com.pkg.Book;
import com.pkg.Borrow;
import com.pkg.User;
import com.pkg.server.borrow.entity.UserBorrowDetail;
import com.pkg.server.borrow.mapper.BorrowMapper;
import com.pkg.server.borrow.service.BorrowService;
import com.pkg.server.borrow.service.client.BookClient;
import com.pkg.server.borrow.service.client.UserClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: qxd
 * @date: 2024/2/19
 * @description:
 */
@Service
public class BorrowServiceImpl implements BorrowService {

    @Resource
    BookClient bookClient;

    @Resource
    UserClient userClient;


    @Resource
    BorrowMapper mapper;

    @LoadBalanced
    @Override
    public UserBorrowDetail getUserBorrowDetailByUid(int uid) {
        List<Borrow> borrow = mapper.getBorrowsByUid(uid);
//        //RestTemplate支持多种方式的远程调用
//        RestTemplate template = new RestTemplate();
//        //这里通过调用getForObject来请求其他服务，并将结果自动进行封装
//        //获取User信息
//        User user = template.getForObject("http://localhost:8301/user/"+uid, User.class);

        //使用OpenFeign
        User user = userClient.getUserById(uid);

        //获取每一本书的详细信息
        List<Book> bookList = borrow
                .stream()
                .map(b -> bookClient.findBookById(b.getBid()))
                .collect(Collectors.toList());
        return new UserBorrowDetail(user, bookList);
    }

    @GlobalTransactional
    @Override
    public boolean doBorrow(int uid, int bid) {
        //1. 判断图书和用户是否都支持借阅
        if(bookClient.bookRemain(bid) < 1)
            throw new RuntimeException("图书数量不足");
        if(userClient.userRemain(uid) < 1)
            throw new RuntimeException("用户借阅量不足");
        //2. 首先将图书的数量-1
        if(!bookClient.bookBorrow(bid))
            throw new RuntimeException("在借阅图书时出现错误！");
        //3. 添加借阅信息
        if(mapper.getBorrow(uid, bid) != null)
            throw new RuntimeException("此书籍已经被此用户借阅了！");
        if(mapper.addBorrow(uid, bid) <= 0)
            throw new RuntimeException("在录入借阅信息时出现错误！");
        //4. 用户可借阅-1
        if(!userClient.userBorrow(uid))
            throw new RuntimeException("在借阅时出现错误！");
        //完成
        return true;
    }
}

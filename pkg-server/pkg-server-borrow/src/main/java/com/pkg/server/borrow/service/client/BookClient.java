package com.pkg.server.borrow.service.client;


import com.pkg.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: qxd
 * @date: 2024/2/20
 * @description:
 */
@FeignClient(value = "book-service",url = "localhost:8080")   //声明为userservice服务的HTTP请求客户端
public interface BookClient {
    @RequestMapping("/book/{bid}")
    Book findBookById(@PathVariable("bid") int bid);

    @RequestMapping("/book/borrow/{bid}")
    boolean bookBorrow(@PathVariable("bid") int bid);

    @RequestMapping("/book/remain/{bid}")
    int bookRemain(@PathVariable("bid") int bid);
}

package com.pkg.server.user.controller;


import com.pkg.User;
import com.pkg.server.user.service.UserService;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


/**
 * @author: qxd
 * @date: 2024/2/19
 * @description:
 */


//实现Nacos热更新
@RefreshScope
@RestController
public class UserController {

    @Resource
    UserService service;
// 从配置中心拿数据
//    @Value("${time.year}")
//    String year;

    //这里以RESTFul风格为例
    @RequestMapping("/user/{uid}")
    public User findUserById(@PathVariable("uid") int uid){
//        System.out.println("接口测试" + year);
        return service.getUserById(uid);
    }

    @RequestMapping("/user/remain/{uid}")
    public int userRemain(@PathVariable("uid") int uid){
        return service.getRemain(uid);
    }

    @RequestMapping("/user/borrow/{uid}")
    public boolean userBorrow(@PathVariable("uid") int uid){
        int remain = service.getRemain(uid);
        return service.setRemain(uid, remain - 1);
    }
}

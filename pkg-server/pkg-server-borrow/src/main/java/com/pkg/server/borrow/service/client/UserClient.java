package com.pkg.server.borrow.service.client;


import com.pkg.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: qxd
 * @date: 2024/2/20
 * @description:
 */
//fallback参数指定为我们刚刚编写的实现类
@FeignClient(value = "user-service",url = "localhost:8080",fallback = UserFallbackClient.class)//声明为userservice服务的HTTP请求客户端
public interface UserClient {
    //路径保证和其他微服务提供的一致即可
    @RequestMapping("/user/{uid}")
    User getUserById(@PathVariable("uid") int uid);  //参数和返回值也保持一致

    @RequestMapping("/user/borrow/{uid}")
    boolean userBorrow(@PathVariable("uid") int uid);

    @RequestMapping("/user/remain/{uid}")
    int userRemain(@PathVariable("uid") int uid);
}

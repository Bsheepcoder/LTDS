package com.pkg.server.borrow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @author: qxd
 * @date: 2024/2/19
 * @description:
 */
@EnableResourceServer
@EnableFeignClients
@SpringBootApplication
@MapperScan("com.pkg.server.borrow.mapper")
public class BorrowApplication {
    public static void main(String[] args) {
        SpringApplication.run(BorrowApplication.class,args);
    }
}

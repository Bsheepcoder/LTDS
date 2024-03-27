package com.pkg.service;


import com.pkg.entity.User;

/**
 * @author: qxd
 * @date: 2024/2/19
 * @description:
 */
public interface UserService {
    User getUserById(int uid);

    int getRemain(int uid);

    boolean setRemain(int uid, int count);
}
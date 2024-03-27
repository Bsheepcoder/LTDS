package com.pkg.service;


import com.pkg.entity.UserBorrowDetail;

/**
 * @author: qxd
 * @date: 2024/2/19
 * @description:
 */
public interface BorrowService {

    UserBorrowDetail getUserBorrowDetailByUid(int uid);

    boolean doBorrow(int uid, int bid);
}
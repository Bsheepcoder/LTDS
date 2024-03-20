package com.pkg.server.borrow.service;


import com.pkg.server.borrow.entity.UserBorrowDetail;

/**
 * @author: qxd
 * @date: 2024/2/19
 * @description:
 */
public interface BorrowService {

    UserBorrowDetail getUserBorrowDetailByUid(int uid);

    boolean doBorrow(int uid, int bid);
}

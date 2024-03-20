package com.pkg.server.borrow.entity;


import com.pkg.Book;
import com.pkg.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author: qxd
 * @date: 2024/2/19
 * @description:
 */
@Data
@AllArgsConstructor
public class UserBorrowDetail {
    User user;
    List<Book> bookList;
}

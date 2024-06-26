package com.pkg.service;


import com.pkg.entity.Book;

/**
 * @author: qxd
 * @date: 2024/2/19
 * @description:
 */
public interface BookService {
    Book getBookById(int bid);

    boolean setRemain(int bid, int count);

    int getRemain(int bid);
}
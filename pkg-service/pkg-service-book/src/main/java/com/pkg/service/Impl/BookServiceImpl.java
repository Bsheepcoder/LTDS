package com.pkg.service.Impl;


import com.pkg.entity.Book;
import com.pkg.mapper.BookMapper;
import com.pkg.service.BookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: qxd
 * @date: 2024/2/19
 * @description:
 */
@Service
public class BookServiceImpl implements BookService {

    @Resource
    BookMapper mapper;

    @Override
    public Book getBookById(int bid) {
        return mapper.getBookById(bid);
    }

    @Override
    public boolean setRemain(int bid, int count) {
        return mapper.setRemain(bid, count) > 0;
    }

    @Override
    public int getRemain(int bid) {
        return mapper.getRemain(bid);
    }
}
package com.liu.service;

import com.liu.dao.BookDao;
import com.liu.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookDao bookDao;

    public List<Book> findAll() {
        return bookDao.queryBooks();
    }
}

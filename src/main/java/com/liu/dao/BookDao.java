package com.liu.dao;

import com.liu.domain.Book;

import java.util.List;

public interface BookDao {

    List<Book> queryBooks();
}

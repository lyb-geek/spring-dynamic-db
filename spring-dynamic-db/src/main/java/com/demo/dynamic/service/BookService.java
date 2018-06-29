package com.demo.dynamic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.dynamic.helpper.DataSource;
import com.demo.dynamic.mapper.BookMapper;
import com.demo.dynamic.model.Book;

@Service
@Transactional
public class BookService {

	@Autowired
	private BookMapper bookMapper;

	@DataSource("master")
	public Book getBookById(Long id) {
		return bookMapper.selectByPrimaryKey(id);
	}

}

package model.dao;

import java.util.List;

import model.entities.Book;

public interface BookDao {

	void insert(Book obj);
	void update(Book obj);
	void deleteById(String string);
	Book findById(String id);
	List<Book> findAll();
	
}

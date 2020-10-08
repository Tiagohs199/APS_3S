package model.dao;

import java.util.List;

import model.entities.AuthorBook;

public interface AuthorBookDao {

	void insert(AuthorBook obj);
	void update(AuthorBook obj);
	void deleteById(Integer id);
	AuthorBook findById(Integer id);
	List<AuthorBook> findAll();
	
}

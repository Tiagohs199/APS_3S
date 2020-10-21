package model.dao;

import java.util.List;

import model.entities.Publisher;

public interface PublisherDao {

	void insert(Publisher obj);
	void update(Publisher obj);
	void deleteById(Integer id);
	Publisher findById(Integer id);
	List<Publisher> findAll();
	
}

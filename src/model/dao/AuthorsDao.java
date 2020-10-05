package model.dao;

import java.util.List;

import model.entities.Authors;

public interface AuthorsDao {

	void insert(Authors obj);
	void update(Authors obj);
	void deleteById(Integer id);
	Authors findById(Integer id);
	List<Authors> findAll();
}

package model.services;

import java.util.List;

import model.dao.AuthorsDao;
import model.dao.DaoFactory;
import model.entities.Authors;

public class AuthorsService {
	
	private AuthorsDao dao = DaoFactory.createAuthorDao();
	
	public List<Authors> findAll(){
		return  dao.findAll();
	}
	
	public void saveOrUpdate(Authors obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Authors obj) {
		dao.deleteById(obj.getId());
	}
}

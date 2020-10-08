package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.AuthorBookDao;
import model.entities.AuthorBook;

public class AuthorBookService {
	
	private AuthorBookDao dao = DaoFactory.createAuthorBookDao();

	public List<AuthorBook> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(AuthorBook obj) {
		if (obj.getSeq_no() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(AuthorBook obj) {
		dao.deleteById(obj.getSeq_no());
	}
}

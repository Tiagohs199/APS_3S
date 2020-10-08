package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.BookDao;
import model.entities.Book;

public class BookService {
	
	private BookDao dao = DaoFactory.createBookDao();

	public List<Book> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Book obj) {
		if (obj.getIsbn() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Book obj) {
		dao.deleteById(obj.getIsbn());
	}
}

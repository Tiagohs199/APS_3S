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
		
		System.out.println(obj);
		if (obj.getIsbn() == null) {
			System.out.println("inserindo");
			dao.insert(obj);
			
		}
		else {
			System.out.println("atualizando");
			dao.update(obj);
			
		}
	}
	
	public void remove(Book obj) {
		dao.deleteById(obj.getIsbn());
	}
}

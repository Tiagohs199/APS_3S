package model.dao;

import db.DB;
import model.dao.impl.AuthorBookDaoJDBC;
import model.dao.impl.AuthorsDaoJDBC;
import model.dao.impl.BookDaoJDBC;
import model.dao.impl.PublisherDaoJDBC;

public class DaoFactory {

	
	public static AuthorsDao createAuthorDao() {
		return new AuthorsDaoJDBC(DB.getConnection());
	}
	
	public static PublisherDao createPublisherDao() {
		return new PublisherDaoJDBC(DB.getConnection());
	}
	public static BookDao createBookDao() {
		return new BookDaoJDBC(DB.getConnection());
	}
	public static AuthorBookDao createAuthorBookDao() {
		return new AuthorBookDaoJDBC(DB.getConnection());
	}
}


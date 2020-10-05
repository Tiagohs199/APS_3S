package model.dao;

import db.DB;
import model.dao.impl.AuthorsDaoJDBC;
import model.dao.impl.PublisherDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	public static AuthorsDao createAuthorDao() {
		return new AuthorsDaoJDBC(DB.getConnection());
	}
	
	public static PublisherDao createPublisherDao() {
		return new PublisherDaoJDBC(DB.getConnection());
	}
}

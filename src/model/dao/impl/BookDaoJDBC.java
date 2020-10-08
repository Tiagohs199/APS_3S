package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.BookDao;
import model.entities.Book;
import model.entities.Publisher;

public class BookDaoJDBC implements BookDao {

	private Connection conn;
	
	public BookDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	@Override
	public Book findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT  * FROM Books "
					+ "WHERE isbn = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Book obj = new Book();
				obj.setTitle(rs.getString("title"));
				obj.setIsbn(rs.getString("isbn"));
				obj.setPrice(rs.getDouble("price"));
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public List<Book> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					" SELECT Books.*, publishers.name " + 
					" from Books inner join publishers " + 
					" on Books.publisher_id = publishers.publisher_id " + 
					"ORDER BY title");
			rs = st.executeQuery();
			
			List<Book> list = new ArrayList<>();
			
			
			while (rs.next()) {
				Book obj = new Book();
				Publisher pbs = new Publisher(); 
				obj.setTitle(rs.getString("title"));
				obj.setIsbn(rs.getString("isbn"));
				pbs.setId(rs.getInt("publisher_id"));
				pbs.setName(rs.getString("name"));
				obj.setPublisher(pbs);
				obj.setPrice(rs.getDouble("price"));
				list.add(obj);
				System.out.println(pbs.getId()+"-=="+obj.getPublisher());
			}
			
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	@Override
	public void insert(Book obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO publishers "
					+ "(title, price ) "
					+ "VALUES "
					+ "(?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getTitle());
			st.setDouble(2, obj.getPrice());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					String id = rs.getString(1);
					obj.setIsbn(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Book obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE Books "
					+ "SET title = ?, price = ? "
					+ "WHERE publishers_id = ?");
			
			st.setString(1, obj.getTitle());
			st.setDouble(2, obj.getPrice());
		
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(String id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM Books WHERE isbn = ?");
			
			st.setString(1, id);
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}
}

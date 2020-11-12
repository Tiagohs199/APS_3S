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
	public Book findById(String id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT  * FROM Books "
					+ "WHERE isbn = ?");
			
			st.setString(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Publisher publisher = instantiatePublisher(rs);
				Book obj = instantiateBook(rs, publisher);
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
				
				Publisher publisher = instantiatePublisher(rs); 
				Book obj = instantiateBook(rs, publisher);
				list.add(obj);
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
					" INSERT INTO books "
					+ " (title, isbn, publisher_id , price) "
					+ " VALUES "
					+ " (?, ?, ?, ?) ",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getTitle());
			st.setString(2, obj.getIsbn());
			st.setDouble(4, obj.getPrice());
			st.setObject(3, obj.getPublisher().getId());

					
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					String id = rs.getString(1);
					obj.setIsbn(id);
				}
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
					+ "SET title = ?, publisher_id = ?,  price = ?"
					+ "WHERE isbn = ? ");
			
			st.setString(1, obj.getTitle());
			st.setDouble(3, obj.getPrice());
			st.setObject(2, obj.getPublisher().getId());
			st.setString(4, obj.getIsbn());
			
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
	
	private Publisher instantiatePublisher(ResultSet rs) throws SQLException {
		Publisher publisher = new Publisher();
		publisher.setId(rs.getInt("publisher_id"));
		
		return publisher;
	}
	private Book instantiateBook(ResultSet rs, Publisher publisher) throws SQLException {
		Book book = new Book();
		book.setTitle(rs.getString("title"));
		book.setIsbn(rs.getString("isbn"));
		book.setPublisher(publisher);
		book.setPrice(rs.getDouble("price"));
		
		return book;
	}
}

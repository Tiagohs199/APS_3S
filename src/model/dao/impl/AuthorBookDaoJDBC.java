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
import model.dao.AuthorBookDao;
import model.entities.AuthorBook;
import model.entities.Authors;
import model.entities.Book;

public class AuthorBookDaoJDBC implements AuthorBookDao {

	private Connection conn;
	
	public AuthorBookDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	@Override
	public AuthorBook findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM BooksAuthors  "
					+ "WHERE seq_no = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				
				Book book = instantiateBook(rs);
				Authors author = instantiateAuthors(rs);
				AuthorBook obj = instantiateAuthorBook(rs, author, book);
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
	public List<AuthorBook> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM BooksAuthors "
					);
			rs = st.executeQuery();
			
			List<AuthorBook> list = new ArrayList<>();
			
			
			while (rs.next()) {
				
				Book book = instantiateBook(rs);
				Authors author = instantiateAuthors(rs);
				AuthorBook obj = instantiateAuthorBook(rs, author, book);
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
	public void insert(AuthorBook obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO BooksAuthors "
					+ "(seq_no) "
					+ "VALUES "
					+ "(?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, obj.getSeq_no());
	
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setSeq_no(id);
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
	public void update(AuthorBook obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE BooksAuthors "
					+ "SET seq_no = ? "
					);
			
			st.setInt(1, obj.getSeq_no());
		
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
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM BooksAuthors WHERE seq_no = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}
	private AuthorBook instantiateAuthorBook(ResultSet rs, Authors authors, Book book) throws SQLException {
		AuthorBook obj = new AuthorBook();
		obj.setSeq_no(rs.getInt("seq_no"));
		obj.setAuthor_id(authors);
		obj.setIsbn(book);
		
		return obj;
	}

	private Authors instantiateAuthors(ResultSet rs) throws SQLException {
		Authors authors = new Authors();
		authors.setId(rs.getInt("author_id"));
//		authors.setName(rs.getString("name"));
//		authors.setFname(rs.getString("fname"));
		
		return authors;
	}
	private Book instantiateBook(ResultSet rs) throws SQLException {
		Book book = new Book();
		book.setIsbn(rs.getString("isbn"));
		
		
		return book;
	}
}

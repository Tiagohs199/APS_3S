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
import db.DbIntegrityException;
import model.dao.AuthorsDao;
import model.entities.Authors;
import model.entities.Book;

public class AuthorsDaoJDBC implements AuthorsDao {

	private Connection conn;
	
	public AuthorsDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Authors findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM Authors "
				+ "WHERE author_id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Authors obj = instantiateAuthors(rs);
				
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
	public List<Authors> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM authors ORDER BY name");
			rs = st.executeQuery();

			List<Authors> list = new ArrayList<>();

			while (rs.next()) {
				Authors obj = instantiateAuthors(rs);
				
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
	public void insert(Authors obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO Authors " +
				"(name, fname) " +
				"VALUES " +
				"(?,?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getFname());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
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
	public void update(Authors obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE Authors " +
				"SET name = ?, fname = ?  "
				+ "WHERE author_id = ? ");

			st.setString(1, obj.getName());
			st.setString(2, obj.getFname());
			st.setInt(3, obj.getId());

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
			st = conn.prepareStatement(
				"DELETE FROM Authors WHERE author_id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}
	private Authors instantiateAuthors(ResultSet rs) throws SQLException {
		Authors authors = new Authors();
		authors.setId(rs.getInt("author_id"));
		authors.setName(rs.getString("name"));
		authors.setFname(rs.getString("fname"));
		
		return authors;
	}
}

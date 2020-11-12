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
import model.dao.PublisherDao;
import model.entities.Publisher;

public class PublisherDaoJDBC implements PublisherDao {

	private Connection conn;
	
	public PublisherDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	@Override
	public Publisher findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT  * FROM publishers "
					+ "WHERE publisher_id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Publisher obj = instantiatePublisher(rs);
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
	public List<Publisher> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM publishers "
					+ " ORDER BY name");
			rs = st.executeQuery();
			
			List<Publisher> list = new ArrayList<>();
			
			
			while (rs.next()) {
				Publisher obj = instantiatePublisher(rs);
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
	public void insert(Publisher obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO publishers "
					+ "(name, url ) "
					+ "VALUES "
					+ "(?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getUrl());
			
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
	public void update(Publisher obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE publishers "
					+ "SET name = ?, url = ? "
					+ "WHERE publisher_id = ?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getUrl());
			st.setInt(3,  obj.getId());
			
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
			st = conn.prepareStatement("DELETE FROM publishers WHERE publisher_id = ?");
			
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
	private Publisher instantiatePublisher(ResultSet rs) throws SQLException {
		Publisher publisher = new Publisher();
		publisher.setId(rs.getInt("publisher_id"));
		publisher.setName(rs.getString("name"));
		publisher.setUrl(rs.getString("url"));
		return publisher;
	}
}

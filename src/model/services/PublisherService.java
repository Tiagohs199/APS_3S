package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PublisherDao;
import model.entities.Publisher;

public class PublisherService {
	
	private PublisherDao dao = DaoFactory.createPublisherDao();

	public List<Publisher> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Publisher obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Publisher obj) {
		dao.deleteById(obj.getId());
	}
}

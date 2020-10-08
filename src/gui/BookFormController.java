package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Contraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Book;
import model.entities.Publisher;
import model.exceptions.ValidationException;
import model.services.BookService;
import model.services.PublisherService;

public class BookFormController implements Initializable{
	
	private Book entity;
	
	private BookService service;
	
	private Publisher entity2 = new Publisher();
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtTitle;
	@FXML
	private TextField txtIsbn;
	@FXML
	private TextField txtPublisher_id;
	@FXML
	private TextField txtPrice;
	@FXML
	private Label labelErrorName;

	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	
	public void setBook(Book entity) {
		this.entity = entity;
	}
	
	public void setBookService(BookService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
			
		
		}catch (ValidationException e) {
			setErrorMessage(e.getErrors());
		}
		catch( DbException e) {
			Alerts.showAlerts("Error saving object", null,e.getMessage() , AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChange();
		}
		
	}
	private Book getFormData() {
		Book obj = new Book();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setIsbn(txtIsbn.getText());
		if(txtIsbn.getText() == null || txtIsbn.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setTitle(txtTitle.getText());
		obj.setPrice(Double.valueOf(txtPrice.getText()));
		
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	private void initializeNodes() {
		Contraints.setTextFielsMaxLength(txtTitle, 20);
		Contraints.setTextFieldDouble(txtPrice);
		Contraints.setTextFielsMaxLength(txtIsbn, 30);
		Contraints.setTextFieldInteger(txtPublisher_id);
		
	}
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtTitle.setText(entity.getTitle());
		txtIsbn.setText(entity.getIsbn());
		txtPublisher_id.setText(String.valueOf(entity2.getId()));
		txtPrice.setText(String.valueOf(entity.getPrice()));
	
	}
	private void setErrorMessage(Map<String, String> error) {
		Set<String> fields = error.keySet();
		
		if (fields.contains("name")) {
			labelErrorName.setText(error.get("name"));
		}
	}
}

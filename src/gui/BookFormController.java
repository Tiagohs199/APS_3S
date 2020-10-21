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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.dao.impl.BookDaoJDBC;
import model.entities.Book;
import model.entities.Publisher;
import model.exceptions.ValidationException;
import model.services.BookService;

public class BookFormController implements Initializable{
	
	private Book entity;
	
	private BookService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtTitle;
	@FXML
	private TextField txtIsbn;
//	@FXML
//	private TextField txtPublisher_id;
	@FXML
	private TextField txtPrice;
	@FXML
	private Label labelErrorTitle;
	@FXML
	private Label labelErrorIsbn;
	@FXML
	private Label labelErrorPrice;
	@FXML
	private ComboBox<Publisher> comboBoxPublisher;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	private ObservableList<Publisher> obsList;
	
	public void onComboBoxAction() {
		Publisher publi = comboBoxPublisher.getSelectionModel().getSelectedItem();
		System.out.println(publi);
	}
	
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
		Publisher publisher = new Publisher();
		ValidationException exception = new ValidationException("Validation error");
		
		if(txtTitle.getText() == null || txtTitle.getText().trim().equals("")) {
			exception.addError("title", "Field can't be empty");
		}
		if(txtIsbn.getText() == null || txtIsbn.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		if(txtPrice.getText() == null || txtPrice.getText().trim().equals("")) {
			exception.addError("price", "Field can't be empty");
		}
		obj.setTitle(txtTitle.getText());
		obj.setIsbn(txtIsbn.getText());
		//publisher.setId(Integer.valueOf(txtPublisher_id.getText()));
		obj.setPublisher(publisher);
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
		BookDaoJDBC dao = new BookDaoJDBC(null);
		
		Publisher pub = new Publisher();
		
		Contraints.setTextFielsMaxLength(txtTitle, 20);
		Contraints.setTextFieldDouble(txtPrice);
		Contraints.setTextFielsMaxLength(txtIsbn, 30);
		//Contraints.setTextFieldInteger(txtPublisher_id);
		
		List<Publisher> list = new ArrayList<>();
		
		while(dao.findById(pub.getId()) != null) {
		list.add(new Publisher() );
		}
		
		obsList = FXCollections.observableArrayList(list);
		comboBoxPublisher.setItems(obsList);
		
		Callback<ListView<Publisher>, ListCell<Publisher>> factory = lv -> new ListCell<Publisher>() {
			@Override
			protected void updateItem(Publisher item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxPublisher.setCellFactory(factory);
		comboBoxPublisher.setButtonCell(factory.call(null));
		
	}
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtTitle.setText(entity.getTitle());
		txtPrice.setText(String.valueOf(entity.getPrice()));
		txtIsbn.setText(entity.getIsbn());
		//txtPublisher_id.setText(String.valueOf(entity.getPublisher()));
	}
	private void setErrorMessage(Map<String, String> error) {
		Set<String> fields = error.keySet();
		
		if (fields.contains("title")) {
			labelErrorTitle.setText(error.get("title"));
		}
		if (fields.contains("name")) {
			labelErrorTitle.setText(error.get("name"));
		}
		if (fields.contains("price")) {
			labelErrorTitle.setText(error.get("price"));
		}
	}
	
}

//public class ViewController implements Initializable{
//	
//	@FXML
//	private ComboBox<Person> comboBoxPerson;
//	@FXML
//	private Button btAll;
//		
//	private ObservableList<Person> obsList;
//	
//	@FXML
//	public void onBtAllAction() {
//		for (Person person : comboBoxPerson.getItems()) {
//			System.out.println(person);
//		}
//	}
//	
//	
//	
//	public void onComboBoxPersonAction() {
//		
//		Person person = comboBoxPerson.getSelectionModel().getSelectedItem();
//		System.out.println(person);
//	}
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		List<Person> list = new ArrayList<>();
//		list.add(new Person(1,"Maria","maria@gmail.com"));
//		list.add(new Person(2,"Alex","alex@gmail.com"));
//		list.add(new Person(3,"Bob","bob@gmail.com"));
//		
//		obsList = FXCollections.observableArrayList(list);
//		comboBoxPerson.setItems(obsList);
//		
//		Callback<ListView<Person>, ListCell<Person>> factory = lv -> new ListCell<Person>() {
//			@Override
//			protected void updateItem(Person item, boolean empty) {
//				super.updateItem(item, empty);
//				setText(empty ? "" : item.getName());
//			}
//		};
//		comboBoxPerson.setCellFactory(factory);
//		comboBoxPerson.setButtonCell(factory.call(null));
//		
//		
//	}
	
	



































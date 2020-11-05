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
import model.entities.Book;
import model.entities.Publisher;
import model.exceptions.ValidationException;
import model.services.BookService;
import model.services.PublisherService;

public class BookFormController implements Initializable{
	
	private Book entity;
	
	private BookService service;
	
	private Publisher publi;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtTitle;
	@FXML
	private TextField txtIsbn;
	@FXML
	private TextField txtPrice;
	@FXML
	private Label labelErrorTitle;
	@FXML
	private Label labelErrorIsbn;
	@FXML
	private Label labelErrorPrice;
	@FXML
	private Label labelErrorComboBox;
	@FXML
	private ComboBox<Publisher> comboBoxPublisher;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	private ObservableList<Publisher> obsList;
	
	public void onComboBoxAction() {
		publi = comboBoxPublisher.getSelectionModel().getSelectedItem();
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
		Publisher pub = new Publisher();
		ValidationException exception = new ValidationException("Validation error");
		
		
		if(txtTitle.getText() == null || txtTitle.getText().trim().equals("")) {
			exception.addError("title", "Field can't be empty");
		}
		if(txtIsbn.getText() == null || txtIsbn.getText().trim().equals("")) {
			exception.addError("isbn", "Field can't be empty");
		}
		if(comboBoxPublisher.getSelectionModel().getSelectedItem() == null ){
			exception.addError("publisher", "Field can't be empty");
		}
		if(txtPrice.getText() == null || txtPrice.getText().trim().equals("")) {
			exception.addError("price", "Field can't be empty");
		}
		
		obj.setTitle(txtTitle.getText());
		obj.setIsbn((txtIsbn.getText()));
		pub = comboBoxPublisher.getSelectionModel().getSelectedItem();
		obj.setPublisher(pub);
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
		initializeCombo();
	}
	
	private void initializeNodes() {
	
		Contraints.setTextFielsMaxLength(txtTitle, 20);
		Contraints.setTextFielsMaxLength(txtIsbn, 13);
		Contraints.setTextFieldDouble(txtPrice);
		
	}
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtTitle.setText(entity.getTitle());
		txtIsbn.setText(entity.getIsbn());
		comboBoxPublisher.getSelectionModel().getSelectedItem();
		txtPrice.setText(String.valueOf(entity.getPrice()));
		
	}
	private void setErrorMessage(Map<String, String> error) {
		Set<String> fields = error.keySet();
		
		if (fields.contains("title")) {
			labelErrorTitle.setText(error.get("title"));
		}
		if (fields.contains("isbn")) {
			labelErrorIsbn.setText(error.get("isbn"));
		}
		if(fields.contains("publisher")) {
			labelErrorComboBox.setText(error.get("publisher"));
		}
		if (fields.contains("price")) {
			labelErrorPrice.setText(error.get("price"));
		}
	}
	
	public void initializeCombo() {
		
	PublisherService service = new PublisherService();
	
	List<Publisher> list = service.findAll();
	
	 //list.forEach(aluno -> System.out.println(aluno));
	
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
}

	
	



































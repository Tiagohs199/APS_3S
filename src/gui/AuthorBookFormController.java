//package gui;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.ResourceBundle;
//import java.util.Set;
//
//import db.DbException;
//import gui.listeners.DataChangeListener;
//import gui.util.Alerts;
//import gui.util.Contraints;
//import gui.util.Utils;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import model.entities.AuthorBook;
//import model.entities.Authors;
//import model.entities.Book;
//import model.exceptions.ValidationException;
//import model.services.AuthorBookService;
//
//public class AuthorBookFormController implements Initializable{
//	
//	private AuthorBook entity;
//	
//	private AuthorBookService service;
//	
//	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
//	
//	@FXML
//	private TextField txtseq_no;
//	@FXML
//	private TextField txtIsbn;
//	@FXML
//	private TextField txtAuthorId;
//	@FXML
//	private Label labelErrorName;
//
//	@FXML
//	private Button btSave;
//	@FXML
//	private Button btCancel;
//	
//	
//	public void setAuthorBook(AuthorBook entity) {
//		this.entity = entity;
//	}
//	
//	public void setAuthorBookService(AuthorBookService service) {
//		this.service = service;
//	}
//	
//	public void subscribeDataChangeListener(DataChangeListener listener) {
//		dataChangeListeners.add(listener);
//	}
//	
//	@FXML
//	public void onBtSaveAction(ActionEvent event) {
//		if(entity == null) {
//			throw new IllegalStateException("Entity was null");
//		}
//		if (service == null) {
//			throw new IllegalStateException("Service was null");
//		}
//		try {
//			entity = getFormData();
//			service.saveOrUpdate(entity);
//			notifyDataChangeListeners();
//			Utils.currentStage(event).close();
//			
//		
//		}catch (ValidationException e) {
//			setErrorMessage(e.getErrors());
//		}
//		catch( DbException e) {
//			Alerts.showAlerts("Error saving object", null,e.getMessage() , AlertType.ERROR);
//		}
//	}
//	
//	private void notifyDataChangeListeners() {
//		for (DataChangeListener listener : dataChangeListeners) {
//			listener.onDataChange();
//		}
//		
//	}
//	private AuthorBook getFormData() {
//		AuthorBook obj = new AuthorBook();
//		Authors author = new Authors();
//		
//		ValidationException exception = new ValidationException("Validation error");
//		
//		obj.setSeq_no(Utils.tryParseToInt(txtseq_no.getText()));
//		if(txtIsbn.getText() == null || txtIsbn.getText().trim().equals("")) {
//			exception.addError("name", "Field can't be empty");
//		}
//		obj.setSeq_no(Utils.tryParseToInt(txtIsbn.getText()));
//		author.setId(Utils.tryParseToInt(txtAuthorId.getText()));
//		obj.setAuthor_id(author);
//		
//		
//		if(exception.getErrors().size() > 0) {
//			throw exception;
//		}
//		
//		return obj;
//	}
//
//	@FXML
//	public void onBtCancelAction(ActionEvent event) {
//		Utils.currentStage(event).close();
//	}
//
//	@Override
//	public void initialize(URL url, ResourceBundle rb) {
//		initializeNodes();
//	}
//	private void initializeNodes() {
//		Contraints.setTextFieldInteger(txtseq_no);
//		Contraints.setTextFielsMaxLength(txtIsbn, 30);
//		Contraints.setTextFielsMaxLength(txtAuthorId, 20);
//		
//	}
//	public void updateFormData() {
//		if(entity == null) {
//			throw new IllegalStateException("Entity was null");
//		}
//		txtseq_no.setText(String.valueOf(entity.getSeq_no()));
//		txtIsbn.setText(String.valueOf(entity.getIsbn()));
//		txtAuthorId.setText(String.valueOf(entity.getAuthor_id()));
//	
//	}
//	private void setErrorMessage(Map<String, String> error) {
//		Set<String> fields = error.keySet();
//		
//		if (fields.contains("seq_no")) {
//			labelErrorName.setText(error.get("seq_no"));
//		}
//	}
//}

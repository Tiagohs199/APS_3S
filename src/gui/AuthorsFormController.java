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
import model.entities.Authors;
import model.exceptions.ValidationException;
import model.services.AuthorsService;

public class AuthorsFormController implements Initializable{
	
	private Authors entity;
	
	private AuthorsService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtFname;
	@FXML
	private Label labelErrorName;
	@FXML
	private Label labelErrorFname;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	
	
	public void setAuthors(Authors entity) {
		this.entity = entity;
	}
	
	public void setAuthorsService(AuthorsService service) {
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
	private Authors getFormData() {
		Authors obj = new Authors();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		if(txtFname.getText() == null || txtFname.getText().trim().equals("")) {
			exception.addError("fname", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		obj.setFname(txtFname.getText());
		
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
		Contraints.setTextFieldInteger(txtId);
		Contraints.setTextFielsMaxLength(txtName, 20);
		Contraints.setTextFielsMaxLength(txtFname, 20);
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtFname.setText(entity.getFname());
		
	}
	
	private void setErrorMessage(Map<String, String> error) {
		Set<String> fields = error.keySet();
		
		labelErrorName.setText(fields.contains("name") ? error.get("name") : "");
		
		labelErrorFname.setText(fields.contains("fname") ? error.get("fname") : "");
		
	}
}

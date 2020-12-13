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
import model.entities.Publisher;
import model.exceptions.ValidationException;
import model.services.PublisherService;

public class PublisherFormController implements Initializable{
	
	private Publisher entity;
	
	private PublisherService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtUrl;
	@FXML
	private Label labelErrorName;
	@FXML
	private Label labelErrorUrl;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	
	public void setPublisher(Publisher entity) {
		this.entity = entity;
	}
	
	public void setPublisherService(PublisherService service) {
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
	private Publisher getFormData() {
		Publisher obj = new Publisher();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		if(txtUrl.getText() == null || txtUrl.getText().trim().equals("")) {
			exception.addError("url", "Field cant't be empty");
		}
		obj.setName(txtName.getText());
		obj.setUrl(txtUrl.getText());
		
		
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
		Contraints.setTextFielsMaxLength(txtName, 30);
		Contraints.setTextFielsMaxLength(txtUrl, 20);
		
	}
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtUrl.setText(entity.getUrl());
	
	}
	private void setErrorMessage(Map<String, String> error) {
		Set<String> fields = error.keySet();
		
		labelErrorName.setText(fields.contains("name") ? error.get("name") : "");
	
		labelErrorUrl.setText(fields.contains("url") ? error.get("url") : "");
	}
}

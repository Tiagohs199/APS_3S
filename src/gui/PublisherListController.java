package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Publisher;
import model.services.PublisherService;

public class PublisherListController implements Initializable, DataChangeListener{

	private PublisherService service;
	@FXML
	private TableView<Publisher> tableViewPublisher;
	
	@FXML
	private TableColumn<Publisher, Integer> TableColumnId;
	@FXML
	private TableColumn<Publisher, String> TableColumnName;
	@FXML
	private TableColumn<Publisher, String> TableColumnUrl;
	
	@FXML
	private TableColumn<Publisher, Publisher> tableColumnEdit;
	@FXML
	private TableColumn<Publisher, Publisher> tableColumnRemove;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Publisher> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Publisher obj = new Publisher();
		createDialogForm(obj, "/gui/PublisherForm.fxml", parentStage);
	}
	public void setPublisherService(PublisherService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		TableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumnUrl.setCellValueFactory(new PropertyValueFactory<>("url"));
		
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewPublisher.prefHeightProperty().bind(stage.heightProperty());
		
	}

	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Publisher> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewPublisher.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	public void createDialogForm(Publisher obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			PublisherFormController controller = loader.getController();
			controller.setPublisher(obj);
			controller.setPublisherService(new PublisherService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Publisher data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
			
		}catch(IOException e) {
			Alerts.showAlerts("IO Exception", "Erro load View", e.getMessage(), AlertType.ERROR);
		}
	}
	
	public void onDataChange() {
		updateTableView();
		
	}
	
	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Publisher,Publisher>() {
			private final Button button = new Button("edit");
			
			@Override
			protected void updateItem(Publisher obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/PublisherForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Publisher, Publisher>(){
			private final Button button = new Button("remove");
			@Override
			protected void updateItem(Publisher obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}
	private void  removeEntity(Publisher obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete ? ");
		
		if (result.get()== ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
			service.remove(obj);
			updateTableView();
			}
			catch(DbIntegrityException e) {
				Alerts.showAlerts("error removing object", null, e.getMessage(), AlertType.ERROR);
				
			}
		}
	}
}

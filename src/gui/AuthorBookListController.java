package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
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
import model.entities.AuthorBook;
import model.services.AuthorBookService;

public class AuthorBookListController implements Initializable {

	private AuthorBookService service;
	@FXML
	private TableView<AuthorBook> tableViewAuthorBook;
	
	@FXML
	private TableColumn<AuthorBook, Integer> TableColumnSeq_no;
	@FXML
	private TableColumn<AuthorBook, String> TableColumnAuthor_id;
	@FXML
	private TableColumn<AuthorBook, String> TableColumnIsbn;
	@FXML
	private TableColumn<AuthorBook, AuthorBook> tableColumnEdit;
	
	@FXML
	private TableColumn<AuthorBook, AuthorBook> tableColumnRemove;
	
	@FXML
	private Button btExit;
	
	@FXML
	private Button btNew;
	
	private ObservableList<AuthorBook> obsList;
	
	@FXML
	public void onBtExitAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		AuthorBook obj = new AuthorBook();
		createDialogForm(obj, "/gui/AuthorBookForm.fxml", parentStage);
	}
	public void setAuthorBookService(AuthorBookService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeAuthorBookNodes();
	}
	
	private void initializeAuthorBookNodes() {
		TableColumnSeq_no.setCellValueFactory(new PropertyValueFactory<>("seq_no"));
		TableColumnAuthor_id.setCellValueFactory(new PropertyValueFactory<>("a_id"));
		TableColumnIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewAuthorBook.prefHeightProperty().bind(stage.heightProperty());
		
	}

	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<AuthorBook> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewAuthorBook.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	public void createDialogForm(AuthorBook obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			AuthorBookFormController controller = loader.getController();
			controller.setAuthorBook(obj);
			controller.setAuthorBookService(new AuthorBookService());
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter AuthorBook data");
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
		tableColumnEdit.setCellFactory(param -> new TableCell<AuthorBook,AuthorBook>() {
			private final Button button = new Button("edit");
			
			@Override
			protected void updateItem(AuthorBook obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/AuthorBookForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<AuthorBook, AuthorBook>(){
			private final Button button = new Button("remove");
			@Override
			protected void updateItem(AuthorBook obj, boolean empty) {
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
	private void  removeEntity(AuthorBook obj) {
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

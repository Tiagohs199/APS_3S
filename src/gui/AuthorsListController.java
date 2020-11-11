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
import model.entities.Authors;
import model.services.AuthorsService;

public class AuthorsListController implements Initializable, DataChangeListener{
	
	private AuthorsService service; // acoplamento forte
	@FXML
	private TableView<Authors> tableViewAuthors; // referencia para o table view
	@FXML
	private TableColumn<Authors, Integer> tableColumnId; // referencia para a coluna id
	@FXML
	private TableColumn<Authors, String> tableColumnName; // referencia para a coluna name
	@FXML
	private TableColumn<Authors, String> tableColumnfName; // referencia para a coluna fName
	@FXML
	private TableColumn<Authors, Authors> tableColumnEdit; // referencia para a coluna edit
	@FXML
	private TableColumn<Authors, Authors> tableColumnRemove; // referencia para a coluna remove
	
	@FXML
	private Button btNew;

	private ObservableList<Authors> obsList;
	
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Authors obj = new Authors();
		createDialogForm(obj,"/gui/AuthorsForm.fxml", parentStage);
	}
	public void setAuthorsService(AuthorsService Service) { //dependencia manual
		this.service = Service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnfName.setCellValueFactory(new PropertyValueFactory<>("fname"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewAuthors.prefHeightProperty().bind(stage.heightProperty()); // ajusta a table view a tela principal
		
	}
	public void updateTableView() { // metodo responsavel por acessar os dados e add na observable list
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Authors> list = service.findAll(); // carrega lista com dados do banco
		obsList = FXCollections.observableArrayList(list); // carrega o obslist com os dados da list
		tableViewAuthors.setItems(obsList); // chama o table view e carrega na tela
		initEditButtons();
		initRemoveButtons();
		
	}
	public void createDialogForm(Authors obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			AuthorsFormController controller = loader.getController();
			controller.setAuthors(obj);
			controller.setAuthorsService(new AuthorsService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Authors data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
			
		}catch(IOException e) {
			Alerts.showAlerts("IO Exception", "Erro load View", e.getMessage(), AlertType.ERROR);
		}
	}
	@Override
	public void onDataChange() {
		updateTableView();
		
	}
	
	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Authors, Authors>() {
			private final Button button = new Button("edit");
			
			@Override
			protected void updateItem(Authors obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/AuthorsForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Authors, Authors>(){
			private final Button button = new Button("remove");
			@Override
			protected void updateItem(Authors obj, boolean empty) {
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
	private void  removeEntity(Authors obj) {
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

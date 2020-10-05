package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable {

	private SellerService service;
	@FXML
	private TableView<Seller> TableViewSeller;
	
	@FXML
	private TableColumn<Seller, Integer> TableColumnId;
	@FXML
	private TableColumn<Seller, String> TableColumnName;
	@FXML
	private TableColumn<Seller, String> TableColumnEmail;
	@FXML
	private TableColumn<Seller, Date> TableColumnBirthDate;
	@FXML
	private TableColumn<Seller, Double> TableColumnBaseSalary;
	@FXML
	private TableColumn<Seller, Integer> TableColumnDepartmentId;
	
	@FXML
	private Button btExit;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Seller> obsList;
	
	@FXML
	public void onBtExitAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller();
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
	}
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeSellerNodes();
	}
	
	private void initializeSellerNodes() {
		TableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		TableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		TableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		TableColumnDepartmentId.setCellValueFactory(new PropertyValueFactory<>("department"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		TableViewSeller.prefHeightProperty().bind(stage.heightProperty());
		
	}

	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException();
		}
		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		TableViewSeller.setItems(obsList);
	}
	
	public void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			SellerFormController controller = loader.getController();
			controller.setSeller(obj);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Seller data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
			
		}catch(IOException e) {
			Alerts.showAlerts("IO Exception", "Erro load View", e.getMessage(), AlertType.ERROR);
		}
	}
}
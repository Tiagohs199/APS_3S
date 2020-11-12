package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
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
	
	private ObservableList<AuthorBook> obsList;
	
	public void setAuthorBookService(AuthorBookService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeAuthorBookNodes();
	}
	private void initializeAuthorBookNodes() {
		TableColumnSeq_no.setCellValueFactory(new PropertyValueFactory<>("seq_no"));
		TableColumnAuthor_id.setCellValueFactory(new PropertyValueFactory<>("author_id"));
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
		
	}
	public void createDialogForm(AuthorBook obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			
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
}

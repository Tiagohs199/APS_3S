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
import model.entities.Book;
import model.entities.Publisher;
import model.services.BookService;

public class BookListController implements Initializable {

	private BookService service;
	@FXML
	private TableView<Book> tableViewBook;
	
	@FXML
	private TableColumn<Publisher, Integer> TableColumnPublisher_id;
	@FXML
	private TableColumn<Book, String> TableColumnTitle;
	@FXML
	private TableColumn<Book, Integer> TableColumnIsbn;
	@FXML
	private TableColumn<Book, Double> tableColumnPrice;
	@FXML
	private TableColumn<Book, Book> tableColumnEdit;
	@FXML
	private TableColumn<Book, Book> tableColumnRemove;
	
	@FXML
	private Button btExit;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Book> obsList;
	
	@FXML
	public void onBtExitAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Book obj = new Book();
		createDialogForm(obj, "/gui/BookForm.fxml", parentStage);
	}
	public void setBookService(BookService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeBookNodes();
	}
	
	private void initializeBookNodes() {
		TableColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		TableColumnIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		TableColumnPublisher_id.setCellValueFactory(new PropertyValueFactory<>("publisher"));
		tableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewBook.prefHeightProperty().bind(stage.heightProperty());
		
	}

	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Book> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewBook.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	public void createDialogForm(Book obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			BookFormController controller = loader.getController();
			controller.setBook(obj);
			controller.setBookService(new BookService());
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Book data");
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
		tableColumnEdit.setCellFactory(param -> new TableCell<Book, Book>() {
			private final Button button = new Button("edit");
			
			@Override
			protected void updateItem(Book obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/BookForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Book, Book>(){
			private final Button button = new Button("remove");
			@Override
			protected void updateItem(Book obj, boolean empty) {
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
	private void  removeEntity(Book obj) {
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

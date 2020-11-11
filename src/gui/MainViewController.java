package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.AuthorBookService;
import model.services.AuthorsService;
import model.services.BookService;
import model.services.PublisherService;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemAuthors; //items de controle do menu
	@FXML
	private MenuItem menuItemPublisher;
	@FXML
	private MenuItem menuItemAuthorBook;
	@FXML
	private MenuItem menuItemBook;
	@FXML
	private MenuItem menuItemAbout;
	@FXML
	private MenuItem menuItemServion;

	@FXML
	public void onMenuItemAuthorsAction() { //ação do menu item authors
		loadView("/gui/AuthorsList.fxml", (AuthorsListController controller) -> {
			controller.setAuthorsService(new AuthorsService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemPublisherAction() { //ação do menu item publishers
		loadView("/gui/PublisherList.fxml",(PublisherListController controller) ->{
			controller.setPublisherService(new PublisherService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAuthorBookAction() { //ação do menu item author book
		loadView("/gui/AuthorBookList.fxml", (AuthorBookListController controller) -> {
			controller.setAuthorBookService(new AuthorBookService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemBookAction() { //ação do menu item  book
		loadView("/gui/BookList.fxml", (BookListController controller) -> {
			controller.setBookService(new BookService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAboutAction() { //ação do menu item about
		loadView("/gui/About.fxml", x -> {
		});
	}
	@FXML
	public void onMenuItemVersionAction() { // ação do menu item version
		loadView("/gui/Version.fxml", x -> {
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) { // synchronized para não deixar ser interrompido durante o carregamento da cenas
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName)); // estacia o FXMLloader 
			VBox newVBox = loader.load(); // carrega os dados 

			Scene mainScene = Main.getMainScene(); // referencia da tela principal

			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); // referencia para o scrollPane, content e Vbox

			Node mainMenu = mainVBox.getChildren().get(0); // salvando o menu bar
			mainVBox.getChildren().clear(); // limpando os dados da tela
			mainVBox.getChildren().add(mainMenu); // add o menu principal
			mainVBox.getChildren().addAll(newVBox.getChildren()); // add todos os dados do caminho absoluteName

			T controller = loader.getController();
			initializingAction.accept(controller);

		} catch (IOException e) {
			Alerts.showAlerts("IoException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}

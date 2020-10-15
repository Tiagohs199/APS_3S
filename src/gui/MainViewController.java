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
	private MenuItem menuItemAuthors;
	@FXML
	private MenuItem menuItemPublisher;
	@FXML
	private MenuItem menuItemAuthorBook;
	@FXML
	private MenuItem menuItemBook;
	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemAuthorsAction() {
		loadView("/gui/AuthorsList.fxml", (AuthorsListController controller) -> {
			controller.setAuthorsService(new AuthorsService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemPublisherAction() {
		loadView("/gui/PublisherList.fxml",(PublisherListController controller) ->{
			controller.setPublisherService(new PublisherService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAuthorBookAction() {
		loadView("/gui/AuthorBookList.fxml", (AuthorBookListController controller) -> {
			controller.setAuthorBookService(new AuthorBookService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemBookAction() {
		loadView("/gui/BookList.fxml", (BookListController controller) -> {
			controller.setBookService(new BookService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();

			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			T controller = loader.getController();
			initializingAction.accept(controller);

		} catch (IOException e) {
			Alerts.showAlerts("IoException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}

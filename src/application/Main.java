package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;


public class Main extends Application {
	
	private static Scene mainScene;
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml")); //estancia um novo objeto FXMLloader passando o caminho da view
			ScrollPane scrollPane = loader.load(); // chama o objeto loader
			
			scrollPane.setFitToHeight(true);// deixa o menu responsivo
			scrollPane.setFitToWidth(true);
			
			mainScene = new Scene(scrollPane); //criando um objeto scene passando minha cena
			primaryStage.setScene(mainScene); // seta a cena principal
			primaryStage.setTitle("Livraria APS"); // titulo do main view
			primaryStage.show();// depois mostra a tela principal
		} catch(Exception e) {
			e.printStackTrace();
		}
	} 
	
	public static Scene getMainScene() {
		return mainScene;
	}
		
	public static void main(String[] args) {
		launch(args);
	}
}


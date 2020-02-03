package gui;
	
import datamodel.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


public class Main extends Application {
		
	private Database db = new Database();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			// BorderPane root = new BorderPane();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/top_tab.fxml"));
			Parent root = loader.load();
			
			Scene scene = new Scene(root,700,440);
			scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

			// obtain main controller
			TopTabView wc = loader.getController();
	        // make the database object visible to the controller
	        wc.setDatabase(db);

			primaryStage.setTitle("Movie Booking System");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			// opening database connection
	        /* --- TODO: change xxx to your user name, yyy to your passowrd --- */	        
	        if(!db.openConnectionLocalhost("Admin", "e39v/8,<WW0-")) {
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.setTitle("Database error");
	            alert.setHeaderText(null);
	            alert.setContentText("Could not connect to the database! Check console for details.");
	            alert.showAndWait();
	        } 
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void stop() {
		// close the database here
		db.closeConnection();		
		try {
			super.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package application;

import java.sql.SQLException;

//import ui_controllers.*;
//import inventoryModel.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui_controllers.InventoryGUIController;
import db_connection.DBconnection;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws ClassNotFoundException, SQLException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InventoryGUI.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root, 1080, 720);
			
			
			InventoryGUIController inventoryController = loader.getController();
			inventoryController.setStage(primaryStage);
			
			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	        
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("Inventory Management System");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		DBconnection.dbConnect();
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
}

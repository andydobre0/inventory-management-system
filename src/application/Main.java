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
			Scene scene = new Scene(root, 1280, 720);
			
			InventoryGUIController inventoryController = loader.getController();
			inventoryController.setStage(primaryStage); // Set the stage
			
	        
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
		
//		Category fruits = new Category(1, "Fruits");
//		
//		Product orange = new Product(01, "Orange", fruits, 10.99, 10);
//		Product apple = new Product(02, "Apple", fruits, 9, 5);
//		Product orange1 = new Product(01, "Orange", fruits, 10.99, 99);
//		Product apple1 = new Product(01, "Apple", fruits, 9, 12);
//		
//		InventoryManager inventory = new InventoryManager();
//		
//		inventory.addProduct(orange);
//		inventory.addProduct(apple);
//		
//		inventory.showProducts();
//		System.out.println();
//		
//		orange.updateQuantity(100);
//		inventory.showProducts();
//		
//		System.out.println();
//		
//		inventory.addProduct(orange1);
//		inventory.showProducts();
//		
//		System.out.println();
//		
//		inventory.removeProduct(apple1);
//		inventory.showProducts();
//		
//		System.out.println();
//		
//		System.out.println(inventory.searchProduct("Orange"));
//		
//		inventory.showLowQtProducts();
//		
//		System.out.println(inventory.getProducts().get(0).getQuantity() == orange.getQuantity());
	}
}

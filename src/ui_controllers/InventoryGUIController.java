package ui_controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//import java.awt.Button;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;



import javafx.event.ActionEvent;

public class InventoryGUIController {
	@FXML
	private TextField tfProduct;
	@FXML
	private Button addItemBtn;
	List<String> products = new ArrayList<>();
	private Parent addProductRoot;
	private Stage stage;
	private Scene scene;
	
	 public void setStage(Stage stage) {
	       this.stage = stage;
    }

	@FXML
	public void addProductBtn(ActionEvent event) throws IOException {
		
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AddProductGUI.fxml"));
		addProductRoot = loader.load();
		
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(addProductRoot, 1080, 720);
		
//		AddProductGUIController controller = loader.getController();
//        controller.setStage(stage);
		
        if (stage != null) {
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Stage is null. Ensure it is properly initialized.");
        }
        
        
		}catch(IOException e) {
			e.printStackTrace();
		}
}
	
	public void showProductsBtn(ActionEvent event) {
		System.out.println(products);
	}

	public Button getAddItemBtn() {
		return addItemBtn;
	}

	public void setAddItemBtn(Button addItemBtn) {
		this.addItemBtn = addItemBtn;
	}
}

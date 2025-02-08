package ui_controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SwitchScenesController {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	public void finishBtnAction(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InventoryGUI.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		root = loader.load();
		scene = new Scene(root, 1280, 720);
		
		if(stage != null) {
			stage.setScene(scene);
			stage.show();
		}
		else {
			System.out.println("Stage is null.");
		}
	}
}

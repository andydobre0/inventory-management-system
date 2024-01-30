package ui_controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

//import java.awt.Button;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import data_access_obj.ProductDAO;
import db_connection.DBconnection;
import inventoryModel.Product;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class InventoryGUIController {
	@FXML
	private TextField searchProductBar;
	@FXML
	private Button addItemBtn;
	List<String> products = new ArrayList<>();
	private Parent addProductRoot;
	private Stage stage;
	private Scene scene;
	
	@FXML
	private TableView<Product> productsTable;
	@FXML
	private TableColumn<Product, Integer> idColumn;
	@FXML
	private TableColumn<Product, String> nameColumn;
	@FXML
	private TableColumn<Product, String> categoryColumn;
	@FXML
	private TableColumn<Product, Integer> qtyColumn;
	@FXML
	private TableColumn<Product, String> unitColumn;
	
	@FXML
	private Button productManagerBtn;
	@FXML 
	private Button categoryManagerBtn;
	@FXML
	private Button searchBtn;
	
	private Parent productManagerRoot;
	private Stage stageProdMngr;
	private Scene sceneProdMngr;
	
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
		
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		
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
	
	@FXML
	public void goToProductManagerBtn(ActionEvent event) throws IOException{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ProductManagerGUI.fxml"));
			productManagerRoot = loader.load();
			
			stageProdMngr = (Stage) ((Node) event.getSource()).getScene().getWindow();
			sceneProdMngr = new Scene(productManagerRoot, 1080, 720);
			sceneProdMngr.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			
			if(stageProdMngr != null) {
				stageProdMngr.setScene(sceneProdMngr);
				stageProdMngr.show();
			}
			else {
				System.out.println("Stage is null.");
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

//	public Button getAddItemBtn() {
//		return addItemBtn;
//	}
//
//	public void setAddItemBtn(Button addItemBtn) {
//		this.addItemBtn = addItemBtn;
//	}
//	
//	public Button getProdMngrBtn() {
//		return productManagerBtn;
//	}
//	public void setProdMngrBtn(Button productManagerBtn) {
//		this.productManagerBtn = productManagerBtn;
//	}
	
	@FXML
	public void initialize() throws ClassNotFoundException, SQLException {
		idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		categoryColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryProperty());
		qtyColumn.setCellValueFactory(cellData -> cellData.getValue().getQtyProperty().asObject());
		unitColumn.setCellValueFactory(cellData -> cellData.getValue().getUnitProperty());
		ObservableList<Product> productsList = ProductDAO.retrieveTable();
		populateTable(productsList);
	}
	
	void populateTable(ObservableList<Product> productsList) {
		productsTable.setItems(productsList);
		idColumn.setSortType(TableColumn.SortType.ASCENDING);
		productsTable.getSortOrder().add(idColumn);
		productsTable.sort();
	}	
	
	@FXML
	private void searchBtnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
		String product = searchProductBar.getText();
		
		if(ProductManagerGUIController.isParsable(product)) {
			String sql = "SELECT * FROM products WHERE id = " + product;
			try{
				ResultSet rs = DBconnection.retrieveData(sql);
				ObservableList<Product> productList = ProductDAO.getProducts(rs);
				if(!productList.isEmpty()) {
				for(Product productToShow : productList) {
					if(productToShow.getDate() != null)
					{
						Date date = productToShow.getDate();
						DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
						LocalDate localDate = date.toLocalDate();
						String formatedDate = localDate.format(formatObj);
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Product found.");
						alert.setHeaderText("Product is " + '"' + productToShow.getName() + '"' + " from " + '"' + productToShow.getCategory() + '"');
						alert.setContentText("Id: " + productToShow.getId() +"\n" + "added on " + formatedDate + "\n" + "quantity: " + productToShow.getQty());
						alert.showAndWait();
					}
					else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Product found.");
						alert.setHeaderText("Product is " + '"' + productToShow.getName() + '"' + " from " + '"' + productToShow.getCategory() + '"');
						alert.setContentText("id: " + productToShow.getId() + "\n" + "quantity: " + productToShow.getQty());
						alert.showAndWait();
					}
				}
				}
				else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Product not found.");
					alert.setHeaderText("The product you are looking for was not found.");
					alert.setContentText("It apppears that the product is not registered.");
					alert.showAndWait();
				}
				}catch(Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			String sql = "SELECT * FROM products WHERE UPPER(name) = " + "'" + product.toUpperCase() + "'";
			try{
				ResultSet rs = DBconnection.retrieveData(sql);
				ObservableList<Product> productList = ProductDAO.getProducts(rs);
				if(!productList.isEmpty()) {
				for(Product productToShow : productList) {
					if(productToShow.getDate() != null)
					{
						Date date = productToShow.getDate();
						DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
						LocalDate localDate = date.toLocalDate();
						String formatedDate = localDate.format(formatObj);
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Product found.");
						alert.setHeaderText("Product is " + '"' + productToShow.getName() + '"' + " from " + '"' + productToShow.getCategory() + '"');
						alert.setContentText("id: " + productToShow.getId() +"\n" + "added on " + formatedDate + "\n" + "quantity: " + productToShow.getQty());
						alert.showAndWait();
					}
					else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Product found.");
						alert.setHeaderText("Product is " + '"' + productToShow.getName() + '"' + " from " + '"' + productToShow.getCategory() + '"');
						alert.setContentText("id: " + productToShow.getId() + "\n" + "quantity: " + productToShow.getQty());
						alert.showAndWait();
					}
				}
				}
				else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Product not found.");
					alert.setHeaderText("The product you are looking for was not found.");
					alert.setContentText("It apppears that the product is not registered.");
					alert.showAndWait();		
				}
				}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
//	@FXML
//	public void categoryManagerBtnAction() {
//		
//	}
}

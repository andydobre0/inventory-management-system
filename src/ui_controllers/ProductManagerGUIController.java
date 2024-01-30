package ui_controllers;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import data_access_obj.CategoryDAO;
import data_access_obj.ProductDAO;
import db_connection.DBconnection;
import inventoryModel.Category;
import inventoryModel.Product;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ProductManagerGUIController {

	@FXML
	private TextField searchProductBar;
	@FXML
	private Button searchProductBtn;
	@FXML
	private Button decreaseBtn;
	@FXML
	private Button increaseBtn;
	@FXML
	private Button deleteBtn;
	
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
	private Button lowQtyBtn;
	@FXML
	private Button allProductsBtn;
	
	@FXML
	private Button finishBtn;

	private Stage stage;
	private Scene scene;
	
	
	@FXML
	public void initialize() throws ClassNotFoundException, SQLException {
		productsTable.setEditable(true);
		productsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
		
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nameColumn.setOnEditCommit(new EventHandler<CellEditEvent<Product, String>>(){

			@Override
			public void handle(CellEditEvent<Product, String> event) {
				Product product = event.getRowValue();
				try {
					ProductDAO.updateProductName(product.getName(), event.getNewValue());
				} catch (Exception e) {
					e.printStackTrace();
				}
				product.setNameProperty(event.getNewValue());
			}
			
		});

		
		categoryColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryProperty());
		categoryColumn.setCellFactory(new Callback<TableColumn<Product, String>, TableCell<Product, String>>(){

			@Override
			public TableCell<Product, String> call(TableColumn<Product, String> param) {
				TableCell<Product, String> cell = new TableCell<Product, String>(){
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if(empty) {
							setText(null);
						}
						else
							setText(item);
					}
				};
				
				cell.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
					if(event.getClickCount() == 2 && !cell.isEmpty()) {
						
						Integer productID = cell.getTableView().getItems().get(cell.getIndex()).getId();
						
						ObservableList<Category> categories;
						try {
							categories = CategoryDAO.getCategories();
					
						List<String> categoriesName = new ArrayList<>();
						for(Category category : categories)
							categoriesName.add(category.getNume());
						
						ChoiceDialog<String> categoryDialog = new ChoiceDialog<>(categoriesName.get(0), categoriesName);
						categoryDialog.setTitle("Choose a category to change to");
						categoryDialog.setHeaderText(null);
						categoryDialog.setContentText("Choose a new category:");
						Optional<String> result = categoryDialog.showAndWait();
						if(result.isPresent()) {
							ProductDAO.updateProductCategory(productID, result.get());
							refreshTableView();
							System.out.println(result.get());
						}
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
				});
				return cell;
			}
			
		});
		
		qtyColumn.setCellValueFactory(cellData -> cellData.getValue().getQtyProperty().asObject());
		qtyColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		qtyColumn.setOnEditCommit(new EventHandler<CellEditEvent<Product, Integer>>(){

			@Override
			public void handle(CellEditEvent<Product, Integer> event) {
				Product product = event.getRowValue();
				try {
					ProductDAO.updateProductQty(product.getId(), event.getNewValue());
				}catch(Exception e) {
					e.printStackTrace();
				}
				product.setQtyProperty(event.getNewValue());
			}
			
		});
		
		unitColumn.setCellValueFactory(cellData -> cellData.getValue().getUnitProperty());
		
		unitColumn.setCellFactory(new Callback<TableColumn<Product, String>, TableCell<Product, String>>(){

			@Override
			public TableCell<Product, String> call(TableColumn<Product, String> param) {
				TableCell<Product, String> cell = new TableCell<Product, String>(){
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if(empty) {
							setText(null);
						}
						else
							setText(item);
					}
				};
				
				cell.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
					if(event.getClickCount() == 2 && !cell.isEmpty()) {
						
						Integer productID = cell.getTableView().getItems().get(cell.getIndex()).getId();
						
						List<String> units = new ArrayList<>();
						try {
							units.add("kg");
							units.add("buc");
					
						
						ChoiceDialog<String> unitDialog = new ChoiceDialog<>(units.get(0), units);
						unitDialog.setTitle("Choose a unit to change to");
						unitDialog.setHeaderText(null);
						unitDialog.setContentText("Choose a new unit:");
						Optional<String> result = unitDialog.showAndWait();
						if(result.isPresent()) {
							ProductDAO.updateUnit(productID, result.get());
							refreshTableView();
							System.out.println(result.get());
						}
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
				});
				return cell;
			}
			
		});
		
		ObservableList<Product> productsList = ProductDAO.retrieveTable();
		populateTable(productsList);
	}
	
	void populateTable(ObservableList<Product> productsList) {
		productsTable.setItems(productsList);
		idColumn.setSortType(TableColumn.SortType.ASCENDING);
		productsTable.getSortOrder().add(idColumn);
		productsTable.sort();
	}	
	
	void refreshTableView() throws ClassNotFoundException, SQLException {
	    ObservableList<Product> productsList = ProductDAO.retrieveTable();
	    for(Product product : productsList) {
	    	if(product.getQty() < 5)
	    		showNotification("Some products are running low", Duration.seconds(2.5));
		}
	    populateTable(productsList);
	}
	
	@FXML
	public void finishBtnAction(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InventoryGUI.fxml"));
		Parent root = loader.load();
		
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		
		scene = new Scene(root, 1080, 720);
		
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		
		if(stage != null) {
			stage.setScene(scene);
			stage.show();
		}
		else
			System.out.println("stage is null");
		}
	
	@FXML
	public void increaseBtnAction() throws Exception {
				
		ObservableList<Product> selectedProducts = productsTable.getSelectionModel().getSelectedItems();
		ArrayList<Product> rows = new ArrayList<>(selectedProducts);
		
		if(!selectedProducts.isEmpty()) {
			try {
				Platform.runLater(() -> {
					try {
						if(rows.size() <= 10 && rows.size() > 0) {
							for(Product product : selectedProducts)
								ProductDAO.updateProductQty(product.getId(), product.getQty() + 1);
							refreshTableView();
						}
						else {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Exception encountered.");
							alert.setHeaderText("Select less than 10 rows");
							alert.setContentText(null);
							alert.showAndWait();
						}
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				});
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Exception encountered");
        	alert.setHeaderText("Please select a row.");
        	alert.setContentText(null);
        	alert.showAndWait();
		}
		
	}
	
	@FXML
	public void decreaseBtnAction() throws Exception {
//		int indexOfSelectedProduct = productsTable.getSelectionModel().getSelectedIndex();
//		Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
//		
//		if(selectedProduct != null) {
//			ProductDAO.updateProductQty(selectedProduct.getId(), selectedProduct.getQty()-1);
//			refreshTableView();
//			productsTable.getSelectionModel().select(indexOfSelectedProduct);
//		}
//		else{
//			Alert alert = new Alert(AlertType.ERROR);
//        	alert.setTitle("Exception encountered");
//        	alert.setHeaderText("Please select a row.");
//        	alert.setContentText(null);
//        	alert.showAndWait();
//		}
		
		ObservableList<Product> selectedProducts = productsTable.getSelectionModel().getSelectedItems();
		ArrayList<Product> rows = new ArrayList<>(selectedProducts);
		
		if(!selectedProducts.isEmpty()) {
			try {
				Platform.runLater(() -> {
					try {
						if(rows.size() <= 10 && rows.size() > 0) {
							for(Product product : selectedProducts)
								ProductDAO.updateProductQty(product.getId(), product.getQty() - 1);
							refreshTableView();
							
						}
						else {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Exception encountered.");
							alert.setHeaderText("Select less than 10 rows");
							alert.setContentText(null);
							alert.showAndWait();
						}
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				});
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Exception encountered");
        	alert.setHeaderText("Please select a row.");
        	alert.setContentText(null);
        	alert.showAndWait();
		}
	}
	
	@FXML
	public void deleteBtnAction(ActionEvent event) throws Exception {
	    ObservableList<Product> selectedRows = productsTable.getSelectionModel().getSelectedItems();
	    ArrayList<Product> rows = new ArrayList<>(selectedRows);
	    
	    List<Integer> idsToDelete = rows.stream().map(Product::getId).collect(Collectors.toList());

	    if (!idsToDelete.isEmpty()) {
	        try {
	            Platform.runLater(() -> {
	                try {
	                    ProductDAO.deleteSelectedProducts(idsToDelete);
	                    rows.forEach(row -> productsTable.getItems().remove(row));
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            });
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        }
	    }
	}
	
	@FXML
	private void lowQtyBtnAction(ActionEvent event) throws Exception {
		ObservableList<Product> productsList = ProductDAO.getLowQtyProducts();
	    populateTable(productsList);
	}
	@FXML
	private void allProductsBtnAction() throws Exception{
		ObservableList<Product> productList = ProductDAO.retrieveTable();
		populateTable(productList);
	}
	
	public static boolean isParsable(String string) {
		if(string == null)
			return false;
		try {
			Integer.parseInt(string);
		}catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	@FXML
	public void searchBtnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
		String product = searchProductBar.getText();
		
		if(isParsable(product)) {
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
	
	
	public void showNotification(String message, Duration duration) {
	    Platform.runLater(() -> {
	        Stage notificationStage = new Stage();

	        notificationStage.initStyle(StageStyle.UNDECORATED);
	        notificationStage.setAlwaysOnTop(true);

	        Label label = new Label(message);
	        label.setStyle("-fx-padding: 10; -fx-background-color: lightblue; -fx-border-color: black;");

	        Scene scene = new Scene(new StackPane(label));
	        notificationStage.setScene(scene);

	        notificationStage.setX(600);
	        notificationStage.setY(50);

	        notificationStage.show();

	        PauseTransition delay = new PauseTransition(duration);
	        delay.setOnFinished(event -> notificationStage.close());
	        delay.play();
	    });
	}
}

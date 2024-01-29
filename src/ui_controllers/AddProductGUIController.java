package ui_controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import data_access_obj.CategoryDAO;
import data_access_obj.ProductDAO;
import inventoryModel.Category;
import inventoryModel.Product;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

//import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddProductGUIController {
	@FXML
	private TextField productName;
	@FXML
	private TextField productQty;
	@FXML
	private TextField productID;
	@FXML
	private Button addItemBtn;
	@FXML
	private Button finishBtn;
	@FXML
	private Button deleteButton;
	@FXML
	private ComboBox<Category> categoryMenu;
	@FXML
	private Button addCategoryBtn;
	
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
	
	private Stage stage;
	private Scene scene;
	
	
	
	public void setStage(Stage stage) {
	       this.stage = stage;
 }
	
	private void refreshTableView() throws ClassNotFoundException, SQLException {
	    ObservableList<Product> productsList = ProductDAO.retrieveTable();
	    populateTable(productsList);
	}
	
	@FXML
	public void addItemBtnAction(ActionEvent event) throws ClassNotFoundException, SQLException{
		ObservableList<Product> productList = ProductDAO.retrieveTable();
	
		if(productID.getText().isEmpty() || productName.getText().isEmpty() || categoryMenu.getSelectionModel().getSelectedItem() == null || productQty.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Empty field!");
			alert.setHeaderText(null);
			alert.setContentText("Please do not leave any field empty.");
			alert.showAndWait();
		}
		else {
			try {
			Category selectedCategory = categoryMenu.getSelectionModel().getSelectedItem();
			ProductDAO.insertProduct(Integer.parseInt(productID.getText()), productName.getText(), selectedCategory.getNume(), Integer.parseInt(productQty.getText()));
			
			refreshTableView();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Done!");
			alert.setHeaderText(null);
			alert.setContentText("Product added!");
			alert.showAndWait();
		
			}catch(SQLException e) {
				int constraintError = 1;
				if(e.getErrorCode() == constraintError) {
				for(Product product : productList) {
					if(product.getId() == Integer.parseInt(productID.getText()))
					{
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Id error");
						alert.setHeaderText(null);
						alert.setContentText("The id you tried to enter already exists");
						alert.showAndWait();
						break;
					}
				}
				}
				else
					System.out.println("error: " + e.getErrorCode());
			}
		}
	}
	
	@FXML
	public void deleteAllButtonAction(ActionEvent event) throws Exception {
	    ObservableList<Product> selectedRows = productsTable.getSelectionModel().getSelectedItems();
	    ArrayList<Product> rows = new ArrayList<>(selectedRows);
	    
	    List<Integer> idsToDelete = rows.stream().map(Product::getId).collect(Collectors.toList());

	    if (!idsToDelete.isEmpty()) {
	        try {
	            Platform.runLater(() -> {
	                try {
	                    ProductDAO.deleteSelectedProducts(idsToDelete);
	                    rows.forEach(row -> productsTable.getItems().remove(row));
	                    System.out.println("Selected products deleted");
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
	public void initialize() throws ClassNotFoundException, SQLException {
		productsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		categoryColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryProperty());
		qtyColumn.setCellValueFactory(cellData -> cellData.getValue().getQtyProperty().asObject());
		ObservableList<Product> productsList = ProductDAO.retrieveTable();
		populateTable(productsList);
		
		refreshCategoryList();
	}
	
	private void populateTable(ObservableList<Product> productsList) {
		productsTable.setItems(productsList);
		idColumn.setSortType(TableColumn.SortType.ASCENDING);
		productsTable.getSortOrder().add(idColumn);
		productsTable.sort();
		
	}

	// revine la pagina principala
	@FXML
	public void finishBtnAction(ActionEvent event) throws IOException{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InventoryGUI.fxml"));
            Parent root = loader.load();
            
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1280, 720);
            
            if(stage != null){
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Stage is null.");
        }
	}

    @FXML
    public void handleCategorySelection(ActionEvent event) {
        Category selectedCategory = categoryMenu.getSelectionModel().getSelectedItem();
        
        categoryMenu.setOnAction(e -> selectedCategory.getNume());
    }
    
    @FXML
    public void showAddCategoryDialog(ActionEvent event) throws ClassNotFoundException, SQLException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Category");
        dialog.setHeaderText("Create a new category");
        dialog.setContentText("Please enter the name of the new category:");

        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
        	Category category = new Category(result.get());
        	CategoryDAO.addCategory(category);
        	refreshCategoryList();
        }
    }
    
    @FXML
    public void refreshCategoryList() {
        try {
            ObservableList<Category> categories = CategoryDAO.getCategories();
            categoryMenu.getItems().clear();
            categoryMenu.getItems().addAll(categories);
            
            categoryMenu.setCellFactory(lv -> new ListCell<Category>() {
                @Override
                protected void updateItem(Category category, boolean empty) {
                    super.updateItem(category, empty);
                    if (empty || category == null) {
                        setText(null);
                    } else {
                        setText(category.getNume());
                    }
                }
            });
            
            categoryMenu.setButtonCell(new ListCell<Category>() {
                @Override
                protected void updateItem(Category category, boolean empty) {
                    super.updateItem(category, empty);
                    if (empty || category == null) {
                        setText(null);
                    } else {
                        setText(category.getNume());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

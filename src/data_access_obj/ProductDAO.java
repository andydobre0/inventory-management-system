package data_access_obj;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import db_connection.DBconnection;
import inventoryModel.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductDAO {
	
	public static void insertProduct(Integer ID, String name, String category, Integer quantity) throws ClassNotFoundException, SQLException{
		String sql = "insert into products (id, name, category, quantity) values('"+ID+"','"+name+"','"+category+"','"+quantity+"')";
		
		try {
			DBconnection.dbExecuteQuerry(sql);
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void deleteSelectedProducts(List<Integer> ids) throws Exception {
		String sql = "DELETE FROM products WHERE ID IN (" + ids.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")";
		try {
			DBconnection.dbExecuteQuerry(sql);
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static ObservableList<Product> retrieveTable() throws SQLException, ClassNotFoundException{
		String sql = "select * from products";
		
		try {
			ResultSet rs = DBconnection.retrieveData(sql);
			ObservableList<Product> productsList = getProducts(rs);
			return productsList;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private static ObservableList<Product> getProducts(ResultSet rs) throws SQLException {
		
		try {
			
		ObservableList<Product> productsList = FXCollections.observableArrayList();
		
		while(rs.next()) {
			Product product = new Product();
			product.setIdProperty(rs.getInt("id"));
			product.setNameProperty(rs.getString("name"));
			product.setCategoryProperty(rs.getString("category"));
			product.setQtyProperty(rs.getInt("quantity"));
			productsList.add(product);
		}
		return productsList;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	public static void updateProduct() {
		
	}
	
	public static void deleteProduct() {
		
	}
}

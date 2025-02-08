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
	
	public static void insertProduct(Integer ID, String name, String category, Integer quantity, String unit, String date) throws ClassNotFoundException, SQLException{
		String sql = "insert into products (id, name, category, quantity, unit, data) values('"+ID+"','"+name+"','"+category+"','"+quantity+"','"+ unit + "','" + date +"')";
		
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
	
	public static ObservableList<Product> getProducts(ResultSet rs) throws SQLException {
		
		try {
			
		ObservableList<Product> productsList = FXCollections.observableArrayList();
		
		while(rs.next()) {
			Product product = new Product();
			product.setIdProperty(rs.getInt("id"));
			product.setNameProperty(rs.getString("name"));
			product.setCategoryProperty(rs.getString("category"));
			product.setQtyProperty(rs.getInt("quantity"));
			product.setUnitProperty(rs.getString("unit"));
			product.setDate(rs.getString("data"));
			productsList.add(product);
		}
		return productsList;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void updateProductName(String oldVal, String newVal) throws Exception {
		String sql = "UPDATE products SET name = " + "'" + newVal +"'" + " WHERE name = " + "'"+ oldVal +"'";
		try {
			DBconnection.dbExecuteQuerry(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void updateProductQty(Integer id,Integer newVal) throws Exception{
		String sql = "UPDATE products SET quantity = " + newVal + " WHERE id = " + id;
		try {
			DBconnection.dbExecuteQuerry(sql);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateUnit(Integer id, String newVal) throws Exception{
		String sql = "UPDATE products SET unit = " + "'" + newVal +"' WHERE id = " + id;
		try {
			DBconnection.dbExecuteQuerry(sql);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateProductCategory(Integer id, String newVal) throws ClassNotFoundException, SQLException {
		String sql = "UPDATE products SET category = " + "'" + newVal + "'" + " WHERE id = " + id;
		
		try{
			DBconnection.dbExecuteQuerry(sql);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ObservableList<Product> getLowQtyProducts() throws Exception{
		String sql = "SELECT * FROM products WHERE quantity < 5";
		
		try {
			ResultSet rs = DBconnection.retrieveData(sql);
			ObservableList<Product> productsList = getProducts(rs);
			return productsList;
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}

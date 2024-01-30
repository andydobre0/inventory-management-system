package data_access_obj;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db_connection.DBconnection;
import inventoryModel.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoryDAO {
	
	public static ObservableList<Category> getCategories() throws SQLException, ClassNotFoundException {
	    ObservableList<Category> categoryList = FXCollections.observableArrayList();
	    String selectStmt = "SELECT * FROM categories";

	    try {
	    	
	        ResultSet rs = DBconnection.retrieveData(selectStmt);

	        while (rs.next()) {
	            Category category = new Category(rs.getString("CATEGORYNAME"));
	            categoryList.add(category);
	        }
	    } catch (SQLException e) {
	        System.out.println("Error occurred while FETCHING the categories: " + e);
	        throw e;
	    }

	    return categoryList;
	}
	
	public static void addCategory(Category category) throws SQLException, ClassNotFoundException {
	    String insertStmt = "INSERT INTO categories (categoryname) VALUES (?)";
	    PreparedStatement stmt = null;
	    try {
	        DBconnection.dbConnect();
	        stmt = DBconnection.connection.prepareStatement(insertStmt, new String[] { "CATEGORYID" });
	        stmt.setString(1, category.getNume());
	        int affectedRows = stmt.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Creating category failed, no rows affected.");
	        }

	        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                category.setID(generatedKeys.getInt(1));
	            } else {
	                throw new SQLException("Creating category failed, no ID obtained.");
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error occurred while INSERTING a new category: " + e);
	        throw e;
	    } finally {
	        if (stmt != null) {
	            stmt.close();
	        }
	        DBconnection.dbDisconnect();
	    }
	}


}

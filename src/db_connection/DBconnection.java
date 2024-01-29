package db_connection;

import java.sql.Statement;

import javax.sql.rowset.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBconnection {
	private static final String dbURL = "jdbc:oracle:thin:@localhost:1521/xepdb1";
	private static final String username = "andy";
	private static final String password = "!dataPort2024*@";
	public static Connection connection = null;
	
	//metoda de conectare la bd
	public static void dbConnect() throws SQLException, ClassNotFoundException{
		
		try {
			connection = DriverManager.getConnection(dbURL, username, password);
		} catch (SQLException e) {
			System.out.println("error!");
			e.printStackTrace();
		}
	}
	
	public static void dbDisconnect() throws SQLException{
		try {
			if(connection != null && !connection.isClosed())
				connection.close();
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//inserare/actualizare/stergere date in bd
	public static void dbExecuteQuerry(String sqlStatement) throws SQLException, ClassNotFoundException{
		Statement stmt = null;
		
		try {
			dbConnect();
			stmt = connection.createStatement();
			stmt.executeUpdate(sqlStatement);
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			if(stmt != null) {
				stmt.close();
			}
			dbDisconnect();
		}
	}
	
	//obtinere date din bd
	public static ResultSet retrieveData(String sqlStatement) throws SQLException, ClassNotFoundException{
		
		Statement stmt = null;
		ResultSet rs = null;
		CachedRowSet crs = null;
		
		try {
			dbConnect();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sqlStatement);
			crs = RowSetProvider.newFactory().createCachedRowSet();
			crs.populate(rs);
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			dbDisconnect();
		}
		return crs;
	}
}

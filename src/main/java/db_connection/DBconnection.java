package db_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

public class DBconnection {
    private static final String dbURL = "jdbc:sqlite:inventory.db";
    public static Connection connection = null;

    public static void dbConnect() throws SQLException {
        try {
            connection = DriverManager.getConnection(dbURL);
        } catch (SQLException e) {
            System.out.println("Error connecting to SQLite database!");
            e.printStackTrace();
            throw e;
        }
    }

    public static void dbDisconnect() throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error disconnecting from SQLite database!");
            throw e;
        }
    }

    public static void dbExecuteQuerry(String sqlStatement) throws SQLException {
        Statement stmt = null;

        try {
            dbConnect();
            stmt = connection.createStatement();
            stmt.executeUpdate(sqlStatement);
        } catch (SQLException e) {
            System.out.println("Error executing query!");
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
    }

    public static ResultSet retrieveData(String sqlStatement) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        CachedRowSet crs = null;

        try {
            dbConnect();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sqlStatement);
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(rs);
        } catch (SQLException e) {
            System.out.println("Error retrieving data!");
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
        return crs;
    }
}

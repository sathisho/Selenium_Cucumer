package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database Utility for connecting to and executing queries against a database.
 * Supports MySQL and other JDBC-compatible databases.
 */
public class DatabaseUtil {
    private static final Logger logger = LogManager.getLogger(DatabaseUtil.class);
    private static Connection connection;
    private static ConfigReader config = new ConfigReader();
    private static final String DB_DRIVER = config.getDatabaseDriver();
    private static final String DB_URL = config.getDatabaseUrl();
    private static final String DB_USERNAME = config.getDatabaseUsername();
    private static final String DB_PASSWORD = config.getDatabasePassword();

    /**
     * Establish database connection
     */
    public static void connect() {
        try {
            Class.forName(DB_DRIVER);
            connection = java.sql.DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            logger.info("Database connection established successfully");
        } catch (ClassNotFoundException e) {
            logger.error("Database driver not found: {}", DB_DRIVER, e);
        } catch (SQLException e) {
            logger.error("Failed to connect to database: {}", DB_URL, e);
        }
    }

    /**
     * Close database connection
     */
    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed");
            }
        } catch (SQLException e) {
            logger.error("Error closing database connection", e);
        }
    }

    /**
     * Check if connection is active
     */
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            logger.error("Error checking connection status", e);
            return false;
        }
    }

    /**
     * Execute SELECT query and return results as List of Maps
     */
    public static List<Map<String, String>> executeQuery(String query) {
        List<Map<String, String>> resultList = new ArrayList<>();
        try {
            if (!isConnected()) {
                connect();
            }

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String value = resultSet.getString(i);
                    row.put(columnName, value);
                }
                resultList.add(row);
            }

            resultSet.close();
            statement.close();
            logger.debug("Query executed successfully. Rows returned: {}", resultList.size());
        } catch (SQLException e) {
            logger.error("Error executing query: {}", query, e);
        }
        return resultList;
    }

    /**
     * Execute UPDATE, INSERT, or DELETE query
     */
    public static int executeUpdate(String query) {
        int rowsAffected = 0;
        try {
            if (!isConnected()) {
                connect();
            }

            Statement statement = connection.createStatement();
            rowsAffected = statement.executeUpdate(query);
            statement.close();
            logger.info("Update query executed. Rows affected: {}", rowsAffected);
        } catch (SQLException e) {
            logger.error("Error executing update query: {}", query, e);
        }
        return rowsAffected;
    }

    /**
     * Execute query using PreparedStatement (prevents SQL injection)
     */
    public static List<Map<String, String>> executePreparedQuery(String query, Object... params) {
        List<Map<String, String>> resultList = new ArrayList<>();
        try {
            if (!isConnected()) {
                connect();
            }

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String value = resultSet.getString(i);
                    row.put(columnName, value);
                }
                resultList.add(row);
            }

            resultSet.close();
            preparedStatement.close();
            logger.debug("Prepared query executed successfully. Rows returned: {}", resultList.size());
        } catch (SQLException e) {
            logger.error("Error executing prepared query", e);
        }
        return resultList;
    }

    /**
     * Get single value from database
     */
    public static String getSingleValue(String query, String columnName) {
        String value = null;
        List<Map<String, String>> results = executeQuery(query);
        if (!results.isEmpty()) {
            value = results.get(0).get(columnName);
        }
        return value;
    }

    /**
     * Check if record exists in database
     */
    public static boolean recordExists(String query) {
        List<Map<String, String>> results = executeQuery(query);
        return !results.isEmpty();
    }

    /**
     * Get row count for a table
     */
    public static int getRowCount(String tableName) {
        String query = "SELECT COUNT(*) as count FROM " + tableName;
        String count = getSingleValue(query, "count");
        return count != null ? Integer.parseInt(count) : 0;
    }

    /**
     * Clear table (delete all records)
     */
    public static void clearTable(String tableName) {
        try {
            String query = "DELETE FROM " + tableName;
            int rowsAffected = executeUpdate(query);
            logger.info("Table {} cleared. Rows deleted: {}", tableName, rowsAffected);
        } catch (Exception e) {
            logger.error("Error clearing table: {}", tableName, e);
        }
    }
}

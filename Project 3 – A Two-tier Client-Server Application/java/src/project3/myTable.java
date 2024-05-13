/* 

Name: Victoria Ten
Course: CNT 4714 Spring 2024 
Assignment title: Project 3
Date:  March 10, 2024 

*/ 

package project3;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.Properties;
import javax.sql.DataSource;
import java.io.IOException;

public class myTable extends AbstractTableModel {

    private Statement statement;
    private ResultSetMetaData metaData;
    private Connection connection;
    private int numberOfRows;
    private ResultSet resultSet;
    private boolean connectedToDatabase = false;

    public Class getColumnClass(int column) throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }
        try {
            String className = metaData.getColumnClassName(column + 1);

            return Class.forName(className);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return Object.class;
    }

    public String getColumnName(int column) throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }
        try {
            return metaData.getColumnName(column + 1);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return "";
    }

    public int getRowCount() throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }
        return numberOfRows;
    }

    public int getColumnCount() throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }
        try {
            return metaData.getColumnCount();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return 0;
    }

    public Object getValueAt(int row, int column)
            throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }
        try {
            resultSet.next();
            resultSet.absolute(row + 1);
            return resultSet.getObject(column + 1);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return "";
    }

    public void setQuery(String query)
            throws SQLException, IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        resultSet = statement.executeQuery(query);
        metaData = resultSet.getMetaData();
        resultSet.last();
        numberOfRows = resultSet.getRow();
        fireTableStructureChanged();
    }

    public void setUpdate(String query)
            throws SQLException, IllegalStateException {
        int res;
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }
        res = statement.executeUpdate(query);
        fireTableStructureChanged();
    }

    public myTable(String query, Connection connection)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            this.connection = connection;
            if (!this.connection.isClosed()) {
                connectedToDatabase = true;
                statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                if (query.contains("select") || query.contains("SELECT")) {
                    try {
                        setQuery(query);
                    } catch (IllegalStateException | SQLException e) {
                        JOptionPane.showMessageDialog(null,
                                e.getMessage(), "Database error",
                                JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        setUpdate(query);
                    } catch (IllegalStateException | SQLException e) {
                        JOptionPane.showMessageDialog(null,
                                e.getMessage(), "Database error",
                                JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }
}

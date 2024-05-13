/* 
Name: Victoria Ten
Course: CNT 4714 Spring 2024 
Assignment title: Project 3 – A Specialized Accountant Application 
Date:  March 10, 2024 
*/ 

package project3;

import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.*;

public class myGUI2 extends JFrame {

	private static final int WIDTH = 800;
	private static final int HEIGHT = 700;
	
    private JLabel jlbDBURLProperties; // Renamed from jlbDriver
    private JLabel jlbDataBaseURL;
    private JLabel jlbUsername;
    private JLabel jlbCommand;
    private JLabel jlbConnection;
    private JLabel jlbPassword;
    private JLabel jlbConnectionStatus;
    private JComboBox dbString;
    private JComboBox userString;
    private JTextField jtfUsername;
    private JPasswordField jpfPassword;
    private JTextArea jtaSqlCommand;
    private JButton jbtConnectToDB;
    private JButton jbtDisconnectFromDB; // Added button
    private JButton jbtClearSQLCommand;
    private JButton jbtExecuteSQLCommand;
    private JButton jbtClearResultWindow;
    private myTable tableModel = null;
    private JTable table;
    private Connection connection;
    private boolean connectedToDatabase = false;

    public myGUI2() throws ClassNotFoundException, SQLException, IOException {
    	setSize(WIDTH, HEIGHT);
        this.createInstanceGUIComponents();
        
        // ActionListener for the Connect to Database button
        this.jbtConnectToDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    String selectedDB = (String) dbString.getSelectedItem();
                    String selectedUser = (String) userString.getSelectedItem();

                    // Read username and password from text fields
                    String enteredUsername = jtfUsername.getText();
                    String enteredPassword = new String(jpfPassword.getPassword());

                    // Read properties file and set up data source
                    Properties properties = new Properties();
                    FileInputStream filein = new FileInputStream(selectedDB);
                    properties.load(filein);
                    MysqlDataSource dataSource = new MysqlDataSource();
                    dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));

                    // Check if username and password match
                    filein = new FileInputStream(selectedUser);
                    properties.load(filein);
                    String storedUsername = properties.getProperty("MYSQL_DB_USERNAME");
                    String storedPassword = properties.getProperty("MYSQL_DB_PASSWORD");

                    if (enteredUsername.equals(storedUsername) && enteredPassword.equals(storedPassword)) {
                        dataSource.setUser(enteredUsername);
                        dataSource.setPassword(enteredPassword);

                        // Establish database connection
                        connection = dataSource.getConnection();
                        connectedToDatabase = true;

                        // Update connection status label
                        jlbConnectionStatus.setText("CONNECTED TO: " + dataSource.getURL());

                        jlbConnectionStatus.setForeground(Color.GREEN);
                    } else {
                    	jlbConnectionStatus.setText("NOT CONNECTED - User Credentials Do Not Match Properties File!");
                    }
                } catch (IOException | SQLException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

     // ActionListener for the Disconnect From Database button
        this.jbtDisconnectFromDB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (connectedToDatabase) {
                        // Close the database connection
                        connection.close();
                        connectedToDatabase = false;

                        // Update connection status label
                        jlbConnectionStatus.setText("Disconnected from database");
                        jlbConnectionStatus.setForeground(Color.BLUE);

                        // Clear table and table model
                        table.setModel(new DefaultTableModel());
                        tableModel = null;

                        // Reset text fields
                        jtfUsername.setText("");
                        jpfPassword.setText("");
                        jtaSqlCommand.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Not connected to any database.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });


        // ActionListener for the Clear SQL Command button
        this.jbtClearSQLCommand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                jtaSqlCommand.setText("");
            }
        });

        // ActionListener for the Execute SQL Command button
        this.jbtExecuteSQLCommand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (connectedToDatabase == true && tableModel == null) {
                    try {
                        tableModel = new myTable(jtaSqlCommand.getText(), connection);
                        table.setModel(tableModel);
                    } catch (ClassNotFoundException | SQLException | IOException e) {
                        table.setModel(new DefaultTableModel());
                        tableModel = null;
                        JOptionPane.showMessageDialog(null,
                                e.getMessage(), "DB error",
                                JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                } else if (connectedToDatabase == true && tableModel != null) {
                    String query = jtaSqlCommand.getText();
                    if (query.contains("select") || query.contains("SELECT")) {
                        try {
                            tableModel.setQuery(query);
                        } catch (IllegalStateException | SQLException e) {
                            table.setModel(new DefaultTableModel());
                            tableModel = null;
                            JOptionPane.showMessageDialog(null,
                                    e.getMessage(), "DB error",
                                    JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            tableModel.setUpdate(query);
                            table.setModel(new DefaultTableModel());
                            tableModel = null;
                        } catch (IllegalStateException | SQLException e) {
                            table.setModel(new DefaultTableModel());
                            tableModel = null;
                            JOptionPane.showMessageDialog(null,
                                    e.getMessage(), "DB error",
                                    JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        // ActionListener for the Clear Result Window button
        this.jbtClearResultWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                table.setModel(new DefaultTableModel());
                tableModel = null;
            }
        });

        JPanel buttons = new JPanel(new GridLayout(1, 4)); // Adjusted GridLayout to accommodate the new button and the label
        buttons.add(this.jbtConnectToDB);
        buttons.add(this.jbtDisconnectFromDB); // Added the Disconnect button
        buttons.add(this.jbtClearSQLCommand);
        buttons.add(this.jbtExecuteSQLCommand);
        
        // Empty cell to align the button
        JPanel connect = new JPanel(new GridLayout(1, 1));
        connect.add(this.jlbConnectionStatus); // Moved the label to the second line and aligned it to the left

        JPanel center = new JPanel(new GridLayout(2, 1));
        center.add(buttons);
        center.add(connect);

        JPanel labelsAndTextFields = new JPanel(new GridLayout(5, 2));
        labelsAndTextFields.add(this.jlbConnection);
        labelsAndTextFields.add(new JLabel());
        labelsAndTextFields.add(this.jlbDBURLProperties); // Renamed from jlbDriver
        labelsAndTextFields.add(this.dbString);
        labelsAndTextFields.add(this.jlbDataBaseURL);
        labelsAndTextFields.add(this.userString);
        labelsAndTextFields.add(this.jlbUsername);
        labelsAndTextFields.add(this.jtfUsername);
        labelsAndTextFields.add(this.jlbPassword);
        labelsAndTextFields.add(this.jpfPassword);
        
        JPanel labelSQL = new JPanel(new BorderLayout());
        labelSQL.add(this.jlbCommand, BorderLayout.NORTH); // Renamed from jlbDriver
        labelSQL.add(this.jtaSqlCommand, BorderLayout.CENTER);

        JPanel top = new JPanel(new GridLayout(1, 2));
        top.add(labelsAndTextFields);
        top.add(labelSQL);
        

        JPanel south = new JPanel();
        south.setLayout(new BorderLayout(20, 0));

        // Create a JLabel for the text "SQL Execution Result Window"
        JLabel resultWindowLabel = new JLabel("SQL Execution Result Window");
        resultWindowLabel.setForeground(Color.BLUE);

        // Add the JLabel to the south panel before the JScrollPane
        south.add(resultWindowLabel, BorderLayout.NORTH);
        south.add(new JScrollPane(this.table), BorderLayout.CENTER); // Changed to CENTER
        south.add(this.jbtClearResultWindow, BorderLayout.SOUTH);



        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent event) {
                try {
                    if (!connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });

    }

    public void createInstanceGUIComponents() throws ClassNotFoundException, SQLException, IOException {
        String[] dbString = {"operationslog.properties"};
        String[] userString = {"theaccountant.properties"};
        this.jlbDBURLProperties = new JLabel("DB URL Properties"); // Renamed from jlbDriver
        this.jlbDataBaseURL = new JLabel("Database URL");
        this.jlbUsername = new JLabel("Username");
        this.jlbPassword = new JLabel("Password");
        this.jlbCommand = new JLabel("Enter An SQL Command");
        this.jlbConnection = new JLabel("Connection Details");
        this.jlbConnection.setForeground(Color.BLUE);
        this.jlbConnectionStatus = new JLabel("NO CONNECTION ESTABLISHED");
        this.jlbConnectionStatus.setForeground(Color.RED);
        this.jlbConnectionStatus.setOpaque(true); // Set the label as opaque
        this.jlbConnectionStatus.setBackground(Color.BLACK);
        this.dbString = new JComboBox(dbString);
        this.dbString.setSelectedIndex(0);
        this.userString = new JComboBox(userString);
        this.jtfUsername = new JTextField();
        this.jpfPassword = new JPasswordField();
        this.jtaSqlCommand = new JTextArea(3, 75);
        this.jtaSqlCommand.setWrapStyleWord(true);
        this.jtaSqlCommand.setLineWrap(true);
        this.jbtConnectToDB = new JButton("Connect to Database");
        this.jbtConnectToDB.setBackground(Color.BLUE);
        this.jbtConnectToDB.setForeground(Color.WHITE);
        this.jbtDisconnectFromDB = new JButton("Disconnect From Database"); // Initialized the new button
        this.jbtDisconnectFromDB.setBackground(Color.RED); // Set background color to red
        this.jbtClearSQLCommand = new JButton("Clear SQL Command");
        this.jbtClearSQLCommand.setBackground(Color.YELLOW);
        this.jbtExecuteSQLCommand = new JButton("Execute SQL Command");
        this.jbtExecuteSQLCommand.setBackground(Color.GREEN);
        this.jbtClearResultWindow = new JButton("Clear Result Window");
        this.table = new JTable();
    }
    public static void main(String[] args) {
        try {
            myGUI2 newGUI2 = new myGUI2();
            newGUI2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            newGUI2.setVisible(true);
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }


}

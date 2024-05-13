
/* Name: Victoria Ten
 Course: CNT 4714 – Spring 2024
 Assignment title: Project 1 – An Event-driven Enterprise Simulation
 Date: Sunday January 28, 2024
*/


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class GUI extends JFrame{
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 700;
	
	//Buttons and Labels
	private JLabel		blankLabel, controlsLabel, idLabel, quantityLabel, detailsLabel, totalLabel, cartLabel;
	private JButton		blankButton, processButton, confirmButton, viewButton, finishButton, newButton, exitButton;
	private JTextField	cartLineArray[], blankTextField, idTextField, quantityTextField, detailsTextField, totalTextField;
	
	private ProcessButtonHandler	processHandler;
	private ConfirmButtonHandler	confirmHandler; 
	private ViewButtonHandler		viewHandler; 
	private FinishButtonHandler		finishHandler;
	private NewButtonHandler		newHandler;
	private ExitButtonHandler		exitHandler;
	
	static int itemCount = 0;
	static double total = 0.00;
	static String[][]cartArray = new String[5][8];
	static String[] dataItem = new String[8];
//___________________________________________________________________________________________________	
	public GUI() {
		
		setSize(WIDTH, HEIGHT);
		setTitle("v10.com");
		
		//Colors
		Color dark_blue = new Color(38, 73, 92);
		Color light_brown = new Color(209, 181, 120);
		Color orange_red = new Color(198, 107, 61);
		Color almost_white = new Color(229, 229, 220);
		Color light_orange = new Color(255, 166, 115);
		Color light_blue = new Color(87, 215, 255);
		
		//Instantiate JLabel objects
		blankButton = new JButton(" ");
		blankLabel = new JLabel(" ", SwingConstants.CENTER);
		
		idLabel = new JLabel("Enter item ID for Item #" + (itemCount+1) + ":", SwingConstants.RIGHT);
		quantityLabel = new JLabel("Enter quantity for Item #" + (itemCount+1) + ":", SwingConstants.RIGHT);
		detailsLabel = new JLabel("Details for Item #" + (itemCount+1) + ":", SwingConstants.RIGHT);
		totalLabel = new JLabel("Current Subtotal for " + (itemCount) + " item(s):", SwingConstants.RIGHT);
		
		cartLabel = new JLabel("Your Shopping Cart Is Empty", SwingConstants.CENTER);
				
		controlsLabel = new JLabel(" USER CONTROLS ", SwingConstants.CENTER);
		
		//Instantiate JTextField objects
		blankTextField = new JTextField();
		idTextField = new JTextField();
		quantityTextField = new JTextField();
		detailsTextField = new JTextField();
		totalTextField = new JTextField();
		cartLineArray = new JTextField[5];
		for (int c=0; c<5; c++) {
			cartLineArray[c] = new JTextField();
		}
		
		
		//instantiate buttons and register handlers
		processButton = new JButton("Find Item #" + (itemCount +1));
		processHandler = new ProcessButtonHandler();
		processButton.addActionListener(processHandler);
		
		confirmButton = new JButton("Add Item #" + (itemCount +1) + " To Cart");
		confirmHandler = new ConfirmButtonHandler();
		confirmButton.addActionListener(confirmHandler);
		
		viewButton = new JButton("View Cart");
		viewHandler = new ViewButtonHandler();
		viewButton.addActionListener(viewHandler);
		
		finishButton = new JButton("Check Out");
		finishHandler = new FinishButtonHandler();
		finishButton.addActionListener(finishHandler);
		
		newButton = new JButton("Empty Cart - Start A New Order");
		newHandler = new NewButtonHandler();
		newButton.addActionListener(newHandler);
		
		exitButton = new JButton("Exit (Close App)");
		exitHandler = new ExitButtonHandler();
		exitButton.addActionListener(exitHandler);
		
		//Initial settings
		processButton.setEnabled(true);
		confirmButton.setEnabled(false);
		viewButton.setEnabled(false);
		finishButton.setEnabled(false);
		
		blankTextField.setBackground(almost_white);
		blankTextField.setVisible(false);
		idTextField.setEnabled(true);
		quantityTextField.setEnabled(true);
		detailsTextField.setEnabled(false);
		totalTextField.setEnabled(false);
		
		for (int c=0; c<5; c++) {
			cartLineArray[c].setEnabled(false);
			cartLineArray[c].setBackground(almost_white);
			cartLineArray[c].setVisible(true);
		}
		
		blankButton.setBackground(almost_white);
		blankButton.setVisible(false);
		
		
		//Container
		Container pane = getContentPane();
		
		//Grids

		GridLayout grid7by1 = new GridLayout(7,1,10,10);
		GridLayout grid6by2 = new GridLayout(6,2,10,10);
		
		//Panels
		JPanel northPanel = new JPanel();
		JPanel centrPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		northPanel.setLayout(grid6by2);
		centrPanel.setLayout(grid7by1);
		southPanel.setLayout(grid6by2);
		
		pane.add(northPanel, BorderLayout.NORTH);
		pane.add(centrPanel, BorderLayout.CENTER);
		pane.add(southPanel, BorderLayout.SOUTH);
		
		pane.setBackground(almost_white);
		
		northPanel.setBackground(dark_blue);
		centrPanel.setBackground(light_brown);
		southPanel.setBackground(orange_red);
		
		centerFrame(WIDTH, HEIGHT);
		
		//North
		northPanel.add(blankLabel);
		northPanel.add(blankTextField);
		idLabel.setForeground(almost_white);
		northPanel.add(idLabel);
		northPanel.add(idTextField);
		quantityLabel.setForeground(almost_white);
		northPanel.add(quantityLabel);
		northPanel.add(quantityTextField);
		detailsLabel.setForeground(light_blue);
		northPanel.add(detailsLabel);
		northPanel.add(detailsTextField);
		totalLabel.setFont(new Font("Calibri", Font.BOLD, 15));
		totalLabel.setForeground(light_orange);
		northPanel.add(totalLabel);
		northPanel.add(totalTextField);
		
		//Center
		cartLabel.setFont(new Font("Calibri", Font.BOLD, 16));
		cartLabel.setForeground(Color.BLACK);
		centrPanel.add(cartLabel);
		cartLabel.setHorizontalAlignment(JLabel.CENTER);
		
		for (int c=0; c<5; c++) {
			centrPanel.add(cartLineArray[c]);
		}
		
		//South
		cartLabel.setForeground(dark_blue);
		southPanel.add(controlsLabel);
		controlsLabel.setHorizontalAlignment(JLabel.CENTER);
		southPanel.add(blankButton);
		
		southPanel.add(processButton);
		southPanel.add(confirmButton);
		southPanel.add(viewButton);
		southPanel.add(finishButton);
		southPanel.add(newButton);
		southPanel.add(exitButton);
		
		
	}
	
//___________________________________________________________________________________________________	

	public void centerFrame(int fWidth, int fHight) {
		Toolkit aToolkit = Toolkit.getDefaultToolkit();
		Dimension screen = aToolkit.getScreenSize();
		
		int xPositoinOfFrame = (screen.width - fWidth)/2;
		int yPositoinOfFrame = (screen.height - fHight)/2;
		
		setBounds(xPositoinOfFrame, yPositoinOfFrame, fWidth, fHight);
	}
//___________________________________________________________________________________________________	
	private class ProcessButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			for (int a = 0; a < dataItem.length; a++) {
				dataItem[a] = null;
			}
			
			//Read File
			File inputFile = new File("inventory.csv");
			FileReader inputFileReader = null;
			BufferedReader inputBufferedReader = null;
			Scanner myScanner = null;
			String inventoryLine;
			String itemIDFromFile;
			boolean found = false;
			boolean stock = false;
			
			String searchItem = idTextField.getText();
			
			try {
				inputFileReader = new FileReader(inputFile);
				inputBufferedReader = new BufferedReader(inputFileReader);
				
				inventoryLine = inputBufferedReader.readLine();
				
				whileloop: while(inventoryLine != null) {
					
					myScanner = new Scanner(inventoryLine).useDelimiter("\\s*,\\s*");
					itemIDFromFile = myScanner.next();
					if(itemIDFromFile.equals(searchItem)) {
						dataItem[0] = itemIDFromFile;
						for (int i=1; i<5; i++) {
							dataItem[i] = myScanner.next();
						}
					
						if (dataItem[2].equals("true")) {
							stock = true;
						}
						else {
							JOptionPane.showMessageDialog(null, "Sorry... that item is out of stock, please try another item", "v10.com", JOptionPane.ERROR_MESSAGE);
							quantityTextField.setText("");
							idTextField.setText("");
						}
						if (stock == true) {
							int inStock = Integer.parseInt(dataItem[3]);
							int quantity = Integer.parseInt(quantityTextField.getText());
							double price = Double.parseDouble(dataItem[4])*quantity;
							
							if (0<quantity && quantity<=inStock) {
								dataItem[5] = Integer.toString(quantity);
								if (quantity <=4) {
									dataItem[6] = "0";
									dataItem[7] = String.format(Locale.US,"%.2f",price);
								} else if (quantity <=9) {
									dataItem[6] = "10";									
									dataItem[7] = String.format(Locale.US,"%.2f",price-price*0.10);
								} else if (quantity <=14) {
									dataItem[6] = "15";
									dataItem[7] = String.format(Locale.US,"%.2f",price-price*0.15);
								}else {
									dataItem[6] = "20";
									dataItem[7] = String.format(Locale.US,"%.2f",price-price*0.20);
								}

								detailsTextField.setText(dataItem[0] + " " + dataItem[1] + " $" + dataItem[4] + " " + dataItem[5] + " " + dataItem[6] + "% $" + dataItem[7]);					
								detailsTextField.setEnabled(true);
								detailsLabel.setText("Details for Item #" + (itemCount+1) + ":");
								processButton.setEnabled(false);
								confirmButton.setEnabled(true);
																
							} else if (quantity>inStock) {
								JOptionPane.showMessageDialog(null, "Insufficient stock. Only "+inStock+" on hand. Please reduce the quantity.", "v10.com", JOptionPane.ERROR_MESSAGE);
								quantityTextField.setText("");
							} else {
								JOptionPane.showMessageDialog(null, "The quantity you enterd is not valid", "v10.com", JOptionPane.ERROR_MESSAGE);
								quantityTextField.setText("");
								idTextField.setText("");
							}
							
						}
						found = true;
						break whileloop;
					}
					else {
						inventoryLine = inputBufferedReader.readLine();
					}
				}
				if (found == false) {
					JOptionPane.showMessageDialog(null, "Item ID " + searchItem + " not in file", "v10.com", JOptionPane.ERROR_MESSAGE);
					quantityTextField.setText("");
					idTextField.setText("");
				}
			}
			
			catch(FileNotFoundException fileNotFoundException) {
				JOptionPane.showMessageDialog(null, "Error: File not found", "v10.com", JOptionPane.ERROR_MESSAGE);
				quantityTextField.setText("");
				idTextField.setText("");
			}
			catch(IOException ioException) {
				JOptionPane.showMessageDialog(null, "Error: Cannot read file", "v10.com", JOptionPane.ERROR_MESSAGE);
				quantityTextField.setText("");
				idTextField.setText("");
			}
		}
	}
//___________________________________________________________________________________________________	
	private class ConfirmButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			quantityTextField.setText("");
			idTextField.setText("");
			for (int i=0; i<8; i++) {
				cartArray[itemCount][i] = dataItem[i];
			}
			processButton.setEnabled(true);
			confirmButton.setEnabled(false);
			viewButton.setEnabled(true);
			finishButton.setEnabled(true);			
			totalTextField.setEnabled(true);
			
			total = total+Double.parseDouble(dataItem[7]);
			totalTextField.setText("$"+String.format(Locale.US,"%.2f",total));
			cartLineArray[itemCount].setText("   Item "+(itemCount+1)+" - SKU: "+dataItem[0]+", Desc: "+dataItem[1]+", Price Es. $"+dataItem[4]+", Qty: "+dataItem[5]+", Total: $"+dataItem[7]);
			cartLineArray[itemCount].setEnabled(true);
			itemCount +=1;
			
			idLabel.setText("Enter item ID for Item #" + (itemCount+1) + ":");
			quantityLabel.setText("Enter quantity for Item #" + (itemCount+1) + ":");
			processButton.setText("Find Item #" + (itemCount +1));
			confirmButton.setText("Add Item #" + (itemCount +1) + " To Cart");
			totalLabel.setText("Current Subtotal for " + (itemCount) + " item(s):");
			
			if (itemCount == 5) {
				idTextField.setEnabled(false);
				quantityTextField.setEnabled(false);
				idTextField.setVisible(false);
				quantityTextField.setVisible(false);
				processButton.setEnabled(false);
				confirmButton.setEnabled(false);
			}
		}
	}
//___________________________________________________________________________________________________	
	private class ViewButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			String status = "";
			for (int i=0; i<itemCount; i++) {
				status = status+(i+1)+". "+cartArray[i][0]+" "+cartArray[i][1]+" $"+cartArray[i][4]+" "+cartArray[i][5]+" "+cartArray[i][6]+"% $"+cartArray[i][7]+"\n";
			}
			
			JOptionPane.showMessageDialog(null, status,"v10.com - Shopping Cart", JOptionPane.DEFAULT_OPTION);
			
		}
	}

//___________________________________________________________________________________________________	
	private class FinishButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			//current date and time
			LocalDate currentDate = LocalDate.now();
			int day = currentDate.getDayOfMonth();
			int month = currentDate.getMonthValue();
	        int year = currentDate.getYear();
	        ZoneId estZone = ZoneId.ofOffset("UTC", ZoneOffset.ofHours(-5));
	        LocalTime currentTime = LocalTime.now(estZone);	        
	        LocalDateTime currentDateTime = LocalDateTime.of(currentDate, currentTime);
	        
	        DateTimeFormatter timestamp = DateTimeFormatter.ofPattern("ddMMuuuuHHmmss");
	        String stamp = currentDateTime.format(timestamp);

	        
	        Month monthStr = Month.of(month);
			String monthName = monthStr.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
	        String formattedTime = currentTime.format(formatter);
	        
	        //text for transcript file
	        String dateInfo = monthName+" "+day+", "+year+", "+formattedTime+" EST";
	        
			String transaction = "";
			for (int i=0; i<itemCount; i++) {
				transaction = transaction+stamp+", "+cartArray[i][0]+", "+cartArray[i][1]+", "+cartArray[i][4]+", "+cartArray[i][5]+", "+Double.parseDouble(cartArray[i][6])/100+", $"+cartArray[i][7]+", "+dateInfo+"\n";
			}
			
	        //write and append file
	        String append = transaction;
	        String fileName = "transactions.csv";
	        try {
	            // Check if the file exists
	            File file = new File(fileName);

	            if (!file.exists()) {
	                // Create the file if it doesn't exist
	                file.createNewFile();
	            }

	            
	            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
	                
	                writer.write(append);
	              
	                writer.newLine();
	            } 
	            catch (IOException e1) {
	            	JOptionPane.showMessageDialog(null, "Error: Cannot append file", "v10.com", JOptionPane.ERROR_MESSAGE);
	            }
	        } 
	        catch (IOException e1) {
	        	JOptionPane.showMessageDialog(null, "Error: Cannot create the file", "v10.com", JOptionPane.ERROR_MESSAGE);
	        }
	        
	        
	        //Text for message	                
	        String itemsList = "";
			for (int i=0; i<itemCount; i++) {
				itemsList = itemsList+(i+1)+". "+cartArray[i][0]+" "+cartArray[i][1]+" $"+cartArray[i][4]+" "+cartArray[i][5]+" "+cartArray[i][6]+"% $"+cartArray[i][7]+"\n";
			} 
			double tax = total*0.06;
			double totalFinal = total+tax;
			
			String invoice = 
					"Date: "+dateInfo
					+"\n\nNumber of line items: "+(itemCount)+"\n\n"
					+ "Item# / ID / Title / Price / Qty / Disc% / Subtotal: \n\n"
					+itemsList
					+"\n\nOrder subtotal:   $"+String.format(Locale.US, "%,.2f", total)
					+"\n\nTax rate:   6%\n\nTax amount:   $"
					+String.format("%.2f",tax)
					+"\n\nORDER TOTAL:   $"+String.format(Locale.US, "%,.2f", totalFinal)
					+"\n\nThanks for shopping at v10.com!\n\n";
			
			JOptionPane.showMessageDialog(null, invoice,"v10.com - FINAL INVOICE", JOptionPane.DEFAULT_OPTION);
			
			idTextField.setEnabled(false);
			quantityTextField.setEnabled(false);
			idTextField.setVisible(false);
			quantityTextField.setVisible(false);
			processButton.setEnabled(false);
			confirmButton.setEnabled(false);
			
			processButton.setEnabled(false);
			confirmButton.setEnabled(false);
			viewButton.setEnabled(true);
			finishButton.setEnabled(false);		
		}
	}
//___________________________________________________________________________________________________	
	private class NewButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			itemCount = 0;
			total = 0.00;
			
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 8; j++) {
					cartArray[i][j] = null;
				}
			}
			
			for (int a = 0; a < dataItem.length; a++) {
				dataItem[a] = null;
			}
			
			for (int c=0; c<5; c++) {
				cartLineArray[c].setText("");
				cartLineArray[c].setEnabled(false);
			}
			idTextField.setText("");
			quantityTextField.setText("");
			detailsTextField.setText("");
			totalTextField.setText("");
			
			idLabel.setText("Enter item ID for Item #" + (itemCount+1) + ":");
			quantityLabel.setText("Enter quantity for Item #" + (itemCount+1) + ":");
			detailsLabel.setText("Details for Item #" + (itemCount+1) + ":");
			totalLabel.setText("Current Subtotal for " + (itemCount) + " item(s):");
			
			processButton.setText("Find Item #" + (itemCount +1));			
			confirmButton.setText("Add Item #" + (itemCount +1) + " To Cart");
			
			processButton.setEnabled(true);
			confirmButton.setEnabled(false);
			viewButton.setEnabled(false);
			finishButton.setEnabled(false);	
			
			idTextField.setEnabled(true);
			quantityTextField.setEnabled(true);
			idTextField.setVisible(true);
			quantityTextField.setVisible(true);
			detailsTextField.setEnabled(false);
			totalTextField.setEnabled(false);
			
		}
	}
//___________________________________________________________________________________________________	
	private class ExitButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

//___________________________________________________________________________________________________	
	
	public static void main(String[] args) {
		JFrame newGUI = new GUI();
		newGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newGUI.setVisible(true);
	}

}
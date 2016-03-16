package main;
import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Class which acts as the main parent Server
 * An instantiation of this class has the responsibilities
 * of connecting to the mysql database, caching the database 
 * entries locally, opening a long running socket and
 * starting child threads for each incoming client connection
 * @author philipgough
 * 
 */
public class MultiThreadedServerA2 {
	// Mapping of account numbers, to client names
	private HashMap <Integer,String> clientMap;
	// Static ServerGUI object to be accessed by n threads
	private static ServerGUI gui = new ServerGUI();;
	
	/**
	 * Constructor for this Class
	 * Calls appropriate methods in order
	 */
	public MultiThreadedServerA2() {
		// Create db connection
		Connection conn = createDbConnection();
		// Put results set in map data structure
		populateResultMap(conn);
		// Make the GUI visible
		createGUI();
		// Attempt to open the socket
		createSocket();				
	}
		
	/**
	 * Method which creates a connection object to the "BankDatabase" db
	 * Catches any exception and prints a message to user
	 * If connection is successful, return Connection object
	 */
	public Connection createDbConnection() {
		
		Connection conn = null;
		try {
			 	Class.forName("com.mysql.jdbc.Driver").newInstance();
			 	conn = DriverManager.getConnection("jdbc:mysql://localhost/BankDatabase", "root", "");
			 	if (!conn.isClosed()) {
			 		System.out.println("Successfully connected to MySQL server...");
			 	}
			}
		
		catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				this.createGUI();
				 gui.textArea.append("There was an error connecting to the database \n");
			 }
		return conn;
	}
	
	/**
	 * Method which instantiates and populates the instance variable clientMap
	 * This method maps each account number in the database to the clients name.
	 * Closes all connections when done
	 */
	public void populateResultMap(Connection conn) {
		this.clientMap = new HashMap<>();
		try {
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM RegisteredApplicants");
			
			while (result.next()) {
				// Iterate through the result set, parse the name and add to map
				int accountNum = result.getInt("AccountNum");
				String firstName = result.getString("FirstName");
				String lastName = result.getString("LastName");
				String clientName = firstName + " " + lastName;
				this.clientMap.put(accountNum, clientName);
				}
			
			//Close the connections
			statement.close();
			result.close();
			conn.close();
			gui.textArea.append("Customer details have been successfully retrieved from database \n");	
		} 
		
		catch (SQLException e) {
			// Handle any errors
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
			e.printStackTrace();
			gui.textArea.append("Unable to retrieve customer data from database \n");
			gui.textArea.append("SQLException: " + e.getMessage());	
		}
	}
	
	
	/**
	 * Try open up a socket on port 8000
	 * Let the gui interface know this is successful or not
	 * For every client connection that hits the accept() method
	 * Create a new thread to deal specifically with this client 
	 * Continue this process in infinite loop to accept n clients
	 */
	public void createSocket(){
		//Keep track of threads
	    int threadCounter = 1;
		try {
			// Create a server socket
			ServerSocket serverSocket = new ServerSocket(8000);
			gui.textArea.append("Server now waiting on connections from clients \n");
			
			while(true) {
				// Listen for a connection request
				Socket socket = serverSocket.accept();
				// Create a thread for this new client
				MyThread clientThread = new MyThread(socket,clientMap,threadCounter);
				// Update the client number
				threadCounter ++;
				clientThread.start();
			}

	    }
	    catch(IOException ex) {
	      System.err.println(ex);
	      gui.textArea.append("There was an error while trying to create a socket \n");
	      gui.textArea.append(ex + " \n"); 
	    }
	}
	
	/**
	 * Set the GUI to visible when required
	 */
	public void createGUI() {
		gui.frame.setVisible(true);
		
	}
	
	/**
	 * Function which makes the static GUI available to each of the threads
	 * This allows multiple threads to write to the singleton GUI
	 * @return ServerGUI gui - the gui object for main server class
	 */
	public static ServerGUI getGui(){
		return gui;
	}
	
	 public static void main(String[] args) {
		    new MultiThreadedServerA2();
		  }   
	
	
}

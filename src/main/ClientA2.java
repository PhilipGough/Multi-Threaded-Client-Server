package main;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * An instantiation of this class represents a client
 * connection to the server. This class is responsible for
 * creating incoming and outgoing data streams to the socket.
 * 
 * @author philipgough
 *
 */
public class ClientA2 {
	
	  // IO streams to the server thread
	  private DataOutputStream toServer;
	  private DataInputStream fromServer;
	  // The GUI for this particular client
	  private ClientGUI gui = new ClientGUI();
	  
	  public ClientA2() {
		  gui.frame.setVisible(true);
		  try {
		      // Create a socket to connect to the server
		       final Socket socket = new Socket("localhost", 8000);
		      
		       // Register listener on submit button
		      gui.btnSubmit.addActionListener(new Listener());
		      
		  	  // Create an input stream to receive data from the server
		      fromServer = new DataInputStream(socket.getInputStream());
		      // Create an output stream to send data to the server
		      toServer = new DataOutputStream(socket.getOutputStream());
		    }
		    catch (IOException ex) {
		      gui.textArea.append(ex.toString() + '\n');
		      gui.textArea.append("Cannot connect to the server, it may not be running!! \n");
		    }
	  }
	  
	  
	 /**
	 * Method that attempts to write data to the server
	 * Flushes buffer when done writing
	 * @param accNum - The account number entered by the user
	 * @param apr - The APR% value entered by the user
	 * @param year - The number of years the loan period is for
	 * @param loan - The total amount of the loan
	 * @return - True if write out to server successful
	 */
	public boolean writeDataToServer(int accNum, double apr, int year, double loan) {
		 
		 try {
			toServer.writeInt(accNum);
			toServer.writeDouble(apr);
			toServer.writeInt(year);
			toServer.writeDouble(loan);
			toServer.flush();
			return true;
		} 
		 catch (IOException e) {
			gui.textArea.append("Problem sending data to server. Please try again \n");
			gui.textArea.append(e.getMessage().toString()+"\n");
			return false;
		}
		
	 }
	 
	 /**
	 * Helper method which validates the input entered
	 * by the user via the GUI are positive values
	 * @param userInput - Value as entered by user
	 * @throws NumberFormatException - if non-positive value
	 */
	public void testForPositiveNum(double userInput){
		 if(userInput <= 0.00) {
			 throw new NumberFormatException();
		 }
	 }
	  
	  public static void main(String[] args) {
		    new ClientA2();
	  }
	  
		
	  
     /**
     * Listener class to add event listener to submit
     * button on the clients gui 
	 * @author philipgough
	 *
	 */
	private class Listener implements ActionListener {
		    /* (non-Javadoc)
		     * Reads and parses each field as entered by the user
		     * Verifies that numbers as opposed to chars are entered
		     * Verifies all fields are non-empty
		     * Verifies all values are positive
		     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		     */
		    @Override
		    public void actionPerformed(ActionEvent e) {
		      try {
		    	  String accField = gui.textField.getText();
		    	  int accountNum = Integer.parseInt(accField);
		    	  testForPositiveNum(accountNum);

		    	  String aprField = gui.textField_1.getText();
		    	  double apr = Double.parseDouble(aprField);
		    	  testForPositiveNum(apr);

		    	  String yearField = gui.textField_2.getText();
		    	  int year = Integer.parseInt(yearField);
		    	  testForPositiveNum(year);

		    	  String loanField = gui.textField_3.getText();
		    	  double loanAmount = Double.parseDouble(loanField);
		    	  testForPositiveNum(loanAmount);
		    	 
		    	  // If the data is sent to server without an error
		    	 if(writeDataToServer(accountNum,apr,year,loanAmount)) {
		    		 
		    	   	 //Read data in from server
		    		 int length=fromServer.readInt();
		    		 byte[] data=new byte[length];
		    		 fromServer.readFully(data);
		    		 String str=new String(data,"UTF-8");
		    		 // Add response to gui text area
		    		 gui.textArea.append(str);
		    		 
		    	 }
		    
		      }
		      catch (IOException ex) {
		        System.err.println(ex);
		      }
		      // Let the user know they have entered incorrect or invalid data
		      catch(NumberFormatException ex){
		    	  gui.textArea.append("Invalid - please ensure only positive numbers have been entered \n "
		    	  		+ "Ensure data has been provided for ALL fields \n"
		    	  		+ "Ensure no non-numeric data has been entered.Thank you \n \n");
		      }
		    }
		
	  }
}

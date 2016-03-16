package main;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import main.MultiThreadedServerA2;

/**
 * An object of this class is spawned as a thread
 * via an object from the main server class. Each instantiation
 * of this class handles a separate client connection to the main server socket.
 * This class is responsible for creating input and output data streams back to
 * the particular client it was created for. It holds a cache of client db data.
 * Each object will have a unique thread number, assigned by main server on creation
 * If a client closes the socket, this thread will return and die
 * @author philipgough
 *
 */
public class MyThread extends Thread {
	
	//The socket the client is connected through
	private Socket socket;
	//The ip address of the client this thread is connected to 
	private InetAddress clientAddress;
	//IO Streams to the client
	private DataInputStream inputFromClient;
	private DataOutputStream outputToClient;
	private HashMap<Integer,String>clientCache;
	//The full name of the client this is connected to
	private String clientsName;
	// The unique thread number of this object
	private String threadNumber;
	// instance var to track connection state
	private boolean initialConnection = true;
	private boolean hasWelcomeMessage = false;
	
	
	public MyThread(Socket socket, HashMap <Integer,String> clientCache,int threadCount) throws IOException{
		this.socket = socket;
		this.clientAddress = this.socket.getInetAddress();
		// Instantiate data input and output streams
		this.inputFromClient = new DataInputStream(this.socket.getInputStream());
		this.outputToClient = new DataOutputStream(this.socket.getOutputStream());
		this.clientCache = clientCache;
		this.threadNumber = String.valueOf(threadCount);
	}

	/* (non-Javadoc)
	 * Method called to start thread from main server
	 * Sends a welcome message to the client on first request
	 * Informs the server gui that a thread has been started 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while(!socket.isClosed()){
			try {
				String sendToClient = "";
				if(this.initialConnection){
					informServerOfConnection();
				}
				// Read the clients account number and verify this account exists
				int accNum = inputFromClient.readInt();
				System.out.println(accNum);

				// Check clients account number exists
				if(verifyAccount(accNum)){
					// Send a welcome message to the client only once
					if(! this.hasWelcomeMessage){
						sendToClient = sendToClient + "Welcome " + clientsName +" \n";
						this.hasWelcomeMessage = true;
					}
					// Read in the variables from the clients gui entries
					double apr = inputFromClient.readDouble();
					int year   = inputFromClient.readInt();
					double loan= inputFromClient.readDouble();
					// Build up a response to send to client
					sendToClient = sendToClient +calculateResponse(apr,year,loan);

				}
				else {
					 clearBuffer();
					 //Inform the client that the given acc number is not registered
					 sendToClient = "Sorry, you are not a registered client.\n";

				}

				// Send data back to the client
				byte[] data=sendToClient.getBytes("UTF-8");
				this.outputToClient.writeInt(data.length);
				this.outputToClient.write(data);
				this.outputToClient.flush();

			} 
			catch (IOException e) {
				writeToGuiConsole("\n Connection with client " + threadNumber+ " is closed \n");
				try {
					// Clean up the socket
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.err.println(e + " on " + socket);
				e.printStackTrace();
				return;
			}

		}
		return;
	}
	
		
	/**
	 * Helper method to check if the provided account number exists
	 * If exists, update the clients name
	 * @param accNum - Account number provided by the client
	 * @return - True if acc number found
	 */
	public boolean verifyAccount(int accNum) {
		if(clientCache.containsKey(accNum)){
			this.clientsName = clientCache.get(accNum);
			return true;
		}
		return false;
	}
	
	/**
	 * Function which calcualtes the value of monthly payments
	 * and total repayments for the client using the information
	 * they have provided via the gui interface.
	 * @param apr - annul percentage rate
	 * @param years - number of years loan required
	 * @param loanAmount - total value of loan required
	 * @return - String - response to provide to the client
	 */
	public String calculateResponse(double apr, int years, double loanAmount){
		int totalMonths = years * 12;
		double rate = apr /1200;
		double calculation = rate / ((Math.pow((1+rate),totalMonths))-1);
		double monthlyPayment = (rate + calculation) * loanAmount;
		monthlyPayment = roundNumbers(monthlyPayment);
		double totalPayment = monthlyPayment * totalMonths;
		totalPayment = roundNumbers(totalPayment);
		String response = "Your monthly repayments would be : " + String.valueOf(monthlyPayment)+"\n";
		response = response + "Your total repayments would come to : " + String.valueOf(totalPayment)+"\n \n";
		return response;
	}
	
	/**
	 * Method that allows a thread to append text to the main sever gui
	 * @param text
	 */
	public void writeToGuiConsole(String text) {
		ServerGUI gui = MultiThreadedServerA2.getGui();
		gui.textArea.append(text);
	}
	
	/**
	 * Method that tells the server a client has connected
	 */
	public void informServerOfConnection(){
		writeToGuiConsole("\n Started thread for client " + threadNumber+ "\n");
		writeToGuiConsole(" Client "+ threadNumber + " hostname is " + clientAddress.getHostName()+"\n");
		writeToGuiConsole(" Client "+ threadNumber + " Inet address is " + clientAddress +"\n");
		this.initialConnection = false;
	}
	
	/**
	 * Method that clears incoming buffer in the case
	 * that a bad request was received from the client
	 * @throws IOException 
	 */
	public void clearBuffer() throws IOException {
		 inputFromClient.readDouble();
		 inputFromClient.readInt();
		 inputFromClient.readDouble();
	}
	
	/**
	 * Helper function to round the double values to the nearest
	 * two decimal places
	 * @param result - Input from monthly payment
	 * @return - Double rounded to two decimal places
	 */
	public double roundNumbers(double value) {
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
	
	


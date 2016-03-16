package main;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;

import javax.swing.JButton;


/**
 * This class builds the GUI for the client window
 * @author philipgough
 *
 */
public class ClientGUI {

	JFrame frame;
	JTextArea textArea;
	JButton btnSubmit;
	JTextField textField;
	JTextField textField_1;
    JTextField textField_2;
	JTextField textField_3;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(2, 0, 20, 20));
		frame.setTitle("Client");
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane);
		
	    textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblAccountNumber = new JLabel("Account Number:");
		GridBagConstraints gbc_lblAccountNumber = new GridBagConstraints();
		gbc_lblAccountNumber.anchor = GridBagConstraints.EAST;
		gbc_lblAccountNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblAccountNumber.gridx = 1;
		gbc_lblAccountNumber.gridy = 0;
		panel.add(lblAccountNumber, gbc_lblAccountNumber);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 3;
		gbc_textField.gridy = 0;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel lblAnnualIntrestRate = new JLabel("Annual Intrest Rate:");
		GridBagConstraints gbc_lblAnnualIntrestRate = new GridBagConstraints();
		gbc_lblAnnualIntrestRate.anchor = GridBagConstraints.EAST;
		gbc_lblAnnualIntrestRate.insets = new Insets(0, 0, 5, 5);
		gbc_lblAnnualIntrestRate.gridx = 1;
		gbc_lblAnnualIntrestRate.gridy = 1;
		panel.add(lblAnnualIntrestRate, gbc_lblAnnualIntrestRate);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 3;
		gbc_textField_1.gridy = 1;
		panel.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JLabel lblYears = new JLabel("Years:");
		GridBagConstraints gbc_lblYears = new GridBagConstraints();
		gbc_lblYears.anchor = GridBagConstraints.EAST;
		gbc_lblYears.insets = new Insets(0, 0, 5, 5);
		gbc_lblYears.gridx = 1;
		gbc_lblYears.gridy = 2;
		panel.add(lblYears, gbc_lblYears);
		
		textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 3;
		gbc_textField_2.gridy = 2;
		panel.add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);
		
		JLabel lblLoanAmount = new JLabel("Loan Amount:");
		GridBagConstraints gbc_lblLoanAmount = new GridBagConstraints();
		gbc_lblLoanAmount.insets = new Insets(0, 0, 5, 5);
		gbc_lblLoanAmount.anchor = GridBagConstraints.EAST;
		gbc_lblLoanAmount.gridx = 1;
		gbc_lblLoanAmount.gridy = 3;
		panel.add(lblLoanAmount, gbc_lblLoanAmount);
		
		textField_3 = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 0);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 3;
		gbc_textField_3.gridy = 3;
		panel.add(textField_3, gbc_textField_3);
		textField_3.setColumns(10);
		
		btnSubmit = new JButton("Submit");
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSubmit.gridx = 3;
		gbc_btnSubmit.gridy = 4;
		panel.add(btnSubmit, gbc_btnSubmit);
		
	}

}

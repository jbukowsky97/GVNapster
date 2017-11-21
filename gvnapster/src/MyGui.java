import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class MyGui extends Observable {

	private boolean userConnected;
	private JTextArea console;
	private JTextField serverHostnameInput;
	private JTextField portInput;
	private JTextField usernameInput;
	private JTextField hostnameInput;
	private JTextField keywordInput;
	private JTextField commandInput;
	private JComboBox<String> speeds;
	private String[][] searchArray;

	/*
	 * helper method for getting padding between labels and panels
	 * returns: a new blank JLabel with 2 spaces of width i.e. "  "
	 */
	public JLabel getPad() {
		JLabel pad = new JLabel("  ");
		return pad;
	}
	
	/*
	 * helper method to determine whether a button should be enabled
	 * based on the JTextField input NOT being blank
	 * a button should be enabled if all the respective fields have been filled out
	 * i.e. username, hostname, port, and server in order to click 'connect'
	 */
	public void buttonChange(JButton button, JTextField input) {
		if (input.getText().trim().equals("") == false) {
			button.setEnabled(true);
		} else {
			button.setEnabled(false);
		}
	}
	
	public void printToConsole(String command) {
		/*
		 * make a time stamp
		 */
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		console.setText(console.getText().concat("[" + sdf.format(cal.getTime()) + "]: "));

		/*
		 * concatenate message to console
		 */
		console.setText(console.getText().concat(command));
		console.setText(console.getText().concat("\n"));
	}

	/*
	 * gui constructor
	 */
	public MyGui() {
		/*
		 * main window
		 */
		JFrame frame = new JFrame();
		
		/*
		 * main panel that will hold the layout for our main three sections
		 * connect, search, and FTP commands
		 */
		JPanel mainPanel = new JPanel();
		
		/*
		 * internet speed options
		 */
		String[] speedStrings = {"Dial-up", "DSL", "Cable", "Satelite", "Broadband", "Fiber", "Other"};
		
		/*
		 * the user is initially NOT connected
		 */
		userConnected = false;

		/*
		 * set the layout of the main panel
		 */
		mainPanel.setLayout(new BorderLayout());

		/*
		 * set the title and preferences for the frame
		 */
		frame.setTitle("GV Nap");
		frame.setMinimumSize(new Dimension(500, 500));
		frame.setSize(frame.getPreferredSize());

		/*
		 * set up the 'connection' part of the main panel
		 */
		JPanel connectionLabel = new JPanel();
		connectionLabel.setBorder(new TitledBorder("Connection"));

		/*
		 * split the connection panel into two panels
		 */
		JPanel connectionTop = new JPanel();
		JPanel connectionBottom = new JPanel();

		/*
		 * create components for upper and lower connection panel
		 */
		JLabel serverHostnameLabel = new JLabel("Server Hostname:");
		JLabel portLabel = new JLabel("Port:");
		JButton connect = new JButton("Connect");
		JButton disconnect = new JButton("Disconnect");
		JLabel usernameLabel = new JLabel("Username:");
		JLabel hostnameLabel = new JLabel("Hostname:");
		JScrollPane searchPane = new JScrollPane();
		
		/*
		 * create variables needed by controller
		 */
		portInput = new JTextField();
		usernameInput = new JTextField();
		serverHostnameInput = new JTextField();
		hostnameInput = new JTextField();
		searchArray = new String[0][0];
		speeds = new JComboBox<String>(speedStrings);

		/*
		 * add components to top portion of connection panel
		 */
		connectionTop.setLayout(new BoxLayout(connectionTop, BoxLayout.LINE_AXIS));
		connectionTop.add(serverHostnameLabel);
		connectionTop.add(getPad());
		connectionTop.add(serverHostnameInput);
		connectionTop.add(getPad());
		connectionTop.add(portLabel);
		connectionTop.add(getPad());
		connectionTop.add(portInput);
		connectionTop.add(getPad());
		connectionTop.add(connect);
		connectionTop.add(disconnect);

		/*
		 * add components to bottom portion of connection panel
		 */
		connectionBottom.setLayout(new BoxLayout(connectionBottom, BoxLayout.LINE_AXIS));
		connectionBottom.add(usernameLabel);
		connectionBottom.add(getPad());
		connectionBottom.add(usernameInput);
		connectionBottom.add(getPad());
		connectionBottom.add(hostnameLabel);
		connectionBottom.add(getPad());
		connectionBottom.add(hostnameInput);
		connectionBottom.add(getPad());
		connectionBottom.add(speeds);

		/*
		 * set the layout for the top and bottom portions of the connection panel
		 * add the necessary components
		 */
		connectionLabel.setLayout(new BorderLayout());
		connectionLabel.add(connectionTop, BorderLayout.NORTH);
		connectionLabel.add(getPad(), BorderLayout.CENTER);
		connectionLabel.add(connectionBottom, BorderLayout.SOUTH);

		/*
		 * 'connect' and 'disconnect' buttons initially not enabled
		 * to enable 'connect' fill out all required fields
		 * to enable 'disconnect' the user must have a successful connection
		 */
		connect.setEnabled(false);
		disconnect.setEnabled(false);

		/*
		 * set up the 'search' portion of the main panel
		 */
		JPanel searchLabel = new JPanel();
		searchLabel.setBorder(new TitledBorder("Search"));

		/*
		 * separate the search panel into top and bottom panels 
		 */
		JPanel searchTop = new JPanel();
		JPanel searchBottom = new JPanel();

		/*
		 * create the components for the search panel
		 */
		JLabel keywordLabel = new JLabel("Keyword:");
		JButton search = new JButton("Search");
		
		/*
		 * create input field needed by controller
		 */
		keywordInput = new JTextField();

		/*
		 * add the gui components to the top portion of the search panel
		 */
		searchTop.setLayout(new BoxLayout(searchTop, BoxLayout.LINE_AXIS));
		searchTop.add(keywordLabel);
		searchTop.add(getPad());
		searchTop.add(keywordInput);
		searchTop.add(getPad());
		searchTop.add(search);

		/*
		 * add the gui components to the bottom portion of the search panel
		 */
		searchBottom.setLayout(new BoxLayout(searchBottom, BoxLayout.LINE_AXIS));
		//searchPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//searchPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		searchBottom.add(searchPane);

		/*
		 * set the layout for the top and bottom portions of the search panel
		 * add the necessary components
		 */
		searchLabel.setLayout(new BorderLayout());
		searchLabel.add(searchTop, BorderLayout.NORTH);
		searchLabel.add(searchBottom, BorderLayout.CENTER);

		/*
		 * 'search' initially not enabled
		 * to enable 'search' keywordInput must NOT be blank AND userConnected must be TRUE
		 * i.e. there has to be a search term and a valid connection
		 */
		search.setEnabled(false);
		keywordInput.setEnabled(false);

		/*
		 * set up the FTP portion of the main panel
		 */
		JPanel ftpLabel = new JPanel();
		ftpLabel.setBorder(new TitledBorder("FTP"));

		/*
		 * separate the FTP portion into an upper and lower section
		 */
		JPanel ftpTop = new JPanel();
		JPanel ftpBottom = new JPanel();

		/*
		 * create the gui components for the FTP panel
		 */
		JLabel commandLabel = new JLabel("Enter Command:");
		JButton go = new JButton("Go");
		console = new JTextArea();
		
		/*
		 * create input field needed by controller
		 */
		commandInput = new JTextField();

		/*
		 * add the gui components to the top portion of the FTP panel
		 */
		ftpTop.setLayout(new BoxLayout(ftpTop, BoxLayout.LINE_AXIS));
		ftpTop.add(commandLabel);
		ftpTop.add(getPad());
		ftpTop.add(commandInput);
		ftpTop.add(getPad());
		ftpTop.add(go);

		/*
		 * add the gui components to the bottom portion of the FTP panel
		 */
		ftpBottom.setLayout(new BoxLayout(ftpBottom, BoxLayout.LINE_AXIS));
		ftpBottom.add(new JScrollPane(console));
		ftpBottom.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() / 5));

		/*
		 * set the layout for the top and bottom portions of the search panel
		 * add the necessary components
		 */
		ftpLabel.setLayout(new BorderLayout());
		ftpLabel.add(ftpTop, BorderLayout.NORTH);
		ftpLabel.add(ftpBottom, BorderLayout.CENTER);

		/*
		 * 'go' initially not enabled
		 * to enable 'go' commandInput must NOT be blank, keywordInput must NOT be blank, AND userConnected must be true
		 * i.e. there must be a search term, a command, and a valid connection to execute a FTP command
		 */
		go.setEnabled(false);
		commandInput.setEnabled(false);
		console.setEditable(false);
		
		/*
		 * add the three sections to the main panel
		 * i.e. connection, search, and FTP
		 */
		mainPanel.add(connectionLabel, BorderLayout.NORTH);
		mainPanel.add(searchLabel, BorderLayout.CENTER);
		mainPanel.add(ftpLabel, BorderLayout.SOUTH);

		/*
		 * add the mainPanel to the frame
		 */
		frame.add(mainPanel);

		/*
		 * set the default close operation for the frame
		 */
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*
		 * make the frame visible
		 */
		frame.setVisible(true);
		
		/*
		 * Action Listeners
		 */
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printToConsole(usernameInput.getText() + "@" + hostnameInput.getText() + " connecting to " + serverHostnameInput.getText() + ":" + portInput.getText() + "...");
				
				/*
				 * upon a successful connection enable/disable the appropriate gui components
				 * i.e. when a user connects they CANNOT change the connection information
				 * they are only able to disconnect through the 'connection' panel
				 * the keywordInput field for the 'search' panel is now available
				 */
				
				setChanged();
				notifyObservers(connect.getText());
				clearChanged();
				
				userConnected = true;
				connect.setEnabled(false);
				disconnect.setEnabled(true);
				serverHostnameInput.setEnabled(false);
				hostnameInput.setEnabled(false);
				usernameInput.setEnabled(false);
				portInput.setEnabled(false);
				speeds.setEnabled(false);
				keywordInput.setEnabled(true);
			}
		});
		
		disconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * clear and reset necessary gui components
				 */
				printToConsole(usernameInput.getText() + "@" + hostnameInput.getText() + " disconnecting...");
				
				setChanged();
				notifyObservers(disconnect.getText());
				clearChanged();
				
				userConnected = false;
				disconnect.setEnabled(false);
				serverHostnameInput.setEnabled(true);
				hostnameInput.setEnabled(true);
				usernameInput.setEnabled(true);
				portInput.setEnabled(true);
				speeds.setEnabled(true);
				search.setEnabled(false);
				go.setEnabled(false);
				commandInput.setEnabled(false);
				keywordInput.setEnabled(false);
				commandInput.setText("");
				keywordInput.setText("");
				searchPane.setViewportView(new JTable());
				buttonChange(connect, serverHostnameInput);
				buttonChange(connect, hostnameInput);
				buttonChange(connect, usernameInput);
				buttonChange(connect, portInput);
			}
		});
		
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * load search data table
				 */
				String[] columnNames = { "Speed", "Hostname", "Filename" };
				DefaultTableModel model = new DefaultTableModel(searchArray, columnNames);
				JTable resultsTable = new JTable(model);
				
				setChanged();
				notifyObservers(search.getText());
				clearChanged();
				
				/*
				 * set the search scroll pane viewport
				 */
				searchPane.setViewportView(resultsTable);
				
				/*
				 * clear and reset necessary gui components
				 */
				keywordInput.setText("");
				search.setEnabled(false);
				commandInput.setEnabled(true);
			}
		});
		
		
		go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * print command to console
				 */
				printToConsole(commandInput.getText());
				
				setChanged();
				notifyObservers(go.getText());
				clearChanged();
				
				/*
				 * clear and reset necessary gui components
				 */
				go.setEnabled(false);
				commandInput.setText("");
			}
		});
		
		/*
		 * Change Listeners
		 * 
		 * these listeners respond to new and removed characters in their parent components text field
		 * they control the buttons relative to their input
		 * if all fields for a button are not complete, then that button will remain unavailable until all fields are NOT blank
		 */
		serverHostnameInput.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				//
			}

			public void insertUpdate(DocumentEvent arg0) {
				if (portInput.getText().trim().equals("") == false 
						&& usernameInput.getText().trim().equals("") == false
						&& hostnameInput.getText().trim().equals("") == false) {
					buttonChange(connect, serverHostnameInput);
				}
			}

			public void removeUpdate(DocumentEvent arg0) {
				if (portInput.getText().trim().equals("") == false 
						&& usernameInput.getText().trim().equals("") == false
						&& hostnameInput.getText().trim().equals("") == false) {
					buttonChange(connect, serverHostnameInput);
				}
			}
		});

		portInput.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				//
			}

			public void insertUpdate(DocumentEvent arg0) {
				if (serverHostnameInput.getText().trim().equals("") == false
						&& usernameInput.getText().trim().equals("") == false
						&& hostnameInput.getText().trim().equals("") == false) {
					buttonChange(connect, portInput);
				}
			}

			public void removeUpdate(DocumentEvent arg0) {
				if (serverHostnameInput.getText().trim().equals("") == false
						&& usernameInput.getText().trim().equals("") == false
						&& hostnameInput.getText().trim().equals("") == false) {
					buttonChange(connect, portInput);
				}
			}
		});

		usernameInput.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				//
			}

			public void insertUpdate(DocumentEvent arg0) {
				if (serverHostnameInput.getText().trim().equals("") == false
						&& portInput.getText().trim().equals("") == false
						&& hostnameInput.getText().trim().equals("") == false) {
					buttonChange(connect, usernameInput);
				}
			}

			public void removeUpdate(DocumentEvent arg0) {
				if (serverHostnameInput.getText().trim().equals("") == false
						&& portInput.getText().trim().equals("") == false
						&& hostnameInput.getText().trim().equals("") == false) {
					buttonChange(connect, usernameInput);
				}
			}
		});

		hostnameInput.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				//
			}

			public void insertUpdate(DocumentEvent arg0) {
				if (serverHostnameInput.getText().trim().equals("") == false
						&& portInput.getText().trim().equals("") == false
						&& usernameInput.getText().trim().equals("") == false) {
					buttonChange(connect, hostnameInput);
				}
			}

			public void removeUpdate(DocumentEvent arg0) {
				if (serverHostnameInput.getText().trim().equals("") == false
						&& portInput.getText().trim().equals("") == false
						&& usernameInput.getText().trim().equals("") == false) {
					buttonChange(connect, hostnameInput);
				}
			}
		});
		
		keywordInput.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				//
			}

			public void insertUpdate(DocumentEvent arg0) {
				if (userConnected == true) {
					buttonChange(search, keywordInput);
				}
			}

			public void removeUpdate(DocumentEvent arg0) {
				if (userConnected == true) {
					buttonChange(search, keywordInput);
				}
			}
		});
		
		commandInput.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				//
			}

			public void insertUpdate(DocumentEvent arg0) {
				buttonChange(go, commandInput);
			}

			public void removeUpdate(DocumentEvent arg0) {
				buttonChange(go, commandInput);
			}
		});
	}
	
	public String getPortInput() {
		return portInput.getText();
	}
	
	public String getUsernameInput() {
		return usernameInput.getText();
	}
	
	public String getServerHostnameInput() {
		return serverHostnameInput.getText();
	}
	
	public String getHostnameInput() {
		return hostnameInput.getText();
	}
	
	public String getKeywordInput() {
		return keywordInput.getText();
	}
	
	public String getCommandInput() {
		return commandInput.getText();
	}
	
	public String getSpeed() {
		return speeds.getSelectedItem().toString();
	}
	
	public void setSearchArray(String[][] newArray) {
		searchArray = newArray;
		
	}
	
	/*
	 * Main Method
	 */
	public static void main(String args[]) {
		new MyGui();
	}
}

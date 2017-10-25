package myGui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

public class myGui {

	public JLabel getPad() {
		JLabel pad = new JLabel("  ");
		return pad;
	}
	
	public void buttonChange(JButton button, JTextField input) {
		if (input.getText().trim().equals("") == false) {
			button.setEnabled(true);
		} else {
			button.setEnabled(false);
		}
	}

	public myGui() {
		JFrame frame = new JFrame();
		JLabel pad = new JLabel();
		JPanel mainPanel = new JPanel();
		//String[][] searchArray;
		
		// searchArray[0] = entry 0, searchArray[1] = entry 1, searchArray[2] = entry 2 ...
		String[][] searchArray = {{"Fiber", "user1", "filesearch.txt"}, {"Satellite", "user2", "search.rtf"}, {"DSL", "user3", "searched.docx"}};
		
		String[] speedStrings = { "Dial-up", "DSL", "Cable", "Satelite", "Broadband", "Fiber", "Other"};

		mainPanel.setLayout(new BorderLayout());

		frame.setTitle("GV Nap");
		frame.setMinimumSize(new Dimension(500, 500));
		frame.setSize(frame.getPreferredSize());

		JPanel connectionLabel = new JPanel();
		connectionLabel.setBorder(new TitledBorder("Connection"));

		JPanel connectionTop = new JPanel();
		JPanel connectionBottom = new JPanel();

		JLabel serverHostnameLabel = new JLabel("Server Hostname:");
		JTextField serverHostnameInput = new JTextField();
		JLabel portLabel = new JLabel("Port:");
		JTextField portInput = new JTextField();
		JButton connect = new JButton("Connect");
		JLabel usernameLabel = new JLabel("Username:");
		JTextField usernameInput = new JTextField();
		JLabel hostnameLabel = new JLabel("Hostname:");
		JTextField hostnameInput = new JTextField();
		JComboBox speeds = new JComboBox(speedStrings);

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

		connectionLabel.setLayout(new BorderLayout());
		connectionLabel.add(connectionTop, BorderLayout.NORTH);
		connectionLabel.add(getPad(), BorderLayout.CENTER);
		connectionLabel.add(connectionBottom, BorderLayout.SOUTH);

		connect.setEnabled(false);

		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
							
			}
		});

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

		JPanel searchLabel = new JPanel();
		searchLabel.setBorder(new TitledBorder("Search"));

		JPanel searchTop = new JPanel();
		JPanel searchBottom = new JPanel();

		JLabel keywordLabel = new JLabel("Keyword:");
		JTextField keywordInput = new JTextField();
		JButton search = new JButton("Search");
		String[] columnNames = { "Speed", "Hostname", "Filename" };
		DefaultTableModel model = new DefaultTableModel(searchArray, columnNames);
		JTable resultsTable = new JTable(model);

		searchTop.setLayout(new BoxLayout(searchTop, BoxLayout.LINE_AXIS));
		searchTop.add(keywordLabel);
		searchTop.add(getPad());
		searchTop.add(keywordInput);
		searchTop.add(getPad());
		searchTop.add(search);

		searchBottom.setLayout(new BoxLayout(searchBottom, BoxLayout.LINE_AXIS));
		searchBottom.add(new JScrollPane(resultsTable));

		searchLabel.setLayout(new BorderLayout());
		searchLabel.add(searchTop, BorderLayout.NORTH);
		searchLabel.add(searchBottom, BorderLayout.CENTER);

		search.setEnabled(false);

		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});

		keywordInput.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				//
			}

			public void insertUpdate(DocumentEvent arg0) {
				buttonChange(search, keywordInput);
			}

			public void removeUpdate(DocumentEvent arg0) {
				buttonChange(search, keywordInput);
			}
		});

		JPanel ftpLabel = new JPanel();
		ftpLabel.setBorder(new TitledBorder("FTP"));

		JPanel ftpTop = new JPanel();
		JPanel ftpBottom = new JPanel();

		JLabel commandLabel = new JLabel("Enter Command:");
		JTextField commandInput = new JTextField();
		JButton go = new JButton("Go");
		JTextArea console = new JTextArea();

		ftpTop.setLayout(new BoxLayout(ftpTop, BoxLayout.LINE_AXIS));
		ftpTop.add(commandLabel);
		ftpTop.add(getPad());
		ftpTop.add(commandInput);
		ftpTop.add(getPad());
		ftpTop.add(go);

		ftpBottom.setLayout(new BoxLayout(ftpBottom, BoxLayout.LINE_AXIS));
		ftpBottom.add(new JScrollPane(console));
		ftpBottom.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() / 5));

		ftpLabel.setLayout(new BorderLayout());
		ftpLabel.add(ftpTop, BorderLayout.NORTH);
		ftpLabel.add(ftpBottom, BorderLayout.CENTER);

		go.setEnabled(false);

		go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				console.setText(console.getText().concat("[" + sdf.format(cal.getTime()) + "]: "));

				console.setText(console.getText().concat(commandInput.getText()));
				console.setText(console.getText().concat("\n"));
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

		mainPanel.add(connectionLabel, BorderLayout.NORTH);
		mainPanel.add(searchLabel, BorderLayout.CENTER);
		mainPanel.add(ftpLabel, BorderLayout.SOUTH);

		frame.add(mainPanel);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String args[]) {
		new myGui();
	}
}

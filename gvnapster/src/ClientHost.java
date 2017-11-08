import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHost {

	/**
	* The maximum file size that may be read in;
	* Integer.MAX_VALUE - 5 keeps the VM from running out of memory.
	*/
	private static final int MAX_FILE_BYTES = Integer.MAX_VALUE - 5;

	/** The path to the client's file directory  */
	private final String ROOT_PATH = ClientHost.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "../client_root/";

	/** Control socket for communicating with the server */
	private Socket controlSocket;

	/** Control output stream for sending commands to the server  */
	private DataOutputStream outToServer;

	/** Control input stream for receiving responses from the server */
	private BufferedReader inFromServer;

	/**
	* Retrieves the specified file from the server
	*
	* @param fileName Name of the file to retrieve from the server
	*/
	private boolean retr(String fileName) {
		// File retrieved successfully
		boolean fileRetr = false;

		// Data socket to receive file
		Socket dataSocket = null;
		InputStream dataFromServer = null;
		ServerSocket welcomeData = null;
		try {
			// Port that the data connection will be established on
			int dataPort = controlSocket.getPort() + 2;

			// Creating the Socket for data connection
			welcomeData = new ServerSocket(dataPort);

			// Notifying the server of the port, command, and file respectively
			outToServer.writeBytes("retr" + " " + fileName + " " + dataPort + "\n");

			// Wait for response from server if file exists
			while (!inFromServer.ready());

			// Exit if the server has no file to be transferred
			if (!inFromServer.readLine().equals("200 command ok")) return fileRetr;

			// Wait for server to establish data connection
			dataSocket = welcomeData.accept();

			// Set up data stream
			dataFromServer = dataSocket.getInputStream();
		} catch (Exception e) {
			// Free resources
			try {
				if (dataSocket != null) dataSocket.close();
			} catch (Exception ex) {
				// Fail quietly
			}

			// Command sending failed, return failure
			return fileRetr;
		} finally {
			try {
				if (welcomeData != null) welcomeData.close();
			} catch (Exception e) {
				// Fail quietly
			}
		}

		// File to write to
		String filePath = ROOT_PATH + fileName;

		int bytesRead;
		int index = 0;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		try {
			// File bytes
			byte[] fileBytes = new byte[MAX_FILE_BYTES];
			fos = new FileOutputStream(filePath);
			bos = new BufferedOutputStream(fos);

			// Continually read the buffer for more file bytes until end of file is recieved
			do {
				// Read bytes from the buffer up to the remaining buffer size
				bytesRead = dataFromServer.read(fileBytes, index, fileBytes.length - index);

				// Shift index by bytesRead
				if (bytesRead >= 0) index += bytesRead;
			} while (bytesRead > -1 && index < MAX_FILE_BYTES);

			// Write bytes read to file
			bos.write(fileBytes, 0, index);
			bos.flush();

			fileRetr = true;

			System.out.println("File " + fileName + " downloaded (" + index + " bytes)");
		} catch (Exception e) {
			// Fail quietly
		}  finally {
			// Free resources
			try {
				if (bos != null) bos.close();
			} catch (Exception e) {
				// Fail quietly
			}

			try {
				if (fos != null) fos.close();
			} catch (Exception e) {
				// Fail quietly
			}

			try {
				if (dataSocket != null) dataSocket.close();
			} catch (Exception e) {
				// Fail quietly
			}
			
		}

		return fileRetr;
	}

	/**
	* Notifies the server of the disconnect and terminates the server connection
	*
	* @return Whether the disconnect was successful
	*/
	private boolean quit() {
		// No server to disconnect from
		if (!isConnected()) return false;

		try {
			outToServer.writeBytes("quit\n");
		} catch (Exception e) {
			// Fail quietly
		} finally {
			// Free resources

			try {
				if (controlSocket != null) controlSocket.close();
			} catch (Exception ex) {
				// Fail quietly
			}

			controlSocket = null;
		}

		return true;
	}


	/**
	* Connects to the server at the specified ip and port
	*
	* @param host String representation of the server address to connect to
	* @param port The port number to connect to on the server
	* @return Whether connect was successful
	*/
	private boolean connect(String host, int port) {
		// Disconnect from current server before connecting to another
		quit();

		try {
			controlSocket = new Socket(host, port);
			outToServer = new DataOutputStream(controlSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
		} catch (Exception e) {
			// Ensure no connection remains
			quit();
		}

		return isConnected();
	}

	/**
 	* A helper method to determine if the client is connected to a server
 	*
 	* @return Whether the client is connected to a server
 	*/
	private boolean isConnected() {
		return controlSocket != null && controlSocket.isConnected() && !controlSocket.isClosed();
	}

	/**
	* Execute the specified command and parameters
	*
	* @param command String of the command to be executed
	*/
	public boolean command(String command) {
		String[] params = command.split(" ");

		// params[0] -> command
		if (params[0].toLowerCase().equals("connect")) {	
			// Attmpting to get a port number
			int port = 0;
			try {
				// params[2] -> port
				port = Integer.parseInt(params[2]);
			} catch (Exception e) {
				// Fail quietly
			}

			// params[1] -> host
			return connect(params[1], port);
		} else if (params[0].toLowerCase().equals("retr")) {
			// params[1] -> file
			return retr(params[1]);
		} else if (params[0].toLowerCase().equals("quit")) {
			return quit();
		}

		return false;
	}

	/**
	* Creates a default ClientHost object
	*/
	public ClientHost() {
		
	}

	public static void main(String[] args) {
		ClientHost c = new ClientHost();

		c.command("connect 35.39.165.117 5338");
		c.command("retr pineapple.jpg");
		c.command("quit");
	}

}

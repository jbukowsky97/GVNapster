import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ThreadHost extends Thread {

	/** The path to the server's file directory */
	private final String ROOT_PATH = ThreadHost.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "../root/";

	/** Control socket for communicating with the client */
	private Socket controlSocket;

	/** Control output stream for sending responses to the client */
	private DataOutputStream outToClient;

	/** Control input stream for receiving commands from the client */
	private BufferedReader inFromClient;

	/** 
	* Attempts to transmit the specified file to the client
	*
	* @param fileName Name of the file to send to the client
	* @param dataPort Port that the server should connect to for message transmission
	*/
	private void retr(String fileName, int dataPort) {
		// Determine if the server has the file specified
		boolean hasFile = false;
		try {
			File folder = new File(ROOT_PATH);

			for (File f: folder.listFiles()) {
				if (f.isFile() && f.getName().equals(fileName)) {
					hasFile = true;
					break;
				}
			}

			// Notify client if file is present to be transmitted
			if (hasFile) {
				outToClient.writeBytes("200 command ok\n");
			} else {
				outToClient.writeBytes("550 file not found\n");
			}
		} catch (Exception e) {
			// Fail quietly
		} finally {
			// There is no file to send, exit
			if (!hasFile) return;
		}

		// Data socket to send file
		Socket dataSocket = null;
		OutputStream dataToClient = null;

		try {
			// Establish data connection with client
			dataSocket = new Socket(controlSocket.getInetAddress(), dataPort);

			// Set up data stream
			dataToClient = dataSocket.getOutputStream();
		} catch (Exception e) {
			// Free resources
			try {
				if (dataSocket != null) dataSocket.close();
			} finally {
				// File sending failed, return
				return;
			}
		}

		// Read and transmit file
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			// Open file for transmission
			File myFile = new File(ROOT_PATH + fileName);

			// File bytes
			byte[] fileBytes = new byte[(int)myFile.length()];
			fis = new FileInputStream(myFile);
			bis = new BufferedInputStream(fis);

			// Reading in the file
			bis.read(fileBytes, 0, fileBytes.length);

			// Sending the file
			dataToClient.write(fileBytes, 0, fileBytes.length);
			dataToClient.flush();

			System.out.println("Sent " + fileName + " (" + fileBytes.length + " bytes)");
		} catch (Exception e) {
			// fail quietly
		} finally {
			// Clean up resources
			try {
				if (bis != null) bis.close();
			} catch (Exception e) {
				// Fail quietly
			}

			try {
				if (fis != null) fis.close();
			} catch (Exception e) {
				// Fail quietly
			}

			try {
				if (dataSocket != null) dataSocket.close();
			} catch (Exception e) {
				// Fail quietly
			}
		}
	}

	/** 
	* Closes the connection to the client
	*/
	private void quit() {
		// Clean up resources
		try {
			if (controlSocket != null) controlSocket.close();
		} catch (Exception e) {
			// Fail quietly
		}

		controlSocket = null;
	}

	/**
	* A helper method to determine if the server is connected to a client
	*
	* @return Whether the server is connected to a client
	*/
	private boolean isConnected() {
		return controlSocket != null && controlSocket.isConnected() && !controlSocket.isClosed();
	}

	/**
	* Operation of this thread
	*/
	public void run() {
		String address = controlSocket.getInetAddress().toString();
		System.out.println(address + " connected");

		while (isConnected()) {
			try {
				// Wait for a command from the client
				while (!inFromClient.ready());

				// Command
				String command = inFromClient.readLine();
				String[] params = command.split(" ");

				// params[0] -> command
				if (params[0].toLowerCase().equals("retr")) {
					// params[1] -> file
					// params[2] -> data port
					retr(params[1], Integer.parseInt(params[2]));
				} else if (params[0].toLowerCase().equals("quit")) {
					quit();
				}
			} catch (Exception e) {
				// Fail quietly
			}
		}

		System.out.println(address + " disconnected\n");
	}

	/** 
	* Create a ThreadHost object with the provided client connection
	*/
	public ThreadHost(Socket controlSocket) {
		this.controlSocket = controlSocket;

		try {
			outToClient = new DataOutputStream(controlSocket.getOutputStream());
			inFromClient = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
		} catch (Exception e) {
			// Close the connection if communications cannot be established
			quit();
		}
	}

}



/**
 * This encomapsses both components necessary for peer to peer communication.
 *
 * @author Troy Madsen
 */
public class Host {

	/** The client of the peer. */
	private ClientHost client;

	/** The server of the peer. */
	private ServerHost server;	

	/**
	* Constructs a new Host object.
	*/
	public Host() {
		// Create a new client
		client = new ClientHost();

		// Create a new server
		server = new ServerHost();
		server.start();
	}

	/**
	* Connects to the specified remote server.
	*
	* @param host Host address to connect to.
	* @param port Port number on host to connect to.
	*/
	public void connect(String host, int port) {
		client.command("connect " + host + " " + port);
	}

	/**
	* Asks the client to retrieve the desired file.
	*
	* @param fileName Name of the file to retrieve from the remote host.
	*/
	public void retr(String fileName) {
		client.command("retr " + fileName);
	}

	/**
	* Disconnects the client from the remote server.
	*/
	public void quit() {
		client.command("quit");
	}

}

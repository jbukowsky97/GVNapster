

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
	}

	/**
	* Asks the client to connect to the specified host and retrieve the desired
	* file.
	*
	* @param host Host ip of the server to connect to.
	* @param port Port on host to connect to.
	* @param fileName Name of the file to retrieve from the remote host.
	*/
	public void retr(String host, int port, String fileName) {
		client.command("connect " + host + " " + port);
		client.command("retr " + fileName);
		client.command("quit");
	}

}

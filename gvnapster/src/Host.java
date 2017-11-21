

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

	/** The client to server connection. */
	private ClientToServer cts;

	/** Search data */
	private String[][] searchData;

	/**
	* Constructs a new Host object.
	*/
	public Host() {
		// Create a new client
		client = new ClientHost();

		//create new clientToServer
		cts = new ClientToServer();

		// Create a new server
		server = new ServerHost();
		server.start();
	}

	/**
	* Provides the search data of the model.
	*
	* @return A two-dimensional String array of the search results from the registration server
	*/
	public String[][] getSearchData() {
		return searchData;
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

	/**
	* Register with the registration server.
	*
	* @param serverHostname Host address of the registration server
	* @param port Port of registration server to connect to
	* @param username Username of the client
	* @param hostname Host address of the client
	* @param speed Speed of the peer connect
	*/
	public void register(String serverHostname, int port, String username, String hostname, String speed) {
		//TODO Sam register with the registration server
		//TODO Send my file list to registration server

		cts.connect(serverHostname, port, , username, speed, hostname);
	}

	/**
	* Query the registratiopn server
	*
	* @param search Search query to send to registration server
	*/
	public void search(String search) {
		searchData = cts.query(search);
	}

	/**
	* Disconnect from the registration server.
	*/
	public void disconnect() {
		cts.disconnect();
	}

}

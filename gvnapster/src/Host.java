public class Host {

	private ClientHost client;
	private ServerHost server;

	public Host() {
		initClient();
		initServer();
	}

	private void initClient() {
		client = new ClientHost();
	}

	private void initServer() {
		server = new ServerHost();
	}

	public ClientHost getClient() {
		return client;
	}

	public ServerHost getServer(){
		return server;
	}

}

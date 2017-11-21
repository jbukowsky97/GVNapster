import java.util.Observable;
import java.util.Observer;

/**
 * The main class of the GVNapster client. Responsible for controlling the view and model of the program.
 *
 * @author Troy Madsen
 */
public class GVNapster implements Observer {

	/** The view component of the MVC set up. */
	MyGui view;

	/** The model component of the MVC set up. */
	Host model;

	/**
	* Constructs a new GVNapster object.
	*/
	public GVNapster() {
		// Create a new peer
		model = new Host();

		// Create a gui and subribe to it
		view = new MyGui();
		view.addObserver(this);
	}

	/**
	* Called by view to notify the controller of input.
	*/
	public void update (Observable o, Object arg) {

		// Cast to action event
		String action = (String)arg;

		if (action.equals("Go")) {

			// Take in command and parse
			String command = view.getCommandInput();
			String[] params = command.split(" ");

			if (params[0].toLowerCase().equals("retr")) {
				if (params.length == 4) {
					// Parse params
					String host = params[1];
					String fileName = params[2];

					// Retrieve file from peer
					model.connect(host, 5340);
					model.retr(fileName);
					model.quit();
				}

			}

		} else if (action.equals("Search")) {

			// Take in search query
			String search = view.getKeywordInput().trim();

			// Have model search for desired query
			model.search(search);

			// Update GUI display
			view.setSearchArray(model.getSearchData());

		} else if (action.equals("Connect")) {

			// Read in registration server fields
			String serverHostname = view.getServerHostnameInput();
			int port = Integer.parseInt(view.getPortInput());
			String username = view.getUsernameInput();
			String hostname = view.getHostnameInput();
			String speed = view.getSpeed();

			// Register with registration server
			model.register(serverHostname, port, username, hostname, speed);

		} else if (action.equals("Disconnect")) {

			// Disconnect from the registration server
			model.disconnect();

		}

	}

	/**
	* Entry point of the GVNapster program.
	*
	* @param args The list of command line arguments to pass at run time.
	*/	
	public static void main(String[] args) {
		new GVNapster();
	}

}

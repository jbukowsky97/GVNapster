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
			String command = view.getCommandInput();
			System.out.println(command);
		} else if (action.equals("Search")) {
			//TODO Read the search box and search the Host for keyword
			String search = view.getKeywordInput();
			System.out.println(search);
		} else if (action.equals("Connect")) {
			//TODO Read the Server Hostname box and Port box and connect to the server
			String serverHostName = view.getServerHostnameInput();
			String port = view.getPortInput();
			String userName = view.getUsernameInput();
			String hostName = view.getHostnameInput();
			String speed = view.getSpeed();
			System.out.println(serverHostName + " " + port + " " + hostName + " " + userName + " " + speed);
		} else if (action.equals("Disconnect")) {
			model.quit();
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

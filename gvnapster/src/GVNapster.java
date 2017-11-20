

/**
 * The main class of the GVNapster client. Responsible for controlling the view and model of the program.
 *
 * @author Troy Madsen
 */
public class GVNapster {

	/** The view component of the MVC set up. */
	MyGui view;

	/** The model component of the MVC set up. */
	Host model;

	/**
	* Constructs a new GVNapster object.
	*/
	public GVNapster() {
		view = new MyGui();
		model = new Host();
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

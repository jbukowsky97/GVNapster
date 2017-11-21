import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int port;

    public Server() {
        port = 6531;
        welcomeFunc();
    }

    public void welcomeFunc() {
        while (true) {
            // Socket to receive new connections to the server
            ServerSocket welcomeSocket = null;

            // New socket being opened
            Socket controlSocket = null;

            System.out.println("Awaiting connections\n");
            try {
                welcomeSocket = new ServerSocket(port);

                // Continually wait for new connections
                while (true) {
                    // Accept new conncetion
                    controlSocket = welcomeSocket.accept();

                    // Create a new thread to handle each client
                    ClientThread thread = new ClientThread(controlSocket);

                    // Start the thread
                    thread.start();
                }
            } catch (Exception e) {
                System.out.println("Error occurred");
            } finally {
                // Clean up resources
                try {
                    if (controlSocket != null) controlSocket.close();
                } catch (Exception e) {
                    // Fail quietly
                }

                try {
                    if (welcomeSocket != null) welcomeSocket.close();
                } catch (Exception e) {
                    // Fail quietly
                }
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
import java.io.*;
import java.net.Socket;

public class ClientToServer extends Thread {

    private String ip, username, connectionSpeed, hostName;
    private int port;
    private File fileList;
    private Socket socket;
    private DataOutputStream outToServer;
    private BufferedReader inFromServer;

    public ClientToServer(){


    }

    public void run(){

    }

    public void connect(String ip, int port, File fileList, String username,
                        String connectionSpeed, String hostName){

        this.ip = ip;
        this.port = port;
        this.username = username;
        this.connectionSpeed = connectionSpeed;
        this.hostName = hostName;

        //create the connection
        try {
            socket = new Socket(ip, port);
            inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToServer = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            //send registration info
            outToServer.writeBytes(username);
            outToServer.writeBytes(connectionSpeed);
            outToServer.writeBytes(hostName);

            //send filelist
            outToServer.wri

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}

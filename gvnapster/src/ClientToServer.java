import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

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
            outToServer.flush();
            outToServer.writeBytes(connectionSpeed);
            outToServer.flush();
            outToServer.writeBytes(hostName);
            outToServer.flush();

            //send filelist
            SAXReader reader = new SAXReader();
            Document document = reader.read(fileList);
            String fileText = document.asXML();
            outToServer.writeBytes(fileText);
            outToServer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

//    public static void main(String[] args){
//        ClientToServer cts = new ClientToServer("", , );
//    }


}

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class ClientToServer {

    private String ip, username, connectionSpeed, hostName;
    private int port;
    private File fileList;
    private Socket socket;
    private DataOutputStream outToServer;
    private BufferedReader inFromServer;

    public ClientToServer(){


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

    public String[][] query(String searchTerm){
        LinkedList<String[]> returnList = new LinkedList<String[]>();
        try {
            outToServer.writeBytes(searchTerm);


            while(!inFromServer.ready());

            while(inFromServer.ready()){

                String[] temp = inFromServer.readLine().split(" ");
                returnList.add(temp);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnList.toArray(new String[returnList.size()][]);

    }

    public void disconnect() {

        try {
            outToServer.writeBytes("disconnect");

            outToServer.close();
            inFromServer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        File filelist = new File("/home/carsonsa/CIS_457/project2/GVNapster/filelist.xml");
        ClientToServer cts = new ClientToServer();
        cts.connect("35.39.165.157", 6531 , filelist,"sam", "DSL", "tomatoes");
    }


}
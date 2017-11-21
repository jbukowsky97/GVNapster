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
            outToServer.writeBytes(username + "\n");
            outToServer.writeBytes(connectionSpeed + "\n");
            outToServer.writeBytes(hostName + "\n");

            //send filelist
            SAXReader reader = new SAXReader();
            Document document = reader.read(fileList);
            String fileText = document.asXML();
            outToServer.writeBytes(fileText + "\n");
            outToServer.writeBytes("end\n");
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
            outToServer.writeBytes(searchTerm + "\n");
            outToServer.flush();

            while(!inFromServer.ready());

            while(inFromServer.ready()){
                String temp = inFromServer.readLine();
                if (temp.equals("none")) {
                    break;
                }
                String[] tempList = temp.split(" ");
                returnList.add(tempList);
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
        File filelist = new File("/home/jonah/Documents/457CIS/GVNapster/filelist.xml");
        ClientToServer cts = new ClientToServer();
        cts.connect("127.0.0.1", 6531 , filelist,"sam", "DSL", "tomatoes");
        String[][] query = cts.query("titties");
        for (int i = 0; i < query.length; i++) {
            System.out.println(query[i][0] + "\t" + query[i][1] + "\t" + query[i][2]);
        }
    }


}
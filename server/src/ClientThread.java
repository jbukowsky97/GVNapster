import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class ClientThread extends Thread {

    /** Control socket for communicating with the client */
    private Socket controlSocket;

    /** Control output stream for sending responses to the client */
    private DataOutputStream outToClient;

    /** Control input stream for receiving commands from the client */
    private BufferedReader inFromClient;

    private String username, connectionSpeed, hostname;

    public ClientThread(Socket controlSocket) {
        this.controlSocket = controlSocket;
        try {
            outToClient = new DataOutputStream(controlSocket.getOutputStream());
            inFromClient = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
        } catch (Exception e) {
            // Close the connection if communications cannot be established
            quit();
        }
    }

    @Override
    public void run() {
        try {
            while (!inFromClient.ready());
            username = inFromClient.readLine();
            connectionSpeed = inFromClient.readLine();
            hostname = inFromClient.readLine();
            System.out.println(username + "@" + hostname + " has connected");
            String xml = "";
            while (true) {
                String temp = inFromClient.readLine();
                if (temp.equals("end")) {
                    break;
                }else {
                    xml += temp;
                }
            }
            Document document = DocumentHelper.parseText(xml);
            Element fileList = document.getRootElement();
            LinkedList<NameDescription> files = new LinkedList<NameDescription>();
            for (Iterator<Element> it = fileList.elementIterator("file"); it.hasNext();) {
                Element file = it.next();
                String name  = file.element("name").getStringValue();
                String description = file.element("description").getStringValue();
                files.add(new NameDescription(name, description));
            }

            ServerData.serverData.add(new Data(username, connectionSpeed, hostname, files));

            runloop: while (true) {
                while (!inFromClient.ready());
                String queryStr = inFromClient.readLine();
                if (queryStr.equals("disconnect")) {
                    outToClient.close();
                    inFromClient.close();
                    controlSocket.close();
                    for (Data d : ServerData.serverData) {
                        if (d.getHostname().equals(hostname) && d.getConnectionSpeed().equals(connectionSpeed) && d.getUsername().equals(username)) {
                            ServerData.serverData.remove(d);
                        }
                    }
                    System.out.println(username + "@" + hostname + " has disconnected");
                    break runloop;
                }
                System.out.println(username + "@" + hostname + " searched for:\t" + queryStr);
                LinkedList<String> returnStrings = new LinkedList<String>();
                for (Data d : ServerData.serverData) {
                    for (NameDescription n : d.getFiles()) {
                        if (n.getName().toLowerCase().contains(queryStr.toLowerCase()) || n.getDescription().toLowerCase().contains(queryStr.toLowerCase())) {
                            returnStrings.add(d.getConnectionSpeed() + " " + d.getHostname() + " " + n.getName());
                        }
                    }
                }
                for (String printStr : returnStrings) {
                    outToClient.writeBytes(printStr + "\n");
                }
                if (returnStrings.size() == 0) {
                    outToClient.writeBytes("none\n");
                }
                outToClient.flush();
            }

        }catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection to the client
     */
    private void quit() {
        // Clean up resources
        try {
            if (outToClient != null) outToClient.close();
            if (inFromClient != null) inFromClient.close();
            if (controlSocket != null) controlSocket.close();
        } catch (Exception e) {
            // Fail quietly
        }
    }
}
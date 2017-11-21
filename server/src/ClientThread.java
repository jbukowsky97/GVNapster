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
            String username, connectionSpeed, hostname;
            username = inFromClient.readLine();
            System.out.println("username:\t" + username);
            connectionSpeed = inFromClient.readLine();
            System.out.println("connectionSpeed:\t" + connectionSpeed);
            hostname = inFromClient.readLine();
            System.out.println("hostname:\t" + hostname);
            String xml = "";
            while (true) {
                String temp = inFromClient.readLine();
                if (temp.equals("end")) {
                    break;
                }else {
                    xml += temp;
                }
            }
            System.out.println("xml:\t" + xml);
            Document document = DocumentHelper.parseText(xml);
            Element fileList = document.getRootElement();
            LinkedList<NameDescription> files = new LinkedList<NameDescription>();
            for (Iterator<Element> it = fileList.elementIterator("file"); it.hasNext();) {
                Element file = it.next();
                String name  = file.element("name").getStringValue();
                String description = file.element("description").getStringValue();
                System.out.println("name:\t" + name + "\n\tdescription:\t" + description);
                files.add(new NameDescription(name, description));
            }

            ServerData.serverData.add(new Data(username, connectionSpeed, hostname, files));
            for (Data d : ServerData.serverData) {
                for (NameDescription n : d.getFiles()) {
                    System.out.println(n.getName() + "\t" + n.getDescription());
                }
            }

            runloop: while (true) {
                while (!inFromClient.ready());
                String queryStr = inFromClient.readLine();
                System.out.println("queryString:\t" + queryStr);
                if (queryStr.equals("disconnect")) {
                    outToClient.close();
                    inFromClient.close();
                    controlSocket.close();
                    break runloop;
                }
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
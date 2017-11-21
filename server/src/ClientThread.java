import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Iterator;

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
            connectionSpeed = inFromClient.readLine();
            hostname = inFromClient.readLine();
            String xml = "";
            while (!inFromClient.ready());
            while (inFromClient.ready()) {
                xml += inFromClient.readLine();
            }
            Document document = DocumentHelper.parseText(xml);
            Element fileList = document.getRootElement();
            for (Iterator<Element> it = fileList.elementIterator("file"); it.hasNext();) {
                Element file = it.next();
                String name  = file.attributeValue("name");
                String description = file.attributeValue("description");
                System.out.println("name:\t" + name + "\n\tdescription:\t" + description);
            }

        }catch (IOException e) {

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
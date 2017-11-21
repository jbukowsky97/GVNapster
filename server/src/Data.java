import java.util.LinkedList;

public class Data {

    private String username;
    private String connectionSpeed;
    private String hostname;
    private LinkedList<String> files;

    public Data(String username, String connectionSpeed, String hostname, LinkedList<String> files) {
        this.username = username;
        this.connectionSpeed = connectionSpeed;
        this.hostname = hostname;
        this.files = files;
    }

    public String getUsername() {
        return username;
    }

    public String getConnectionSpeed() {
        return connectionSpeed;
    }

    public String getHostname() {
        return hostname;
    }

    public LinkedList<String> getFiles() {
        return files;
    }
}

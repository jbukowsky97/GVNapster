import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ServerData {

    public static final List<Data> serverData = Collections.synchronizedList(new LinkedList<Data>());

}

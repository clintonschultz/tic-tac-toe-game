import java.io.IOException;
import java.util.*;
import java.net.*;

public class Dispatcher {

    public static ServerSocket port;

    public static void main(String[] args) throws IOException {

        ServerThread serverthread;
        Socket serverthreadsocket;

        port = new ServerSocket(8787);

        while (true) {
            serverthreadsocket = port.accept();
            serverthread = new ServerThread(serverthreadsocket);
            serverthread.start();
        }
    }
}


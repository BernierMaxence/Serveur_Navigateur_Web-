import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws SocketException {
        //getOpenPorts(80,1200);
        try {
            Thread t = new Thread(new Server());
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getOpenPorts(int startPort, int endPort)
    {
        DatagramSocket ds;
        for(int i=startPort; i<=endPort; i++)
        {
            try {
                ds = new DatagramSocket(i);
            } catch (SocketException e) {
                System.out.println("Port " + i + " fermé");
            }
        }
    }
}


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by Krister on 09/05/2017.
 */
public class ComC implements Runnable {

    private DatagramSocket socket;
    private int port;
    private Server server;
    private InetAddress ip;
    private String username;
    public ComC(String username, InetAddress ip, int port, Server server) throws IOException {
        this.username = username;
        this.server = server;
        this.port = port;
        this.ip = ip;
        socket = new DatagramSocket(port);

        byte[] acceptance = "Approved".getBytes();
        DatagramPacket dp = new DatagramPacket(acceptance, acceptance.length,ip,port);
        socket.send(dp);
        server.sendMessage("{" + username + "}" + " has connected");
    }

    @Override
    public void run() {
        byte[] result = new byte[100];
        DatagramPacket dp = new DatagramPacket(result, result.length);
        try {
            while(true)
            {   result = new byte[100];
                dp.setData(result);
                socket.receive(dp);
                String msg = new String(result);
                if(msg.substring(0, 12).equals("{disconnect}"))
                {
                    System.out.println("Client disconnecting");
                    server.sendMessage("{" + username + "}" + " has disconnected");
                    server.removeClient(this);
                }
                else if(msg.substring(0, 5).equals("{msg}"))
                {
                    msg = msg.substring(msg.indexOf('}')+1);
                    server.sendMessage("{" + username + "} " + msg);
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg)
    {
        System.out.println("IP: " + this.getIp());
        System.out.println(msg);
        byte[] bmsg = msg.getBytes();
        DatagramPacket dpTemp = new DatagramPacket(bmsg, bmsg.length,this.getIp(),port);
        try {
            socket.send(dpTemp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Send to client " + port);
    }

    public InetAddress getIp() {
        return ip;
    }
    public void setIp(InetAddress ip) {
        this.ip = ip;
    }
}

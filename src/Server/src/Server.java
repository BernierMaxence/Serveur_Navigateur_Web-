import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

/**
 * Created by Krister on 09/05/2017.
 */
public class Server implements Runnable {
    DatagramSocket socket;
    int port;
    ArrayList<ComC> clients = new ArrayList<>();
    public Server() throws IOException {
        this.port = port;
        socket = new DatagramSocket(1337);
    }

    @Override
    public void run() {
        byte[] result = new byte[10];

        DatagramPacket dp = new DatagramPacket(result, result.length);
        boolean error = false;
        try {
            dp.setData(new byte[1000]);

            while(true)
            {
                error = false;
                socket.receive(dp);
                System.out.println("ip: " + dp.getAddress());
                System.out.println("Port: " + dp.getPort());
                System.out.println("Msg: " + new String(dp.getData()));
                String data = new String(dp.getData());
                if(data.substring(1,7).equals("conreq"))
                    {
                    data = data.substring(8);
                    String username = data.substring(data.indexOf("{")+1);
                    username = username.substring(0, username.indexOf("}"));
                    String password = data.substring(data.indexOf("}"));
                    password = password.substring(password.indexOf("{")+1);
                    password = password.substring(0, password.indexOf("}"));
                    if(login(username, password))
                    {
                        clients.add(new ComC(username, dp.getAddress(), dp.getPort(), this));
                        Thread t = new Thread(clients.get(clients.size()-1));
                        t.start();
                        System.out.println("Starting server " + clients.size() + " on " + dp.getPort());
                        error = true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg)
    {
        for(ComC client : clients)
        {
            client.sendMessage(msg);
        }
    }

    public void removeClient(ComC client)
    {
        clients.remove(client);
    }

    public boolean login(String username, String password)
    {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader("src/userfile.kmod");
            br = new BufferedReader(fr);
            String line;
            String[] data;
            while((line = br.readLine()) != null)
            {
                data = line.split(" ");
                if(data[0].equals(username) && data[1].equals(password))
                {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

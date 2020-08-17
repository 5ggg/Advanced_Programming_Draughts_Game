import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author jialiang Song 2410536s
 *
 * This class contains the user list, responsible for accepting user connections
 * and creating service threads for each user.
 *
 * By the way, there will be a Player class. It is the the thread class
 * responsible for interacting with users, and be created by the Server class.
 */
public class Server {

    public static void main(String[] args) throws UnknownHostException {
        Server server = new Server();
        server.start();
    }

    public void start() {
        final int PORT = 8888;

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started. The port is: " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();

                Player player = new Player(socket);
                players.add(player);
                new Thread(player).start();
            }

        } catch (IOException e) {
            System.out.println("Server Error: " + e.getMessage());
        }
    }

    private List<Player> players = new ArrayList<>();

    public class Player implements Runnable {
        private ObjectOutputStream output;
        private ObjectInputStream input;

        public Player(Socket socket) throws IOException {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            if (players.size() == 1) {
                players.get(0).sendObject("StartWhite");
            }
        }

        private void sendObject(Object object) {
            try {
                output.writeObject(object);
                output.flush();
            } catch (Exception e) {
                players.remove(this);
            }
        }

        public void run() {
            try {
                while (true) {
                    Object object = input.readObject();
                    System.out.println("Receive: " + object.getClass().toString());

                    for (Player other : players) {
                        if (other != this) {
                            other.sendObject(object);
                        }
                    }
                }
            } catch (Exception e) {
                players.remove(this);
            }
        }
    }

}

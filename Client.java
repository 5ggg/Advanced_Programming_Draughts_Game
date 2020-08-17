import javax.swing.JOptionPane;

/**
 * @author jialiang Song 2410536s
 *
 * The Client class (is a Client...) that create and display the GUI
 */
public class Client {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 8888; // it will throw "Server Error: Address already in use: JVM_Bind" if conflict.

        GUI gui = new GUI();
        gui.setVisible(true);

        try {
            gui.ConnectServer(host, port);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "failed to connect server, " + e.getMessage());
            System.exit(0);
        }
    }

}

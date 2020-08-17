import java.awt.Font;
import java.awt.Graphics;
import java.awt.JobAttributes;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author jialiang Song 2410536s
 *
 * The class GUI drew the chess board and handle user click events.
 * Also responsible for interacting with the Server.
 *
 */
public class GUI extends JFrame implements Runnable {
    private Draughts draughts;
    private PieceType myType;
    private boolean myTurn = false;

    private JLabel status;

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public GUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        setContentPane(new GamePanel());
        draughts = new Draughts();
        myType = PieceType.Red;
    }

    public void ConnectServer(String host, int port) throws UnknownHostException, IOException {
        socket = new Socket(host, port);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());

        new Thread(this).start();
    }

    public void setTurn() {
        if (myTurn) {
            if (myType == PieceType.Red) {
                status.setText("Red's Turn");
            } else {
                status.setText("Black's Turn");
            }
        } else {
            if (myType == PieceType.Red) {
                status.setText("Black's Turn");
            } else {
                status.setText("Red's Turn");
            }
        }
    }

    public void run() {
        try {
            while (true) {

                Object object = input.readObject();

                if (object instanceof String) {
                    String str = (String) object;
                    System.out.println(str);

                    if (str.equals("StartWhite")) {
                        setTitle("Draughts - Red");

                        draughts.reset();
                        myType = PieceType.Red;
                        myTurn = true;
                        setTurn();

                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {

                        }
                        output.writeObject("StartBlack");
                    } else if (str.equals("StartBlack")) {
                        setTitle("Draughts - Black");

                        draughts.reset();
                        myType = PieceType.Black;
                        myTurn = false;
                        setTurn();
                    }
                } else if (object instanceof Draughts) {
                    System.out.println("Draughts");

                    myTurn = true;
                    setTurn();

                    this.draughts = (Draughts) object;
                    repaint();
                }
            }
        } catch (Exception e) {

        }
    }

    public PieceType getOpponentType() {
        if (myType == PieceType.Black) {
            return PieceType.Red;
        } else {
            return PieceType.Black;
        }
    }

    private class GamePanel extends JPanel {
        private int x = 10;
        private int y = 10;

        private Cell start, end;

        public GamePanel() {
            setLayout(null);
            status = new JLabel();
            status.setFont(new Font("Monospaced", Font.PLAIN, 20));
            status.setBounds(10, 40, 500, 40);
            status.setText("Waiting for Your Opponent");
            add(status);

            y = 120;

            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!myTurn) {
                        return;
                    }

                    int clickX = e.getX();
                    int clickY = e.getY();

                    Cell cell = draughts.getCell(x, y, clickX, clickY);

                    if (cell != null) {
                        Piece piece = cell.getPiece();

                        if (piece != null && piece.getType() == myType) {
                            draughts.clearSelect();
                            cell.setSelected(true);
                            start = cell;

                            repaint();
                        } else if (start != null && piece == null) {
                            end = cell;

                            draughts.clearSelect();

                            if (draughts.move(start, end)) {

                                repaint();

                                try {
                                    output.writeObject(draughts);
                                    output.flush();
                                    System.out.println("Move Finish");
                                    myTurn = false;
                                    setTurn();

                                    if (draughts.count(getOpponentType()) == 0) {
                                        status.setText("You Win!");
                                        output.writeObject("You Lose!");
                                        output.flush();

                                        try {
                                            Thread.sleep(500);
                                        } catch (Exception e1) {

                                        }

                                        draughts.reset();
                                        myType = PieceType.Red;
                                        myTurn = true;
                                        setTurn();

                                        try {
                                            Thread.sleep(2000);
                                        } catch (Exception e1) {

                                        }

                                        setTitle("Draughts - Red");
                                        output.writeObject("StartBlack");
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

                            } else {
                                JOptionPane.showMessageDialog(GUI.this, "Invalid Move");
                            }

                            start = end = null;
                            repaint();
                        }
                    }
                }
            });
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            draughts.draw(x, y, g);
        }

    }
}

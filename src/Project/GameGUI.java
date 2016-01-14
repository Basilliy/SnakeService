package Project;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameGUI extends JFrame implements Runnable {
    private JPanel mainPanel;
    private JPanel gamePanel;
    private JLabel place1st;
    private JLabel place2st;
    private JLabel place3st;
    private JLabel place4st;
    private JLabel place5st;
    private JLabel place6st;
    private JLabel place7st;
    private JLabel place8st;
    private JPanel notationForServer;


    private Player player;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean working = true;

    public GameGUI(Player player, ObjectOutputStream out, ObjectInputStream in,  boolean server, JFrame owner) {
        notationForServer.setVisible(server);
        this.out = out;
        this.in = in;
        this.player = player;

        gamePanel.setSize(Const.RESTRICTIONS_MAX.x, Const.RESTRICTIONS_MAX.y);
        gamePanel.setMinimumSize(gamePanel.getSize());
        gamePanel.setMaximumSize(gamePanel.getSize());

        setLocationRelativeTo(owner);
        setTitle("SnakeService - " + player.getName());
        setSize(Const.RESTRICTIONS_MAX.x + 334, Const.RESTRICTIONS_MAX.y + 50 );
        setMinimumSize(getSize());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setVisible(true);
    }

    @Override
    public void run() {
        System.out.println("game is run");
//        while (working)
//            System.out.println(getSize());
    }
}

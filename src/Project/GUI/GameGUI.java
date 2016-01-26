package Project.GUI;

import Project.SentObjects.Board;
import Project.Const;
import Project.SentObjects.Player;
import Project.SentObjects.Score;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;

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


    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean working = true;
    private boolean canChangeWay = false;
    private int way;
    private int[][] board = new int[Const.RESTRICTION.x / Const.DELAY]
            [Const.RESTRICTION.y / Const.DELAY];
    private JLabel[] labels = new JLabel[8];
    private Image apple;
    private Image body0;
    private Image[] body = new Image[8];
    private Image[] head = new Image[4];
    private Image[] beetle = new Image[4];
    private BufferedImage backgroundImage = new BufferedImage(Const.RESTRICTION.x,
            Const.RESTRICTION.y, BufferedImage.TYPE_INT_RGB);
    private Graphics g = backgroundImage.getGraphics();


    public GameGUI(Player player, ObjectOutputStream out, ObjectInputStream in,
                   boolean server, JFrame owner) {
        notationForServer.setVisible(server);
        this.out = out;
        this.in = in;

        try {
            body0 = ImageIO.read(new File(Const.PATH + "body.png"));
            apple = ImageIO.read(new File(Const.PATH + "apple.png"));
            head[0] = ImageIO.read(new File(Const.PATH + "headDown.png"));
            head[1] = ImageIO.read(new File(Const.PATH + "headLeft.png"));
            head[2] = ImageIO.read(new File(Const.PATH + "headRight.png"));
            head[3] = ImageIO.read(new File(Const.PATH + "headUp.png"));
            beetle[0] = ImageIO.read(new File(Const.PATH + "beetleDown.png"));
            beetle[1] = ImageIO.read(new File(Const.PATH + "beetleLeft.png"));
            beetle[2] = ImageIO.read(new File(Const.PATH + "beetleRight.png"));
            beetle[3] = ImageIO.read(new File(Const.PATH + "beetleUp.png"));
            for (int i = 0; i < 8; i++)
                body[i] = ImageIO.read(new File(Const.PATH + "body" + (i + 1) + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        gamePanel.setSize(Const.RESTRICTION.x, Const.RESTRICTION.y);
        gamePanel.setMinimumSize(gamePanel.getSize());
        gamePanel.setMaximumSize(gamePanel.getSize());

        setFocusable(true);
        addKeyListener(new myKeyAdapter());

//        setLocationRelativeTo(owner);
        setTitle("SnakeService - " + player.getName());
        if (server) setSize(Const.RESTRICTION.x + 300, Const.RESTRICTION.y + 50);
        else setSize(Const.RESTRICTION.x + 160, Const.RESTRICTION.y + 50);
        setMaximumSize(getSize());
        setMinimumSize(getSize());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        labels[0] = place1st;
        labels[1] = place2st;
        labels[2] = place3st;
        labels[3] = place4st;
        labels[4] = place5st;
        labels[5] = place6st;
        labels[6] = place7st;
        labels[7] = place8st;
        setVisible(true);
    }

    @Override
    public void run() {
        while (working) {
            try {
                Object object = in.readObject();
                if (object.getClass().equals(Board.class)) {
                    board = ((Board) object).getBoard();
                    repaintBoard();
                } else
                if (object.getClass().equals(Score.class)) {
                    setScore(((Score) object).getScore());
                } else
                if (object.getClass().equals(String.class)) {
                    String s = (String) object;
                    s = s.substring(Const.STATUS.length());
                    JOptionPane.showMessageDialog(this, s);
                } else
                if (object.getClass().equals(Integer.class)) {
                    int i = (int) object;
                    System.out.println("int: " + i);
                } else
                    System.out.println("GameGUI: Not supported " + object.getClass());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(GameGUI.this, "Сервер отключился");
                System.exit(0);
                break;
            }
        }
    }

    private void setScore(String[] s) {
//        System.out.println("Arrays: " + Arrays.toString(s));
        for (JLabel label: labels) label.setVisible(false);
        for (int i = 0; i < s.length; i++) {
            labels[i].setVisible(true);
            labels[i].setText(s[i]);
        }
    }

    private void repaintBoard() {
        g.setColor(Color.lightGray);
        g.fillRect(0,0,backgroundImage.getWidth(),backgroundImage.getHeight());

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Image image = null;
                int d = board[i][j];
                if (d != 0) {
                    if (d > 0) {
                        if (d%10 != 4) {
                            image = head[d%10];
                        } else {
                            switch (d) {
                                case 14: image = body[0]; break;
                                case 24: image = body[1]; break;
                                case 34: image = body[2]; break;
                                case 44: image = body[3]; break;
                                case 54: image = body[4]; break;
                                case 64: image = body[5]; break;
                                case 74: image = body[6]; break;
                                case 84: image = body[7]; break;
                                default: image = body0;
                            }
                        }
                    } else {
                        if (d == Const.APPLE) image = apple;
                        else image = beetle[-d % 10];
                    }
                }

                g.drawImage(image, i*Const.DELAY, j*Const.DELAY, null);
            }
        }

        Graphics graphics = gamePanel.getGraphics();
        graphics.drawImage(backgroundImage, 0, 0, null);
        canChangeWay = true;
    }

    private class myKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int i;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: i = Const.UP; break;
                    case KeyEvent.VK_A: i = Const.LEFT; break;
                    case KeyEvent.VK_S: i = Const.DOWN; break;
                    case KeyEvent.VK_D: i = Const.RIGHT; break;
                    default: i = 0;
                }
                    way = i;
                    try {
                        out.writeObject(canChangeWay);
                        out.writeObject(way);
                        out.reset();
                        canChangeWay = false;
                    } catch (IOException e1) { e1.printStackTrace(); }
        }
    }

}

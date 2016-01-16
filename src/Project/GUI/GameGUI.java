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
    private int[][] board = new int[Const.RESTRICTIONS_MAX.x / Const.DELAY]
            [Const.RESTRICTIONS_MAX.y / Const.DELAY];
    private JLabel[] labels = new JLabel[8];
    private Image apple;
    private Image body0;
    private Image[] body = new Image[8];
    private Image[] head = new Image[4];
    private BufferedImage backgroundImage = new BufferedImage(Const.RESTRICTIONS_MAX.x,
            Const.RESTRICTIONS_MAX.y, BufferedImage.TYPE_INT_RGB);
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
            for (int i = 0; i < 8; i++)
                body[i] = ImageIO.read(new File(Const.PATH + "body" + (i + 1) + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        gamePanel.setSize(Const.RESTRICTIONS_MAX.x, Const.RESTRICTIONS_MAX.y);
        gamePanel.setMinimumSize(gamePanel.getSize());
        gamePanel.setMaximumSize(gamePanel.getSize());
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(new myKeyAdapter());

        setLocationRelativeTo(owner);
        setTitle("SnakeService - " + player.getName());
        setSize(Const.RESTRICTIONS_MAX.x + 334, Const.RESTRICTIONS_MAX.y + 50 );
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

        //Тестовый код:Начало
        /*
        {
            int[][] ints = new int[][]{
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, -1, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 14, 14, 12, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 21, 24, 24, 24, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 0, 0, 0},
                    {0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };

            int[] sc = new int[4];
            String[] n = new String[4];

            sc[0] = 4;
            sc[1] = 2;
            sc[2] = 6;
            sc[3] = 4;

            n[0] = "rik";
            n[1] = "fgd";
            n[2] = "ggg";
            n[3] = "bas";

            Score score = new Score(sc, n);
            score.addScore(-40, "bas");
            System.out.println("before1: " + Arrays.toString(score.getScore()));

            try {
                out.writeObject(score);
                out.reset();
                out.writeObject(new Board(ints));
                out.reset();
                score.addScore(30, "bas"); // -6
                System.out.println("before2: " + Arrays.toString(score.getScore()));
                out.writeObject(score);
                out.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */
        //Тестовый код:Конец

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
                Image image;
                if (board[i][j] == -1) {
                    image = apple;
                    g.drawImage(image, j*Const.DELAY, i*Const.DELAY, null);
                }
                if (board[i][j] > 0) {
                    int d = board[i][j];
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
                    g.drawImage(image, j*Const.DELAY, i*Const.DELAY, null);
                }
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

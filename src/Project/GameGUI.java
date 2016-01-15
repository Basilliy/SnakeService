package Project;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

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
        this.player = player;

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

        int[][] ints = new int[][]{
                {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
                {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
                {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
                {0,  0,  0,  0,  0,  14, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
                {0,  0,  0,  0,  0,  14, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
                {0,  0, -1,  0,  0,  14, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
                {0,  0,  0,  0,  0,  14, 14, 12, 0,  0,  0,  0,  0,  0,  0,  0},
                {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
                {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
                {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
                {0,  0,  0,  0,  0,  0,  21, 24, 24, 24, 0,  0,  0,  0,  0,  0},
                {0,  0,  0,  0,  0,  0,  0,  0,  0,  24, 0,  0,  0,  0,  0,  0},
                {0,  0, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
                {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
                {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
                {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}
        };
        String st = createMap(ints);
        try {
            out.writeObject(st);
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (working) {
            try {
                Object object = in.readObject();
                if (object.getClass().equals(String.class)) {
                    String s = (String) object;
                    System.out.println(s);
                    if (s.startsWith(Const.BOARD)) {
                        s = s.substring(Const.BOARD.length());
                        board = readBoard(s);
                        repaintBoard();
                    }
                    if (s.startsWith(Const.STATUS)) {
                        s = s.substring(Const.STATUS.length());
                        setScore(s);
                    }
                } else System.out.println("Другой тип");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setScore(String s) {
        for (JLabel label: labels) label.setVisible(false);
        String[] split = s.split(";");
        for (int i = 0; i < split.length; i++) {
            labels[i].setVisible(true);
            String[] strings = split[i].split(",");
            labels[i].setText(strings[0] + " " + strings[1]);
        }
    }

    private int[][] readBoard(String s) {
        String[] split = s.split(";");
        int[][] map = new int[split.length][split[0].length() / 2 + 1];
        for (int i = 0; i < map.length; i++) {
            String[] strings = split[i].split(",");
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = Integer.parseInt(strings[j]);
            }
        }
        return map;
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
    }


    private String createMap(int[][] n) {
        StringBuilder sb = new StringBuilder();
        sb.append(Const.BOARD);
        for (int[] aN : n) {
            for (int j = 0; j < n[0].length; j++) {
                sb.append(aN[j]);
                if (j != n[0].length - 1)
                    sb.append(",");
            }
            sb.append(";");
        }
        return sb.toString();
    }
}

package Project.GUI;

import Project.Const;
import Project.SentObjects.Player;
import Project.ServerController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.ArrayList;

public class RoomGUI extends JFrame implements Runnable {
    private JPanel mainPanel;
    private JTextArea chatTextArea;
    private JButton chatButtonEnter;
    private JTextField chatTextField;
    private JScrollPane chatScroll;
    private JLabel bottomLabel;
    private JTable roomTable;
    private JButton roomButtonReady;
    private JButton roomButtonStartGame;
    private JComboBox<String> roomComboBox;
    private JButton buttonExit;
    private JLabel labelLimit;
    private JSlider scoreLimitSlider;

    private Player player;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ArrayList<Player> players = new ArrayList<>();
    private boolean server;
    private boolean working = true;

    public RoomGUI(ObjectOutputStream out, ObjectInputStream in, String name, JFrame owner, boolean server) {
        if (Const.PLAYER_LIST != null) players = Const.PLAYER_LIST;
        this.server = server;
        setTitle(name);
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(450, 500));
        setSize(460, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(owner);
        roomButtonStartGame.setVisible(server);
        scoreLimitSlider.setVisible(server);

        labelLimit.setText("Очков для победы: " + Const.VICTORY);

        roomComboBox.removeAllItems();
        roomComboBox.addItem(Const.STRING_NOT_CHOSEN);
        roomComboBox.addItem(Const.STRING_UP_LEFT);
        roomComboBox.addItem(Const.STRING_UP);
        roomComboBox.addItem(Const.STRING_UP_RIGHT);
        roomComboBox.addItem(Const.STRING_RIGHT);
        roomComboBox.addItem(Const.STRING_DOWN_RIGHT);
        roomComboBox.addItem(Const.STRING_DOWN);
        roomComboBox.addItem(Const.STRING_DOWN_LEFT);
        roomComboBox.addItem(Const.STRING_LEFT);
        try {
            this.out = out;
            this.in = in;
            bottomLabel.setText(name + " (" + InetAddress.getLocalHost() + ")");
            player = new Player(name, InetAddress.getLocalHost().getHostAddress(), false, Const.MAP_NOT_CHOSEN);
            Const.PLAYER = player;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(RoomGUI.this, "Ошибка подключения");
            System.exit(0);
        }
        buttonExit.addActionListener(e -> {
            setVisible(false);
            working = false;
            new SelectRoomGUI(this);
            try {
                out.writeObject(Const.EXIT);
                out.reset();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        scoreLimitSlider.setMinimum(Const.VICTORY_MIN);
        scoreLimitSlider.setMaximum(Const.VICTORY_MAX);
        scoreLimitSlider.setValue(Const.VICTORY);
        scoreLimitSlider.addChangeListener(e -> {
            try {
                out.writeObject(scoreLimitSlider.getValue());
                out.reset();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        roomComboBox.addActionListener(e -> changePosition(roomComboBox.getSelectedIndex()));
        roomButtonStartGame.addActionListener(e -> createGame());
        roomButtonReady.addActionListener(e -> setReady());
        chatButtonEnter.addActionListener(e -> {
            enterText();
            chatTextField.setFocusable(true);
        });
        chatTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    enterText();
            }
        });
        setVisible(true);
    }

    private void createGame() {
        try {
            out.writeObject(Const.START);
            out.reset();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void startGame() {
        if (server) ServerController.setGameIsStarted(true);
        setVisible(false);
        working = false;
        Const.ROOM = this;
        GameGUI gameGUI = new GameGUI(player, out, in, server, RoomGUI.this);
        Thread thread = new Thread(gameGUI);
        thread.start();
    }

    public void enterText() {
        if (!chatTextField.getText().equals("")) {
            try {
                out.writeObject(Const.CHAT + player.getName() + ": " + chatTextField.getText() + "\n");
                out.reset();
            } catch (IOException e) { e.printStackTrace(); }
            JScrollBar scrollBar = chatScroll.getVerticalScrollBar();
            scrollBar.setValue(scrollBar.getMaximum());
            chatTextField.setText("");
        }
    }

    private void changePosition(int position) {
        player.setPositionOnMap(position);
        try {
            out.writeObject(player);
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setReady() {
        if (player.getPositionOnMap() != Const.MAP_NOT_CHOSEN) {
            player.setReady(!player.isReady());
            roomComboBox.setEnabled(!player.isReady());
            Const.PLAYER = player;
            try {
                out.writeObject(player);
                out.reset();
            } catch (IOException ignored) {}
        } else {
            JOptionPane.showMessageDialog(RoomGUI.this, "Для начала игры необходимо выбрать стартовую позицию");
        }
    }

    private void updatePlayers() {
        Const.PLAYER_LIST = players;
        String[] columnNames = new String[]{"Имя", "Адресс", "Готовность", "Позиция"};
        Object[][] obj = new Object[players.size()][4];
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            obj[i][0] = p.getName();
            obj[i][1] = p.getAddress();
            if (p.isReady()) obj[i][2] = "Готов";
            else obj[i][2] = "Не готов";
            String position;
            switch (p.getPositionOnMap()) {
                case Const.MAP_UP_LEFT: position = Const.STRING_UP_LEFT; break;
                case Const.MAP_UP: position = Const.STRING_UP; break;
                case Const.MAP_UP_RIGHT: position = Const.STRING_UP_RIGHT; break;
                case Const.MAP_RIGHT: position = Const.STRING_RIGHT; break;
                case Const.MAP_DOWN_RIGHT: position = Const.STRING_DOWN_RIGHT; break;
                case Const.MAP_DOWN: position = Const.STRING_DOWN; break;
                case Const.MAP_DOWN_LEFT: position = Const.STRING_DOWN_LEFT; break;
                case Const.MAP_LEFT: position = Const.STRING_LEFT; break;
                default: position = Const.STRING_NOT_CHOSEN;
            }
            obj[i][3] = position;
        }
        DefaultTableModel model = new DefaultTableModel(obj, columnNames);
        roomTable.setModel(model);

        if (server) {
            boolean b = true;
            for (Player o : players) {
                b &= o.isReady();
            }
            roomButtonStartGame.setEnabled(b);
        }
    }

    @Override
    public void run() {
        try {
            out.writeObject(player);
            out.reset();
        } catch (IOException se) { /*Nothing TO DO */ }
        while (working) {
            try {
                Object object = in.readObject();
                if (object.getClass().equals(Player.class)) {
                    player = (Player) object;
                    bottomLabel.setText(player.getName() + " (" + InetAddress.getLocalHost() + ")");
                } else if (object.getClass().equals(ArrayList.class)) {
                    players = (ArrayList<Player>) object;
                    updatePlayers();
                } else if (object.getClass().equals(String.class)) {
                    String s = (String) object;
                    if (s.startsWith(Const.START)) {//Если сообщение старта игры
                        startGame();
                    }
                    if (s.startsWith(Const.ERROR)) { //Если сообщение ошибки
                        s = s.substring(Const.ERROR.length(), s.length());
                        RoomGUI.this.setVisible(false);
                        JOptionPane.showMessageDialog(RoomGUI.this, s);
                        System.exit(2);
                    }
                    if (s.startsWith(Const.CHAT)) { //Если сообщение чата
                        s = s.substring(Const.CHAT.length(), s.length());
                        if (!s.equals("")) {
                            chatTextArea.setText(chatTextArea.getText() + s);
                            JScrollBar scrollBar = chatScroll.getVerticalScrollBar();
                            scrollBar.setValue(scrollBar.getMaximum());
                        }
                    }
                } else if (object.getClass().equals(Integer.class)) {
                    Const.VICTORY = (int) object;
                    labelLimit.setText("Очков для победы: " + Const.VICTORY);
                } else
                    System.out.println("RoomGUI: Not supported " + object.getClass());
                Thread.yield();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(RoomGUI.this, "Сервер отключился");
                System.exit(0);
                break;
            }
        }
    }

}

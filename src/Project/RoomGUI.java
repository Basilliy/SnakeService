package Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
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

    private Player player;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ArrayList players = new ArrayList<>();
    private boolean server;
    private boolean working = true;

    public RoomGUI(Socket socket, String name, JFrame owner, boolean server) {
        this.server = server;
        setTitle(name);
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(450, 500));
        setSize(450, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(owner);
        roomButtonStartGame.setVisible(server);
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            bottomLabel.setText(name + " (" + InetAddress.getLocalHost() + ")");
            player = new Player(name, InetAddress.getLocalHost().getHostAddress(), false);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(RoomGUI.this, "Ошибка подключения");
            System.exit(0);
        }
        roomButtonStartGame.addActionListener(e2 -> createGame());
        roomButtonReady.addActionListener(e1 -> setReady());
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
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void startGame() {
        working = false;
        RoomGUI.this.setVisible(false);
        GameGUI gameGUI = new GameGUI(player, out, in, server, RoomGUI.this);
        Thread thread = new Thread(gameGUI);
        thread.start();
    }

    public void enterText() {
        if (!chatTextField.getText().equals("")) {
            try {
                out.writeObject(Const.CHAT + player.getName() + ": " + chatTextField.getText() + "\n");
            } catch (IOException e) { e.printStackTrace(); }
            JScrollBar scrollBar = chatScroll.getVerticalScrollBar();
            scrollBar.setValue(scrollBar.getMaximum());
            chatTextField.setText("");
        }
    }

    private void setReady() {
        player = new Player(player.getName(), player.getAddress(), !player.isReady());
        try {
            out.writeObject(player);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void updatePlayers() {
        String[] columnNames = new String[]{"Номер", "Имя", "Адресс", "Готовность"};
        Object[][] obj = new Object[players.size()][4];
        for (int i = 0; i < players.size(); i++) {
            Player p = (Player) players.get(i);
            obj[i][0] = i+1;
            obj[i][1] = p.getName();
            obj[i][2] = p.getAddress();
            if (p.isReady()) obj[i][3] = "Готов";
            else obj[i][3] = "Не готов";
        }
        DefaultTableModel model = new DefaultTableModel(obj, columnNames);
        roomTable.setModel(model);

        boolean b = true;
        for (Object o: players) {
            Player p = (Player) o;
            b &= p.isReady();
        }
        roomButtonStartGame.setEnabled(b);
    }

    @Override
    public void run() {
        try {
            out.writeObject(player);
        } catch (IOException se) { /*Nothing TO DO */ }
        while (working) {
            try {
                Object object = in.readObject();
                if (object.getClass().equals(String.class)) {
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
                    if (s.startsWith(Const.STATUS)) { //Если сообщение сатуса
                        s = s.substring(Const.STATUS.length(), s.length());
                        players = new ArrayList();
                        String[] split = s.split(";");
                        for (String st : split) {
                            String[] strings = st.split(",");
                            Player p = new Player(strings[0], strings[1], strings[2].equals("true"));
                            players.add(p);
                            updatePlayers();
                        }
                    }
                }
                Thread.yield();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(RoomGUI.this, "Сервер отключился");
                System.exit(0);
                break;
            }
        }
    }


}

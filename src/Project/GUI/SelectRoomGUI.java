package Project.GUI;

import Project.Const;
import Project.ServerController;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

public class SelectRoomGUI extends JFrame{
    private JPanel mainPanel;
    private JButton buttonConnectToRoom;
    private JButton buttonAddRoom;
    private JButton buttonCreateRoom;
    private JButton buttonDeleteRoom;
    private JButton buttonUpdateRooms;
    private JList<Object> listRoom;
    private JTextField textField;
    private JLabel bottomLabel;

    private File rooms = new File(Const.PATH_DATA + "Servers.txt");
    private ArrayList<ListItem> list;
    private final static int PORT = Const.PORT;
    private Properties properties = new Properties();


    public SelectRoomGUI(JFrame owner) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(owner);
        setMinimumSize(new Dimension(400, 390));
        setSize(new Dimension(400, 390));
        setContentPane(mainPanel);
        setVisible(true);
        try {
            bottomLabel.setText(InetAddress.getLocalHost().toString());
            String name = InetAddress.getLocalHost().getHostName();
            properties.load(new FileInputStream(new File(Const.PATH_CONFIG)));
            String s = properties.getProperty(Const.CONFIG_NAME);
            if (s == null) {
                properties.setProperty(Const.CONFIG_NAME, name);
                properties.store(new FileOutputStream(new File(Const.PATH_CONFIG)),
                        "File for store data about name of player");
            }
            else name = s;

            textField.setText(name);
        } catch (Exception ignore) {}

        readRooms();

        buttonAddRoom.addActionListener(e -> {
            writeRoom(JOptionPane.showInputDialog(SelectRoomGUI.this, "Введи ip-адресс или название сервера",
                    "Добавление комнаты", JOptionPane.PLAIN_MESSAGE));
            readRooms();
        });

        buttonUpdateRooms.addActionListener(e -> updateRooms());

        buttonConnectToRoom.addActionListener(e -> {
            if (listRoom.getSelectedIndex() >= 0)
                startLikeClient((ListItem) listRoom.getSelectedValue(),textField.getText());
        });

        buttonCreateRoom.addActionListener(e -> startLikeServer(textField.getText()));

        buttonDeleteRoom.addActionListener(e -> deleteRoom(listRoom.getSelectedIndex()));
    }

    private void writeRoom(String ip) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(rooms, true));
            writer.write(ip);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            try {
                boolean b = rooms.createNewFile();
                if (!b) System.out.println("Отсутствует путь src/Data");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void readRooms() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(rooms));
            list = new ArrayList<>();
            while (reader.ready()) {
                String s = reader.readLine();
                if (s.length() > 1)
                    list.add(new ListItem(s));
            }
            reader.close();
            listRoom.setListData(list.toArray());
        } catch (FileNotFoundException e) {
            try {
                if (!rooms.createNewFile()) JOptionPane.showMessageDialog(SelectRoomGUI.this,
                        "Отсутствует путь Data");
            } catch (IOException e1) { /*Nothing TO DO */ }
        } catch (IOException e) { /*Nothing TO DO */ }
    }

    private void updateRooms() {
        for (ListItem item : list) {
            item.isConnected = false;
            item.name = null;
        }
        listRoom.setListData(list.toArray());

        for (ListItem item : list) {
            try {
                InetAddress inetAddress = InetAddress.getByName(item.ip);
                if (inetAddress.isReachable(1500)) {
                    item.isConnected = true;
                    item.name = inetAddress.getCanonicalHostName();
                } else {
                    item.isConnected = false;
                    item.name = inetAddress.getCanonicalHostName();
                }
            } catch (Exception e) {
                item.isConnected = false;
                e.printStackTrace();
            }
            listRoom.setListData(list.toArray());
        }
    }

    private void deleteRoom(int index) {
        list.remove(index);
        listRoom.setListData(list.toArray());
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(rooms));
            for (ListItem item: list) {
                writer.write(item.ip);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startLikeClient(ListItem item, String name) {
        if (!name.equals("")) {
            try {
                Socket socket = new Socket(InetAddress.getByName(item.ip), PORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Const.SERVER_OUT = out;
                Const.SERVER_IN = in;
                this.setVisible(false);
                saveNameInConfig();
                RoomGUI roomGUI = new RoomGUI(out, in, name, SelectRoomGUI.this, false);
                (new Thread(roomGUI)).start();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(SelectRoomGUI.this, "Не получилось подключится к комнате");
            }
        } else { JOptionPane.showMessageDialog(SelectRoomGUI.this, "Введите имя"); }
    }

    private void startLikeServer(String name) {
        if (!name.equals("")) {
            try {
                new ServerController(PORT);
                System.out.println("LoopbackAddress = " + InetAddress.getLoopbackAddress());
                System.out.println("LocalHost = " + InetAddress.getLocalHost());
                Socket socket = new Socket(InetAddress.getLoopbackAddress(), PORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Const.IS_SERVER = true;
                Const.SERVER_OUT = out;
                Const.SERVER_IN = in;
                this.setVisible(false);
                saveNameInConfig();
                RoomGUI roomGUI = new RoomGUI(out, in, name, SelectRoomGUI.this, true);
                (new Thread(roomGUI)).start();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(SelectRoomGUI.this, "Не получилось подключится к комнате");
            }
        } else {
            JOptionPane.showMessageDialog(SelectRoomGUI.this, "Введите имя");
        }
    }

    private void saveNameInConfig() {
        String name = textField.getText();
        if (!Objects.equals(name, "")) {
            try {
                properties.setProperty(Const.CONFIG_NAME, name);
                properties.store(new FileOutputStream(new File(Const.PATH_CONFIG)),
                        "File for store data about name of player");
            } catch (Exception ignore) {}
        }
    }

    private class ListItem {
        public String name;
        public String ip;
        public boolean isConnected;

        public ListItem(String ip) {
            this.ip = ip;
        }

        @Override
        public String toString() {
            if (name == null) return ip;
            if (name.equals(ip))
                if (isConnected) return ip + " : Доступно";
                else return ip + " : Недоступно";
            if (isConnected) return name + " : " + ip + " : Доступно";
            else return ip + " : Недоступно";
        }
    }

    public static void main(String... console) throws Exception {
//        new SelectRoomGUI(null);
        new MenuGUI();
    }
}

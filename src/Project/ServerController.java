package Project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerController extends Thread {
    private static int PORT;
    private static boolean listening = true;
    private static final ArrayList<ObjectOutputStream> oos = new ArrayList<>();
    private static final ArrayList<Player> players = new ArrayList<>();
    private static int[] ways;
    private static boolean gameIsStarted = false;
    private int p = 0;

    public ServerController(int PORT) {
        super("ServerController");
        ServerController.PORT = PORT;


        start();
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (listening) {
                boolean b;
                synchronized (oos) { b = oos.size() < 8; }
                Socket socket = serverSocket.accept();
                ObjectOutputStream socketOOS = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream socketOIS = new ObjectInputStream(socket.getInputStream());
                if (!gameIsStarted) {
                    if (b) {
                        synchronized (oos) {
                            oos.add(socketOOS);
                        }
                        ServerThread readerThread = new ServerThread(oos, socketOIS, socketOOS, players, ways);
                        readerThread.start();
                    } else {
                        socketOOS.writeObject(Const.ERROR + "Невозможно подключится\n" +
                                "Достигнуто максимальное число игроков в комнате");
                    }
                } else {
                    socketOOS.writeObject(Const.ERROR + "Невозможно подключится\nИгра уже началась");
                }
                ServerController.yield();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Не удается прослушать порт " + PORT);
        }
    }

    public static void setGameIsStarted(boolean b) {
        synchronized ((Boolean)gameIsStarted) {
            gameIsStarted = b;
        }

        if (gameIsStarted) {
            new MagicMachine(oos, players, ways);
        }
    }
}


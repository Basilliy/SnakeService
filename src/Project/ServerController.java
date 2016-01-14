package Project;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerController extends Thread {
    private static int PORT;
    public boolean listening = true;
    private static final ArrayList<ObjectOutputStream> oos = new ArrayList<>();
    private static final ArrayList<Player> players = new ArrayList<>();

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
                synchronized (oos) { b = oos.size() < 2; }
                Socket socket = serverSocket.accept();
                ObjectOutputStream socketOOS = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream socketOIS = new ObjectInputStream(socket.getInputStream());
                if (b) {
                    synchronized (oos) { oos.add(socketOOS); }
                    System.out.println("ServerThread " + oos.size());
                    ServerThread readerThread = new ServerThread(oos, socketOIS, socketOOS, players);
                    readerThread.start();
                } else {
                    socketOOS.writeObject(Const.ERROR + "Достигнуто максимальное число игроков в комнате");
                }
                ServerController.yield();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Не удается прослушать порт " + PORT);
        }
    }
}


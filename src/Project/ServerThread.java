package Project;

import java.io.*;
import java.util.ArrayList;

public class ServerThread extends Thread {

    public boolean listening = true;
    private final ArrayList<ObjectOutputStream> oos;
    private final ObjectInputStream in;
    private final ObjectOutputStream forRemove;
    private final ArrayList<Player> players;
    private Player player;

    public ServerThread(ArrayList<ObjectOutputStream> oos, ObjectInputStream in,
                        ObjectOutputStream forRemove, ArrayList<Player> players) {
        this.oos = oos;
        this.in = in;
        this.forRemove = forRemove;
        this.players = players;
    }

    @Override
    public synchronized void run() {
        boolean bName = true;
        while (listening) {
            try {
                Object object = in.readObject();
                if (object.getClass().equals(Player.class)) {
                    player = (Player) object;
                    if (bName) {
                        bName = false;
                        synchronized (players) {
                            players.add(player);
                        }
                        synchronized (oos) {

                            for (ObjectOutputStream out : oos) {
                                out.writeObject(Const.STATUS + createStatus());
                                out.writeObject(Const.CHAT + "В комнату вошел игрок " + player.getName() + "\n");
                            }
                        }
                    } else {
                        synchronized (players) {
                            for (Player p: players) {
                                if (p.getName().equals(player.getName())) {
                                    p.setReady(player.isReady());
                                    synchronized (oos) {
                                        for (ObjectOutputStream out : oos) out.writeObject(Const.STATUS + createStatus());
                                    }
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    synchronized (oos) {
                        for (ObjectOutputStream out : oos) out.writeObject(object);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                synchronized (players){
                    players.remove(player);
                }
                synchronized (oos) {
                    oos.remove(forRemove);
                    e.printStackTrace();
                    System.out.println("ServerThread " + oos.size());
                    for (ObjectOutputStream out : oos) {
                        try {
                            out.writeObject(Const.STATUS + createStatus());
                            out.writeObject(Const.CHAT + "Игрок " + player.getName() + " покинул комнату\n");
                        } catch (IOException e1) { /*Nothing TO DO */ }
                    }
                }
                break;
            }
        }
    }

    private String createStatus() {
        String status = "";
        synchronized (players) {
            for (Player u : players) {
                status += u.getName() + ",";
                status += u.getAddress() + ",";
                status += u.isReady() + ";";
            }
        }
        return status;
    }
}

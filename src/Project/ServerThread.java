package Project;

import Project.SentObjects.Player;

import java.io.*;
import java.util.ArrayList;

public class ServerThread extends Thread {

    public boolean listening = true;
    private final ArrayList<ObjectOutputStream> oos;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final ArrayList<Player> players;
    private Player player;
    private int[] ways;

    public ServerThread(ArrayList<ObjectOutputStream> oos, ObjectInputStream in,
                        ObjectOutputStream forRemove, ArrayList<Player> players, int[] ways) {
        this.oos = oos;
        this.in = in;
        this.out = forRemove;
        this.players = players;
        this.ways = ways;
    }

    @Override
    public synchronized void run() {
        boolean bName = true;
        while (listening) {
            try {
                Object object = in.readObject();
                if (object.getClass().equals(Boolean.class)) {
                    boolean can = (boolean) object;
                    int nWay = (int) in.readObject();
                    if (can) {
                        int position = 0;
                        synchronized (players) {
                            for (int i = 0; i < players.size(); i++)
                                if (players.get(i).getName().equals(player.getName())) {
                                    position = i;
                                    break;
                                }
                        }
                        if (ways != null) {
                            synchronized (ways) {
                                if ((ways[position] + nWay) != 3)
                                    ways[position] = nWay;
                            }
                        }
                    }
                } else
                if (object.getClass().equals(Player.class)) {
                    player = (Player) object;
                    if (bName) {
                        bName = false;
                        synchronized (players) {
                            player = checkPlayerName(player);
                            out.writeObject(player);
                            out.reset();
                            players.add(player);
                        }
                        sendAll(players);
                        sendAll(Const.CHAT + "В комнату вошел игрок " + player.getName() + "\n");
                    } else {
                        synchronized (players) {
                            for (Player p: players) {
                                if (p.getName().equals(player.getName())) {
                                    p.setReady(player.isReady());
                                    p.setPositionOnMap(player.getPositionOnMap());
                                    sendAll(players);
                                    break;
                                }
                            }
                        }
                    }
                } else if (object.getClass().equals(String.class)) {
                    String s = (String) object;
                    if (s.startsWith(Const.START)) {
                        if (checkPlayersPositions()) sendAll(Const.START);
                        else sendAll(Const.CHAT + "<Сервер>:Начальные позиции всех " +
                                "игроков не должны совпадать\n");
                    } else if (s.equals(Const.EXIT)) {
                        throw new Exception("Выход игрока по нажатию кнопки");
                    } else {
                        sendAll(object);
                    }
                } else {
                    sendAll(object);
                }
            } catch (Exception e) {
                e.printStackTrace();
                synchronized (players) {
                    for (Player p : players)
                        if (p.getName().equals(player.getName())) {
                            players.remove(p); break;
                        }
                }
                synchronized (oos) { oos.remove(out); }
                sendAll(players);
                sendAll(Const.CHAT + "Игрок " + player.getName() + " покинул комнату\n");
                if (ServerController.gameIsStarted) thoughtOutPlayer();
                break;
            }
        }
    }

    private void thoughtOutPlayer() {
        MagicMachine.score.playerCameOut(player.getName());
        for (Snake snake : MagicMachine.snakes)
            if (snake.player.getName().equals(player.getName())) {
                snake.kill();
                break;
            }
    }

    private void sendAll(Object message) {
        synchronized (oos) {
            try {
                for (ObjectOutputStream out : oos) {
                    out.writeObject(message);
                    out.reset();
                }
            } catch (IOException ignored) { }
        }
    }

    private boolean checkPlayersPositions() {
        boolean check = true;
        for (int i = 0; i < players.size(); i++) {
            int checkingPosition = players.get(i).getPositionOnMap();
            check &= checkingPosition != Const.MAP_NOT_CHOSEN;
            for (int j = 0; j < players.size(); j++)
                if (i != j) check &= checkingPosition != players.get(j).getPositionOnMap();
        }
        return check;
    }

    private Player checkPlayerName(Player player) {
        int postfix = 2;
        String name = player.getName();
        synchronized (players) {
            while (true) {
                boolean trip = true;
                for (Player p : players) {
                    trip &= !p.getName().equals(name);
                }
                if (trip) return new Player(name, player.getAddress(), player.isReady(), player.getPositionOnMap());
                else {
                    name = player.getName() + postfix++;
                }
            }
        }
    }
}

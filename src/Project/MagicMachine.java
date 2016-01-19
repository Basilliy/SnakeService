package Project;

import Project.SentObjects.Board;
import Project.SentObjects.Player;
import Project.SentObjects.Score;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MagicMachine {
    private final ArrayList<ObjectOutputStream> oos;
    private final ArrayList<Player> players;
    private final ArrayList<Snake> snakes = new ArrayList<>();
    private int[] ways;
    private Score score;
    private Timer timer;
    private int[][] board = new int[Const.RESTRICTIONS_MAX.x / Const.DELAY]
            [Const.RESTRICTIONS_MAX.y / Const.DELAY];

    public MagicMachine(ArrayList<ObjectOutputStream> oos, ArrayList<Player> players, int[] ways) {
        this.oos = oos;
        this.players = players;
        int size = oos.size();
        if (ways == null) ways = new int[players.size()];
        this.ways = ways;
        String[] playersNames = new String[size];
        for (int i = 0; i < players.size(); i++)
            playersNames[i] = players.get(i).getName();
        score = new Score(new int[size], playersNames);

        // Установка начальное значение направлений
        for (int i = 0; i < players.size(); i++) {
            int way = -1;
            switch (players.get(i).getPositionOnMap()) {
                case Const.MAP_UP_LEFT:
                case Const.MAP_UP:
                case Const.MAP_UP_RIGHT: way = 0; break;
                case Const.MAP_RIGHT: way = 1; break;
                case Const.MAP_DOWN_RIGHT:
                case Const.MAP_DOWN:
                case Const.MAP_DOWN_LEFT: way = 3; break;
                case Const.MAP_LEFT: way = 2; break;
            }
            ways[i] = way;
        }

//          Принять начальное значение положения
        int position;
        int x=0, y=0;
        for (int i = 0; i < players.size(); i++) {
           position =  players.get(i).getPositionOnMap();
            switch (position){
                case Const.MAP_UP_LEFT : x = 1; y = 4; break;
                case Const.MAP_UP : x = 15; y = 4; break;
                case Const.MAP_UP_RIGHT : x = 29; y = 4; break;
                case Const.MAP_RIGHT : x = 26; y = 16; break;
                case Const.MAP_DOWN_RIGHT : x = 29; y = 26; break;
                case Const.DOWN : x = 16; y = 26; break;
                case Const.MAP_DOWN_LEFT : x = 1; y = 26; break;
                case Const.MAP_LEFT : x = 4; y = 15; break;
            }
            Snake sn = new Snake(x, y, ways[i], i + 1);
//            sn.bodyIncrease();
//            sn.bodyIncrease();
            snakes.add(sn);
        }
        mapPrint();
        // Установика начальное значение очков
        for (Player player : players)  //Всем плюс +3
            score.addScore(3, player.getName());

        timer = new Timer(Const.TIMER, e -> doSomething());
        timer.start();
    }

    public void mapPrint() {
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board.length; j++)
                board[i][j] = 0;

        for (Snake s : snakes) s.paint(board);
    }


    // TODO: 15.01.2016 В этом методе установить дейсвтие каждого шага:
    // TODO: 15.01.2016 1) расчет карты
    // TODO: 15.01.2016 2) начисление очков
    // TODO: 15.01.2016 Отправление данных клиентам происходит по методу toClient()
    private void doSomething() {
        synchronized (ways) {

        }


        toClient();
    }

    private void toClient() {
        System.out.println("ways = " + ways[0]);
        for (int[] ints : board) {
            for (int anInt : ints) System.out.print(anInt);
            System.out.println();
        }
        synchronized (oos) {
            for (ObjectOutputStream out : oos) {
                try {
                    out.writeObject(new Board(board));
                    out.writeObject(score);
                    out.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

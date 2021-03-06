package Project;

import Project.SentObjects.Board;
import Project.SentObjects.Player;
import Project.SentObjects.Score;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class MagicMachine {
    private final ArrayList<ObjectOutputStream> oos;
    public static ArrayList<Player> players;
    public static ArrayList<Snake> snakes;
    public static ArrayList<Beetle> beetles;
    private final int[] ways;
    public static Score score;
    public static int[][] board;
    public static Apple mainApple;
    private Timer timer;

    public MagicMachine(ArrayList<ObjectOutputStream> oos, ArrayList<Player> players, int[] ways) {
        board = new int[Const.SIZE][Const.SIZE];
        snakes = new ArrayList<>();
        beetles = new ArrayList<>();
        this.oos = oos;
        MagicMachine.players = players;
        int size = oos.size();
        this.ways = ways;
        String[] playersNames = new String[size];
        for (int i = 0; i < players.size(); i++)
            playersNames[i] = players.get(i).getName();
        score = new Score(new int[size], playersNames);

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
            Snake sn = new Snake(x, y, ways[i], i + 1, board);

            snakes.add(sn);
        }

        new Apple(10,10);
        new Apple(11,10);
        new Apple(12,10);
        new Apple(13,10);
        new Apple(10,11);
        new Apple(11,11);
        new Apple(12,11);
        new Apple(13,11);
        new Apple(10,12);
        new Apple(11,12);
        new Apple(12,12);
        new Apple(13,12);
        new Apple(10,13);
        new Apple(11,13);
        new Apple(12,13);
        new Apple(13,13);

        timer = new Timer(Const.TIMER, e -> doSomething());
        timer.start();
    }

    // TODO: 15.01.2016 В этом методе установить дейсвтие каждого шага:
    // TODO: 15.01.2016 1) расчет карты
    // TODO: 15.01.2016 2) начисление очков
    // TODO: 15.01.2016 Отправление данных клиентам происходит по методу toClient()
    private void doSomething() {
        synchronized (ways) {
            for (int i = 0; i < players.size(); i++) snakes.get(i).turn(ways[i]);
        }
        if (mainApple == null) mainApple = new Apple(createPointForApple());
        board[mainApple.point.x][mainApple.point.y] = -1;
        for (Snake snake : snakes)
            if (snake.isAlive())
                snake.move();

        for (Beetle beetle : beetles)
            beetle.move();

        write(new Board(board));
        write(score);
        String winner = score.getWinner();
        if (winner != null) write(Const.WINNER + winner);
    }

    private Point createPointForApple() {
        Random random = new Random();
        int x;
        int y;
        while (true) {
            x = random.nextInt(board.length);
            y = random.nextInt(board[0].length);
            if (board[x][y] == 0) break;
        }
        return new Point(x,y);
    }

    private void write(Object o) {
        synchronized (oos) {
            for (ObjectOutputStream out : oos) {
                try {
                    out.writeObject(o);
                    out.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        timer.stop();
    }

}

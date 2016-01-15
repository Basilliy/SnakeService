package Project;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MagicMachine {
    private final ArrayList<ObjectOutputStream> oos;
    private final ArrayList<Player> players;
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

        // TODO: 15.01.2016 Установить начальное значение направлений
        // TODO: 15.01.2016 Установить начальное значение положения
        // TODO: 15.01.2016 Установить начальное значение очков (сделано)
        for (Player player : players)  //Всем плюс +3
            score.addScore(3, player.getName());

        timer = new Timer(Const.TIMER, e -> doSomething());
        timer.start();
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

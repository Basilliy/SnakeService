package Project.SentObjects;

import Project.Const;
import Project.MagicMachine;
import Project.Snake;
import sun.text.resources.no.CollationData_no;

import java.io.Serializable;
import java.util.Arrays;

public class Score implements Serializable {
    private int[] scores;
    private String[] names;

    public Score(int[] scores, String[] names) {
        this.names = names;
        this.scores = scores;
    }

    public void addScore(int score, String name) {
        for (int i = 0; i < names.length; i++)
            if (names[i].equals(name)) {
                scores[i] += score;
                break;
            }

    }

    public String[] getScore() {
        sort();
        String[] s = new String[scores.length];
        for (int i = 0; i < s.length; i++) {
            s[i] = scores[i] + " " + names[i];
        }
        return s;
    }

    public void playerCameOut(String name) {
        for (int i = 0; i < names.length; i++)
            if (names[i].equals(name)) {
                names[i] += " (вышел)";
                break;
            }
        for (Player player : MagicMachine.players)
            if (player.getName().equals(name)) {
                MagicMachine.players.remove(player);
                break;
            }
    }

    public void playerKilled(String name) {
        for (int i = 0; i < names.length; i++)
            if (names[i].equals(name)) {
                names[i] += " (убит)";
                break;
            }
    }

    public String getWinner() {
        sort();
        if (scores[0] >= Const.VICTORY) return names[0];
        else {
            int c = 0;
            for (Snake snake : MagicMachine.snakes) if (snake.isAlive()) c++;
            if (c == 1 || c == 0) return names[0];
            else return null;
        }
    }

    private void sort() {
        for (int i = 0; i < scores.length; i++)
            for (int j = 0; j < scores.length; j++)
                if (scores[i] > scores[j]) {
                    int t1 = scores[i];
                    scores[i] = scores[j];
                    scores[j] = t1;
                    String t2 = names[i];
                    names[i] = names[j];
                    names[j] = t2;
                }
    }

}

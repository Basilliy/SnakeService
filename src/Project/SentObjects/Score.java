package Project.SentObjects;

import java.io.Serializable;

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
        for (int i = 0; i < scores.length; i++) {
            for (int j = 0; j < scores.length; j++) {
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
        String[] s = new String[scores.length];
        for (int i = 0; i < s.length; i++) {
            s[i] = scores[i] + " " + names[i];
        }
        return s;
    }
}

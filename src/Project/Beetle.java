package Project;

import java.awt.*;
import java.util.Random;

public class Beetle {
    public Point point;
    private int way;
    private int counter = 0;

    public Beetle(Point point) {
        this.point = point;
        MagicMachine.beetles.add(this);
        turn();
        MagicMachine.board[point.x][point.y]= Const.BEETLE - way;
    }

    public Beetle(int x, int y) {
        point = new Point(x,y);
        MagicMachine.beetles.add(this);
        turn();
        MagicMachine.board[point.x][point.y]= Const.BEETLE - way;
    }

    public void move() {
        if (counter >= 0) {
            Point p = Snake.toMove(point, way);
            if (MagicMachine.board[p.x][p.y] == 0) {
                MagicMachine.board[point.x][point.y] = 0;
                point = p;
                MagicMachine.board[point.x][point.y]= Const.BEETLE - way;
                counter--;
            }
            else turn();
        } else turn();
    }

    private void turn() {
        Random random = new Random();
        counter = random.nextInt(4) + 2;
        way = random.nextInt(4);
    }
}

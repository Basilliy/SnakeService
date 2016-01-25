package Project;

import java.awt.*;

public class Apple {
    public Point point;

    public Apple(Point point) {
        this.point = point;
        MagicMachine.board[point.x][point.y] = -1;
    }

    public Apple(int x, int y) {
        point = new Point(x,y);
        MagicMachine.board[point.x][point.y] = -1;
    }

}

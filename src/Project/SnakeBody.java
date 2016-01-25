package Project;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class SnakeBody {
    public int way;
    public Point point;
    public Queue<Integer> wayTurn = new LinkedList<>(); //Очередь изменений напрвлений блока
    public Queue<Point> wayPoint = new LinkedList<>(); //Очередь точек поворота
    private int[][] board;

    public SnakeBody(Point point, int way, int[][] board){
        this.board = board;
        this.point = point;
        this.way = way;
    }

    /** Добавляем новую точку поворота в очередь */
    public void turn(Point point, int way){
        wayPoint.add(point);
        wayTurn.add(way);
    }

    /** Делаем шаг, если дошли до точки поворота - поворачиваем
     * и убераем точку из очереди */
    public void move(){
        Point pointS = Snake.toMove(point, way);

        int x = pointS.x;
        int y = pointS.y;

        point = new Point(x,y);
        if (!wayPoint.isEmpty())
        if (point.equals(wayPoint.element())) {
            wayPoint.remove();
            way = wayTurn.remove();
        }
    }

    /** Рисуемся на текущих координатах */
    public void paint(int numberOfSnake) {
//        if (point.x >= 0 && point.y >= 0 && point.x < Const.RESTRICTIONS_MAX.x/Const.DELAY &&
//                point.y < Const.RESTRICTIONS_MAX.y/Const.DELAY)
        board[point.x][point.y] = (numberOfSnake * 10) + 4;
    }

}

package Project;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class SnakeBody {
    private static final Image body;

    public int way;
    public Point point;
    public Queue<Integer> wayTurn = new LinkedList<>(); //Очередь изменений напрвлений блока
    public Queue<Point> wayPoint = new LinkedList<>(); //Очередь точек поворота
    private int[][] board;

    static {
        Image image;
        try {
            image = ImageIO.read(new File("src/Images/body.png"));
        } catch (IOException e) {
            image = null;
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Вставь картинку для туловища змеи 16x16 как " +
                    "\nImages/body.png");
            System.exit(404);
        }
        body = image;
    }

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
        int x = point.x;
        int y = point.y;
        switch (way) {
            case Const.DOWN: y += 1; break;
            case Const.UP: y -= 1; break;
            case Const.RIGHT: x += 1; break;
            case Const.LEFT: x -= 1; break;
        }
        point = new Point(x,y);
        if (!wayPoint.isEmpty())
        if (point.equals(wayPoint.element())) {
            wayPoint.remove();
            way = wayTurn.remove();
        }
    }

    /** Рисуемся на текущих координатах */
    public void paint(int numberOfSnake) {
        board[point.x][point.y] = (numberOfSnake * 10) + 4;
    }

    public Rectangle getRect() {
        return new Rectangle(point.x,point.y, Const.DELAY, Const.DELAY);
    }
}

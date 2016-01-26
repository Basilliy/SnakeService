package Project;

import Project.SentObjects.Player;

import java.awt.*;
import java.util.ArrayList;

public class Snake {
    public Point headPoint;
    public Point tailPoint;
    public int length;
    public ArrayList<SnakeBody> body = new ArrayList<>();
    public int headWay;
    public int numberOfSnake; // 1 или более
    private int[][] board;
    private Player player;
    private boolean alive = true;

    public Snake(int x, int y, int way, int numberOfSnake, int[][] board) {
        this.numberOfSnake = numberOfSnake;
        this.player = MagicMachine.players.get(numberOfSnake - 1);
        this.board = board;
        headWay = way;
        headPoint = new Point(x, y);

        int X1 = x;
        int Y1 = y;
        int X2 = x;
        int Y2 = y;

        switch (way) {
            case Const.DOWN : Y1 -= 1; Y2 -= 2; break;
            case Const.LEFT: X1 += 1; X2 += 2; break;
            case Const.RIGHT: X1 -= 1; X2 -= 2; break;
            case Const.UP: Y1 += 1;Y2 += 2; break;
        }

        body.add(new SnakeBody(new Point(X1, Y1), way, board));
        body.add(new SnakeBody(new Point(X2, Y2), way, board));

        tailPoint = body.get(body.size()-1).point;
        paint();
    }

    /** Делаем шаг, определяем координаты головы в
     * соответсвии с ограничениями */
    public void move() {
        Point point = toMove(headPoint, headWay);
        int x = point.x;
        int y = point.y;

            boolean b = false;
            // поедание яблока
            int d = board[x][y];
            if (d != 0) if (d < 0) {
                if (MagicMachine.mainApple.point.equals(point))
                    MagicMachine.mainApple = null;
                if (d <= -20 && d >= -23) {
                    MagicMachine.score.addScore(2, player.getName());
                    for (Beetle beetle : MagicMachine.beetles) {
                        if (beetle.point.equals(point)) {
                            MagicMachine.beetles.remove(beetle);
                            break;
                        }
                    }
                }
                bodyIncrease();
                b = true;
            } else {
                Snake snake = MagicMachine.snakes.get(d / 10 - 1);
                if (d % 10 != 4) {
                    Snake.choiceToKill(this, snake);
                } else {
                    Point point1 = snake.body.get(0).point;
                    if (point1.equals(point)) Snake.choiceToKill(this, snake);
                    else {
                        snake.cut(point);
                    }
                }
            }

            nullBoard();
            headPoint = new Point(x, y);
            if (b) for (int i = 0; i < body.size() - 1; i++) body.get(i).move();
            else body.forEach(SnakeBody::move);

            paint();
    }

    /** Изменение направление головы
     * и всех блоков туловища */
    public void turn(int way) {
        headWay = way;
        for (SnakeBody sb : body) sb.turn(headPoint, way);
    }

    /** Рисуемся на текущих координатах */
    public void paint() {
        board[headPoint.x][headPoint.y] = numberOfSnake * 10 + headWay;
        for (SnakeBody sb : body) sb.paint(numberOfSnake);
    }

    /** Обнуление карты по змее */
    public void nullBoard() {
        board[headPoint.x][headPoint.y] = 0;
        for (SnakeBody sb: body) {
            board[sb.point.x][sb.point.y] = 0;
        }
    }

    /** Увеличение змеи */
    public void bodyIncrease() {
        SnakeBody sb = body.get(body.size() - 1);
        SnakeBody nsb = new SnakeBody(sb.point, sb.way, board);
        for (Point point: sb.wayPoint) nsb.wayPoint.add(new Point(point.x, point.y));
        for (Integer integer: sb.wayTurn) nsb.wayTurn.add(integer);

        MagicMachine.score.addScore(1, player.getName());

        body.add(nsb);
        length = body.size();
    }

    public static Point toMove(Point p, int way) {
        int x = p.x;
        int y = p.y;
        switch (way) {
            case Const.DOWN: y += 1; break;
            case Const.UP: y -= 1; break;
            case Const.RIGHT: x += 1; break;
            case Const.LEFT: x -= 1; break;
        }
        if (x >= Const.SIZE) x -= Const.SIZE;
        if (y >= Const.SIZE) y -= Const.SIZE;
        if (x < 0) x += Const.SIZE;
        if (y < 0) y += Const.SIZE;
        return new Point(x,y);
    }

    public static void choiceToKill(Snake one, Snake two) {
        if (one.body.size() > two.body.size()) { //Если первая змея больше, убить вторую
            two.kill();
        } else if (one.body.size() < two.body.size()) { //Если вторая змея больше, убить первую
            one.kill();
        } else { //Если змеи одинаковы, убить обоих
            one.kill();
            two.kill();
        }
    }

    public void kill() {
        System.out.println("Snake #" + numberOfSnake + " is killed at [" + headPoint.x + "; " + headPoint.y + "]");
        alive = false;
        new Apple(headPoint);
        for (SnakeBody aBody : body) new Apple(aBody.point);
    }

    public void cut(Point point) {
        int k = 0;
        for (int i = 0; i < body.size(); i++)
            if (body.get(i).point.equals(point)) {
                k = i;
                break;
            }
        MagicMachine.score.addScore(-(body.size() - k - 1), player.getName());
        for(int j = body.size()-1; j > k; j--) {
            new Apple(body.get(j).point);
            body.remove(j);
        }
    }

    public boolean isAlive() {
        return alive;
    }

}
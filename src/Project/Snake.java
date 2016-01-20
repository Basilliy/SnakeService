package Project;

import Pro.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Snake {

    public static final int height = 16 ;
    public static final int width = 16 ;

    public Point headPoint;
    public Point tailPoint;
    public int length;
    public ArrayList<SnakeBody> body = new ArrayList<>();
    public int headWay;
    public int numberOfSnake = 1; // 1 или более
    private int[][] board;

    public Snake(int x, int y, int way, int numberOfSnake, int[][] board) {
        this.numberOfSnake = numberOfSnake;
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

    /** Рисуемся на текущих координатах */
    public void paint() {
        board[headPoint.x][headPoint.y] = numberOfSnake * 10 + headWay;
        for (SnakeBody sb : body) sb.paint(numberOfSnake);
    }

    /** Делаем шаг, определяем координаты головы в
     * соответсвии с ограничениями */
    public void move() {
        int x = headPoint.x;
        int y = headPoint.y;
        switch (headWay) {
            case Const.DOWN: y += 1; break;
            case Const.UP: y -= 1; break;
            case Const.RIGHT: x += 1; break;
            case Const.LEFT: x -= 1; break;
        }

        if (x >= Const.RESTRICTIONS_MIN.x/Const.DELAY && x < Const.RESTRICTIONS_MAX.x/Const.DELAY
                && y >= Const.RESTRICTIONS_MIN.y/Const.DELAY && y < Const.RESTRICTIONS_MAX.y/Const.DELAY) {
            nullBoard();
            headPoint = new Point(x,y);
            boolean b = false;

            // поедание яблока
            if (board[headPoint.x][headPoint.y] == -1) {
                if(MagicMachine.mainApple.point.equals(headPoint))
                    MagicMachine.mainApple = null;
                for (Apple apple: MagicMachine.apples)
                    if (apple.point.equals(headPoint)) {
                        MagicMachine.apples.remove(apple);
                        break;
                    }
                bodyIncrease();
                b = true;
            }

            if (b) for (int i = 0; i < body.size() - 1; i++) body.get(i).move();
            else body.forEach(SnakeBody::move);

            paint();
        }
    }

    /** Изменение направление головы
     * и всех блоков туловища */
    public void turn(int way) {
        headWay = way;
        for (SnakeBody sb : body) sb.turn(headPoint, way);
    }

    /** Установление карты по змее*/
    public void updateBoard() {
        board[headPoint.x][headPoint.y] = numberOfSnake * 10 + headWay;
        for (SnakeBody sb: body) board[sb.point.x][sb.point.y] = numberOfSnake * 10 + 4;
    }

    /** Обнуление карты по змее */
    public void nullBoard() {
        board[headPoint.x][headPoint.y] = 0;
        for (SnakeBody sb: body) board[sb.point.x][sb.point.y] = 0;
    }

    public void bodyIncrease() {
        SnakeBody sb = body.get(body.size() - 1);
        SnakeBody nsb = new SnakeBody(sb.point, sb.way, board);
        for (Point point: sb.wayPoint) nsb.wayPoint.add(new Point(point.x, point.y));
        for (Integer integer: sb.wayTurn) nsb.wayTurn.add(integer);

        body.add(nsb);

        length = body.size();
    }

    public Rectangle getRect() {
        return new Rectangle(headPoint.x,headPoint.y, height, width);
    }

    public void CollisionWihtYourself (){
        int i;
        for(i = 0; i < body.size(); i ++)
            if (body.get(i).getRect().intersects(getRect())) break;
        for(int j = body.size()-1; j >=i; j--){
            if(j!=i)
                Pro.Paint.apples.add(new Apple(new Point(body.get(j).point.x,body.get(j).point.y)));
            body.remove(j);
        }

    }

}
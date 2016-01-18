package Project;

import Pro.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Snake {
    private static final Image[] HEAD = new Image[4];

    public static final int height = 16 ;
    public static final int width = 16 ;

    public Point headPoint;
    public Point tailPoint;
    public int length;
    public ArrayList<SnakeBody> body = new ArrayList<>();
    public int headWay;
    public boolean controlWay = true;
    public int numberOfSnake = 1; // 1 или более
    public int numberOfBody = 2;

    static {
        try {
            HEAD[0] = ImageIO.read(new File("src/Images/headDown.png"));
            HEAD[1] = ImageIO.read(new File("src/Images/headLeft.png"));
            HEAD[2] = ImageIO.read(new File("src/Images/headRight.png"));
            HEAD[3] = ImageIO.read(new File("src/Images/headUp.png"));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Вставь картинку для головы змеи 16x16 как " +
                    "\nImages/headDown.png (Left/Up/Right)");
            System.exit(404);
        }
    }

    public Snake(int x, int y, int way, int numberOfSnake) {
        this.numberOfSnake = numberOfSnake;
        headWay = way;
        headPoint = new Point(x*Const.DELAY, y*Const.DELAY);
        tailPoint = body.get(body.size()-1).point;
        updateBoard();
    }

    /** Рисуемся на текущих координатах */
    public void paint(int[][] board) {
       board[headPoint.x][headPoint.y] = numberOfSnake * 10 + headWay;
        for (SnakeBody sb: body) sb.paint(board,numberOfSnake);

    }

    /** Делаем шаг, определяем координаты головы в
     * соответсвии с ограничениями */
    public void move() {
        int x = headPoint.x;
        int y = headPoint.y;
        switch (headWay){
            case Const.DOWN: y += Const.DELAY; break;
            case Const.UP: y -= Const.DELAY; break;
            case Const.RIGHT: x += Const.DELAY; break;
            case Const.LEFT: x -= Const.DELAY; break;
        }
        if (x >= Const.RESTRICTIONS_MIN.x && x < Const.RESTRICTIONS_MAX.x
                && y >= Const.RESTRICTIONS_MIN.y && y < Const.RESTRICTIONS_MAX.y) {
            nullBoard();
            headPoint = new Point(x,y);

            boolean b = false;
//            if (Pro.Paint.BOARD[headPoint.x/Const.DELAY][headPoint.y/Const.DELAY] == -1) {
//                Pro.Paint.BOARD[headPoint.x/Const.DELAY][headPoint.y/Const.DELAY] = 0;
//
//                if(Pro.Paint.mainApple.point.equals(headPoint))
//                    Pro.Paint.mainApple = null;
//                for (Apple apple: Pro.Paint.apples
//                        ) {
//                    if(apple.point.equals(headPoint)) {
//                        Pro.Paint.apples.remove(apple);
//                        break;
//                    }
//                }
//                bodyIncrease();
//                b = true;
//            }

            if (b) for (int i = 0; i < body.size() - 1; i++) body.get(i).move();
            else body.forEach(SnakeBody::move);

            updateBoard();
        }
    }

    /** Изменение направление головы
     * и всех блоков туловища */
    public void turn(int way){
        if (controlWay) {
            controlWay = false;
            if ((way == Const.DOWN && headWay == Const.UP)
                    || (way == Const.UP && headWay == Const.DOWN)
                    || (way == Const.LEFT && headWay == Const.RIGHT)
                    || (way == Const.RIGHT && headWay == Const.LEFT))
                return;
            headWay = way;
            for (SnakeBody sb : body) sb.turn(headPoint, way);
        }
    }

    /** Установление карты по змее*/
    public void updateBoard() {
//        Pro.Paint.BOARD[headPoint.x/Const.DELAY][headPoint.y/Const.DELAY] = numberOfSnake;
//        for (SnakeBody sb: body) Pro.Paint.BOARD[sb.point.x / Const.DELAY][sb.point.y / Const.DELAY] = numberOfBody;
    }

    /** Обнуление карты по змее */
    public void nullBoard() {
//        Pro.Paint.BOARD[headPoint.x/Const.DELAY][headPoint.y/Const.DELAY] = 0;
//        for (SnakeBody sb: body) Pro.Paint.BOARD[sb.point.x / Const.DELAY][sb.point.y / Const.DELAY] = 0;
    }


    public void bodyIncrease() {
        SnakeBody sb = body.get(body.size() - 1);
        SnakeBody nsb = new SnakeBody(sb.point, sb.way);
        for (Point point: sb.wayPoint) nsb.wayPoint.add(new Point(point.x, point.y));
        for (Integer integer: sb.wayTurn) nsb.wayTurn.add(integer);

        body.add(nsb);

        length = body.size();
    }

    public Rectangle getRect()
    {
        return new
                Rectangle(headPoint.x,headPoint.y, height, width);

    }

    public void CollisionWihtYourself (){
        int i;
        for(i = 0; i < body.size(); i ++){
            if(body.get(i).getRect().intersects(getRect())){
//System.out.println("It's happend"); 
                break;

            }
        }
        for(int j = body.size()-1; j >=i; j--){
            if(j!=i)
                Pro.Paint.apples.add(new Apple(new Point(body.get(j).point.x,body.get(j).point.y)));
            body.remove(j);
        }

    }

}
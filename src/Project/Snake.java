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
    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DELAY = 16;
    ///Добавил высоту и ширину
    public static final int height = 16;
    public static final int width = 16;

    public static final Point RESTRICTIONS_MAX = new Point(256, 256); // Верхнее ограничение
    public static final Point RESTRICTIONS_MIN = new Point(0, 0); // Нижнее ограничение


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
            HEAD[0] = ImageIO.read(new File("Images/headDown.png"));
            HEAD[1] = ImageIO.read(new File("Images/headLeft.png"));
            HEAD[2] = ImageIO.read(new File("Images/headRight.png"));
            HEAD[3] = ImageIO.read(new File("Images/headUp.png"));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Вставь картинку для головы змеи 16x16 как " +
                    "\nImages/headDown.png (Left/Up/Right)");
            System.exit(404);
        }
    }

    public Snake() {
        headWay = 0;
        headPoint = new Point(2*DELAY, 4*DELAY);
        body.add(new SnakeBody(new Point(2*DELAY, 3*DELAY), headWay));
        body.add(new SnakeBody(new Point(2*DELAY, 2*DELAY), headWay));
        length = body.size();
        tailPoint = body.get(body.size()-1).point;
        updateBoard();
    }

    /** Рисуемся на текущих координатах */
    public void paint(Graphics g) {
        g.drawImage(HEAD[headWay], headPoint.x, headPoint.y, null);
        for (SnakeBody sb: body) sb.paint(g);
        controlWay = true;
    }

    /** Делаем шаг, определяем координаты головы в
     * соответсвии с ограничениями */
    public void move() {
        int x = headPoint.x;
        int y = headPoint.y;
        switch (headWay){
            case DOWN: y += DELAY; break;
            case UP: y -= DELAY; break;
            case RIGHT: x += DELAY; break;
            case LEFT: x -= DELAY; break;
        }
        if (x >= RESTRICTIONS_MIN.x && x < RESTRICTIONS_MAX.x
                && y >= RESTRICTIONS_MIN.y && y < RESTRICTIONS_MAX.y) {
            nullBoard();
            headPoint = new Point(x,y);

            boolean b = false;
            if (Pro.Paint.BOARD[headPoint.x/DELAY][headPoint.y/DELAY] == -1) {
                Pro.Paint.BOARD[headPoint.x/DELAY][headPoint.y/DELAY] = 0;

                if(Pro.Paint.mainApple.point.equals(headPoint))
                    Pro.Paint.mainApple = null;
                for (Apple apple: Pro.Paint.apples
                        ) {
                    if(apple.point.equals(headPoint)) {
                        Pro.Paint.apples.remove(apple);
                        break;
                    }
                }
                bodyIncrease();
                b = true;
            }

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
            if ((way == DOWN && headWay == UP)
                    || (way == UP && headWay == DOWN)
                    || (way == LEFT && headWay == RIGHT)
                    || (way == RIGHT && headWay == LEFT))
                return;
            headWay = way;
            for (SnakeBody sb : body) sb.turn(headPoint, way);
        }
    }

    /** Установление карты по змее*/
    public void updateBoard() {
        Pro.Paint.BOARD[headPoint.x/DELAY][headPoint.y/DELAY] = numberOfSnake;
        for (SnakeBody sb: body) Pro.Paint.BOARD[sb.point.x / DELAY][sb.point.y / DELAY] = numberOfBody;
    }

    /** Обнуление карты по змее */
    public void nullBoard() {
        Pro.Paint.BOARD[headPoint.x/DELAY][headPoint.y/DELAY] = 0;
        for (SnakeBody sb: body) Pro.Paint.BOARD[sb.point.x / DELAY][sb.point.y / DELAY] = 0;
    }


    private void bodyIncrease() {
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
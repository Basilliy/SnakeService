package Pro;

import Project.Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Paint extends JPanel {
    Timer mainTimer = new Timer(30, null);
    BufferedImage image;
    Snake snake = new Snake();
    public static final int[][] BOARD = new int[Snake.RESTRICTIONS_MAX.x / Snake.DELAY]
            [Snake.RESTRICTIONS_MAX.y / Snake.DELAY];
    public static Apple mainApple;

    public static ArrayList<Apple> apples = new ArrayList<>();


    public Paint() {
        mainTimer.start();
        addKeyListener(new myKeyAdapter());
        setFocusable(true);
    }


    @Override
    public void paint(Graphics g) {
        image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0,0,256,256);

        snake.paint(graphics);

//        for(int i = 0; i < BOARD.length; i ++){
//            for(int j = 0; j < BOARD.length; j++){
//                System.out.print(BOARD[i][j]);
//            }
//            System.out.println("");
//        }
//        System.out.println("");

        if (mainApple == null) mainApple = new Apple(createPointForApple());
        mainApple.paint(graphics);

        for (Apple apple :
                apples) {
            apple.paint(graphics);
        }
        g.drawImage(image, 0,0, null);
    }

    public void moveSnake(){
        snake.move();
        snake.CollisionWihtYourself();

    }

    private Point createPointForApple(){
        Random random = new Random();
        int x;
        int y;
        while (true) {
            x = random.nextInt(BOARD.length);
            y = random.nextInt(BOARD[0].length);
            if (BOARD[x][y] == 0) break;
        }
        return new Point(x*Snake.DELAY,y*Snake.DELAY);
    }



    private class myKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_W: snake.turn(Snake.UP); break;
                case KeyEvent.VK_A: snake.turn(Snake.LEFT); break;
                case KeyEvent.VK_S: snake.turn(Snake.DOWN); break;
                case KeyEvent.VK_D: snake.turn(Snake.RIGHT); break;
            }
        }
    }

}

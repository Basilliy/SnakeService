package Pro;

import Project.Snake;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Apple {
    private static final Image apple;

    public int way;
    public Point point;

    static {
        Image image;
        try {
            image = ImageIO.read(new File("src/Images/apple.png"));
        } catch (IOException e) {
            image = null;
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Вставь картинку для яблока 16x16 как " +
                    "\nImages/apple.png");
            System.exit(404);
        }
        apple = image;
    }


    public Apple(Point point){
        this.point = point;
//        Paint.BOARD[point.x/ Snake.DELAY][point.y/Snake.DELAY] = -1;
    }

    public void paint(Graphics g) {
        g.drawImage(apple, point.x, point.y, null);
    }

}

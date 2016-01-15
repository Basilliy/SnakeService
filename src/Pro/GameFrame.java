package Pro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameFrame extends JFrame {

    public GameFrame() {
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 400);
        setBackground(Color.yellow);
        Paint paint = new Paint();
        add(paint);
        repaint();
        setVisible(true);
        Timer timer = new Timer(400, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paint.moveSnake();
                repaint();
            }
        });
        timer.start();
    }
}

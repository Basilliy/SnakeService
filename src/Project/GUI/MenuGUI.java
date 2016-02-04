package Project.GUI;

import Project.Const;

import javax.swing.*;
import java.io.*;

public class MenuGUI extends JFrame{
    private JButton backToMenu;
    private JButton toSelectRoomGUI;
    private JButton toHowToSetup;
    private JButton toExit;
    private JPanel mainPanel;
    private JPanel menuPanel;
    private JPanel instructionPanel;
    private JButton toHowToPlay;
    private JTextPane textPane;
    private JScrollPane scrollPane;

    private String howToSetup;
    private String howToPlay;

    public MenuGUI() {
        setSize(400, 450);
        setContentPane(mainPanel);
        setTitle("SnakeService");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        instructionPanel.setVisible(false);
        setVisible(true);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(Const.PATH_DATA + "HowToSetup.txt")));
            StringBuilder string = new StringBuilder();
            while (reader.ready()) string.append(reader.readLine()).append("\n");
            howToSetup = string.toString();
            reader = new BufferedReader(new FileReader(new File(Const.PATH_DATA + "HowToPlay.txt")));
            string = new StringBuilder();
            while (reader.ready()) string.append(reader.readLine()).append("\n");
            howToPlay = string.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        toHowToSetup.addActionListener(e -> {
            textPane.setText(howToSetup);
            changeVisible();
        });
        toHowToPlay.addActionListener(e -> {
            textPane.setText(howToPlay);
            changeVisible();
        });
        backToMenu.addActionListener(e -> changeVisible());
        toExit.addActionListener(e -> System.exit(2));
        toSelectRoomGUI.addActionListener(e -> {
            setVisible(false);
            new SelectRoomGUI(this);
        });
    }

    private void changeVisible() {
        instructionPanel.setVisible(!instructionPanel.isVisible());
        menuPanel.setVisible(!menuPanel.isVisible());
    }
}

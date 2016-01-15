package Project;

import java.awt.*;

public class Const {
    public static final String CHAT = "#CHAT:";
    public static final String STATUS = "#STATUS:";
    public static final String ERROR = "#ERROR:";
    public static final String START = "#START:";
    public static final String BOARD = "#BOARD:";
    public static final String WAY = "#WAY:";
    public static final String PATH = "src/Images/";
    public static final int PORT = 55000;
    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DELAY = 16;
    public static final int TIMER = 500; // Время в миллисекундах
    public static final Point RESTRICTIONS_MAX = new Point(256, 256); // Верхнее ограничение
    public static final Point RESTRICTIONS_MIN = new Point(0, 0); // Нижнее ограничение




//    String t = Const.STATUS + "54,bas;46,fff;32,tttt;";



//    private String createMap(int[][] n) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(Const.BOARD);
//        for (int[] aN : n) {
//            for (int j = 0; j < n[0].length; j++) {
//                sb.append(aN[j] + ",");
//            }
//            sb.append(";");
//        }
//        return sb.toString();
//    }
}

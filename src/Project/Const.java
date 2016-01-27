package Project;


import java.awt.*;

public class Const {
    public static final int APPLE = -1;
    public static final int BEETLE = -20;

    public static final String CHAT = "#CHAT:";
    public static final String ERROR = "#ERROR:";
    public static final String START = "#START:";
    public static final String EXIT = "#EXIT:";
    public static final String PATH_IMAGES = "src/Images/";
    public static final String PATH_CONFIG = "src/Data/Config.ini";
    public static final String CONFIG_NAME = "Name";
    public static final int PORT = 55000;

    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;

    public static final int DELAY = 16;
    public static final int TIMER = 500; // Время в миллисекундах
    public static final Point RESTRICTION = new Point(DELAY * 30, DELAY * 30); // Верхнее ограничение
    public static final int SIZE = RESTRICTION.x/DELAY;
    public static final int VICTORY = 100; //Счет для победы

    //Константы позиций на карте
    public static final int MAP_NOT_CHOSEN = 0;
    public static final int MAP_UP_LEFT = 1;
    public static final int MAP_UP = 2;
    public static final int MAP_UP_RIGHT = 3;
    public static final int MAP_RIGHT = 4;
    public static final int MAP_DOWN_RIGHT = 5;
    public static final int MAP_DOWN = 6;
    public static final int MAP_DOWN_LEFT = 7;
    public static final int MAP_LEFT = 8;
    public static final String STRING_NOT_CHOSEN = MAP_NOT_CHOSEN + ".Не выбрано";
    public static final String STRING_UP_LEFT = MAP_UP_LEFT + ".Сверху-слева";
    public static final String STRING_UP = MAP_UP + ".Сверху-центр";
    public static final String STRING_UP_RIGHT = MAP_UP_RIGHT + ".Сверху-справа";
    public static final String STRING_RIGHT = MAP_RIGHT + ".Центр-справа";
    public static final String STRING_DOWN_RIGHT = MAP_DOWN_RIGHT + ".Снизу-справа";
    public static final String STRING_DOWN = MAP_DOWN + ".Снизу-центр";
    public static final String STRING_DOWN_LEFT = MAP_DOWN_LEFT + ".Снизу-слева";
    public static final String STRING_LEFT = MAP_LEFT + ".Центр-слева";
}

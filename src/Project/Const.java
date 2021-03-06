package Project;


import Project.GUI.RoomGUI;
import Project.SentObjects.Player;

import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Const {

    /* Общие переменные */
    public static ObjectOutputStream SERVER_OUT = null;
    public static ObjectInputStream SERVER_IN = null;
    public static MagicMachine MACHINE = null;
    public static Player PLAYER = null;
    public static boolean IS_SERVER = false;
    public static ArrayList<Player> PLAYER_LIST = null;
    public static RoomGUI ROOM = null;
    public static int VICTORY = 30; //Счет для победы

    /* Константы */
    public static final int APPLE = -1;
    public static final int BEETLE = -20;

    public static final int VICTORY_MIN = 10;
    public static final int VICTORY_MAX = 100;

    public static final String CHAT = "#CHAT:";
    public static final String WINNER = "#WINNER:";
    public static final String ERROR = "#ERROR:";
    public static final String START = "#START:";
    public static final String EXIT = "#EXIT:";
    public static final String PATH_IMAGES = "Images/";
    public static final String PATH_DATA = "Data/";
    public static final String PATH_CONFIG = "Data/Config.ini";
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

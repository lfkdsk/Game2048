package com.lfk.game2048.Info;

/**
 * Created by liufengkai on 15/11/23.
 */
public class UseInfo {
    public static final int LINES = 4;
    public static int CARD_WIDTH = 0;
    public static int CARD_HEIGHT = 0;
    public static int SCORE = 0;

    public static void addScore(int num) {
        SCORE += num;
    }
}

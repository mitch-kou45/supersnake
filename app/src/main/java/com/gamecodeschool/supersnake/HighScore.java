package com.gamecodeschool.supersnake;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;

public class HighScore {

    private static final String PREFS_FILE = "SnakeGamePrefs";
    private static final String HIGH_SCORE_KEY = "HighScore";
    private static final String SECOND_HIGH_SCORE_KEY = "SecondHighScore";
    private static final String THIRD_HIGH_SCORE_KEY = "ThirdHighScore";
    private static final int MAX_SCORES = 5;
    private SharedPreferences prefs;

    public HighScore(Context context) {
        prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    }

    public void saveHighScore(int highScore) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(HIGH_SCORE_KEY, highScore);
        editor.apply();
    }

    public int loadHighScore() {
        // return the saved high score, return 0 as default if high score not found
        return prefs.getInt(HIGH_SCORE_KEY, 0);
    }
    public void saveSecondHighScore(int secondHighScore) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SECOND_HIGH_SCORE_KEY, secondHighScore);
        editor.apply();
    }

    public int loadSecondHighScore() {
        return prefs.getInt(SECOND_HIGH_SCORE_KEY, 0);
    }

    public void saveThirdHighScore(int thirdHighScore) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(THIRD_HIGH_SCORE_KEY, thirdHighScore);
        editor.apply();
    }

    public int loadThirdHighScore() {
        return prefs.getInt(THIRD_HIGH_SCORE_KEY,0);
    }
    public int[] loadAndSortHighScores() {
        int[] highScores = new int[MAX_SCORES];
        highScores[0] = loadHighScore();
        highScores[1] = loadSecondHighScore();
        highScores[2] = loadThirdHighScore();


        Arrays.sort(highScores);
        return highScores;
    }
}




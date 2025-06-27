package com.gamecodeschool.supersnake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;

public class GameOverScreen {


    private Paint mPaint;
    private Context mContext;
    private boolean mGameOver;
    private Rect mRestartButton;
    private Bitmap mBackground;
    private HighScore mHighscore;

    public GameOverScreen(Context context, Point size,HighScore highScore) {
        mContext = context;
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(100);
        mGameOver = false;
        mBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.forest);
        mBackground = Bitmap.createScaledBitmap(mBackground, size.x, size.y, true);
        mRestartButton = new Rect(300, 500, 900, 625);
        mHighscore=highScore;
        font();
    }

    public void draw(Canvas canvas, int score) {
        canvas.drawBitmap(mBackground, 0, 0, null);
        mPaint.setColor(Color.WHITE);
        canvas.drawText("Game Over", 300, 300, mPaint);
        canvas.drawText("Score: " + score, 300, 450, mPaint);
        canvas.drawText("High Scores: " , 1200, 300, mPaint);
        canvas.drawText("1. " + mHighscore.loadHighScore(), 1200, 450, mPaint);
        canvas.drawText("2. " + mHighscore.loadSecondHighScore(), 1200, 600, mPaint);
        canvas.drawText("3. " + mHighscore.loadThirdHighScore(), 1200, 750, mPaint);
        // Draw restart button
        mPaint.setColor(Color.GREEN);
        canvas.drawRect(mRestartButton, mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawText("Restart", 310, 600, mPaint);
    }

    public boolean isGameOver() {
        return mGameOver;
    }

    public void setGameOver(boolean gameOver) {
        mGameOver = gameOver;
    }

    public boolean isRestartTouched(int x, int y) {
        return mRestartButton.contains(x, y);
    }

    private void font() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Typeface typeface = mContext.getResources().getFont(R.font.videogame);
            mPaint.setTypeface(typeface);
        }
    }
}

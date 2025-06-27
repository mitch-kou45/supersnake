package com.gamecodeschool.supersnake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class TitleScreen {
    private Canvas mCanvas;
    private Paint mPaint;
    private Context mContext;

    public TitleScreen(Context context) {
        mContext = context;
        mPaint = new Paint();

        // Set initial properties for the paint object
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(200);
        font();
    }

    public void draw(Canvas canvas) {
        mCanvas = canvas;
        // Draw the title
        String title = "Super Snake";
        float titleWidth = mPaint.measureText(title);
        float titleX = (mCanvas.getWidth() - titleWidth) / 2;
        mCanvas.drawText(title, titleX, 400, mPaint);

    }
    public void drawTap(Canvas canvas) {
        mPaint.setTextSize(120);
        String tapToPlay = "Tap To Play!";
        float tapToPlayWidth = mPaint.measureText(tapToPlay);
        float tapToPlayX = (mCanvas.getWidth() - tapToPlayWidth) / 2;
        mCanvas.drawText(tapToPlay, tapToPlayX, 700, mPaint);
    }



    private void font() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Typeface typeface = mContext.getResources().getFont(R.font.videogame);
            mPaint.setTypeface(typeface);
        }
    }
}


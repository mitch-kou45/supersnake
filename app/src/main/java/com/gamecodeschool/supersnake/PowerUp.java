package com.gamecodeschool.supersnake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class PowerUp {
    private boolean active;
    private long activationTime;
    private Point location;
    private int blockSize;
    private int num_blocks_wide;
    private int num_blocks_high;

    private Bitmap mPowerUp;


    public PowerUp(Context context,int blockSize, int num_blocks_wide, int num_blocks_high) {
        active = false;
        activationTime = 0;
        this.blockSize = blockSize;
        this.num_blocks_wide = num_blocks_wide;
        this.num_blocks_high = num_blocks_high;
        LoadBodyBitmaps(context);
    }
    private void LoadBodyBitmaps(Context context) {
        // Straight body segment
        mPowerUp = BitmapFactory.decodeResource(context.getResources(), R.drawable.golden_apple);
        mPowerUp = Bitmap.createScaledBitmap(mPowerUp, blockSize, blockSize, false);

    }

    public void spawn() {
        active = true;
        Random random = new Random();
        int randomX = random.nextInt(num_blocks_wide); // Random X coordinate
        int randomY = random.nextInt(num_blocks_high); // Random Y coordinate
        location = new Point(randomX, randomY);
        activationTime = System.currentTimeMillis();

    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
        activationTime = 0;
    }

    public boolean isInvincible() {
        return active && (System.currentTimeMillis() - activationTime) <= 5000;
    }

    public Point getLocation() {
        return location;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (active && mPowerUp != null) {
            canvas.drawBitmap(mPowerUp,location.x*blockSize,location.y*blockSize,paint);
        }
    }

}


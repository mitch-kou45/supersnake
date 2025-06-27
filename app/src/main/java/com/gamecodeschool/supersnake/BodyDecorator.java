package com.gamecodeschool.supersnake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;

public class BodyDecorator implements Decorator {
    private Bitmap mBitmapBody;
    private Bitmap mBitmapVert;
    private Bitmap mBitmapUpRight;
    private Bitmap mBitmapUpLeft;
    private Bitmap mBitmapDownRight;
    private Bitmap mBitmapDownLeft;
    private Bitmap mPowerBody;
    private Bitmap mPowerDownRight;
    private Bitmap mPowerVert;
    private Bitmap mPowerUpRight;
    private Bitmap mPowerUpLeft;

    private Bitmap mPowerDownLeft;
    private final int mSegmentSize;
    private final ArrayList<Point> segmentLocations;
    private boolean powerUpActive = false;
    private Snake snake;

    public BodyDecorator(Context context, int ss, ArrayList<Point> segmentLocations,Snake snake) {
        mSegmentSize = ss;
        this.segmentLocations = segmentLocations;
        createBodyBitmaps(context);
        this.snake = snake;

    }

    private void createBodyBitmaps(Context context) {
        // Straight body segment
        mBitmapBody = BitmapFactory.decodeResource(context.getResources(), R.drawable.body_right);
        mBitmapBody = Bitmap.createScaledBitmap(mBitmapBody, mSegmentSize, mSegmentSize, false);

        mPowerBody = BitmapFactory.decodeResource(context.getResources(), R.drawable.poweruphorizontal);
        mPowerBody = Bitmap.createScaledBitmap(mPowerBody, mSegmentSize, mSegmentSize, false);

        mBitmapDownRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.body_bottomright);
        mBitmapDownRight = Bitmap.createScaledBitmap(mBitmapDownRight, mSegmentSize, mSegmentSize, false);

        mPowerDownRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.bodybotrightpowerup);
        mPowerDownRight = Bitmap.createScaledBitmap(mPowerDownRight, mSegmentSize, mSegmentSize, false);

        Matrix matrix = new Matrix();
        // Vertical body segment
        matrix.setRotate(90);
        mBitmapVert = Bitmap
                .createBitmap(mBitmapBody,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);
        mPowerVert = Bitmap
                .createBitmap(mPowerBody,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);

        // Rotate left curve segment 90 degrees clockwise
        mBitmapDownLeft = Bitmap
                .createBitmap(mBitmapDownRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);

        mPowerDownLeft = Bitmap
                .createBitmap(mPowerDownRight,
                0, 0, mSegmentSize, mSegmentSize, matrix, true);

        matrix.setRotate(-90);
        mBitmapUpRight =  Bitmap
                .createBitmap(mBitmapDownRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);

        mPowerUpRight = Bitmap
                .createBitmap(mPowerDownRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);

        matrix.setRotate(180);
        mBitmapUpLeft = Bitmap
                .createBitmap(mBitmapDownRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);

        mPowerUpLeft = Bitmap
                .createBitmap(mPowerDownRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);
    }
    public void setPowerUpActive(boolean active) {
        powerUpActive = active;
    }
    @Override
    public void draw(Canvas canvas, Paint paint) {
        boolean isInvincible = snake.isInvincible();
        for (int i = 1; i < segmentLocations.size() - 1; i++) {
            Point current = segmentLocations.get(i);
            Point previous = segmentLocations.get(i - 1);
            Point next = segmentLocations.get(i + 1);

            Bitmap bodySegmentBitmap;
            if ((previous.x == next.x) || (previous.y == next.y)) {
                if( isInvincible) {
                    bodySegmentBitmap = (previous.x == next.x) ? mPowerVert : mPowerBody;
                }else{
                    bodySegmentBitmap = (previous.x == next.x) ? mBitmapVert : mBitmapBody;
                }
            } else {
                // Curved body
                if ((previous.x < current.x && current.y > next.y) ||
                        (previous.y < current.y && current.x > next.x)) {
                    if( isInvincible) {
                        bodySegmentBitmap = mPowerUpLeft;
                    }else{
                        bodySegmentBitmap=mBitmapUpLeft;
                    }
                } else if ((previous.x > current.x && current.y > next.y) ||
                        (previous.y < current.y && current.x < next.x)) {
                    if( isInvincible) {
                        bodySegmentBitmap = mPowerUpRight;
                    }else{
                        bodySegmentBitmap=mBitmapUpRight;
                    }
                } else if ((previous.x > current.x && current.y < next.y) ||
                        (previous.y > current.y && current.x < next.x)) {
                    if( isInvincible) {
                        bodySegmentBitmap = mPowerDownRight;
                    }else{
                        bodySegmentBitmap=mBitmapDownRight;
                    }
                } else if ((previous.x < current.x && current.y < next.y) ||
                        (previous.y > current.y && current.x > next.x)) {
                    if( isInvincible) {
                        bodySegmentBitmap = mPowerDownLeft;
                    }else{
                        bodySegmentBitmap=mBitmapDownLeft;
                    }
                } else {
                    if( isInvincible) {
                        bodySegmentBitmap = mPowerBody;
                    }else{
                        bodySegmentBitmap=mBitmapBody;
                    }
                }
            }

            canvas.drawBitmap(bodySegmentBitmap, current.x * mSegmentSize, current.y * mSegmentSize, paint);
        }
    }
}




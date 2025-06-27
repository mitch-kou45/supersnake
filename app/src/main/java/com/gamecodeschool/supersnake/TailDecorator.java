package com.gamecodeschool.supersnake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;

public class TailDecorator implements Decorator {

    private Bitmap mBitmapTailRight;
    private Bitmap mBitmapTailLeft;
    private Bitmap mBitmapTailUp;
    private Bitmap mBitmapTailDown;
    private Bitmap mPowerRight;
    private Bitmap mPowerLeft;
    private Bitmap mPowerUp;
    private Bitmap mPowerDown;
    private final int mSegmentSize;
    private final ArrayList<Point> segmentLocations;
    private Snake snake;
    public TailDecorator(Context context, int ss, ArrayList<Point> segmentLocations,Snake snake) {
        mSegmentSize = ss;
        this.segmentLocations = segmentLocations;
        createTailBitmaps(context);
        this.snake = snake;
    }
    private void createTailBitmaps(Context context) {
        mBitmapTailRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.tail_right);
        mBitmapTailRight = Bitmap.createScaledBitmap(mBitmapTailRight, mSegmentSize, mSegmentSize, false);

        mPowerRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.tailpow);
        mPowerRight = Bitmap.createScaledBitmap(mPowerRight, mSegmentSize, mSegmentSize, false);

        Matrix matrix = new Matrix();

        //  left tail
        matrix.setRotate(-180);
        mBitmapTailLeft = Bitmap
                .createBitmap(mBitmapTailRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);
        mPowerLeft = Bitmap
                .createBitmap(mPowerRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);


        // up tail
        matrix.setRotate(-90);
        mBitmapTailUp = Bitmap
                .createBitmap(mBitmapTailRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);
        mPowerUp = Bitmap
                .createBitmap(mPowerRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);

        //  down tail
        matrix.setRotate(90);
        mBitmapTailDown = Bitmap
                .createBitmap(mBitmapTailRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);
        mPowerDown = Bitmap
                .createBitmap(mPowerRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);
    }
    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (segmentLocations.size() > 1) {
            boolean isInvincible = snake.isInvincible();
            Point tailPoint = segmentLocations.get(segmentLocations.size() - 1);
            Point tailPrevPoint = segmentLocations.get(segmentLocations.size() - 2);
            Bitmap tailBitmap;
            if (tailPoint.y > tailPrevPoint.y) {
                if( isInvincible) {
                    tailBitmap = mPowerDown;
                }else{
                    tailBitmap = mBitmapTailDown;
                }
            } else if (tailPoint.x > tailPrevPoint.x) {
                if( isInvincible) {
                    tailBitmap = mPowerRight;
                }else{
                    tailBitmap = mBitmapTailRight;
                }
            } else if (tailPoint.x < tailPrevPoint.x) {
                if( isInvincible) {
                    tailBitmap = mPowerLeft;
                }else{
                    tailBitmap = mBitmapTailLeft;
                }
            } else if (tailPoint.y < tailPrevPoint.y) {
                if( isInvincible) {
                    tailBitmap = mPowerUp;
                }else{
                    tailBitmap = mBitmapTailUp;
                }
            } else {
                if( isInvincible) {
                    tailBitmap = mPowerUp;
                }else{
                    tailBitmap = mBitmapTailUp;
                }
            }

            canvas.drawBitmap(tailBitmap,
                    tailPoint.x * mSegmentSize,
                    tailPoint.y * mSegmentSize, paint);
        }
    }
}


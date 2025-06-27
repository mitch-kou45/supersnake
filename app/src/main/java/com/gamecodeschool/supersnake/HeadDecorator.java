package com.gamecodeschool.supersnake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;

public class HeadDecorator implements Decorator {

    private Snake.Heading heading = Snake.Heading.RIGHT; // Assuming this is the default initial heading
    private Bitmap mBitmapHeadRight;
    private Bitmap mBitmapHeadLeft;
    private Bitmap mBitmapHeadUp;
    private Bitmap mBitmapHeadDown;
    private Bitmap mPowerRight;
    private Bitmap mPowerLeft;
    private Bitmap mPowerUp;
    private Bitmap mPowerDown;
    private final int mSegmentSize;
    private final ArrayList<Point> segmentLocations;
    private Snake snake;

    public HeadDecorator(Context context, int ss, ArrayList<Point> segmentLocations, Snake snake) {
        mSegmentSize = ss;
        this.segmentLocations = segmentLocations;
        this.snake = snake;
        createBitmaps(context);
    }

    private void createBitmaps(Context context) {
        mBitmapHeadRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.head_right);
        mBitmapHeadRight = Bitmap.createScaledBitmap(mBitmapHeadRight, mSegmentSize, mSegmentSize, false);

        mPowerRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.headpowerup);
        mPowerRight = Bitmap.createScaledBitmap(mPowerRight, mSegmentSize, mSegmentSize, false);

        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);

        mBitmapHeadLeft = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);

        mPowerLeft = Bitmap
                .createBitmap(mPowerRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);

        matrix.preRotate(-90);
        mBitmapHeadUp = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);

        mPowerUp = Bitmap
                .createBitmap(mPowerRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);

        matrix.preRotate(180);
        mBitmapHeadDown = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);

        mPowerDown = Bitmap
                .createBitmap(mPowerRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);
    }

    public void draw(Canvas canvas, Paint paint) {
        if (!segmentLocations.isEmpty()) {
            boolean isInvincible = snake.isInvincible();
            // Draw the head
            heading = snake.getHeading();
            Bitmap headBitmap;
            switch (heading) {
                case LEFT:
                    if( isInvincible) {
                        headBitmap = mPowerLeft;
                    }else{
                    headBitmap= mBitmapHeadLeft;
                    }
                    break;
                case UP:
                    if( isInvincible) {
                        headBitmap = mPowerUp;
                    }else{
                        headBitmap= mBitmapHeadUp;
                    }
                    break;
                case DOWN:
                    if( isInvincible) {
                        headBitmap = mPowerDown;
                    }else{
                        headBitmap= mBitmapHeadDown;
                    }
                    break;
                default:
                    if( isInvincible) {
                        headBitmap = mPowerRight;
                    }else{
                        headBitmap= mBitmapHeadRight;
                    }
                    break;
            }
            canvas.drawBitmap(headBitmap,
                    segmentLocations.get(0).x * mSegmentSize,
                    segmentLocations.get(0).y * mSegmentSize, paint);
        }
    }
}

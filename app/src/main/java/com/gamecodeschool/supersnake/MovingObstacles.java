package com.gamecodeschool.supersnake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class MovingObstacles {

    private Point mSpawnRange;
    private int mSize;
    private Bitmap mBitmapFlyingObstacle;
    private List<Point> mLocations;

    public MovingObstacles(Context context, Point sr, int s) {
        mSpawnRange = sr;
        mSize = s;

        mLocations = new ArrayList<>();

        mBitmapFlyingObstacle = BitmapFactory.decodeResource(context.getResources(), R.drawable.rock);
        mBitmapFlyingObstacle = Bitmap.createScaledBitmap(mBitmapFlyingObstacle, s, s, false);
    }

    public Point spawn() {
        Random random = new Random();
        int margin = 2;
        int maxX = Math.max(mSpawnRange.x - margin, 1);
        int maxY = Math.max(mSpawnRange.y - margin, 1);
        int x = random.nextInt(maxX) + margin / 2;
        int y = random.nextInt(maxY) + margin / 2;

        Point obstaclePosition = new Point(x, y);
        mLocations.add(obstaclePosition);

        return obstaclePosition;
    }

    public void move() {

        for (int i = 0; i < mLocations.size(); i++) {
            Point obstacle = mLocations.get(i);
            obstacle.x++;
            if (obstacle.x >= mSpawnRange.x) {
                mLocations.remove(i);
                i--;
            }
        }
    }


    public void reset() {
        mLocations.clear();
    }
    public boolean checkCollision(Point playerPosition) {
        for (Point movingObstacle : mLocations) {
            if (playerPosition.equals(movingObstacle)) {
                return true;
            }
        }
        return false;
    }
    public boolean deleteObstacle(Point position) {
        for (Point obstacle : mLocations) {
            if (obstacle.equals(position)) {
               mLocations.remove(obstacle);
                return true;
            }
        }
        return false;
    }

    void draw(Canvas canvas, Paint paint) {
        for (Point location : mLocations) {
            canvas.drawBitmap(mBitmapFlyingObstacle, location.x * mSize, location.y * mSize, paint);
        }
    }
}

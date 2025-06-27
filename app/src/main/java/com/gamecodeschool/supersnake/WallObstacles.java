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

public class WallObstacles {
    private List<Point> locations;
    private Point mSpawnRange;
    private int mSize;
    public final ArrayList<Point> segmentLocations;
    private Bitmap mBitmapWall;

    public WallObstacles(Context context, Point sr, int s) {
        segmentLocations = new ArrayList<>();
        mSpawnRange = sr;
        mSize = s;
        mBitmapWall = BitmapFactory.decodeResource(context.getResources(), R.drawable.tree);
        mBitmapWall = Bitmap.createScaledBitmap(mBitmapWall, s , s, false);

        locations = new ArrayList<>();

    }

    public List<Point> spawn(int numObstacles) {
        Random random = new Random();
        locations.clear();

        int margin = 2;
        int maxX = Math.max(mSpawnRange.x - margin, 1);
        int maxY = Math.max(mSpawnRange.y - margin, 1);

        for (int i = 0; i < numObstacles; i++) {
            int x = random.nextInt(maxX) + margin / 2;
            int y = random.nextInt(maxY) + margin / 2;

            // Randomly choose whether to add a vertical or horizontal wall
            boolean isVertical = random.nextBoolean();

            if (isVertical) {
                locations.add(new Point(x, y));
                locations.add(new Point(x, y + 1));
                locations.add(new Point(x, y + 2));
            } else {
                locations.add(new Point(x, y));
                locations.add(new Point(x + 1, y));
                locations.add(new Point(x + 2, y));
            }
        }

        return locations;
    }

    public boolean deleteObstacle(Point position) {
        for (Point obstacle : segmentLocations) {
            if (obstacle.equals(position)) {
                segmentLocations.remove(obstacle);
                return true;
            }
        }
        return false;
    }

    public void addObstacle(Point position) {
        segmentLocations.add(position);
    }

    public void reset() {
        segmentLocations.clear();
    }

    public void update(int numToRemove) {
        for (int i = 0; i < numToRemove && !segmentLocations.isEmpty(); i++) {
            segmentLocations.remove(i);
        }
    }

    void draw(Canvas canvas, Paint paint) {
        for (Point location : segmentLocations) {

            boolean isVertical = (location.x + 2 < mSpawnRange.x);

            if (!isVertical && location.y < mSpawnRange.y) {
                canvas.drawBitmap(mBitmapWall, location.x * mSize, location.y * mSize, paint);
            }

            if (isVertical && location.y + 2 < mSpawnRange.y) {
                canvas.drawBitmap(mBitmapWall, location.x * mSize, location.y * mSize, paint);
            }
        }
    }
}
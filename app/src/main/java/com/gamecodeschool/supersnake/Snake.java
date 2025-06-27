package com.gamecodeschool.supersnake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.util.Log;
import java.util.ArrayList;

class Snake implements Decorator{

    public final ArrayList<Point> segmentLocations;
    private final int mSegmentSize;
    private final Point mMoveRange;
    private final int halfWayPoint;

    enum Heading {
        UP, RIGHT, DOWN, LEFT
    }

    private Heading heading = Heading.RIGHT;
    private Bitmap mBitmapHeadRight;
    private Bitmap mBitmapHeadLeft;
    private Bitmap mBitmapHeadUp;
    private Bitmap mBitmapHeadDown;
    private boolean invincible;
    private long invincibilityDuration;
    private final int top;
    private final int left;
    private final int right;
    private final int bottom;
    private TailDecorator tailDecorator;
    private BodyDecorator bodyDecorator;
    private HeadDecorator headDecorator;

    //debugging purpose
    public void printSegmentLocations() {
        for (int i = 0; i < segmentLocations.size(); i++) {
            Point segment = segmentLocations.get(i);
            Log.d("Snake", "Segment " + i + ": x = " + segment.x + ", y = " + segment.y);
        }
    }
    Snake(Context context, Point mr, int ss) {
        segmentLocations = new ArrayList<>();
        mSegmentSize = ss;
        mMoveRange = mr;

        headDecorator = new HeadDecorator(context, mSegmentSize, segmentLocations,this);
        tailDecorator = new TailDecorator(context, mSegmentSize, segmentLocations,this);
        bodyDecorator = new BodyDecorator(context, mSegmentSize, segmentLocations,this);
        int mScreenHeight = mr.y;
        int mScreenWidth = mr.x;
        int buttonWidth = mScreenWidth / 14;
        int buttonHeight = mScreenHeight / 12;
        int buttonPadding = mScreenWidth / 90;
        left =  mScreenWidth - buttonPadding - buttonWidth;
        top =  mScreenHeight - (buttonHeight * 2) - (buttonPadding * 2);
        right = mScreenWidth - buttonPadding;
        bottom = mScreenHeight - buttonHeight - (buttonPadding *2);



        halfWayPoint = mr.x * ss / 2;

        invincible = false;
        invincibilityDuration = 0;

    }
    protected void reset(int w, int h) {

        heading = Heading.RIGHT;

        segmentLocations.clear();

        segmentLocations.add(new Point(w / 2, h / 2));
    }


    protected void move() {
        // Move the body
        for (int i = segmentLocations.size() - 1; i > 0; i--) {
            segmentLocations.get(i).x = segmentLocations.get(i - 1).x;
            segmentLocations.get(i).y = segmentLocations.get(i - 1).y;
        }


        Point p = segmentLocations.get(0);

        switch (heading) {
            case UP:
                p.y--;
                break;

            case RIGHT:
                p.x++;
                break;

            case DOWN:
                p.y++;
                break;

            case LEFT:
                p.x--;
                break;
        }

    }

    protected boolean detectDeath() {
        boolean dead = segmentLocations.get(0).x == -1 ||
                segmentLocations.get(0).x > mMoveRange.x ||
                segmentLocations.get(0).y == -1 ||
                segmentLocations.get(0).y > mMoveRange.y;

        // Hit any of the screen edges

        // Eaten itself?
        for (int i = segmentLocations.size() - 1; i > 0; i--) {
            // Have any of the sections collided with the head
            if (segmentLocations.get(0).x == segmentLocations.get(i).x &&
                    segmentLocations.get(0).y == segmentLocations.get(i).y) {

                dead = true;
                break;
            }
        }
        return dead;
    }
    protected void addBody(int count) {
        for (int i = 0; i < count; i++) {
            segmentLocations.add(new Point(-10, -10));
        }
    }

    protected boolean removeBody(int count) {
        if (count <= segmentLocations.size() - 1) {
            for (int i = 0; i < count; i++) {
                segmentLocations.remove(segmentLocations.size() - 1);
            }
            return true;
        } else {
            return false;
        }
    }
    public Heading getHeading() {
        return heading;
    }

    protected boolean isHeadOnly() {
        return segmentLocations.size() == 1;
    }
    protected boolean checkDinner(Point l) {
        if (segmentLocations.get(0).x == l.x &&
                segmentLocations.get(0).y == l.y) {

            return true;
        }
        return false;
    }

    public void drawButton(Canvas canvas, Paint paint) {
        // Set button color
        paint.setColor(Color.argb(100, 255, 255, 255));

        // Draw button rectangle
        canvas.drawRect(left, top, right, bottom, paint);

        // Reset paint color
        paint.setColor(Color.argb(255, 255, 255, 255));
    }
    public void draw(Canvas canvas, Paint paint) {
            headDecorator.draw(canvas,paint);
            tailDecorator.draw(canvas, paint);
            bodyDecorator.draw(canvas, paint);
            drawButton(canvas,paint);

        }

    protected void switchHeading(MotionEvent motionEvent) {


        if (motionEvent.getX() >= halfWayPoint) {
            switch (heading) {
                case UP:
                    heading = Heading.RIGHT;
                    break;
                case RIGHT:
                    heading = Heading.DOWN;
                    break;
                case DOWN:
                    heading = Heading.LEFT;
                    break;
                case LEFT:
                    heading = Heading.UP;
                    break;
            }
        } else {
            switch (heading) {
                case UP:
                    heading = Heading.LEFT;
                    break;
                case LEFT:
                    heading = Heading.DOWN;
                    break;
                case DOWN:
                    heading = Heading.RIGHT;
                    break;
                case RIGHT:
                    heading = Heading.UP;
                    break;
            }
        }
    }
    public void activateInvincibility() {
        invincible = true;
        invincibilityDuration = System.currentTimeMillis();
    }

    public boolean isInvincible() {
        return invincible && (System.currentTimeMillis() - invincibilityDuration) <= 5000;
    }
}

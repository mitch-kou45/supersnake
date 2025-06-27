package com.gamecodeschool.supersnake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

class SnakeGame extends SurfaceView implements Runnable{


    private Thread mThread = null;

    private long mNextFrameTime;

    private volatile boolean mPlaying = false;
    private volatile boolean mPaused = true;


    // The size in segments of the playable area
    private final int NUM_BLOCKS_WIDE = 40;
    private int mNumBlocksHigh;

    private int mScore;
    private int highScore;
    private int secondScore;
    private int thirdScore;
    private HighScore highScoreManager;

    // Objects for drawing
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;

    // A snake ssss
    private Snake mSnake;
    // And an apple
    private Apple mApple;
    private SoundEngine mSoundEngine;
    private Obstacles mObstacles;
    private WallObstacles mWallObstacles;

    private int mPauseButtonLeft;
    private int mPauseButtonRight;
    private int mPauseButtonTop;
    private int mPauseButtonBottom;
    private Bitmap mBackground;
    private Bitmap mTitle;
    private volatile boolean mPausedGame = false;
    private boolean mBlink = false;
    private long mLastBlinkTime;

    private GameOverScreen mGameOverScreen;
    private PowerUp mPowerUp;
    private MovingObstacles mMovingObstacles;




    // This is the constructor method that gets called
    // from SnakeActivity
    public SnakeGame(Context context, Point size) {
        super(context);

        // Load the background image
        mBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        mTitle = BitmapFactory.decodeResource(context.getResources(), R.drawable.titleforest);
        mTitle = Bitmap.createScaledBitmap(mTitle, size.x, size.y, true);
        // Work out how many pixels each block is
        int blockSize = size.x / NUM_BLOCKS_WIDE;
        // How many blocks of the same size will fit into the height
        mNumBlocksHigh = size.y / blockSize;

        // Calculate how many times the background image can fit both horizontally and vertically
        int numHorizontalTiles = (size.x + mBackground.getWidth() - 1) / mBackground.getWidth();
        int numVerticalTiles = (size.y + mBackground.getHeight() - 1) / mBackground.getHeight();

        // Create a new bitmap to store the tiled background
        Bitmap tiledBackground = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tiledBackground);

        // Tile the background image horizontally and vertically
        for (int y = 0; y < numVerticalTiles; y++) {
            for (int x = 0; x < numHorizontalTiles; x++) {
                canvas.drawBitmap(mBackground, x * mBackground.getWidth(), y * mBackground.getHeight(), null);
            }
        }

        // Set the tiled background as the mBackground
        mBackground = tiledBackground;

        mSoundEngine = SoundEngine.getInstance(context);

        // Initialize the drawing objects
        mSurfaceHolder = getHolder();
        mPaint = new Paint();

        // Call the constructors of our two game objects
        mApple = new Apple(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        mSnake = new Snake(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        mObstacles = new Obstacles(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        mWallObstacles = new WallObstacles(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        mMovingObstacles = new MovingObstacles(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);



        // Pause Button
        int buttonSize = 250;
        mPauseButtonLeft = 880;
        mPauseButtonTop = 25;
        mPauseButtonRight = 1200;
        mPauseButtonBottom = 150;

        // Initialize the HighScore class
        highScoreManager = new HighScore(context);
        highScore = highScoreManager.loadHighScore();
        secondScore = highScoreManager.loadSecondHighScore();
        thirdScore = highScoreManager.loadThirdHighScore();

        mGameOverScreen = new GameOverScreen(context,size,highScoreManager);

        mPowerUp = new PowerUp(context,blockSize, NUM_BLOCKS_WIDE, mNumBlocksHigh);

    }


    // Called to start a new game
    public void newGame() {

        // reset the snake
        mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);
        mObstacles.reset();
        mWallObstacles.reset();
        mMovingObstacles.reset();

        // Get the apple ready for dinner
        mApple.spawn();

        // Reset the mScore
        mScore = 0;

        // Setup mNextFrameTime so an update can triggered
        mNextFrameTime = System.currentTimeMillis();
        mPowerUp.spawn();
        mSoundEngine.playBackground();

    }


    // Handles the game loop
    @Override
    public void run() {
        while (mPlaying) {
            if(!mPaused) {
                // Update 10 times a second
                if (updateRequired()) {
                    update();
                }
            }

            draw();

        }
    }


    // Check to see if it is time for an update
    public boolean updateRequired() {

        // Run at 10 frames per second
        final long TARGET_FPS = 10;
        // There are 1000 milliseconds in a second
        final long MILLIS_PER_SECOND = 1000;

        // Are we due to update the frame
        if(mNextFrameTime <= System.currentTimeMillis()){
            // Tenth of a second has passed

            // Setup when the next update will be triggered
            mNextFrameTime =System.currentTimeMillis()
                    + MILLIS_PER_SECOND / TARGET_FPS;

            // Return true so that the update and draw
            // methods are executed
            return true;
        }

        return false;
    }


    // Update all the game objects
     public void update() {
        if (!mPausedGame) {
            moveSnakeAndObstacles();
            handleEatingApple();
            handleSnakeRock();
            handleSnakeWall();
            handlePowerUp();
            handleBombs();
            handleGameOver();
        }
    }

    private void moveSnakeAndObstacles() {
        mSnake.move();
        mMovingObstacles.move();
    }

    private void handleEatingApple() {
        if (mSnake.checkDinner(mApple.getLocation())) {
            mSnake.addBody(1);
            mApple.spawn();


            mSoundEngine.playEat();
            difficultyScaler();
        }
    }
    private void difficultyScaler(){

        int numRocks = 1;
        int numWalls = 0;
        int numBombs = 1;
        int numToRemove = 0;
        if (mScore > 15 && mScore < 30) {
            numWalls = 1;
            numRocks = 2;
            numBombs = 2;
            numToRemove = 0;
        } else if (mScore > 30) {
            numWalls = 2;
            numBombs = 3;
            numRocks = 3;
            numToRemove = 0;
        }
        for (int i = 0; i < numRocks; i++) {
            mMovingObstacles.spawn();
        }
        List<Point> obstaclePositions = mObstacles.spawn(numBombs);
        List<Point> wallobstaclePositions = mWallObstacles.spawn(numWalls);

        int previousScore = 0;
        if (mScore > previousScore) {
            mObstacles.update(numToRemove);
            mWallObstacles.update(numToRemove);
        }

        for (Point position : obstaclePositions) {
            mObstacles.addObstacle(position);
        }
        for (Point position : wallobstaclePositions) {
            mWallObstacles.addObstacle(position);
        }

        previousScore = mScore;
        mScore +=3;
    }

    private void handleSnakeRock() {
        for (int i = 0; i < mSnake.segmentLocations.size(); i++) {
            if (mMovingObstacles.checkCollision(mSnake.segmentLocations.get(i))) {
                if (mSnake.isInvincible()) {
                    mSoundEngine.playBoom();
                    Point collidedPosition = mSnake.segmentLocations.get(i);
                    mMovingObstacles.deleteObstacle(collidedPosition);
                }else if(!mSnake.isInvincible()){
                    if (mSnake.isHeadOnly()) {
                        handleSnakeDead();
                        return;
                    }
                    mSoundEngine.playCrash();
                    mScore--;

                    Point collidedPosition = mSnake.segmentLocations.get(i);
                    mMovingObstacles.deleteObstacle(collidedPosition);

                }
                }
            }
        }


    private void handleSnakeWall() {
        boolean snakeDead = false;
        for ( int m = 0; m < mWallObstacles.segmentLocations.size(); m++) {
            Point wallobstaclePosition = mWallObstacles.segmentLocations.get(m);
            if (mSnake.checkDinner(wallobstaclePosition)) {
                if (mSnake.isInvincible()) {
                    // If the snake is invincible, delete the obstacle
                    mWallObstacles.deleteObstacle(wallobstaclePosition);
                    mSoundEngine.playBoom();
                } else{
                    snakeDead = true;
                    break;
                }
            }
        }
        if (snakeDead) {
            handleSnakeDead();
        }
    }

    private void handlePowerUp() {
        if (mPowerUp.isActive() && mSnake.checkDinner(mPowerUp.getLocation())) {
            mPowerUp.deactivate();
            mSnake.activateInvincibility();
            mSoundEngine.playPower();
        }
        if (!mPowerUp.isActive() && !mSnake.isInvincible()) {
            mPowerUp.spawn();
        }
    }

    private void handleBombs() {
        for (int i = 0; i < mObstacles.segmentLocations.size(); i++) {
            Point obstaclePosition = mObstacles.segmentLocations.get(i);
            if (mSnake.checkDinner(obstaclePosition) && mSnake.isInvincible()) {
                mSoundEngine.playBoom();
                mObstacles.deleteObstacle(obstaclePosition);
            } else if (mSnake.checkDinner(obstaclePosition) && !mSnake.isInvincible()) {
                if (mSnake.removeBody(1)) {
                    if (mSnake.isHeadOnly()) {
                        handleSnakeDead();
                        return;
                    }
                    mSoundEngine.playCrash();
                    mScore--;

                    // Remove the obstacle upon collision
                    mObstacles.deleteObstacle(obstaclePosition);

                    }
                }
            }
        }


    private void handleSnakeDead() {
        mSoundEngine.playCrash();
        if (mScore >= highScoreManager.loadHighScore()) {
            highScoreManager.saveThirdHighScore(highScoreManager.loadSecondHighScore());
            highScoreManager.saveSecondHighScore(highScore);
            highScore = mScore;
            highScoreManager.saveHighScore(highScore);
        }
        if (mScore < highScoreManager.loadHighScore() && mScore >= highScoreManager.loadSecondHighScore()) {
            highScoreManager.saveThirdHighScore(highScoreManager.loadSecondHighScore());
            highScoreManager.saveSecondHighScore(mScore);
        }
        if (mScore >= highScoreManager.loadThirdHighScore() && mScore < highScoreManager.loadSecondHighScore()) {
            highScoreManager.saveThirdHighScore(mScore);
        }

        mPaused = true;
        mGameOverScreen.setGameOver(true);
    }
    private void handleGameOver() {
        if (mSnake.detectDeath()) {
            handleSnakeDead();
        }
    }
    // Do all the drawing
    public void draw() {
        // Get a lock on the mCanvas
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();
            font();


            if (mGameOverScreen.isGameOver()) {
                mGameOverScreen.draw(mCanvas, mScore); // Draw game over screen
            } else {
                // Fill the screen with a color
                mCanvas.drawBitmap(mBackground, 0, 0, null);


                // Set the size and color of the mPaint for the text
                mPaint.setColor(Color.argb(255, 255, 255, 255));
                mPaint.setTextSize(120);

                mPaint.setTextSize(50);
                // Draw the score
                mCanvas.drawText("" + mScore, 20, 160, mPaint);

                // Draw the high score
                mCanvas.drawText("High Score: " + highScore, 20, 80, mPaint);

                // Draw the apple and the snake
                mApple.draw(mCanvas, mPaint);
                mSnake.draw(mCanvas, mPaint);
                mObstacles.draw(mCanvas,mPaint);
                mWallObstacles.draw(mCanvas,mPaint);
                mMovingObstacles.draw(mCanvas,mPaint);


                mPaint.setColor(Color.argb(0, 0,0,0));
                mCanvas.drawRect(mPauseButtonLeft, mPauseButtonTop, mPauseButtonRight, mPauseButtonBottom, mPaint);
                mPaint.setTextSize(100);
                mPaint.setColor(Color.argb(255, 255,255,255));
                mCanvas.drawText("Pause", 900, 100, mPaint);

                if (mPowerUp.isActive()) {
                    mPowerUp.draw(mCanvas, mPaint);
                }

                // Draw some text while paused
                if (mPaused) {
                    mCanvas.drawBitmap(mTitle, 0, 0, null);
                    titleScreen();
                }

                if (mPausedGame) {
                    mPaint.setColor(Color.argb(255, 0, 0, 0));
                    mPaint.setTextSize(150);
                    mCanvas.drawText("Game is paused", 300, 500, mPaint);
                }
            }



            // Unlock the mCanvas and reveal the graphics for this frame
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int touchX = (int) motionEvent.getX();
        int touchY = (int) motionEvent.getY();
        boolean Button = touchX >= mPauseButtonLeft && touchX <= mPauseButtonRight && touchY >= mPauseButtonTop && touchY <= mPauseButtonBottom;

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (mGameOverScreen.isGameOver()) {
                    pause();
                    if (mGameOverScreen.isRestartTouched(touchX, touchY)) {
                        resume();
                        mGameOverScreen.setGameOver(false);
                        newGame();

                    }
                }

                if (mPausedGame) {
                    mPausedGame = false;
                    return true;
                } else {
                    if (Button) {
                        mPausedGame = true;
                        return true;
                    }
                }

                if (mPaused) {
                    mPaused = false;
                    newGame();

                    // Don't want to process snake direction for this tap
                    return true;
                }

                // Let the Snake class handle the input
                mSnake.switchHeading(motionEvent);
                break;

            default:
                break;

        }
        return true;
    }

    private void font() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Typeface typeface = getResources().getFont(R.font.videogame);
            mPaint.setTypeface(typeface);
        }
    }


    // Stop the thread
    protected void pause() {
        mPlaying = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    // Start the thread
    protected void resume() {
        mPlaying = true;
        mThread = new Thread(this);
        mThread.start();
    }

    private void titleScreen() {

        TitleScreen titleScreen = new TitleScreen(getContext());
        titleScreen.draw(mCanvas);
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastBlinkTime > 500) {   // Blink every 500 milliseconds
            mBlink = !mBlink;
            mLastBlinkTime = currentTime;
        }

        // Draw the "Tap To Play!" message with blinking effect
        if (mBlink) {
            titleScreen.drawTap(mCanvas);
        }

    }

}
package edu.indiana.gshores.finalproject;

/*
 *GameActivity.java
 *
 * Contains the Java code for the Game Activity in this project.
 * After the Surface is created startGame() and then runGame() are called,
 * with runGame() updating the activity until isGameOver() returns true;
 *
 * Created by Gabe Shores
 * Created on: 2/26/24
 * Last Modified by: Gabe Shores
 * Last Modified on: 2/29/24 ~ Added some more comments and refactored methods to reduce redundant calls to getFirst()
 * Assignment/Project: A290 Android Development
 * Part of: Final Project, refers to activity_game.xml
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    // The snake, represented by a linked list of "Points", which are arrays with x position in index 0 and y in index 1
    private final LinkedList<int[]> snake = new LinkedList<>();
    //Convenient and descriptive way to index into the [X,Y] coordinate representation
    private final int X = 0, Y = 1;
    //Variable updated to show how many game updates that the end of the snake is not popped off, thus lengthening it.
    private int spareEnd = 0;
    //Initial length of the snake at the beginning of the game
    private final int initialSnakeLength = 4;
    //Speed that the game updates. should be < 1000
    private final int gameUpdateSpeed = 100;
    // Snake moving position. Values are from Direction enum
    // By default snake moves RIGHT
    private Direction direction = Direction.RIGHT;
    //Integer represent the location of the random apple
    private int[] randomApple = {0,0};
    private final Random rand = new Random();

    //SurfaceView and associated holder
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    //Dimension of surfaceView
    private int surfaceWidth, surfaceHeight;
    private TextView curScoreText;

    // score
    private int highScore;

    // snake size / point size
    private int pixelSize;
    private int pixelBuffer;
    //current Score
    private int curScore;

    //Timer that updates the game at a fixed rate
    private Timer timer;
    //Canvas used to draw game
    private Canvas canvas;

    //Colors associated with the given theme
    private Paint snakePaint;
    private Paint foodPaint;
    private int backgroundColor;

    //Shared preferences allowing to access the high score and selected theme
    private SharedPreferences preferences;
    //Media player needed to play sounds when an apple is eaten
    private MediaPlayer mp;


    /**
     * Method called when this activity is created. Sets up different views, objects,
     * and preferences that need to be initialized upon creation.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Sets view to this
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Accessing sharedPreferences.
        preferences = getSharedPreferences("GamePreferences", Context.MODE_PRIVATE);

        //Gets the highScore, if none is found assigns to 0
        highScore = preferences.getInt("highScore", 0);
        //Gets the current theme. If none is found sets to default
        applyTheme(ThemeList.getTheme(preferences.getString("theme", "default")));

        mp = MediaPlayer.create(this, R.raw.beep);

        // getting ImageButtons from xml file
        View upButton = findViewById(R.id.btn_Up);
        upButton.setOnClickListener(this::onClick);

        View downButton = findViewById(R.id.btn_Down);
        downButton.setOnClickListener(this::onClick);

        View leftButton = findViewById(R.id.btn_Left);
        leftButton.setOnClickListener(this::onClick);

        View rightButton = findViewById(R.id.btn_Right);
        rightButton.setOnClickListener(this::onClick);

        //Accessing surfaceView and score textview from the XML file
        surfaceView = findViewById(R.id.view_Canvas);
        curScoreText = findViewById(R.id.txt_CurScore);

        //Adding the callback
        surfaceView.getHolder().addCallback(this);
    }


    /**
     * Once the surface is created, assigns the surface holder and begins game
     * @param surfaceHolder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        //Gets the surface holder Surface Holder
        this.surfaceHolder = surfaceHolder;
        //Can now start game
        startGame();
    }

    /**
     * Sets up/resets variables related to the game, initializing all objects to proceed.
     * After setup calls runGame() to begin game loop
     */
    private void startGame() {
        //Gets the width and height of the surfaceView
        surfaceWidth = surfaceView.getWidth();
        surfaceHeight = surfaceView.getHeight();

        //Creates pixelSize to be congruent across screens
        pixelSize = surfaceWidth / 33;
        pixelBuffer = pixelSize / 12;

        //Resets snake
        snake.clear();

        //Resets default score and textview
        curScore = 0;
        curScoreText.setText(String.valueOf(curScore));

        //Resets direction of snake
        direction = Direction.RIGHT;

        //Beginning position of snake. has to be this far to adjust for initialSnakeLength
        int startOfNextPoint = pixelSize * initialSnakeLength;

        //Making initial snake based off of length
        for (int i = 0; i < initialSnakeLength; i++) {
            //Add to end of snake
            snake.addLast(new int[]{startOfNextPoint, pixelSize});
            //Sets next value one back
            startOfNextPoint = startOfNextPoint - pixelSize;
        }

        // Adds a random apple to the screen
        addApple();

        // Start the game
        runGame();
    }

    /**
     * Runs the game at consistent intervals.
     * Called by startGame() and finished when isGameOver() returns true;
     */
    private void runGame() {
        //Makes the timer and then updates the game at a fixed time, allowing for frames at a regular interval
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Get head of snake
                int[] snakeHead = snake.getFirst();

                //Check if an apple has been eaten
                if (isOverlap(snakeHead, randomApple)) {
                    growSnake();
                    addApple();
                }

                updateSnakeHead(snakeHead);

                snakeHead = snake.getFirst();

                //Checks to see if snake is out of bounds, if so clean up and launch dialog
                if (isGameOver(snakeHead)) {
                    //Destroys timer in order to stop this thread
                    destroyTimer();
                    //Create dialogue to play again or exit
                    createDialog();
                } else {
                    //Update the rest of the snake and then draw the next frame
                    updateSnakeEnd();
                    drawFrame();

                }
            }
        }, gameUpdateSpeed, gameUpdateSpeed);
    }

    /**
     * Draws the next game frame
     */
    public void drawFrame() {

        //Gets the canvas and locks it from any other threads
        canvas = surfaceHolder.lockCanvas();

        // Clears canvas with the theme background
        canvas.drawColor(backgroundColor);

        // Draw a random apple to be eaten by the snake
        drawPoint(randomApple, foodPaint);

        Iterator<int[]> iter = snake.iterator();
        //Draws head of snake, with subsequent points following
        drawPoint(iter.next(), snakePaint);

        while (iter.hasNext()) {
            int[] next = iter.next();
            drawPoint(next, snakePaint);
            //If the apple is on the snake, spawn another one
            if(isOverlap(next, randomApple)) {
                growSnake();
                addApple();
            }
        }
        //Updates changes made to the canvas
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    /**
     * Spawns a new random apple at beginning or when one is eaten
     */
    private void addApple() {
        //Keeps the positions bounded to any points on the surfaceView
        int randomXPosition = rand.nextInt(surfaceWidth / pixelSize);
        int randomYPosition = rand.nextInt(surfaceHeight / pixelSize);

        //Scales position to pixelSize
        randomApple[X] = pixelSize * randomXPosition;
        randomApple[Y] = pixelSize * randomYPosition;
    }


    /**
     * Update the end of the snake, removing or reducing "spareEnd"
     */
    private void updateSnakeEnd() {
        //Lengthens if spareEnd is 0, if not removes last part of the snake
        if(spareEnd < 1)
            snake.removeLast();
        else
            spareEnd--;
    }

    /**
     * Destroys the timer, stopping the thread
     */
    private void destroyTimer() {
        timer.purge();
        timer.cancel();
    }


    /**
     * Updates the snake's head based on the current direction it has
     * @param snakeHead
     */
    private void updateSnakeHead(int[] snakeHead) {
        //Moves snake based on direction
        //Adds or subtracts pixelSize, moving the snake one "pixel" in its respective direction
        switch (direction) {
            case UP:
                snake.addFirst(new int[]{snakeHead[X], snakeHead[Y] - pixelSize});
                break;
            case DOWN:
                snake.addFirst(new int[]{snakeHead[X], snakeHead[Y] + pixelSize});
                break;
            case RIGHT:
                snake.addFirst(new int[]{snakeHead[X] + pixelSize, snakeHead[Y]});
                break;
            case LEFT:
                snake.addFirst(new int[]{snakeHead[X] - pixelSize, snakeHead[Y]});
                break;
            default:
                Log.d("LifeCycle", "Snake has invalid direction");
        }
    }

    /**
     * Returns if two points overlap
     * @param point1
     * @param point2
     * @return if point1 and point2 represent the same point
     */
    private boolean isOverlap(int[] point1, int[] point2) {
        return point1[X] == point2[X] && point1[Y] == point2[Y];
    }

    /**
     * Draws a point as a rectangle
     * @param point - The point to be drawn
     * @param paint - The paint with which to draw
     */
    private void drawPoint(int[] point, Paint paint) {
        canvas.drawRect(point[X] + pixelBuffer, point[Y] + pixelBuffer, point[X] + pixelSize - pixelBuffer, point[Y] + pixelSize - pixelBuffer, paint);
    }

    /**
     * Adjusts variables allowing for the snake to grow by three
     */
    private void growSnake() {
        //Plays a beep since an apple has been consumed
        mp.start();

        // Adding 3 to sparEnd makes it so for 3 frames the tail won't be popped off
        spareEnd += 3;

        // setting score to TextViews
        updateScore();
    }

    /**
     * Updates both the curScore variable and score on the screen
     */
    private void updateScore() {
        curScore++;
        //Was having errors updating earlier. Since this is called from the timer I need to make sure this is updated on the main UI thread
        runOnUiThread(() -> curScoreText.setText(String.valueOf(curScore)));
    }

    /**
     * Checks whether the game is over by the snake being out of bounds
     * @param snakeHead
     * @return
     */
    private boolean isGameOver(int[] snakeHead) {

        // true if head touches edges
        if (snakeHead[X] < 0 || snakeHead[X] >= surfaceView.getWidth() || snakeHead[Y]< 0 || snakeHead[Y] >= surfaceView.getHeight()) {
            return true;
        } else {

            //Sees if snake has run into itself
            Iterator<int[]> iter = snake.iterator();
            iter.next();
            while(iter.hasNext()) {
                if(isOverlap(snakeHead, iter.next()))
                    return true;
            }
        }

        return false;
    }


    /**
     * On a directional button clicked, determines where to move the Snake
     * @param view
     */
    private void onClick(View view) {
        final int id = view.getId();
        //Updates movement if it wouldn't move the snake back into itself
        if(id == R.id.btn_Up && direction != Direction.DOWN)
            direction = Direction.UP;
        else if(id == R.id.btn_Down && direction != Direction.UP)
            direction = Direction.DOWN;
        else if(id == R.id.btn_Left && direction != Direction.RIGHT)
            direction = Direction.LEFT;
        else if(id==R.id.btn_Right && direction != Direction.LEFT)
            direction = Direction.RIGHT;
    }

    /**
     * Creates dialog for when game is over
     */
    private void createDialog() {
        boolean newHighScore = updateIfHighScore();

        //Build game over dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

        //Assigns message based on whether there is a new high score or not
        String msg = newHighScore ? "You got a new high score of " + highScore : "Score: " + curScore + "\nHigh score: " + highScore;

        builder.setMessage(msg);
        builder.setTitle("Game Over!");
        //Can't exit out of the dialog. Forces user to engage in order to not cause issues
        builder.setCancelable(false);

        //Creates Exit and Play again options
        builder.setPositiveButton("Exit", (dialogInterface, i) -> {
            //Goes to main activity
            startActivity(new Intent(GameActivity.this, MainActivity.class));
            //Finishes this activity. Its over
            finish();
        });

        builder.setNegativeButton("Play again", (dialogInterface, i) -> {
            // Restart the game
            startGame();
        });


        //Has to be shown on UI thread or causes crashes
        runOnUiThread(() -> builder.show());
    }

    /**
     * Updates the high score if the current score is greater
     * @return
     */
    private boolean updateIfHighScore() {
        if(curScore > highScore) {
            //writes to shared preferences
            highScore = curScore;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("highScore", highScore);
            editor.commit();
            return true;
        }

        return false;
    }

    /**
     * Returns a paint made by the given color
     * @param paint
     * @param color
     * @return
     */
    private Paint createPaint(Paint paint, int color) {
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        //Makes smooth edges
        paint.setAntiAlias(true);

        return paint;
    }

    /**
     * Applies the given theme to the different colors
     * @param theme
     */
    public void applyTheme(Theme theme) {
        this.foodPaint = createPaint(foodPaint, theme.getAppleColor());
        this.snakePaint  = createPaint(snakePaint, theme.getSnakeColor());
        this.backgroundColor = theme.getBackgroundColor();
    }

    //Overridden not used methods
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {}
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {}

    /**
     * onBackPressed() is overridden so that the user has to interact with the dialog created
     */
    @Override
    public void onBackPressed() {}

}
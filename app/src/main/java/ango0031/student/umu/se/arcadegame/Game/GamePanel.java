package ango0031.student.umu.se.arcadegame.Game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static android.R.attr.button;

/**
 * Created by Anton on 15/08/2017.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;


    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private boolean movingPlayer = false;

    private boolean gameOver = false;
    private long gameOverTime;
    private OrientationData orientationData;
    private long frameTime;
    private Button button;

    private Context context;
    private  Canvas canvas;


    public GamePanel(Context context, Button button) {
        super(context);
        getHolder().addCallback(this);

        canvas = new Canvas();
        thread = new MainThread(getHolder(), this, canvas);

        setFocusable(true);

        this.button = button;
        this.context = context;

        player = new RectPlayer(new Rect(100, 100, 200, 200), Color.rgb(255, 0, 0));
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT - 100);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200, 350, 150, player.getShots());

        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this, canvas);
        thread.setRunning(true);
        thread.start();
    }

    /**
     * Säger år tråden att sluta kalla på update och draw
     */
    public void shutDown() {
        thread.setRunning(false);
    }

    public void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT - 100);
        player.update(playerPoint);
        player.resetShots();
        obstacleManager = new ObstacleManager(200, 350, 150, player.getShots());
        movingPlayer = false;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();

            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }

    }

    /**
     * Tar emot ett touch event och om det är ett tryck avfyras ett skott
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!gameOver && player.getRectangle().contains((int) event.getX(), (int) event.getY())) {
                    movingPlayer = true;
                    player.shoot();
                }
                if (!gameOver) {
                    player.shoot();
                }
                if (gameOver && System.currentTimeMillis() - gameOverTime >= 1000) {
                    gameOver = false;
                    orientationData.newGame();
                    reset();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!gameOver && movingPlayer)
                    playerPoint.set((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
        }
        return true;
    }


    public void update() {
        if (!gameOver) {
            if (frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapstedTime = (int) (System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if (orientationData.getOrientation() != null && orientationData.getStartOrientation() != null) {
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];
                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH / 1000f; // Om skärmen är maximalt lutad -> en sekund att ta sig över den
                playerPoint.x += Math.abs(xSpeed * elapstedTime) > 5 ? xSpeed * elapstedTime : 0;
            }
            if (playerPoint.x < 0)
                playerPoint.x = 0;
            else if (playerPoint.x > Constants.SCREEN_WIDTH)
                playerPoint.x = Constants.SCREEN_WIDTH;

            player.update(playerPoint);
            obstacleManager.update();

            if (obstacleManager.playerCollides(player)) {
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.YELLOW);

        player.draw(canvas);
        obstacleManager.draw(canvas);
        if (gameOver) {
            int xPos = (canvas.getWidth() / 2);
            Paint textPaint = new Paint();
            textPaint.setARGB(200, 0, 0, 0);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(200);
            int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));

            canvas.drawText("Game Over", xPos, yPos, textPaint);
            textPaint.setTextSize(50);
            canvas.drawText("Touch the screen to try again or", xPos, yPos+100, textPaint);
            canvas.drawText("press back to go to the menu", xPos, yPos+150, textPaint);

        }

    }


}

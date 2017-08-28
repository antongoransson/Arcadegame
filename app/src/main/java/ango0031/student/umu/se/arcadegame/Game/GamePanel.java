package ango0031.student.umu.se.arcadegame.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import ango0031.student.umu.se.arcadegame.R;

/**
 * Created by Anton on 15/08/2017.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private boolean gameOver = false;
    private long gameOverTime;
    private OrientationData orientationData;
    private long frameTime;

    private Canvas canvas;


    private Bitmap daisies;
    private Rect daisiesRect;
    private ArrayList<Rect> daisiesRects;

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);

        canvas = new Canvas();
        setUpDrawables();

        setFocusable(true);
        initGame();

    }

    private void initGame() {
        player = new RectPlayer(new Rect(0, 0, Constants.SCREEN_WIDTH / 9, Constants.SCREEN_WIDTH / 8));
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT - 180);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200, 350, 150, player.getShots());

        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();
    }

    private void setUpDrawables() {
        BitmapFactory bf = new BitmapFactory();
        daisies = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.daisies);
        daisiesRect = new Rect(0, Constants.SCREEN_HEIGHT - 150, Constants.SCREEN_WIDTH - 600, Constants.SCREEN_HEIGHT);
        daisiesRects = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            daisiesRects.add(new Rect(i * Constants.SCREEN_WIDTH / 4, Constants.SCREEN_HEIGHT - 150, (i + 1) * Constants.SCREEN_WIDTH / 4, Constants.SCREEN_HEIGHT));
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this, canvas);
        Constants.INIT_TIME = System.currentTimeMillis();
        thread.setRunning(true);
        thread.start();
    }

    public void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT - 180);
        player.resetShots();
        obstacleManager = new ObstacleManager(200, 350, 150, player.getShots());
        player.update(playerPoint);
    }

    /**
     * När ytan förstörs försöker tråden stängas av
     *
     * @param holder
     */
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
     * Tar emot ett touch event och om det är ett tryck och det inte är gameover avfyras ett skott
     * annars ifall en halv sekund har gått startar spelet om
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!gameOver)
                    player.shoot();

                if (gameOver && System.currentTimeMillis() - gameOverTime >= 500) {
                    gameOver = false;
                    orientationData.newGame();
                    reset();
                }
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
                float xSpeed = roll * Constants.SCREEN_WIDTH / 1000f; // Om skärmen är maximalt lutad -> en sekund att ta sig över den
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
                obstacleManager.removeObstacles();
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * Ritar bakgrunden, blommorna, spelaren samt hindrena. Om spelet är slut ritas även en förklarande text
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.rgb(156, 204, 101));

        for (Rect r : daisiesRects)
            canvas.drawBitmap(daisies, null, r, new Paint());

        player.draw(canvas);
        obstacleManager.draw(canvas);
        if (gameOver) {
            int xPos = (canvas.getWidth() / 2);
            Paint textPaint = new Paint();
            textPaint.setARGB(200, 0, 0, 0);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(250);
            int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));

            canvas.drawText("Game Over", xPos, yPos, textPaint);
            textPaint.setTextSize(80);
            canvas.drawText("Touch the screen to try again or", xPos, yPos + 100, textPaint);
            canvas.drawText("press back to go to the menu", xPos, yPos + 190, textPaint);

        }
    }
}

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



//    private SceneManager manager;

    private Rect r = new Rect();

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


    public GamePanel(Context context, Button button) {
        super(context);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this, button);

        setFocusable(true);

        this.button = button;
        this.context=context;

        player = new RectPlayer(new Rect(100, 100, 200, 200), Color.rgb(255, 0, 0));
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200, 350, 75, Color.BLUE, player.getShots());

        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();


    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }
    public boolean gameOver(){
        return gameOver;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this, button);

        thread.setRunning(true);
        thread.start();
    }

    public void shutDown(){
        thread.setRunning(false);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!gameOver && player.getRectangle().contains((int) event.getX(), (int) event.getY())){
                    movingPlayer = true;
                    player.shoot();
                }
                if (!gameOver ){
                    player.shoot();
                }
                if (gameOver && System.currentTimeMillis() - gameOverTime >= 2000) {
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

    public void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200, 350, 75, Color.BLUE,player.getShots());
        movingPlayer= false;

    }


    public void update() {


        if (!gameOver) {
            if(frameTime < Constants.INIT_TIME)
                frameTime= Constants.INIT_TIME;
            int elapstedTime = (int) (System.currentTimeMillis()-frameTime);
            frameTime = System.currentTimeMillis();
            if(orientationData.getOrientation()!=null&& orientationData.getStartOrientation()!=null){
                float pitch = orientationData.getOrientation()[1]-orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2]-orientationData.getStartOrientation()[2];

                float xSpeed = 2*roll*Constants.SCREEN_WIDTH/1000f; // Om skärmen är maximalt lutad -> en sekund att ta sig över den
                float ySpeed = pitch*Constants.SCREEN_HEIGHT/1000f;

                playerPoint.x+=Math.abs(xSpeed*elapstedTime) > 5? xSpeed*elapstedTime: 0;
                playerPoint.y-=Math.abs(ySpeed*elapstedTime) > 5? ySpeed*elapstedTime: 0;
            }
            if(playerPoint.x <0)
                playerPoint.x = 0;
            else if (playerPoint.x>Constants.SCREEN_WIDTH)
                playerPoint.x = Constants.SCREEN_WIDTH;
            if(playerPoint.y <0)
                playerPoint.y = 0;
            else if (playerPoint.y>Constants.SCREEN_HEIGHT)
                playerPoint.y = Constants.SCREEN_HEIGHT;

            player.update(playerPoint);
            obstacleManager.update();

            if (obstacleManager.playerCollides(player)) {
                gameOver = true;

                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    public void terminate() {

    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.YELLOW);

        player.draw(canvas);
        obstacleManager.draw(canvas);
        if (gameOver) {
            Intent intent = new Intent();
            int xPos = (canvas.getWidth() / 2);
            Paint textPaint = new Paint();
            textPaint.setARGB(200, 254, 0, 0);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(200);
            int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
            //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.


            canvas.drawText("Game Over", xPos, yPos, textPaint);
        }

    }
    public void putScore(){
        SharedPreferences results = context.getSharedPreferences("RESULTS", Context.MODE_PRIVATE);
        List<String> list;
        SharedPreferences.Editor editor = results.edit();
        Set<String> score= results.getStringSet("SCORE", null);
        if(score!=null)
            score = new TreeSet<>(score);
         else
            score = new TreeSet<>();
        Log.d("score","SCORE "+score);
            score.add(score.toString());
            editor.putStringSet("SCORE", score);
            editor.commit();
    }
}

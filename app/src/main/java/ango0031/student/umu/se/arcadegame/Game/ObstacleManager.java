package ango0031.student.umu.se.arcadegame.Game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.provider.SyncStateContract;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Anton on 17/08/2017.
 */

public class ObstacleManager {
    // higher index = lower on screen = higher y value
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;

    private long startTime;
    private long initTime;

    private List<Rect> shots;
    private int score = 0;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color, List<Rect> shots) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.color = color;
        this.obstacleHeight = obstacleHeight;

        this.shots = shots;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<Obstacle>();

        populateObstacles();

    }

    private void populateObstacles() {
        int currY = -5 * Constants.SCREEN_HEIGHT / 4;
        while (currY < 0) {
            int xStart = (int) (Math.random() * (Constants.SCREEN_WIDTH));
            if (xStart < 50)
                xStart += 50;
            obstacles.add(new Obstacle(obstacleHeight, color, xStart, currY, playerGap));
            currY += obstacleHeight + obstacleGap;

        }
    }

    public boolean playerCollides(RectPlayer player) {
        for (Obstacle ob : obstacles) {
            if (ob.playerCollide(player) || ob.getRectangle().bottom > Constants.SCREEN_HEIGHT) {
                putScore();
                return true;
            }
        }
        return false;
    }

    public void putScore() {
        SharedPreferences results = Constants.CURRENT_CONTEXT.getSharedPreferences("RESULTS", Context.MODE_PRIVATE);
        List<String> list;
        SharedPreferences.Editor editor = results.edit();
        Set<String> scoreSet = results.getStringSet("SCORE", null);
        if (scoreSet != null)
            scoreSet = new TreeSet<>(scoreSet);
        else
            scoreSet = new TreeSet<>();
        Log.d("score", "SCORE " + scoreSet);
        scoreSet.add(Integer.toString(score));
        editor.putStringSet("SCORE", scoreSet);
        editor.commit();
    }

    public void update() {
        int elapsedtime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float) (Math.sqrt(1 + (startTime - initTime) / 1000.0)) * Constants.SCREEN_HEIGHT / (10000.0f);
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle ob = obstacles.get(i);
            ob.incrementY(speed * elapsedtime);
            for (int j = 0; j < shots.size(); j++) {
                Rect shot = shots.get(j);
                if (Rect.intersects(shot, ob.getRectangle())) {
                    shots.remove(shot);
                    obstacles.remove(i);
                    score++;
                }
            }
        }
        int xStart = (int) (Math.random() * (Constants.SCREEN_WIDTH - playerGap));
        if (obstacles.size() < 5)
            obstacles.add(0, new Obstacle(obstacleHeight, color, xStart, obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap, playerGap));
        if (obstacles.size() > 0 && obstacles.get(obstacles.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            obstacles.add(0, new Obstacle(obstacleHeight, color, xStart, obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap, playerGap));
            obstacles.remove(obstacles.size() - 1);
            score++;
        }
    }

    public void draw(Canvas canvas) {
        for (Obstacle ob : obstacles) {
            ob.draw(canvas);
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);
            canvas.drawText("Score " + score, 50, 100, paint);
        }
    }
}

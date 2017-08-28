package ango0031.student.umu.se.arcadegame.Game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.preference.PreferenceManager;
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
    private int obstacleGap;
    private int obstacleHeight;

    private long startTime;
    private long initTime;

    private List<Rect> shots;
    private int score = 0;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, List<Rect> shots) {
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.shots = shots;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();

        populateObstacles();

    }

    private void populateObstacles() {
        int currY = -5 * Constants.SCREEN_HEIGHT / 4;
        while (currY < 0) {
            int xStart = (int) (Math.random() * (Constants.SCREEN_WIDTH - 150));
            obstacles.add(new Obstacle(obstacleHeight, xStart, currY));
            currY += obstacleHeight + obstacleGap;
        }
    }

    public boolean playerCollides(RectPlayer player) {
        for (Obstacle ob : obstacles) {
            if (ob.playerCollide(player) || ob.getRectangle().bottom > Constants.SCREEN_HEIGHT - 150) {
                putScore();
                return true;
            }
        }
        return false;
    }

    public void removeObstacles() {
        obstacles = new ArrayList<>();
    }


    public void update() {
        if (startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;
        int elapsedtime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float) (Math.sqrt(1 + (startTime - initTime) / 1000.0)) * Constants.SCREEN_HEIGHT / (10000.0f);
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle ob = obstacles.get(i);
            ob.incrementY(speed * elapsedtime - 3);
            for (int j = 0; j < shots.size(); j++) {
                Rect shot = shots.get(j);
                if (Rect.intersects(shot, ob.getRectangle())) {
                    shots.remove(shot);
                    obstacles.remove(i);
                    score++;
                }
            }
        }
        // Om där är färre än 5 hinder lägg till ett nytt
        if (obstacles.size() < 5) {
            int xStart = (int) (Math.random() * (Constants.SCREEN_WIDTH - 150));
            int yStart = -obstacleHeight - obstacleGap;
            if (obstacles.size() > 0)
                yStart = obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap;
            obstacles.add(0, new Obstacle(obstacleHeight, xStart, yStart));
        }
    }

    /**
     * Ritar alla hindrena
     *
     * @param canvas ytan som ritas på
     */
    public void draw(Canvas canvas) {
        for (Obstacle ob : obstacles) {
            ob.draw(canvas);
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);
            canvas.drawText("Score: " + score, 50, 100, paint);
        }
    }

    /**
     * Lägger in resultatet i sharedpreferences
     */
    private void putScore() {
        SharedPreferences results = Constants.CURRENT_CONTEXT.getSharedPreferences("RESULTS", Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Constants.CURRENT_CONTEXT);
        String user_name = prefs.getString("user_name", "No User");
        List<String> list;
        SharedPreferences.Editor editor = results.edit();
        Set<String> scoreSet = results.getStringSet("SCORE", null);
        if (scoreSet != null)
            scoreSet = new TreeSet<>(scoreSet);
        else
            scoreSet = new TreeSet<>();
        scoreSet.add(Integer.toString(score) + ":" + Integer.toString(Constants.DIFFICULTY) + ":" + user_name);
        if (scoreSet.size() > 15) {
            removeLowest(scoreSet);
        }
        editor.putStringSet("SCORE", scoreSet);
        editor.commit();
    }

    /**
     * Tar bort det lägsta resultatet, kallas på när fler än 10 resultat är inlagda
     *
     * @param score
     */
    private void removeLowest(Set<String> score) {
        String lowest = "";
        int index = 0;
        for (String s : score) {
            String value = s.split(":")[0];
            if (index == 0 || Integer.valueOf((String) lowest).compareTo(Integer.valueOf((String) value)) > 0)
                lowest = s;
            index++;

        }
        score.remove(lowest);
    }
}

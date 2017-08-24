package ango0031.student.umu.se.arcadegame.Game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import static ango0031.student.umu.se.arcadegame.Game.MainThread.canvas;

/**
 * Created by Anton on 17/08/2017.
 */

public class Obstacle implements GameObject {

    private Rect rectangle;
    private Rect rectangle2;
    private int color;
    private int playerGap;

    public Obstacle(int rectHeight, int color, int startX, int startY, int playergap) {
        this.color = color;
        this.playerGap = playergap;
        rectangle = new Rect(startX-50, startY, startX, startY + rectHeight);
        System.out.println(Constants.SCREEN_WIDTH);
//        rectangle2 = new Rect(startX + playergap, startY, Constants.SCREEN_WIDTH, startY + rectHeight);
    }

    public boolean playerCollide(RectPlayer player) {
        return Rect.intersects(rectangle,player.getRectangle());

//                rectangle.contains(player.getRectangle().left, player.getRectangle().top) || rectangle.contains(player.getRectangle().right, player.getRectangle().top) ||
//                rectangle.contains(player.getRectangle().left, player.getRectangle().bottom) || rectangle.contains(player.getRectangle().right, player.getRectangle().bottom);
    }

    public Rect getRectangle() {
        return rectangle;
    }

    public void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawRect(rectangle, paint);
    }

    @Override
    public void update() {

    }

}

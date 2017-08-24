package ango0031.student.umu.se.arcadegame.Game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ango0031.student.umu.se.arcadegame.R;

/**
 * Created by Anton on 17/08/2017.
 */

public class RectPlayer implements GameObject {
    private Rect rectangle;
    private int color;
    private Bitmap idleImg;
    private Bitmap shot;


    private List<Rect> shots;

    public RectPlayer(Rect rectangle, int color) {
        this.rectangle = rectangle;
        this.color = color;
        shots = new ArrayList<>();
        BitmapFactory bf = new BitmapFactory();

        idleImg = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue);
        shot = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.snakeslime);

    }

    public void shoot() {
        Rect shot = new Rect(rectangle.left, rectangle.top - 100, rectangle.right - 50, rectangle.bottom);
        shots.add(new Rect(shot));
    }

    public void resetShots() {
        shots = new ArrayList<>();
    }

    public List<Rect> getShots() {
        return shots;
    }

    public Rect getRectangle() {
        return rectangle;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawBitmap(idleImg, null, rectangle, paint);
        for (Rect r : shots) {
            canvas.drawBitmap(shot, null, r, new Paint());
        }
    }



    public void update(Point point) {
        for (int i = 0; i < shots.size(); i++) {
            Rect r = shots.get(i);
            r.top -= 15f;
            r.bottom -= 15f;
            if (r.top < 0)
                shots.remove(i);

        }
        rectangle.set(point.x - rectangle.width() / 2, point.y - rectangle.height() / 2, point.x + rectangle.width() / 2, point.y + rectangle.height() / 2);
    }
}

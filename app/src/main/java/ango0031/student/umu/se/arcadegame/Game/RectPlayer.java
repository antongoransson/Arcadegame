package ango0031.student.umu.se.arcadegame.Game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import ango0031.student.umu.se.arcadegame.R;

/**
 * Created by Anton on 17/08/2017.
 */

public class RectPlayer {
    private Rect rectangle;
    private Bitmap idleImg;
    private Bitmap shot;

    private List<Rect> shots;

    public RectPlayer(Rect rectangle) {
        this.rectangle = rectangle;
        shots = new ArrayList<>();
        BitmapFactory bf = new BitmapFactory();

        idleImg = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue);
        shot = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.snakeslime);
    }

    /**
     * Skapar ett nytt skott som utgår från spelaren och lägger dit i listan
     */
    public void shoot() {
        Rect shot = new Rect(rectangle.left, rectangle.top - 150, rectangle.right - 80, rectangle.bottom);
        shots.add(new Rect(shot));
    }

    /**
     * Återställer antalet skott till 0
     */
    public void resetShots() {
        shots = new ArrayList<>();
    }

    /**
     * Returnerar listan med skotten som är i spel just nu
     *
     * @return
     */
    public List<Rect> getShots() {
        return shots;
    }

    /**
     * Returnerar rektangeln som utgör spelaren
     *
     * @return
     */
    public Rect getRectangle() {
        return rectangle;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawBitmap(idleImg, null, rectangle, paint);
        for (int i = 0; i < shots.size(); i++) {
            Rect r = shots.get(i);
            canvas.drawBitmap(shot, null, r, new Paint());
        }
    }

    /**
     * Uppdaterar spelaren position med hjälp av den nya punkten samt flyttar alla skott 15 pixlar uppåt
     *
     * @param point
     */
    public void update(Point point) {
        for (int i = 0; i < shots.size(); i++) {
            Rect r = shots.get(i);
            r.top -= 30f;
            r.bottom -= 30f;
            if (r.top < 0)
                shots.remove(i);
        }
        rectangle.set(point.x - rectangle.width() / 2, point.y - rectangle.height() / 2, point.x + rectangle.width() / 2, point.y + rectangle.height() / 2);
    }
}

package ango0031.student.umu.se.arcadegame.Game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import ango0031.student.umu.se.arcadegame.R;


/**
 * Created by Anton on 17/08/2017.
 */

public class Obstacle implements GameObject {
    private Rect rectangle;
    private Bitmap snail;

    /**
     * Skapar ett nytt hinder i form av en rektangel
     * @param rectHeight hur hög rektangeln ska vara
     * @param startX startvärdet för rektangels vänstra sida
     * @param startY start startvärdet för rektangels topp
     */
    public Obstacle(int rectHeight, int startX, int startY) {
        BitmapFactory bf = new BitmapFactory();
        snail = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.snail);
        rectangle = new Rect(startX, startY, startX + 150, startY + rectHeight);
    }

    /**
     * Returner ifall spelaren krockar med ett hinder
     *
     * @param player
     * @return true om spelaren träffats annars false
     */
    public boolean playerCollide(RectPlayer player) {
        return Rect.intersects(rectangle, player.getRectangle());
    }

    /**
     * Returnerar rektanglen som utgör hindret
     *
     * @return Rect som motsvarar hindret
     */
    public Rect getRectangle() {
        return rectangle;
    }

    /**
     * Flyttar hindret y pixlar neråt på skärmen
     *
     * @param y hur många pixlar hindret ska flyttas
     */
    public void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
    }

    /**
     * Ritar hindret i form av en snigel
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(snail, null, rectangle, new Paint());
    }
}

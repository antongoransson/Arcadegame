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
    private Animation idle;
    private Animation walkRight;
    private Animation walkLeft;

    private AnimationManager animManager;

    private List<Rect> shots;

    public RectPlayer(Rect rectangle, int color) {
        this.rectangle = rectangle;
        this.color = color;
        shots = new ArrayList<>();

        BitmapFactory bf = new BitmapFactory();

        Bitmap idleImg = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue);
        Bitmap walk1 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_walk1);
        Bitmap walk2 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_walk2);

        idle = new Animation(new Bitmap[]{idleImg}, 2);
        walkRight = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);

        Matrix m = new Matrix();
        m.preScale(-1, 1);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), m, false);

        walkLeft = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);

        animManager = new AnimationManager(new Animation[]{idle,walkRight,walkLeft});


    }
    public void shoot(){
        Rect shot = new Rect(rectangle.left,rectangle.top,rectangle.right-50,rectangle.bottom);
        shots.add(new Rect(shot));

    }
    public List<Rect> getShots(){
        return shots;
    }

    public Rect getRectangle() {
        return rectangle;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
//        canvas.drawRect(rectangle, paint);
        animManager.draw(canvas,rectangle);
        for(Rect r:shots){
              canvas.drawRect(r,paint);
        }
    }

    @Override
    public void update() {
        animManager.update();

    }

    public void update(Point point) {
        for(int i =0;i< shots.size();i++){
            Rect r = shots.get(i);
            r.top-=15f;
            r.bottom-=15f;
            if(r.top<0)
                shots.remove(i);

        }
        //Left,right,top,bottom
        float oldLeft = rectangle.left;
        rectangle.set(point.x - rectangle.width() / 2, point.y - rectangle.height() / 2, point.x + rectangle.width() / 2, point.y + rectangle.height() / 2);
        int state = 0;
        if (rectangle.left - oldLeft > 5) {
            state = 1;
        } else if (rectangle.left - oldLeft < -5) {
            state = 2;
        }
        animManager.playAnim(state);
        animManager.update();
    }
}

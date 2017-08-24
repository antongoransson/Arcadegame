package ango0031.student.umu.se.arcadegame.Game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;


/**
 * Created by Anton on 15/08/2017.
 */

public class MainThread extends Thread {
    public static final int MAX_FPS = 50;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    private Canvas canvas;

    /**
     * Skapar en ny tråd
     * @param surfaceHolder innehåller ytan där spelaren rör på sig
     * @param gamePanel
     * @param canvas
     */
    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel,Canvas canvas) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
        this.canvas = canvas;
    }

    /**
     * Sätter att tråden ska köras till true eller false
     * @param running true eller false
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Kallar på Gamepanels update samt draw MAX_FPS ggr/sekund
     */
    @Override
    public void run() {
        long startTime;
        long timeMillis = 1000 / MAX_FPS;
        long waitTime;
        long frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000 / MAX_FPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    gamePanel.update();
                    gamePanel.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            ;
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;
            try {
                if (waitTime > 0)
                    this.sleep(waitTime);

            } catch (Exception e) {
                e.printStackTrace();
            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == MAX_FPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println("AverageFPS "+ averageFPS);
            }
        }
    }
}

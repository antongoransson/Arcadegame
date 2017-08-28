package ango0031.student.umu.se.arcadegame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import ango0031.student.umu.se.arcadegame.Game.Constants;
import ango0031.student.umu.se.arcadegame.Game.GamePanel;

/**
 * Aktiviteten som startar själva spelet. Den sätter skärmen till fullscreen och sparar
 * enhetens bredd samt höjd
 */
public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.CURRENT_CONTEXT = this;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String difficulty = prefs.getString("difficulty_list", "1");
        Constants.DIFFICULTY = Integer.parseInt(difficulty);

        GamePanel gameView = new GamePanel(this);

        setContentView(gameView);
    }
}

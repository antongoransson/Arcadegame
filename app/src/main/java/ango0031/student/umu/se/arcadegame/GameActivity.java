package ango0031.student.umu.se.arcadegame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import ango0031.student.umu.se.arcadegame.Game.Constants;
import ango0031.student.umu.se.arcadegame.Game.GamePanel;

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

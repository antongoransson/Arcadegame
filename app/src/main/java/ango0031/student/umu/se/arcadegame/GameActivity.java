package ango0031.student.umu.se.arcadegame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import ango0031.student.umu.se.arcadegame.Game.Constants;
import ango0031.student.umu.se.arcadegame.Game.GamePanel;

public class GameActivity extends Activity {
    private Button endGameButton;

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


        FrameLayout game = new FrameLayout(this);
        LinearLayout gameWidgets = new LinearLayout(this);

        endGameButton = new Button(this);

        final GamePanel gameView = new GamePanel(this, endGameButton);
        TextView myText = new TextView(this);

        endGameButton.setWidth(300);
        endGameButton.setText("Go to menu Game");
        endGameButton.setVisibility(View.INVISIBLE);
        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GameActivity.this, MenuActivity.class);
                gameView.shutDown();
                startActivity(i);
            }
        });

        gameWidgets.addView(myText);
        gameWidgets.addView(endGameButton);

        game.addView(gameView);
        game.addView(gameWidgets);

//        endGameButton.setOnClickListener(this);
        setContentView(game);


//        Button menUButton = (Button) findViewById(R.id.go_to_menu);
//        menUButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(GameActivity.this, MenuActivity.class);
//                startActivityForResult(intent, 0);
//            }
//        });
    }

    public void setVisibile() {
        endGameButton.setVisibility(View.VISIBLE);
    }
}

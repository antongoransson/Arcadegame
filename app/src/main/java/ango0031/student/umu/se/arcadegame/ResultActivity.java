package ango0031.student.umu.se.arcadegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ango0031.student.umu.se.arcadegame.R;

public class ResultActivity extends AppCompatActivity {
    private TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        table = (TableLayout) findViewById(R.id.table);



//        TextView diffHeader = new TextView(this);
//
//        scoreHeader.setText("Score");
//        diffHeader.setText("Difficulty");
//        Log.d("String","String + "+"score");
//
//        topRow.addView(scoreHeader);
//        topRow.addView(diffHeader);
//        table.addView(topRow,0);
//

        List<String> score = getScore();
        for (int i = 0; i <= score.size(); i++) {

            TableRow row = new TableRow(this);
             TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView scoreTV = new TextView(this);
            TextView difficultyTV = new TextView(this);
            if(i==0){
                String k = "Score";
                 Log.d("String","String + "+"score");
                String d = "Difficulty";
                scoreTV.setText(score.get(i));
                difficultyTV.setText(score.get(i).split(":")[0]);
            }else if (score.get(i-1) != null) {
                String result = score.get(i-1);
                scoreTV.setText(result.split(":")[0]);
                difficultyTV.setText(result.split(":")[1]);
            }
            row.addView(scoreTV);
            row.addView(difficultyTV);
//            Log.d("WADSD","I + "+i);
            table.addView(row, i);
        }

    }

    public List<String> getScore() {
        SharedPreferences results = getSharedPreferences("RESULTS", Context.MODE_PRIVATE);
        List<String> list;
        Set<String> score = results.getStringSet("SCORE", null);
        if (score != null) {
            score = new TreeSet<>(score);
            list = new ArrayList<>(score);
            Object[] list1 = list.toArray();
            Arrays.sort(list1, new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    return Integer.valueOf(((String) o2).split(":")[0]).compareTo(Integer.valueOf(((String) o1).split(":")[0]));
                }
            });
            ;
            list = new ArrayList<>();
            for (Object o : list1) {
                list.add(o.toString());
            }
        } else
            list = new ArrayList<>();
        return list;


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, MenuActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

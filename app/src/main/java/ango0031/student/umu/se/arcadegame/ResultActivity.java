package ango0031.student.umu.se.arcadegame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Visar resultaten som fåtts, dessa hämtas via sharedpreferences
 */
public class ResultActivity extends AppCompatActivity {

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
        init();
    }

    /**
     * Skapar tabellrader från resultaten
     */
    private void init() {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table);
        List<String> score = getScore();
        for (int i = 0; i <= score.size(); i++) {
            TableRow row = new TableRow(this);

            TextView scoreTV = createTextView();
            TextView difficultyTV = createTextView();
            TextView userTV = createTextView();
            if (i == 0) {
                scoreTV.setText(R.string.score);
                difficultyTV.setText(R.string.difficulty);
                userTV.setText(R.string.user_name);
            } else if (score.get(i - 1) != null) {
                String result = score.get(i - 1);
                scoreTV.setText(result.split(":")[0]);
                difficultyTV.setText(getDifficulty(result.split(":")[1]));
                userTV.setText(result.split(":")[2]);
            }
            row.addView(scoreTV);
            row.addView(difficultyTV);
            row.addView(userTV);

            tableLayout.addView(row);
        }

    }
    private TextView createTextView(){
        TextView tempTV = new TextView(this);
        tempTV.setTextColor(Color.BLACK);
        tempTV.setTextSize(25f);
        tempTV.setGravity(Gravity.CENTER);
        return tempTV;
    }

    private String getDifficulty(String difficulty){
        switch (difficulty){
            case "0": return "Low";
            case "1": return "Medium";
            case "2": return "Hard";
            default: return "";
        }
    }

    /**
     * Sorterar samt returnerar resultaten
     * @return Resultaten i form av en lista
     */
    private List<String> getScore() {
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
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

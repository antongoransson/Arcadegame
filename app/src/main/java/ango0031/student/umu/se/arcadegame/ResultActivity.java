package ango0031.student.umu.se.arcadegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ango0031.student.umu.se.arcadegame.R;

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


        ListView list= (ListView)findViewById(R.id.results_list);
         List<String> results = getScore();
        Log.d("Hello","List"+results);
        list.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,results));

    }

    public List<String> getScore(){
        SharedPreferences results = getSharedPreferences("RESULTS", Context.MODE_PRIVATE);
        List<String> list;
        Set<String> score= results.getStringSet("SCORE", null);
        Log.d("Hello","Sore "+score);
        if(score!=null) {
            score = new TreeSet<>(score);
             list = new ArrayList<>(score);
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

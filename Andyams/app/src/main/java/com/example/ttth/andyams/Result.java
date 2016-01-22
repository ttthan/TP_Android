package com.example.ttth.andyams;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    TextView result;
    Button restart;
    ListView lv,lvPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        result = (TextView)findViewById(R.id.result);
        restart = (Button)findViewById(R.id.restart);
        lv = (ListView)findViewById(R.id.lvScore);

        lvPolicy = (ListView)findViewById(R.id.lvScorePolicy);

        final Andyams.ScorePolicy[] scorePolicy = Andyams.getScorePolicies();
        ArrayAdapter<Andyams.ScorePolicy> adapter = new ArrayAdapter<Andyams.ScorePolicy>
                (this, android.R.layout.simple_list_item_1, scorePolicy);

        lvPolicy.setAdapter(adapter);


        final int[] scoreResult = getIntent().getIntArrayExtra("resultPoplicy");
        Integer[] newScore = new Integer[scoreResult.length];
        int i = 0;
        for (int value : scoreResult) {
            newScore[i++] = Integer.valueOf(value);
        }
        ArrayAdapter<Integer> adapter1 = new ArrayAdapter<Integer>
                (this, android.R.layout.simple_list_item_1, newScore);

        lv.setAdapter(adapter1);

        int value = getIntent().getExtras().getInt("result");
        result.setText(value+"");
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(getApplicationContext(),MainActivity.class);
                finish();
                startActivity(resultIntent);
            }
        });
    }
    }


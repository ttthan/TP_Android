package com.example.ttth.chrono;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView time;
    Button start, stop, reset, refresh;
    long time1 = 0;
    long time2 = 0;
    long elapsedTime = 0;
Handler handler = new  Handler();
    Integer i =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button)findViewById(R.id.start);
        stop = (Button)findViewById(R.id.stop);
        reset = (Button)findViewById(R.id.reset);
        refresh = (Button)findViewById(R.id.refresh);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChrono();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopChrono();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetChrono();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateChrono();
            }
        });
    }

    public void startChrono(){

        if(time1>0) return ;
        time1 = System.nanoTime();
        time =(TextView)findViewById(R.id.time);
        //time.setText(TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS) + "");


        handler.post(updateRunnable);
    }

    public void stopChrono(){
        if(time1 < 0) return;
        elapsedTime += (System.nanoTime() - time1);
        time1 = -1;
        time =(TextView)findViewById(R.id.time);
        time.setText(TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS) + "");

    }

    public void resetChrono(){
        startChrono();
        elapsedTime = 0;
        time1 = -1;
        time =(TextView)findViewById(R.id.time);
        time.setText(TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS) + "");

    }

    public  void updateChrono(){
        long time2 = elapsedTime + ((time1 >= 0)?(System.nanoTime() - time1):0);
        time.setText(TimeUnit.SECONDS.convert(time2, TimeUnit.NANOSECONDS) + "");
    }



       private Runnable updateRunnable = new Runnable() {
            @Override
            public void run()
            {
                long time2 = elapsedTime + ((time1 >= 0)?(System.nanoTime() - time1):0);
                time.setText(TimeUnit.SECONDS.convert(time2, TimeUnit.NANOSECONDS) + "");
                handler.postDelayed(updateRunnable,500);
            }
        };

}

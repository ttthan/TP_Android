package com.example.ttth.coucoudroid;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends Activity {

    ImageButton start;
    Button stop;
    BroadcastReceiver coucouReceiver;
    Boolean available = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Coucou...", Toast.LENGTH_LONG).show();

        start = (ImageButton)findViewById(R.id.start);
        stop = (Button)findViewById(R.id.appel);
        stop.setClickable(false);
        stop.setEnabled(false);
        Calendar calendar = Calendar.getInstance();
        coucouReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().compareTo(Intent.ACTION_TIME_TICK)==0){
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR);
                    int min = calendar.get(Calendar.MINUTE);
                    if (min%15 == 0){
                        MyAsyncTask myAsyncTask = new MyAsyncTask();
                        myAsyncTask.execute();
                    }

                }
            }
        };

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!available) {
                    Toast.makeText(MainActivity.this, "Coucou reveille...", Toast.LENGTH_LONG).show();
                    coucouPlay();
                    available = true;
                    start.setImageResource(R.drawable.download);
                    stop.setClickable(false);
                    stop.setEnabled(false);
                    registerReceiver(coucouReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
                }
                else{
                    available = false;
                    start.setImageResource(R.drawable.images);
                    Toast.makeText(getApplicationContext(),"Coucou s'endorme Zzz...", Toast.LENGTH_LONG).show();
                    unregisterReceiver(coucouReceiver);
                    stop.setClickable(true);
                    stop.setEnabled(true);
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallTask myAsyncTask = new CallTask();
                myAsyncTask.execute();
            }
        });

        registerReceiver(coucouReceiver,new IntentFilter(Intent.ACTION_TIME_TICK));

//        MyAsyncTask myAsyncTask = new MyAsyncTask();
//        myAsyncTask.execute();


    }

    protected class MyAsyncTask extends AsyncTask<Void, Integer, Void>{
        @Override
        protected void onProgressUpdate(Integer... values) {
            TextView num = (TextView)findViewById(R.id.num);
            num.setText(values[0] + "");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            for (int i = 0; i<=100; i++){
                SystemClock.sleep(100);
                coucouPlay();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    protected class CallTask extends AsyncTask<Void, Integer, Void>{
        @Override
        protected void onProgressUpdate(Integer... values) {
            ImageButton call = (ImageButton)findViewById(R.id.start);
            call.setImageResource(R.drawable.download);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            coucouPlay();
            SystemClock.sleep(900);
            return null;
        }

        @Override
        protected void onPreExecute() {
            ImageButton call = (ImageButton)findViewById(R.id.start);
            call.setImageResource(R.drawable.download);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ImageButton call = (ImageButton)findViewById(R.id.start);
            call.setImageResource(R.drawable.images);
            super.onPostExecute(aVoid);
        }
    }


    @Override
    protected void onStop() {
        if (coucouReceiver!=null)
            unregisterReceiver(coucouReceiver);
        super.onStop();
    }

    public void coucouPlay(){
        Intent myIntent = new Intent(MainActivity.this,CoucouService.class);
        this.startService(myIntent);
    }

    public void coucouStop(){
        Intent myIntent = new Intent(MainActivity.this,CoucouService.class);
        this.stopService(myIntent);
    }



}

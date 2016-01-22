package com.example.ttth.andyams;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView[] de = new TextView[5];
    CheckBox[] check = new CheckBox[5];
    ImageView[] iv = new ImageView[5];
    ListView lv;
    Button btn, btnScore;
    Random r = new Random();
    public  Boolean init ;
    Integer num;
    Integer session;
    private Integer[] mThumbIds;


    TextView scoreResult;
    public int[] deVal = new int[5];
    int scoreFinal, score;
    Andyams.ScorePolicy[] p;
    boolean[] policy = {false,false,false,false,false,false,false,false,false,false,false,false,false};
    int[] resultPolicy = {0,0,0,0,0,0,0,0,0,0,0,0,0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = 0;
        num = 1;
        scoreFinal = 0;
        init = true;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        p = Andyams.getScorePolicies();
        mThumbIds = new Integer[]{R.drawable.img1,R.drawable.img2,R.drawable.img3,
                R.drawable.img4,R.drawable.img5,R.drawable.img6
        };
        de[0] = (TextView)findViewById(R.id.de1);
        de[1] = (TextView)findViewById(R.id.de2);
        de[2] = (TextView)findViewById(R.id.de3);
        de[3] = (TextView)findViewById(R.id.de4);
        de[4] = (TextView)findViewById(R.id.de5);

        iv[0] = (ImageView)findViewById(R.id.iv1);
        iv[1] = (ImageView)findViewById(R.id.iv2);
        iv[2] = (ImageView)findViewById(R.id.iv3);
        iv[3] = (ImageView)findViewById(R.id.iv4);
        iv[4] = (ImageView)findViewById(R.id.iv5);

        scoreResult = (TextView)findViewById(R.id.score);
        check[0] = (CheckBox)findViewById(R.id.check1);
        check[1] = (CheckBox)findViewById(R.id.check2);
        check[2] = (CheckBox)findViewById(R.id.check3);
        check[3] = (CheckBox)findViewById(R.id.check4);
        check[4] = (CheckBox)findViewById(R.id.check5);

        btn = (Button)findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num>0)
                {
                    num++;
                    MyAsyncTask myTask = new MyAsyncTask();
                    myTask.execute();

                    if (num == 3)
                        btn.setEnabled(false);
                }
                    else
                {

                }

            }
        });



        lv = (ListView)findViewById(R.id.lvPolicy);

        final Andyams.ScorePolicy[] scorePolicy = Andyams.getScorePolicies();
        ArrayAdapter<Andyams.ScorePolicy> adapter = new ArrayAdapter<Andyams.ScorePolicy>
                (this, android.R.layout.simple_list_item_1, scorePolicy);

        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (session<=13){

                    lv.getItemIdAtPosition(position);
                    if (!policy[position]){
                        session++;
                        score = p[position].computeScore(deVal);
                        resultPolicy[position] = score;
                        scoreFinal = scoreFinal + score;
                        Toast.makeText(getApplicationContext(),"Score marque: "+score+"",Toast.LENGTH_LONG).show();
                        scoreResult.setText(scoreFinal + "");
                        policy[position] = true;

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Politique deja choisi.",Toast.LENGTH_SHORT).show();
                    }
                    if (session == 13){
                        Intent resultIntent = new Intent(MainActivity.this,Result.class);
                        resultIntent.putExtra("result",scoreFinal);
                        resultIntent.putExtra("resultPoplicy",resultPolicy);
                        startActivity(resultIntent);


                    }
                    reset();
                }
                return false;
            }
        });

    }

    protected class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

        Integer[] mThumbIds = new Integer[]{R.drawable.img1,R.drawable.img2,R.drawable.img3,
                R.drawable.img4,R.drawable.img5,R.drawable.img6
        };
        Random r = new Random();
        //TextView[] de;
        TextView[] de = new TextView[5];
        CheckBox[] check = new CheckBox[5];
        ImageView[] iv = new ImageView[5];
        Button lancer;

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i<5; i++){
                SystemClock.sleep(500);
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            lancer = (Button)findViewById(R.id.btn1);
            lancer.setEnabled(true);
            Toast.makeText(getApplicationContext(),"Commencer à lancer les dé",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values){

            ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar);

            lancer = (Button)findViewById(R.id.btn1);
            lancer.setEnabled(false);

            de[0] = (TextView)findViewById(R.id.de1);
            de[1] = (TextView)findViewById(R.id.de2);
            de[2] = (TextView)findViewById(R.id.de3);
            de[3] = (TextView)findViewById(R.id.de4);
            de[4] = (TextView)findViewById(R.id.de5);

            check[0] = (CheckBox)findViewById(R.id.check1);
            check[1] = (CheckBox)findViewById(R.id.check2);
            check[2] = (CheckBox)findViewById(R.id.check3);
            check[3] = (CheckBox)findViewById(R.id.check4);
            check[4] = (CheckBox)findViewById(R.id.check5);
            iv[0] = (ImageView)findViewById(R.id.iv1);
            iv[1] = (ImageView)findViewById(R.id.iv2);
            iv[2] = (ImageView)findViewById(R.id.iv3);
            iv[3] = (ImageView)findViewById(R.id.iv4);
            iv[4] = (ImageView)findViewById(R.id.iv5);

            int val = values[0];
            int perCent;
            perCent = (val+1)*100/5;

            bar.setProgress(perCent);
            TextView tv = (TextView)findViewById(R.id.tv1);
            tv.setText(perCent + "%");
            if (init)
            {
                de[val].setText(String.valueOf( (r.nextInt(5)+1) ));
                init = false;
                iv[val].getLayoutParams().width = 150;
                iv[val].getLayoutParams().height = 150;
                iv[val].setAdjustViewBounds(true);
                iv[val].setImageResource(mThumbIds[Integer.parseInt(de[val].getText().toString())-1]);
            }
            else {
                if (!check[val].isChecked())
                    de[val].setText(String.valueOf( (r.nextInt(5) + 1)));
                iv[val].getLayoutParams().width = 150;
                iv[val].getLayoutParams().height = 150;
                iv[val].setAdjustViewBounds(true);
                iv[val].setImageResource(mThumbIds[Integer.parseInt(de[val].getText().toString())-1]);
            }

        }

        @Override
        protected void onPostExecute(Void result){

            lancer = (Button)findViewById(R.id.btn1);
            lancer.setEnabled(true);
            for (int i = 0; i<5; i++){
                deVal[i] = Integer.parseInt(de[i].getText().toString());
//                iv[i].getLayoutParams().width = 150;
//                iv[i].getLayoutParams().height = 150;
//                iv[i].setAdjustViewBounds(true);
//                iv[i].setImageResource(mThumbIds[deVal[i]-1]);
             }
            Toast.makeText(getApplicationContext(),"Terminer de lancer les dé",Toast.LENGTH_SHORT).show();
        }
    }


    public void reset(){
        num = 1;
        score = 0;
        init = true;
        de[0] = (TextView)findViewById(R.id.de1);

        de[1] = (TextView)findViewById(R.id.de2);
        de[2] = (TextView)findViewById(R.id.de3);
        de[3] = (TextView)findViewById(R.id.de4);
        de[4] = (TextView)findViewById(R.id.de5);
        check[0] = (CheckBox)findViewById(R.id.check1);
        check[1] = (CheckBox)findViewById(R.id.check2);
        check[2] = (CheckBox)findViewById(R.id.check3);
        check[3] = (CheckBox)findViewById(R.id.check4);
        check[4] = (CheckBox)findViewById(R.id.check5);

        for (int i = 0; i<5; i++){
            de[i].setText("");
            check[i].setChecked(false);
        }
    }

}


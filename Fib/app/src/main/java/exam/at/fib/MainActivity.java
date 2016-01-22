package exam.at.fib;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.math.BigInteger;

public class MainActivity extends Activity {

    public TextView tv ;
   public ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.button);
        tv = (TextView)findViewById(R.id.fiboResultTextView);
        pb = (ProgressBar)findViewById(R.id.fiboProgressBar);

        final EditText editText = (EditText)findViewById(R.id.fiboQueryEditView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiboTask fib = new FiboTask();
                fib.execute(Integer.parseInt(editText.getText().toString()));
            }
        });
    }


    public class FiboTask  extends AsyncTask<Integer, Integer, BigInteger> {
        public static final int PROGRESS_STEP = 10;
        private int n = -1;


        @Override
        protected void onPostExecute(BigInteger result) {
            Toast.makeText(MainActivity.this, "Fibonacci number has been computed", Toast.LENGTH_LONG).show();
        tv.setText(result.toString());
        }

        @Override
        protected void onCancelled(BigInteger bigInteger) {
            Toast.makeText(MainActivity.this,  "Fibonacci number computation has been canceled", Toast.LENGTH_LONG).show();
//        tv.setText("Cancelled");
//        tb.setChecked(false);
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(MainActivity.this, "Starting the computation", Toast.LENGTH_LONG).show();

        tv.setText("");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            tv.setText(values[0] + "");
            pb.setProgress(values[values.length - 1]);
        }

        @Override
        protected BigInteger doInBackground(Integer... params) {
            n = params[0] +1;
        BigInteger a = BigInteger.ONE, b = BigInteger.ONE;
        if (n < 3) return a;
        for (int i = 3; i < n; i++)
        {
            if (isCancelled()) return null;
            BigInteger tmp = a;
            a = b;
            b = tmp.add(a);
            if (i % PROGRESS_STEP == 0) publishProgress(i);
        }
        return b;
        }

    }


}
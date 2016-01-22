package com.example.ttth.chronometre;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
    public static final long BILLION = 1000000000L;
    public static final int SMOOTH_UPDATE_PERIOD = 1000/25; /* in milliseconds */
    public static final int TWITCH_UPDATE_PERIOD = 1000; /* update every second */
    public static final long DEFAULT_COUNTDOWN = 300 * BILLION; /* 5 minutes */

    private Bitmap clockBackground;
    private MyClockView clockView;

    class MyClockView extends View
    {
        private long time = -1; // in nanoseconds

        /** Definition of the drawables used to draw each clock hand */
        private final ClockHandDrawable[] hands = new ClockHandDrawable[]
                {
                        new ClockHandDrawable(60 * 1000000000L, 0.75f, 2), // for the second hand
                        new ClockHandDrawable(60 * 60 * 1000000000L, 0.60f, 15), // for the minute hand
                        new ClockHandDrawable(12 * 60 * 60 * 1000000000L, 0.40f, 30) // for the hour hand
                };

        public MyClockView(Context context)
        {
            super(context);
        }

        @Override
        public void onDraw(Canvas c)
        {
            // Draw the background
            Rect r = new Rect(0, 0, c.getWidth(), c.getHeight());
            c.drawBitmap(clockBackground, null, r, null);
            // ...and now draw the handles
            if (time >= 0)
                for (ClockHandDrawable h: hands)
                    h.draw(c);
        }

        public void setTime(long newTime)
        {
            if (newTime != time)
            {
                time = newTime;
                for (ClockHandDrawable h: hands)
                    h.setTime(newTime);
                invalidate(); // Order the view to be redrawn
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Load the saved fields in the bundle
        if (savedInstanceState != null)
        {
            cumulatedTime = savedInstanceState.getLong("cumulatedTime", 0L);
            sessionStartTime = savedInstanceState.getLong("sessionStartTime", -1L);
            countdown = savedInstanceState.getLong("countdown", -1L);
            lastCountdown = savedInstanceState.getLong("lastCountdown", DEFAULT_COUNTDOWN);
            smoothRefresh = savedInstanceState.getBoolean("smoothRefresh", false);
        }
        // Load a bitmap with the Swiss train clock background
        clockBackground =
                BitmapFactory.decodeResource(getResources(), R.raw.swissclock);
        // Create a view displaying the clock background and the hands
        clockView = new MyClockView(this);
        // Add a click listener to start/stop the chronometer
        clockView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (sessionStartTime < 0)
                    startChronometer();
                else
                    stopChronometer();
            }
        });
        setContentView(clockView);
    }

    /**
     * Stores the cumulated time (in nanoseconds)
     */
    private long cumulatedTime = 0L;

    /**
     * Start time of the last session (in nanoseconds)
     */
    private long sessionStartTime = -1L;

    /**
     * Mode of the chronometer:
     * -1 for the classical "countup" mode,
     * a positive value for the countdown mode (value of the countdown)
     */
    private long countdown = -1L;

    /** To memorize the latest countdown (to initialize the countdown dialog) */
    private long lastCountdown = DEFAULT_COUNTDOWN;

    /**
     * If the display is enabled
     */
    private boolean enabledDisplay;

    /**
     * If the smooth refresh is enabled
     */
    private boolean smoothRefresh = false;

    /**
     * To save the content of the fields if the activity is killed
     * (e.g. if the screen is rotated)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putLong("cumulatedTime", cumulatedTime);
        outState.putLong("sessionStartTime", sessionStartTime);
        outState.putLong("countdown", countdown);
        outState.putLong("lastCountdown", lastCountdown);
        outState.putBoolean("smoothRefresh", smoothRefresh);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // We enable the display
        enabledDisplay = true;
        triggerChronometerDisplay();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        // We disable the display
        enabledDisplay = false;
    }

    private void triggerChronometerDisplay()
    {
        final Runnable updateRunnable = new Runnable() {
            @Override
            public void run()
            {
                // We update the chronometer
                long time = cumulatedTime + ((sessionStartTime >= 0)?(System.nanoTime() - sessionStartTime):0);
                if (countdown >= 0)
                    time = Math.max(0, countdown - time);
                clockView.setTime(time);
                if (enabledDisplay && sessionStartTime > 0) // Continue the update chain
                    clockView.postDelayed(this, smoothRefresh?SMOOTH_UPDATE_PERIOD:TWITCH_UPDATE_PERIOD);
            }
        };
        updateRunnable.run(); // First run
    }

    private void startChronometer()
    {
        if (sessionStartTime >= 0) return; // already started
        sessionStartTime = System.nanoTime();
        triggerChronometerDisplay();
        Toast.makeText(this, "Chronometer has started", Toast.LENGTH_SHORT).show();
    }

    private void stopChronometer()
    {
        if (sessionStartTime < 0) return; // already stopped
        cumulatedTime += (System.nanoTime() - sessionStartTime);
        sessionStartTime = - 1L;
        Toast.makeText(this, "Chronometer has been stopped", Toast.LENGTH_SHORT).show();
    }

    private void resetChronometer()
    {
        stopChronometer();
        this.cumulatedTime = 0L;
        triggerChronometerDisplay();
    }


}
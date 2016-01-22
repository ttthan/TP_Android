package com.example.ttth.coucoudroid;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by TTTH on 1/7/2016.
 */
public class CoucouService extends Service {
    MediaPlayer coucouPlayer;

    public CoucouService(){

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        coucouPlayer = MediaPlayer.create(getApplicationContext(), R.raw.coucou);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        coucouPlayer.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        coucouPlayer.release();
        super.onDestroy();
    }
}

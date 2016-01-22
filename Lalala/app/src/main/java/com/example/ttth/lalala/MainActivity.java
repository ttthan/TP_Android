package com.example.ttth.lalala;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
Button play, stop, record;
//    AudioRecord myAudio = null;
MediaRecorder myAudio = null;
    String outputFile = null;
    static final String TAG = "MediaRecording";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button)findViewById(R.id.play);
        stop = (Button)findViewById(R.id.stop);
        record = (Button)findViewById(R.id.record);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        Toast.makeText(getApplicationContext(), outputFile, Toast.LENGTH_LONG).show();

        if (myAudio == null) {
            myAudio = new MediaRecorder();
            myAudio.reset();
            validateMicAvailability();

            myAudio.setAudioSource(MediaRecorder.AudioSource.MIC);
                myAudio.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                myAudio.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
                myAudio.setOutputFile(outputFile);
        }


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 try {
               myAudio.prepare();
               myAudio.start();
            }

            catch (IllegalStateException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }

            catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }

            
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myAudio.stop();
                myAudio.release();
                myAudio = null;


                Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(outputFile);
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    m.prepare();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                m.start();
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void validateMicAvailability()  {
        AudioRecord recorder =
                new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_DEFAULT, 44100);
        try{
            if(recorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED ){
                }

            recorder.startRecording();
            if(recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING){
                recorder.stop();
                 }
            recorder.stop();
        } finally{
            recorder.release();
            recorder = null;
        }
    }
}

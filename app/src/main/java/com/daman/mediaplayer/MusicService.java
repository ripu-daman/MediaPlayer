package com.daman.mediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.widget.Toast;

import java.io.IOException;

public class MusicService extends Service {

    String songToPlay,songpath;
    MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"MusicService Created..",Toast.LENGTH_LONG).show();
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        Toast.makeText(this,"MyMusicService Started..",Toast.LENGTH_LONG).show();
        songToPlay = intent.getStringExtra("keySong");
        songpath=intent.getStringExtra("songpath");
       // String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+songToPlay;

        try {
            mediaPlayer.setDataSource(songpath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId); }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

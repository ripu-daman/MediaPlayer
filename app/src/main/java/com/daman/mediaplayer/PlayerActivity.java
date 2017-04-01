package com.daman.mediaplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtSongName;
    ImageView albumart;
    SeekBar seekBar;
    ImageButton btnPlayPause;
    ImageButton btnPrevious;
    ImageButton btnNext;
    String songName,songImage,songpath;
    boolean isPlaying = false;
    void init(){
        txtSongName= (TextView) findViewById(R.id.musicTitle);
        albumart= (ImageView) findViewById(R.id.imageView);
        seekBar= (SeekBar) findViewById(R.id.seekBar);
        btnPlayPause= (ImageButton) findViewById(R.id.play_pause);
       btnPrevious= (ImageButton) findViewById(R.id.previous);
        btnNext= (ImageButton) findViewById(R.id.next);

        Intent rcv = getIntent();
        songName = rcv.getStringExtra("keySong");
        songpath=rcv.getStringExtra("songpath");
        Log.d("song in playactivity",songName);
        songImage = rcv.getStringExtra("songImage");
        txtSongName.setText(songName);
      //  albumart.setBackgroundResource(Integer.parseInt(songImage));
        btnPlayPause.setOnClickListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        init();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.play_pause){
            Intent intent = new Intent(PlayerActivity.this,MusicService.class);
            intent.putExtra("keySong",songName);
            intent.putExtra("songpath",songpath);

            startService(intent);


        }


    }
}

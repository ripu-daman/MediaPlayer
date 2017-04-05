package com.daman.mediaplayer;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener,RecognitionListener {

    TextView txtSongName;
    ImageView albumart;
    SeekBar seekBar;
    ImageButton btnPlayPause;
    ImageButton btnPrevious;
    ImageButton btnNext;
    ImageButton btnmic;
    String songName,songImage,songpath;
    int songindex;
    boolean isPlaying = true;
    BindMusicService service;
    Handler handler;
   Runnable runnable;
    ArrayList<FileModel> myList;
    int currentSongIndex;
    SpeechRecognizer speechRecognizer;
    ProgressDialog progressDialog;
    void init(){
        txtSongName= (TextView) findViewById(R.id.musicTitle);
        albumart= (ImageView) findViewById(R.id.imageView);
        seekBar= (SeekBar) findViewById(R.id.seekBar);
        btnPlayPause= (ImageButton) findViewById(R.id.play_pause);
       btnPrevious= (ImageButton) findViewById(R.id.previous);
        btnNext= (ImageButton) findViewById(R.id.next);
        btnmic= (ImageButton) findViewById(R.id.mic);
        speechRecognizer=speechRecognizer.createSpeechRecognizer(this);
        Intent rcv = getIntent();
        songName = rcv.getStringExtra("keySong");
        songpath=rcv.getStringExtra("songpath");
        songindex= rcv.getIntExtra("songIndex",0);
        Log.i("index in init", String.valueOf(songindex));
         myList= (ArrayList<FileModel>) rcv.getSerializableExtra("key");
        Log.d("song in playactivity", String.valueOf(myList));
        songImage = rcv.getStringExtra("songImage");
       if(songName.length()<4){
         songName=songName.toUpperCase();
       }else songName.substring(0,songName.length()-4);
        txtSongName.setText(songName);
      //  albumart.setBackgroundResource(Integer.parseInt(songImage));
        btnPlayPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnmic.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        speechRecognizer.setRecognitionListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Listening...");
        progressDialog.setCancelable(false);
        //seekBar.setEnabled(false);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        init();
        Intent intent=new Intent(PlayerActivity.this,BindMusicService.class);
        intent.putExtra("keySong",songName);
        intent.putExtra("songpath",songpath);
        intent.putExtra("songIndex",songindex);
        intent.putExtra("key",myList);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
        handler=new Handler();
        currentSongIndex=songindex;
        //seekUpdate();
    }
    ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            BindMusicService.MyBinder myBinder=(BindMusicService.MyBinder)iBinder;
            service=myBinder.getServiceReference();
            seekBar.setMax(service.getDuration());
            playCycle();
            if(isPlaying){
                service.playMusic();
                isPlaying=false;

                btnPlayPause.setBackgroundResource(R.drawable.pausebutton);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.play_pause){
            if(isPlaying){
                service.playMusic();
                isPlaying=false;

                btnPlayPause.setBackgroundResource(R.drawable.pausebutton);
            }else{
                service.pauseMusic();
                isPlaying=true;
                btnPlayPause.setBackgroundResource(R.drawable.playbutton);


            }

        }else if(id==R.id.next){
          onNext();

        }else if(id==R.id.previous){
            onPrevious();
        }else if(id==R.id.mic){
            service.pauseMusic();

            btnPlayPause.setBackgroundResource(R.drawable.playbutton);
            isPlaying=true;
            speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(this));
        }
    }
public void onPrevious(){
    if(currentSongIndex >0){
        songindex=songindex-1;
        service.playSong(songindex);
        currentSongIndex = currentSongIndex - 1;
        txtSongName.setText(myList.get(songindex).getName());
        Log.i("Test","if part");
        Log.i("current song", String.valueOf(currentSongIndex));
    }else{
        service.playSong(myList.size()-1);
        currentSongIndex =myList.size()-1;
        songindex=myList.size()-1;
        txtSongName.setText(myList.get(songindex).getName());
        Log.i("Test","else part");
        Log.i("current song", String.valueOf(currentSongIndex));
    }

    Log.d("index", String.valueOf(songindex));
    //txtSongName.setText();
    btnPlayPause.setBackgroundResource(R.drawable.pausebutton);
}
public void onNext(){
    if(currentSongIndex < (myList.size()-1)){
        songindex=songindex+1;
        service.playSong(songindex);
        currentSongIndex = currentSongIndex + 1;
        txtSongName.setText(myList.get(songindex).getName());
        Log.i("Test","if part");
        Log.i("current song", String.valueOf(currentSongIndex));
    }else{

        service.playSong(0);
        currentSongIndex = 0;
        songindex=0;
        txtSongName.setText(myList.get(songindex).getName());
        Log.i("Test","else part");
        Log.i("current song", String.valueOf(currentSongIndex));
    }

    Log.d("index", String.valueOf(songindex));
    //txtSongName.setText();
    btnPlayPause.setBackgroundResource(R.drawable.pausebutton);
}
    @Override


    protected void onDestroy() {
        super.onDestroy();

        service.stopMusic();

        unbindService(serviceConnection);
    }
/*Runnable runnable=new Runnable() {
    @Override
    public void run() {
seekUpdate();
    }
};
public void seekUpdate(){
    seekBar.setProgress(service.currentPosition());
    handler.postDelayed(runnable,1000);
}*/
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        service.seekTo(progress);


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    public  void playCycle(){
        seekBar.setProgress(service.currentPosition());
        if(service.isPlaying()){
        runnable=new Runnable() {
            @Override
            public void run() {
            playCycle();
            }
        };
        handler.postDelayed(runnable,1000);
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {
progressDialog.show();
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
progressDialog.dismiss();
    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> resultList = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (resultList != null && resultList.size() > 0) {
            String output = resultList.get(0);
            if(output.toLowerCase().contains("play") && output.toLowerCase().contains("music") ){
                service.playSong(songindex);
                isPlaying=false;

                btnPlayPause.setBackgroundResource(R.drawable.pausebutton);
                //tts.speak("I am fine, Thank You very much", TextToSpeech.QUEUE_FLUSH,null);

            }else if(output.toLowerCase().contains("pause") && output.toLowerCase().contains("music")){
                service.pauseMusic();
                isPlaying=true;
                btnPlayPause.setBackgroundResource(R.drawable.playbutton);

            }
            else if(output.toLowerCase().contains("next") && output.toLowerCase().contains("song")){
               onNext();
            }else if(output.toLowerCase().contains("previous") && output.toLowerCase().contains("song")){
                onPrevious();
            }
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}

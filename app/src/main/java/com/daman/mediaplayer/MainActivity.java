package com.daman.mediaplayer;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    ArrayList<FileModel> filelist;
    FileModel fm;
    FileAdapter fileAdapter;
    final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
    private String mp3Pattern = ".mp3";
    public String path;
    void init(){
        listView= (ListView) findViewById(R.id.listview);
        filelist= new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //readFile();
        getPlayList();
    }
  /*  void readFile(){
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(filePath);
        File files[] = file.listFiles();
        for (File f : files){
            FileModel fm = new FileModel();
//            if(f.isFile()){
//                fm.setImage(R.drawable.file);
//                fm.setName(f.getName());
//            }else{
//               fm.setImage(R.drawable.folder );
//               //fm.setImage(R.mipmap.);
//                fm.setName(f.getName());
//            }

            if(f.isFile() && f.getName().endsWith(".mp3")){
                fm.setImage(R.drawable.music);
                fm.setName(f.getName());
                filelist.add(fm);
            }

           // filelist.add(fm);

        }
        fileAdapter = new FileAdapter(this,R.layout.file_explorer,filelist);
        listView.setAdapter(fileAdapter);
    }*/

  public  void getPlayList(){
      if (MEDIA_PATH != null) {
          File home = new File(MEDIA_PATH);
          File[] listFiles = home.listFiles();
          if (listFiles != null && listFiles.length > 0) {
              for (File file : listFiles) {

                    if (file.isDirectory()) {
                      scanDirectory(file);
                  } else {
                      addSongToList(file);


                  }
              }
          }
      }
      fileAdapter = new FileAdapter(this,R.layout.file_explorer,filelist);
      listView.setAdapter(fileAdapter);
      listView.setOnItemClickListener(this);
  }
    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }

                }
            }
        }
    }
    private void addSongToList(File song) {
        if (song.getName().endsWith(mp3Pattern)) {
            fm = new FileModel();
            fm.setImage(R.drawable.music);
            fm.setName(song.getName().substring(0, (song.getName().length() - 4)));
            path =song.getAbsolutePath();
            fm.setPath(path);
            filelist.add(fm);

        }/*Log.i("test","path of song"+path);
            Log.i("test","path "+filelist.toString());*/
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       int songIndex=position;
        Log.i("test","You selected: "+filelist.get(position));
        Intent intent=new Intent(MainActivity.this,PlayerActivity.class);
        intent.putExtra("keySong",filelist.get(position).getName());

        intent.putExtra("songpath",filelist.get(position).getPath());
        //intent.putExtra("songImage",fm.getImage());
        intent.putExtra("songIndex", songIndex);
        intent.putExtra("key",filelist);
        startActivity(intent);
    }
}

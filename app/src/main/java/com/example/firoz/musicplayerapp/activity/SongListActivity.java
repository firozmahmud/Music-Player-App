package com.example.firoz.musicplayerapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.firoz.musicplayerapp.R;

import java.io.File;
import java.util.ArrayList;

public class SongListActivity extends AppCompatActivity {

    private ListView playerList;
    private String[] allSongs;
    private long previousTime;
    private long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        loadSongs();
    }

    private void initView() {
        setContentView(R.layout.activity_song_list);
        setTitle("My Player");

        playerList = findViewById(R.id.lvPlayerList);

    }

    private void loadSongs() {


        if (Build.VERSION.SDK_INT >= 23)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

                return;
            }


        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());


        allSongs = new String[mySongs.size()];

        if (mySongs.size() == 0) {
            Toast.makeText(this, "No Songs Found", Toast.LENGTH_LONG).show();
        }

        for (int i = 0; i < mySongs.size(); i++) {

            allSongs[i] = mySongs.get(i).getName().replace(".mp3", "");

        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.songs_layout, R.id.title, allSongs);

        playerList.setAdapter(adapter);


        // -- add click listener
        playerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(SongListActivity.this, PlayMusicActivity.class).putExtra("position", position).putExtra("songs", mySongs));
            }
        });

    }


    private ArrayList<File> findSongs(File root) {

        ArrayList<File> allsongs = new ArrayList<>();

        // --- get all file into an array
        File[] files = root.listFiles();

        if (files != null)
            for (File singleFile : files) {

                if (singleFile.isDirectory() && !singleFile.isHidden()) {

                    allsongs.addAll(findSongs(singleFile));

                } else {
                    if (singleFile.getName().endsWith(".mp3")) {

                        // --- add this mp3 into the arraylist
                        allsongs.add(singleFile);

                    }
                }

            }


        return allsongs;
    }


    @Override
    public void onBackPressed() {


        previousTime = currentTime;
        currentTime = System.currentTimeMillis();

        if ((currentTime - previousTime) < 250) {
            finish();
        } else {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (PlayMusicActivity.mediaPlayer != null) {
            PlayMusicActivity.mediaPlayer.stop();
            PlayMusicActivity.mediaPlayer.release();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


           loadSongs();

        } else {

            Toast.makeText(this, "Not Permitted", Toast.LENGTH_SHORT).show();
        }

    }
}

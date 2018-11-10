package com.example.firoz.musicplayerapp.activity;

import android.content.Intent;
import android.os.Environment;
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
}

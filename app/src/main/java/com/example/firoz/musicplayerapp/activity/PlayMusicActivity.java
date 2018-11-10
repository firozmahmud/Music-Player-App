package com.example.firoz.musicplayerapp.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.firoz.musicplayerapp.R;

import java.io.File;
import java.util.ArrayList;

public class PlayMusicActivity extends AppCompatActivity {


    private ArrayList<File> songs;
    private int position = 0;
    private ImageButton pauseBtn;
    public static MediaPlayer mediaPlayer;
    private Uri songUri;
    private SeekBar sbVolume;

    private SeekBarThread seekBarThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        getData();
        addListener();
        initMediaPlayer();

    }

    private void addListener() {


        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });


    }


    private void initView() {

        setContentView(R.layout.activity_play_music);

        pauseBtn = findViewById(R.id.pauseButton);
        sbVolume = findViewById(R.id.sbVolume);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }


    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            songs = (ArrayList) bundle.getParcelableArrayList("songs");

            position = bundle.getInt("position");

            setTitle(songs.get(position).getName());
        }
    }

    private void initMediaPlayer() {

        seekBarThread = new SeekBarThread();

        songUri = Uri.parse(songs.get(position).toString());

        mediaPlayer = MediaPlayer.create(this, songUri);
        mediaPlayer.start();

        sbVolume.setMax(mediaPlayer.getDuration());

        seekBarThread.start();


    }

    public void buttonClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.pauseButton:

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    pauseBtn.setBackgroundResource(R.drawable.pause);
                } else {
                    mediaPlayer.start();
                    pauseBtn.setBackgroundResource(R.drawable.play_button);

                }
                break;
            case R.id.forwardButton:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
                break;
            case R.id.backwardButton:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);

                break;
            case R.id.nextSongButton:

                mediaPlayer.stop();
                mediaPlayer.release();

                if (position == (songs.size() - 1)) {
                    Toast.makeText(this, "No more songs", Toast.LENGTH_SHORT).show();

                } else {

                    position++;
                }

                songUri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(this, songUri);
                mediaPlayer.start();
                sbVolume.setMax(mediaPlayer.getDuration());

                setTitle(songs.get(position).getName());


                break;
            case R.id.previousSongButton:

                mediaPlayer.stop();
                mediaPlayer.release();
                if (position == 0) {
                    Toast.makeText(this, "Not available", Toast.LENGTH_SHORT).show();

                } else {

                    position--;
                }

                songUri = Uri.parse(songs.get(position).toString());

                mediaPlayer = MediaPlayer.create(this, songUri);
                mediaPlayer.start();
                sbVolume.setMax(mediaPlayer.getDuration());

                setTitle(songs.get(position).getName());


                break;

        }

    }


    class SeekBarThread extends Thread {


        @Override
        public void run() {

            int totalDuration = mediaPlayer.getDuration();
            int currectPosition = 0;

            while (currectPosition < totalDuration) {

                try {

                    Thread.sleep(500);

                    if (mediaPlayer != null)
                        currectPosition = mediaPlayer.getCurrentPosition();
                    else currectPosition = 0;

                    if (sbVolume != null)
                        sbVolume.setProgress(currectPosition);

                } catch (InterruptedException e) {

                    e.printStackTrace();

                } catch (Exception e) {

                }
            }

        }
    }


    @Override
    public void onBackPressed() {

        // do nothing

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

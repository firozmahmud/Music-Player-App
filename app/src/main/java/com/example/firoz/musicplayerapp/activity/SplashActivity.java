package com.example.firoz.musicplayerapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.firoz.musicplayerapp.R;

public class SplashActivity extends AppCompatActivity {

    private LinearLayout parentLayout;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        parentLayout = findViewById(R.id.parent);
        titleTextView = findViewById(R.id.txt);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.my_animation);

        parentLayout.startAnimation(animation);
        titleTextView.setAnimation(animation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashActivity.this, SongListActivity.class));
                SplashActivity.this.finish();

            }
        }, 4000);
    }
}

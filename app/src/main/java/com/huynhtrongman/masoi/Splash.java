package com.huynhtrongman.masoi;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;

public class Splash extends AppCompatActivity {
    ImageView icon_wolf;
    SoundPool mSound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        icon_wolf = findViewById(R.id.icon_wolf);
        Data.sharedPreferences = getSharedPreferences("Account", MODE_PRIVATE);
        Data.editSharedPrefereces = Data.sharedPreferences.edit();
        AudioManager audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSound = new SoundPool.Builder().setAudioAttributes(audioAttrib).setMaxStreams(6).build();
            final int id_wolf = mSound.load(Splash.this,R.raw.wolf_intro,0);
            mSound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                    soundPool.play(id_wolf,1,1,0,0,1);
                }
            });
        }
        else {

            mSound = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        Animator animatorVBM = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in);
        animatorVBM.setTarget(icon_wolf);
        animatorVBM.setDuration(4000);
        animatorVBM.start();
        CountDownTimer countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(Splash.this,Login.class));
                finish();
            }
        };
        countDownTimer.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}


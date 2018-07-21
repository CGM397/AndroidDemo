package com.example.myapplication.mediaService;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.myapplication.activity.R;

public class MusicServer extends Service{

    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId){
        mediaPlayer = new MediaPlayer();
        //if(mediaPlayer.isPlaying())
            //mediaPlayer.pause();
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.music_00001);
        //mediaPlayer.setLooping(true);
        mediaPlayer.start();
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy(){
        if(mediaPlayer != null || mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}

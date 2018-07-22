package com.example.myapplication.mediaService;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

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
        mediaPlayer = MediaPlayer.create(getApplicationContext(),intent.getIntExtra("music_to_play",10086));
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

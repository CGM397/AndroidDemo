package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.myapplication.entity.AnalysisContent;
import com.example.myapplication.entity.Music;
import com.example.myapplication.mediaService.MusicServer;
import com.example.myapplication.service.MusicManagementService;
import com.example.myapplication.serviceImplement.MusicManagementImpl;

import java.io.File;
import java.util.ArrayList;

public class ShowAcceptedMusicDetail extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_accepted_music_detail);

        //get the textView
        TextView id = (TextView)findViewById(R.id.accepted_music_id);
        TextView name = (TextView)findViewById(R.id.accepted_music_name);
        TextView author = (TextView)findViewById(R.id.accepted_music_author);
        TextView selectMusicName = (TextView)findViewById(R.id.accepted_select_music);

        //get the music_info
        Intent intent = getIntent();
        String musicInfo = intent.getStringExtra("acceptedMusicInfo");

        //set music_id and music_name
        final String musicId = musicInfo.substring(0,musicInfo.indexOf("----"));
        String musicName = musicInfo.substring(musicInfo.indexOf("----")+4);
        id.append(musicId);
        name.append(musicName);
        selectMusicName.setText(musicName);

        //set music_author
        final MusicManagementService musicManagement = new MusicManagementImpl();
        final File file = new File(getFilesDir().getPath() + "/Music/" + musicId + ".txt");
        final Music selectedMusic = musicManagement.readMusic(file);
        author.append(selectedMusic.getAuthor());

        //return button
        Button returnBtn = (Button)findViewById(R.id.accepted_return_button);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到上一个页面
                finish();
            }
        });

        //play button
        Button acceptButton = (Button)findViewById(R.id.accepted_play_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ShowAcceptedMusicDetail.this, MusicServer.class);
                int url = getResources().getIdentifier("music_"+musicId,"raw","com.example.myapplication");
                intent1.putExtra("music_to_play",url);
                startService(intent1);
            }
        });
        //perform button
        Button performButton = (Button)findViewById(R.id.accepted_perform_button);
        performButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MIDIHandler midiHandler = new MIDIHandler();
                String path = getFilesDir().getPath() + "/MidiAnalysisResult/" + musicId + ".txt";
                ArrayList<AnalysisContent> storeContent = midiHandler.readMidi(path);
            }
        });
    }

    @Override
    protected void onStop(){
        Intent intent = new Intent(ShowAcceptedMusicDetail.this, MusicServer.class);
        stopService(intent);
        super.onStop();
    }
}

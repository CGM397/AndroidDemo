package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.myapplication.entity.Music;
import com.example.myapplication.service.MusicManagementService;
import com.example.myapplication.serviceImplement.MusicManagementImpl;

import java.io.File;

public class ShowGradedMusicDetail extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_graded_music_detail);

        //get the textViews
        TextView selectMusicName = (TextView)findViewById(R.id.graded_select_music);
        TextView musicId = (TextView)findViewById(R.id.graded_music_id);
        TextView musicAuthor = (TextView)findViewById(R.id.graded_music_author);

        Intent intent = getIntent();
        String musicInfo = intent.getStringExtra("gradedMusicInfo");

        //set the musicId and musicName
        final String id = musicInfo.substring(0,musicInfo.indexOf("----"));
        String name = musicInfo.substring(musicInfo.indexOf("----") + 4);
        musicId.append(id);
        selectMusicName.setText(name);

        //set musicAuthor
        final MusicManagementService musicManagement = new MusicManagementImpl();
        final File file = new File(getFilesDir().getPath() + "/Music/" + musicId + ".txt");
        final Music selectedMusic = musicManagement.readMusic(file);
        musicAuthor.append(selectedMusic.getAuthor());

        //return button
        Button returnBtn = (Button)findViewById(R.id.graded_return_button);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到上一个页面
                finish();
            }
        });
    }
}

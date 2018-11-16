package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.entity.Music;
import com.example.myapplication.service.MusicManagementService;
import com.example.myapplication.serviceImplement.MusicManagementImpl;

import java.io.File;
import java.io.InputStream;

public class ShowAvailableMusicDetail extends Activity {

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_available_music_detail);

        //get the textView
        TextView id = (TextView)findViewById(R.id.available_music_id);
        TextView name = (TextView)findViewById(R.id.available_music_name);
        TextView author = (TextView)findViewById(R.id.available_music_author);
        TextView selectMusicName = (TextView)findViewById(R.id.available_select_music);

        //get the music_info
        Intent intent = getIntent();
        String musicInfo = intent.getStringExtra("availableMusicInfo");

        //set music_id and music_name
        final String musicId = musicInfo.substring(0,musicInfo.indexOf("----"));
        String musicName = musicInfo.substring(musicInfo.indexOf("----")+4);
        id.append(musicId);
        name.append(musicName);
        selectMusicName.setText(musicName);

        final MusicManagementService musicManagement = new MusicManagementImpl();
        final MIDIHandler midiHandler = new MIDIHandler();
        final File file = new File(getFilesDir().getPath() + "/Music/" + musicId + ".txt");
        int url = getResources().getIdentifier("music_"+musicId,"raw","com.example.myapplication");
        final InputStream midiFileInputStream = getResources().openRawResource(url);
        final String analyseResultPath = getFilesDir().getPath() + "/MidiAnalysisResult/" + musicId + ".txt";

        //set music_author
        final Music selectedMusic = musicManagement.readMusic(file);
        author.append(selectedMusic.getAuthor());

        //return button
        Button returnBtn = (Button)findViewById(R.id.available_return_button);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到上一个页面
                finish();
            }
        });

        //accept button
        Button acceptButton = (Button)findViewById(R.id.available_accept_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                midiHandler.analyseMidi(midiFileInputStream,analyseResultPath);
                selectedMusic.setState(1);
                selectedMusic.setAnalyzed(true);
                selectedMusic.setAnalysisContentId(musicId);
                musicManagement.writeMusic(file, selectedMusic);
                Toast.makeText(ShowAvailableMusicDetail.this,"接收成功(已解析)",Toast.LENGTH_SHORT).show();
                //返回到上一层界面
                mHandler.sendEmptyMessageDelayed(1,1000);
            }
        });
    }
}

package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;
import com.example.myapplication.entity.Music;
import com.example.myapplication.service.MusicManagementService;
import com.example.myapplication.serviceImplement.MusicManagementImpl;

import java.io.File;

public class MainMenu extends Activity {

    private static boolean isExit = false;
    private static String firstMusicPath = "/Music/00001.txt";
    private static String secondMusicPath = "/Music/00002.txt";
    private static String thirdMusicPath = "/Music/00003.txt";

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            myHandler.sendEmptyMessageDelayed(0, 2003);
        } else {
            finish();
            System.exit(0);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        //初始化数据
        File musicInfoFile = new File(getFilesDir().getPath()+"/Music");
        File midiInfoFile = new File(getFilesDir().getPath()+"/MidiAnalysisResult");
        if(!musicInfoFile.exists())
            musicInfoFile.mkdirs();
        if(!midiInfoFile.exists())
            midiInfoFile.mkdirs();

        Music one = new Music("00001", "小星星","Mozart",
                "Null", false, 0);
        Music two = new Music("00002", "茉莉花","何仿(整理改编)",
                "Null", false, 0);
        Music third = new Music("00003", "让我们荡起双桨","刘炽",
                "Null", false, 0);
        File file1 = new File(getFilesDir().getPath()+firstMusicPath);
        File file2 = new File(getFilesDir().getPath()+secondMusicPath);
        File file3 = new File(getFilesDir().getPath()+thirdMusicPath);

        if(!file1.exists() && !file2.exists() && !file3.exists()){
            MusicManagementService musicManagement = new MusicManagementImpl();
            musicManagement.writeMusic(file1, one);
            musicManagement.writeMusic(file2, two);
            musicManagement.writeMusic(file3, third);
        }

        //add buttonListeners
        RadioButton available = (RadioButton)findViewById(R.id.available);
        RadioButton accepted = (RadioButton)findViewById(R.id.accepted);
        RadioButton graded = (RadioButton)findViewById(R.id.graded);

        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换界面
                Intent intent = new Intent(MainMenu.this, ShowAvailableMusic.class);
                startActivity(intent);
                finish();
            }
        });
        accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换界面
                Intent intent = new Intent(MainMenu.this, ShowAcceptedMusic.class);
                startActivity(intent);
                finish();
            }
        });
        graded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换界面
                Intent intent = new Intent(MainMenu.this, ShowGradedMusic.class);
                startActivity(intent);
                finish();
            }
        });
    }

}

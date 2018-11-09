package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.example.myapplication.entity.Music;
import com.example.myapplication.layout.MusicAdapter;
import com.example.myapplication.service.MusicManagementService;
import com.example.myapplication.serviceImplement.MusicManagementImpl;

import java.util.ArrayList;

public class ShowAvailableMusic extends Activity{

    private static boolean isExit = false;

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
            myHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_available_music);

        //add buttonListeners
        RadioButton available = (RadioButton)findViewById(R.id.available);
        available.setChecked(true);
        RadioButton accepted = (RadioButton)findViewById(R.id.accepted);
        RadioButton graded = (RadioButton)findViewById(R.id.graded);

        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换界面
                Intent intent = new Intent(ShowAvailableMusic.this, ShowAvailableMusic.class);
                startActivity(intent);
                finish();
            }
        });
        accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换界面
                Intent intent = new Intent(ShowAvailableMusic.this, ShowAcceptedMusic.class);
                startActivity(intent);
                finish();
            }
        });
        graded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换界面
                Intent intent = new Intent(ShowAvailableMusic.this, ShowGradedMusic.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 将界面数据初始化方法写在onResume()中，可以实现从另一activity返回时自动刷新此界面
     */
    @Override
    protected void onResume(){
        super.onResume();

        //add listView
        MusicManagementService musicManagement = new MusicManagementImpl();
        final ArrayList<String> musicList = new ArrayList<>();
        String dirName = getFilesDir().getPath()+"/Music";
        ArrayList<Music> musicArrayList = musicManagement.getAllAvailableMusic(dirName);
        for(Music oneMusic: musicArrayList)
            musicList.add(oneMusic.getMusicId()+"----"+oneMusic.getMusicName());

        //自定义适配器
        MusicAdapter adapter = new MusicAdapter(ShowAvailableMusic.this, R.layout.sublayout_music_list, musicList);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        //add listItemListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long Id) {
                String musicInfo = musicList.get(position);
                Intent intent = new Intent(ShowAvailableMusic.this, ShowAvailableMusicDetail.class);
                intent.putExtra("availableMusicInfo",musicInfo);
                startActivityForResult(intent,1);
            }
        });
    }
}

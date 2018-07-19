package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import com.example.myapplication.entity.Music;
import com.example.myapplication.layout.MusicAdapter;
import com.example.myapplication.service.MusicManagementService;
import com.example.myapplication.serviceImplement.MusicManagementImpl;

import java.util.ArrayList;

public class ShowGradedMusic extends Activity{

    private static boolean isExit = false;

    Handler myHandler = new Handler() {

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

    public void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            myHandler.sendEmptyMessageDelayed(0, 2002);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_graded_music);

        RadioButton available = (RadioButton)findViewById(R.id.available);
        RadioButton accepted = (RadioButton)findViewById(R.id.accepted);
        RadioButton graded = (RadioButton)findViewById(R.id.graded);
        graded.setChecked(true);

        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换界面
                Intent intent = new Intent(ShowGradedMusic.this, ShowAvailableMusic.class);
                startActivity(intent);
                finish();
            }
        });
        accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换界面
                Intent intent = new Intent(ShowGradedMusic.this, ShowAcceptedMusic.class);
                startActivity(intent);
                finish();
            }
        });
        graded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换界面
                Intent intent = new Intent(ShowGradedMusic.this, ShowGradedMusic.class);
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
        ArrayList<Music> musicArrayList = musicManagement.getAllGradedMusic(dirName);
        for(int i = 0; i < musicArrayList.size(); i++)
            musicList.add(musicArrayList.get(i).getMusicId()+"----"+musicArrayList.get(i).getMusicName());

        //自定义适配器
        MusicAdapter adapter = new MusicAdapter(ShowGradedMusic.this, R.layout.sublayout_music_list, musicList);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        //add listItemListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long Id) {
                String musicInfo = musicList.get(position);
                Intent intent = new Intent(ShowGradedMusic.this, ShowGradedMusicDetail.class);
                intent.putExtra("gradedMusicInfo",musicInfo);
                startActivityForResult(intent,1);
            }
        });
    }
}

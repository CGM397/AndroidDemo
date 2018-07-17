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
import com.example.myapplication.layout.MusicAdapter;

import java.util.ArrayList;

public class ShowAvailableMusic extends Activity{

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

        //add listView
        final ArrayList<String> musicList = new ArrayList<>();
        musicList.add("hello");
        musicList.add("android");
        //自定义适配器
        MusicAdapter adapter = new MusicAdapter(ShowAvailableMusic.this, R.layout.sublayout_music_list, musicList);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        //add listItemListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long Id) {
                String musicName = musicList.get(position);
                Intent intent = new Intent(ShowAvailableMusic.this, ShowAvailableMusicDetail.class);
                intent.putExtra("availableMusicName",musicName);
                startActivityForResult(intent,1);
            }
        });


        //add buttonListeners
        RadioButton available = (RadioButton)findViewById(R.id.available);
        available.setChecked(true);
        RadioButton accepted = (RadioButton)findViewById(R.id.accepted);
        RadioButton analyzed = (RadioButton)findViewById(R.id.analyzed);

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
        analyzed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换界面
                Intent intent = new Intent(ShowAvailableMusic.this, ShowAnalyzedMusic.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

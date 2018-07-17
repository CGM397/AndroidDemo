package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainMenu extends Activity {

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        RadioButton available = (RadioButton)findViewById(R.id.available);
        available.setChecked(true);
        RadioButton accepted = (RadioButton)findViewById(R.id.accepted);
        RadioButton analyzed = (RadioButton)findViewById(R.id.analyzed);

        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换界面
                Intent intent = new Intent(MainMenu.this, ShowAvailableMusic.class);
                startActivity(intent);
                finish();               //确保退出程序时所有activity都结束，不会返回到上一层界面
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
        analyzed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换界面
                Intent intent = new Intent(MainMenu.this, ShowAnalyzedMusic.class);
                startActivity(intent);
                finish();
            }
        });
    }

}

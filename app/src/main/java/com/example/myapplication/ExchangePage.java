package com.example.myapplication;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class ExchangePage extends Activity {
    public void changeToAvailableMusic(Button available){
        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换界面
                setContentView(R.layout.show_available_music);
            }
        });
    }

    public void changeToAcceptedMusic(Button accepted){
        accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换界面
                setContentView(R.layout.show_accepted_music);
            }
        });
    }

    public void changeToAnalyzedMusic(Button available){
        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换界面
                setContentView(R.layout.show_analyzed_music);
            }
        });
    }

}

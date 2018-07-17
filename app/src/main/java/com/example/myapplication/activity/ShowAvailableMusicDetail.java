package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowAvailableMusicDetail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_available_music_detail);

        //get the textView
        TextView id = (TextView)findViewById(R.id.music_id);
        TextView name = (TextView)findViewById(R.id.music_name);

        //get the music name
        Intent intent = getIntent();
        String musicName = intent.getStringExtra("availableMusicName");

        //set text
        id.append("id");
        name.append(musicName);

        Button returnBtn = (Button)findViewById(R.id.return_button);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到上一个页面
                finish();
            }
        });
    }
}

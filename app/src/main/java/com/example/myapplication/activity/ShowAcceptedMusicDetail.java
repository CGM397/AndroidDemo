package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.entity.AnalysisContent;
import com.example.myapplication.entity.Music;
import com.example.myapplication.mediaService.MusicServer;
import com.example.myapplication.service.MusicManagementService;
import com.example.myapplication.serviceImplement.MusicManagementImpl;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class ShowAcceptedMusicDetail extends Activity {

    //服务器IP地址，可能会改变，因为IP地址是动态获得的
    private final String HOST_IP = "192.168.4.1";
    private final int HOST_PORT = 8080;

    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message.what == 0x1){
                Toast.makeText(ShowAcceptedMusicDetail.this,"开始建立连接！",Toast.LENGTH_SHORT).show();
            }else if(message.what == 0x2){
                Toast.makeText(ShowAcceptedMusicDetail.this,"开始传输数据！",Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    });

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_accepted_music_detail);

        //get the textView
        TextView id = (TextView)findViewById(R.id.accepted_music_id);
        TextView name = (TextView)findViewById(R.id.accepted_music_name);
        TextView author = (TextView)findViewById(R.id.accepted_music_author);

        //get the music_info
        Intent intent = getIntent();
        String musicInfo = intent.getStringExtra("acceptedMusicInfo");

        //set music_id and music_name
        final String musicId = musicInfo.substring(0,musicInfo.indexOf("----"));
        final String musicName = musicInfo.substring(musicInfo.indexOf("----")+4);
        id.append(musicId);
        name.append(musicName);

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
                final ArrayList<AnalysisContent> storeContent = midiHandler.readMidi(path);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Socket socket = new Socket(HOST_IP, HOST_PORT);
                            PrintStream printStream = new PrintStream(socket.getOutputStream());
                            printStream.println(musicName);
                            printStream.close();
                            socket.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();

                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            myHandler.sendEmptyMessage(0x1);
                            Socket socket = new Socket(HOST_IP, HOST_PORT);
                            myHandler.sendEmptyMessage(0x2);
                            PrintStream printStream = new PrintStream(socket.getOutputStream());
                            for(AnalysisContent content: storeContent){
                                printStream.print(content.getTimeIntervalFromLastCommand() + ",");
                                printStream.print(content.getState() + ",");
                                printStream.print(content.getMusicNote() + ",");
                                printStream.print(content.getMusicScale() + ",");
                                printStream.println(content.getDuration());
                            }
                            printStream.close();
                            socket.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();*/
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

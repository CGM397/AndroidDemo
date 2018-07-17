package com.example.myapplication.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.myapplication.activity.R;

import java.util.List;

public class MusicAdapter extends ArrayAdapter<String> {

    private int resourceId;

    public MusicAdapter(Context context, int textViewResourceId, List<String> objects){
        super(context, textViewResourceId, objects);

        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String musicName = getItem(position);
        View view  = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView music_name = (TextView)view.findViewById(R.id.music_name);
        music_name.setText(musicName);
        return view;
    }
}

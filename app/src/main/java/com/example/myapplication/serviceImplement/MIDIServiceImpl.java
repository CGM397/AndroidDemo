package com.example.myapplication.serviceImplement;

import com.example.myapplication.service.MIDIService;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MIDIServiceImpl implements MIDIService {

    public ArrayList<String> getSequence(String path){
        ArrayList<String> store = new ArrayList<>();
        try{
            InputStream inputStream = new FileInputStream(path);
            int res;
            while((res = inputStream.read()) != -1){
                String tmp = Integer.toHexString(res);
                if(tmp.length() == 1)
                    tmp = "0" + tmp;
                store.add(tmp);
            }
            for (String str : store) {
                System.out.print(str + " ");
            }
            System.out.println();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return store;
    }

    public ArrayList<ArrayList<String>> getTracks(ArrayList<String> store){
        String MTrk = "4d54726b";           //轨道块标记
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        for(int i = 14; i < store.size() - 4; i++){
            if((store.get(i) + store.get(i+1) + store.get(i+2) + store.get(i+3)).equals(MTrk)){
                int chunkLen = Integer.parseInt(store.get(i+4)+store.get(i+5)+store.get(i+6)+store.get(i+7),16);
                i += 8;
                ArrayList<String> oneChunk = new ArrayList<>();
                int count = 0;
                while(i < store.size() && count < chunkLen){
                    oneChunk.add(store.get(i));
                    i++;
                    count++;
                }
                i--;
                res.add(oneChunk);
            }
        }
        return res;
    }

    public ArrayList<Integer> getDeltaTime(ArrayList<String> event) {
        ArrayList<String> store = new ArrayList<>();

        for(String str : event){
            if(Integer.valueOf(str.charAt(0)+"",16) < 8){
                store.add(str);
                break;
            }else
                store.add(str);
        }
        int len = store.size();
        ArrayList<Integer> res = new ArrayList<>();
        res.add(len);
        int res2 = 0;
        if(len > 0){
            for(int i = 0; i < len; i++){
                int coefficient = Integer.valueOf(store.get(i),16) - 128;
                if(coefficient < 0)
                    coefficient += 128;
                res2 += coefficient * Math.pow(128, len-i-1);
            }
        }
        res.add(res2);
        return res;
    }

    public String getMusicalNote(String note) {
        int num = Integer.valueOf(note,16);
        ArrayList<String> musicalName = new ArrayList<>();
        musicalName.add("C");
        musicalName.add("#C");
        musicalName.add("D");
        musicalName.add("#D");
        musicalName.add("E");
        musicalName.add("F");
        musicalName.add("#F");
        musicalName.add("G");
        musicalName.add("#G");
        musicalName.add("A");
        musicalName.add("#A");
        musicalName.add("B");
        int pos = num % 12;
        int musicalScale = num / 12 - 1;
        String res = musicalName.get(pos);
        res += musicalScale;
        return res;
    }

    public ArrayList<String> getEventLen(String command, String lastCommand, int offset, ArrayList<String> leftEvents) {
        char leftNybble = command.charAt(0);
        ArrayList<String> res = new ArrayList<>();
        if(leftNybble == '8'){
            res.add("2");
            res.add("0");
            String musicalNote = getMusicalNote(leftEvents.get(offset+1));
            res.add(musicalNote);
        } else if(leftNybble == '9'){
            res.add("2");
            int vv = Integer.valueOf(leftEvents.get(offset+2),16);
            res.add(vv == 0 ? "0" : "1");
            String musicNote = getMusicalNote(leftEvents.get(offset+1));
            res.add(musicNote);
        } else if(leftNybble == 'a' || leftNybble == 'b' || leftNybble == 'e'){
            res.add("2");
        } else if(leftNybble == 'c' || leftNybble == 'd'){
            res.add("1");
        } else if(command.equals("ff")){
            int metaDataLen = Integer.valueOf(leftEvents.get(offset+2),16);
            res.add((2 + metaDataLen) + "");
        } else if(command.equals("f0")){
            int count = 1;
            while(offset + count < leftEvents.size() && !leftEvents.get(offset+count).equals("f7"))
                count++;
            res.add(count + "");
        } else if(Integer.valueOf(command,16) >= 0 && Integer.valueOf(command,16) <= 127 && !lastCommand.equals("")){
            res = getEventLen(lastCommand,lastCommand,offset - 1,leftEvents);
        } else
            res.add("0");
        return res;
    }
}

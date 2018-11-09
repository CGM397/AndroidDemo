package com.example.myapplication.serviceImplement;

import com.example.myapplication.activity.FileHandler;
import com.example.myapplication.entity.Music;
import com.example.myapplication.service.MusicManagementService;

import java.io.File;
import java.util.ArrayList;

public class MusicManagementImpl implements MusicManagementService {
    @Override
    public void writeMusic(File fileName, Music music) {
        FileHandler fileHandler = new FileHandler();
        if(fileName.exists())
            fileName.delete();
        fileHandler.writeString(fileName, music.getMusicName()+"\r\n");
        fileHandler.writeString(fileName, music.getAuthor()+"\r\n");
        fileHandler.writeString(fileName, music.getAnalysisContentId()+"\r\n");
        fileHandler.writeString(fileName, (music.getAnalyzed() ? "true" : "false")+"\r\n");
        fileHandler.writeString(fileName, music.getState()+"\r\n");
    }

    @Override
    public Music readMusic(File fileName) {
        FileHandler fileHandler = new FileHandler();
        Music music = new Music();
        String name = fileName.getName();
        music.setMusicId(name.substring(0, name.indexOf(".")));
        music.setMusicName(fileHandler.readString(fileName,1));
        music.setAuthor(fileHandler.readString(fileName,2));
        music.setAnalysisContentId(fileHandler.readString(fileName,3));
        music.setAnalyzed(fileHandler.readString(fileName,4).equals("true"));
        music.setState(Integer.parseInt(fileHandler.readString(fileName,5)));
        return music;
    }

    @Override
    public ArrayList<Music> getAllAvailableMusic(String dirName) {
        ArrayList<Music> result = new ArrayList<>();
        ArrayList<Music> store = getAllMusic(dirName);
        for(Music oneMusic : store){
            if(oneMusic.getState() == 0)
                result.add(oneMusic);
        }
        return result;
    }

    @Override
    public ArrayList<Music> getAllAcceptedMusic(String dirName) {
        ArrayList<Music> result = new ArrayList<>();
        ArrayList<Music> store = getAllMusic(dirName);
        for(Music oneMusic : store){
            if(oneMusic.getState() == 1 || oneMusic.getState() == 2)
                result.add(oneMusic);
        }
        return result;
    }

    @Override
    public ArrayList<Music> getAllGradedMusic(String dirName) {
        ArrayList<Music> result = new ArrayList<>();
        ArrayList<Music> store = getAllMusic(dirName);
        for(Music oneMusic : store){
            if(oneMusic.getState() == 2)
                result.add(oneMusic);
        }
        return result;
    }

    @Override
    public ArrayList<Music> getAllMusic(String dirName) {
        ArrayList<Music> result = new ArrayList<>();
        File file = new File(dirName);
        String[] fileList = file.list();
        for(String oneFile : fileList){
            File file1 = new File(dirName + "/" + oneFile);
            Music oneMusic = readMusic(file1);
            result.add(oneMusic);
        }
        return result;
    }
}

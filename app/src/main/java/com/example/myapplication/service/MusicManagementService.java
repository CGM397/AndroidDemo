package com.example.myapplication.service;

import com.example.myapplication.entity.Music;

import java.io.File;
import java.util.ArrayList;

public interface MusicManagementService {

    public void writeMusic(File fileName, Music music);

    public Music readMusic(File fileName);

    public ArrayList<Music> getAllAvailableMusic(String dirName);

    public ArrayList<Music> getAllAcceptedMusic(String dirName);

    public ArrayList<Music> getAllGradedMusic(String dirName);

    public ArrayList<Music> getAllMusic(String dirName);

}

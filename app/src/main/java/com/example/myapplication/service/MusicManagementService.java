package com.example.myapplication.service;

import com.example.myapplication.entity.Music;

import java.io.File;
import java.util.ArrayList;

public interface MusicManagementService {

    void writeMusic(File fileName, Music music);

    Music readMusic(File fileName);

    ArrayList<Music> getAllAvailableMusic(String dirName);

    ArrayList<Music> getAllAcceptedMusic(String dirName);

    ArrayList<Music> getAllGradedMusic(String dirName);

    ArrayList<Music> getAllMusic(String dirName);

}

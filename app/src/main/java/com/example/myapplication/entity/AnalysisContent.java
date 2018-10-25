package com.example.myapplication.entity;

public class AnalysisContent {

    private int timeIntervalFromLastCommand = 0;

    private int currentTime = 0;

    private int state = 0;

    private String musicNote = "";

    private String musicScale = "";

    private int duration = 0;

    public AnalysisContent() {}

    public int getTimeIntervalFromLastCommand() {
        return timeIntervalFromLastCommand;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public int getState() {
        return state;
    }

    public String getMusicNote() {
        return musicNote;
    }

    public String getMusicScale() {
        return musicScale;
    }

    public int getDuration() {
        return duration;
    }

    public void setTimeIntervalFromLastCommand(int timeIntervalFromLastCommand) {
        this.timeIntervalFromLastCommand = timeIntervalFromLastCommand;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setMusicNote(String musicNote) {
        this.musicNote = musicNote;
    }

    public void setMusicScale(String musicScale) {
        this.musicScale = musicScale;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

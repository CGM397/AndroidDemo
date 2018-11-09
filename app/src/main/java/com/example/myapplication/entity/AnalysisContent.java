package com.example.myapplication.entity;

public class AnalysisContent {

    /**
     * 距离上一个动作开始的时间间隔，单位 : 毫秒
     */
    private int timeIntervalFromLastCommand = 0;
    /**
     * 当前时间，单位 : tick
     */
    private int currentTime = 0;

    /**
     * 开 : 1; 关 : 0
     */
    private int state = 0;

    /**
     * 音名
     */
    private String musicNote = "";

    /**
     * 音阶
     */
    private String musicScale = "";

    /**
     * 持续时间，单位 : 毫秒
     */
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

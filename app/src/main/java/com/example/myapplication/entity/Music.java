package com.example.myapplication.entity;

public class Music {

    private String musicId;
    private String musicName;

    /**
     * 如果没有解析则为null
     */
    private String analysisContentId;
    private boolean analyzed;

    /**
     * 三种状态: 0代表可接收; 1代表已接收; 2代表已打分(懒得用枚举类了。。。)
     */
    private int state;

    public Music() {}

    public Music(String musicId, String musicName, String analysisContentId, boolean analyzed, int state){
        this.musicId = musicId;
        this.musicName = musicName;
        this.analysisContentId = analysisContentId;
        this.analyzed = analyzed;
        this.state = state;
    }

    public String getMusicId() {
        return musicId;
    }

    public String getMusicName() {
        return musicName;
    }

    public String getAnalysisContentId() {
        return analysisContentId;
    }

    public int getState() {
        return state;
    }

    public boolean getAnalyzed() {
        return analyzed;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public void setAnalysisContentId(String analysisContentId) {
        this.analysisContentId = analysisContentId;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setAnalyzed(boolean analyzed) {
        this.analyzed = analyzed;
    }
}

package com.example.myapplication.entity;

import java.util.ArrayList;

public class AnalysisContent {
    private String analysisContentId;
    private ArrayList<String> content;

    public AnalysisContent() {}

    public String getAnalysisContentId() {
        return analysisContentId;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public void setAnalysisContentId(String analysisContentId) {
        this.analysisContentId = analysisContentId;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }
}

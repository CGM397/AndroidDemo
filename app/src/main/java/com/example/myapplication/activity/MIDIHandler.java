package com.example.myapplication.activity;

import com.example.myapplication.entity.AnalysisContent;
import com.example.myapplication.service.MIDIService;
import com.example.myapplication.serviceImplement.MIDIServiceImpl;

import java.io.*;
import java.util.ArrayList;

public class MIDIHandler {

    public void analyseMidi(InputStream midiFileInputStream, String analyseResultPath){
        MIDIService midiService = new MIDIServiceImpl();
        ArrayList<String> store = midiService.getSequence(midiFileInputStream);
        ArrayList<ArrayList<String>> tracks = midiService.getTracks(store);
        int currentTime;
        String lastCommand = "";
        int ticksPerQuarterNote = Integer.parseInt(store.get(12)+store.get(13),16);
        double microsecondsPerQuarterNote;
        double microsecondsPerTick = 0;
        ArrayList<AnalysisContent> resultData = new ArrayList<>();

        for(int i = 0; i < tracks.size(); i++){
            ArrayList<String> oneTrack = tracks.get(i);
            int count = 0;
            //假定是多轨同步
            currentTime = 0;
            while(count < oneTrack.size()){
                AnalysisContent analysisContent = new AnalysisContent();
                ArrayList<String> leftEvents = new ArrayList<>();
                for(int m = count; m < oneTrack.size(); m++)
                    leftEvents.add(oneTrack.get(m));

                ArrayList<Integer> deltaTimeInfo = midiService.getDeltaTime(leftEvents);
                int deltaTimeLen = deltaTimeInfo.get(0);
                int deltaTime = deltaTimeInfo.get(1);
                //get the command of this event
                String command = leftEvents.get(deltaTimeLen);
                //get the microsecondsPerTick
                if(command.equals("ff") && leftEvents.get(deltaTimeLen+1).equals("51")){
                    String str = leftEvents.get(deltaTimeLen+3) + leftEvents.get(deltaTimeLen+4) +
                            leftEvents.get(deltaTimeLen+5);
                    microsecondsPerQuarterNote = Integer.valueOf(str,16);
                    microsecondsPerTick = microsecondsPerQuarterNote/ticksPerQuarterNote;
                }
                //get the delta-time
                currentTime += deltaTime;
                //get current time
                ArrayList<String> eventInfo = midiService.getEventLen(command, lastCommand, deltaTimeLen, leftEvents);
                //assume that eventInfo.size() >= 1, trust me!
                count = count + deltaTimeLen + Integer.parseInt(eventInfo.get(0));
                //save the music note
                if(eventInfo.size() >= 4){
                    analysisContent.setCurrentTime(currentTime);
                    analysisContent.setState(Integer.parseInt(eventInfo.get(1)));
                    analysisContent.setMusicNote(eventInfo.get(2));
                    analysisContent.setMusicScale(eventInfo.get(3));
                    resultData.add(analysisContent);
                }
                //in case that the current command is lastCommand
                if(Integer.valueOf(command,16) >= 128){
                    count++;
                    lastCommand = command;
                }
            }
        }
        //sort the result sequence by time
        resultData = resultSequenceSort(resultData);
        //calculate the timeInterval
        resultData.get(0).setTimeIntervalFromLastCommand(resultData.get(0).getCurrentTime());
        resultData.get(0).setDuration(getMusicNoteDuration(resultData.get(0), resultData, 1));
        for(int i = 1; i < resultData.size(); i++){
            int timeInterval = resultData.get(i).getCurrentTime() - resultData.get(i-1).getCurrentTime();
            resultData.get(i).setTimeIntervalFromLastCommand(timeInterval);
            int offset = i+1;
            resultData.get(i).setDuration(getMusicNoteDuration(resultData.get(i), resultData, offset));
        }
        //transform ticks into microseconds
        for(AnalysisContent sequence : resultData){
            double timeIntervalToMicroseconds = (microsecondsPerTick * sequence.getTimeIntervalFromLastCommand())/Math.pow(10,3);
            double durationToMicroseconds = (microsecondsPerTick * sequence.getDuration())/Math.pow(10,3);
            sequence.setTimeIntervalFromLastCommand((int)Math.round(timeIntervalToMicroseconds));
            sequence.setDuration((int)Math.round(durationToMicroseconds));
        }
        //save the result
        saveResultData(resultData, analyseResultPath);
    }

    public ArrayList<AnalysisContent> readMidi(String path){
        ArrayList<AnalysisContent> res = new ArrayList<>();
        try{
            FileInputStream fin = new FileInputStream(new File(path));
            InputStreamReader inputStreamReader = new InputStreamReader(fin);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while((line = reader.readLine()) != null){
                String[] store = line.split(",");
                AnalysisContent oneLine = new AnalysisContent();
                oneLine.setTimeIntervalFromLastCommand(Integer.parseInt(store[0]));
                oneLine.setCurrentTime(Integer.parseInt(store[1]));
                oneLine.setState(Integer.parseInt(store[2]));
                oneLine.setMusicNote(store[3]);
                oneLine.setMusicScale(store[4]);
                oneLine.setDuration(Integer.parseInt(store[5]));
                res.add(oneLine);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return res;
    }

    private ArrayList<AnalysisContent> resultSequenceSort(ArrayList<AnalysisContent> sequences){
        for(int i = 0; i < sequences.size(); i++){
            for(int j = 0; j < sequences.size() - 1; j++){
                if(sequences.get(j).getCurrentTime() > sequences.get(j+1).getCurrentTime()){
                    int tempCurrentTime = sequences.get(j).getCurrentTime();
                    int tempState = sequences.get(j).getState();
                    String tempMusicNote = sequences.get(j).getMusicNote();
                    String temMusicScale = sequences.get(j).getMusicScale();

                    sequences.get(j).setCurrentTime(sequences.get(j+1).getCurrentTime());
                    sequences.get(j).setState(sequences.get(j+1).getState());
                    sequences.get(j).setMusicNote(sequences.get(j+1).getMusicNote());
                    sequences.get(j).setMusicScale(sequences.get(j+1).getMusicScale());

                    sequences.get(j+1).setCurrentTime(tempCurrentTime);
                    sequences.get(j+1).setState(tempState);
                    sequences.get(j+1).setMusicNote(tempMusicNote);
                    sequences.get(j+1).setMusicScale(temMusicScale);
                }
            }
        }
        return sequences;
    }

    private void saveResultData(ArrayList<AnalysisContent> analysisContents, String path){
        try{
            File file = new File(path);
            FileWriter fileWriter = new FileWriter(file,false);
            fileWriter.write("0,0,0,0,0,0\r\n");
            for (AnalysisContent analysisContent: analysisContents) {
                String str =  analysisContent.getTimeIntervalFromLastCommand() + "," +
                        analysisContent.getCurrentTime() + "," +
                        analysisContent.getState() + "," +
                        analysisContent.getMusicNote() + "," +
                        analysisContent.getMusicScale() + "," +
                        analysisContent.getDuration() + "\r\n";
                fileWriter.write(str);
            }
            fileWriter.flush();
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private int getMusicNoteDuration(AnalysisContent analysisContent, ArrayList<AnalysisContent> analysisContents, int offset){
        int duration = 0;
        if(analysisContent.getState() == 0)
            duration = 0;
        else{
            String oneMusicNote = analysisContent.getMusicNote();
            String oneMusicScale = analysisContent.getMusicScale();
            while (offset < analysisContents.size()){
                if(analysisContents.get(offset).getMusicNote().equals(oneMusicNote) &&
                        analysisContents.get(offset).getMusicScale().equals(oneMusicScale) &&
                        analysisContents.get(offset).getState() == 0){
                    duration = analysisContents.get(offset).getCurrentTime() - analysisContent.getCurrentTime();
                    break;
                }
                offset++;
            }
        }
        return duration;
    }


}

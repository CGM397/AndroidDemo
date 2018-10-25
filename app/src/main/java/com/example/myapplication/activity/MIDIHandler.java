package com.example.myapplication.activity;

import com.example.myapplication.entity.AnalysisContent;
import com.example.myapplication.service.MIDIService;
import com.example.myapplication.serviceImplement.MIDIServiceImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MIDIHandler {

    public void analyseMidi(String dirPath, String fileName){
        MIDIService midiService = new MIDIServiceImpl();
        ArrayList<String> store = midiService.getSequence(dirPath + "/" + fileName);
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
                if(eventInfo.size() == 3){
                    analysisContent.setCurrentTime(currentTime);
                    analysisContent.setState(Integer.parseInt(eventInfo.get(1)));
                    analysisContent.setMusicNote(eventInfo.get(2).charAt(0)+"");
                    analysisContent.setMusicScale(eventInfo.get(2).charAt(1)+"");
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
        for(int i = 1; i < resultData.size(); i++){
            int timeInterval = resultData.get(i).getCurrentTime() - resultData.get(i-1).getCurrentTime();
            resultData.get(i).setTimeIntervalFromLastCommand(timeInterval);
            int counter = i+1;
            String oneMusicNote = resultData.get(i).getMusicNote();
            String oneMusicScale = resultData.get(i).getMusicScale();

            if(resultData.get(i).getState() == 1){
                while(counter < resultData.size()){
                    if(resultData.get(counter).getMusicNote().equals(oneMusicNote) &&
                            resultData.get(counter).getMusicScale().equals(oneMusicScale) &&
                            resultData.get(counter).getState() == 0){
                        int duration = resultData.get(counter).getCurrentTime() - resultData.get(i).getCurrentTime();
                        resultData.get(i).setDuration(duration);
                    }
                }
            }else{
                resultData.get(i).setDuration(0);
            }
        }
        //transform ticks into microseconds
        for(AnalysisContent sequence : resultData){
            double timeIntervalToMicroseconds = (microsecondsPerTick * sequence.getTimeIntervalFromLastCommand())/Math.pow(10,3);
            double durationToMicroseconds = (microsecondsPerTick * sequence.getDuration())/Math.pow(10,3);
            sequence.setTimeIntervalFromLastCommand((int)Math.round(timeIntervalToMicroseconds));
            sequence.setDuration((int)Math.round(durationToMicroseconds));
        }
        //save the result
        String savePath = dirPath.substring(0, dirPath.lastIndexOf('/')) +
                "MidiFile/" + fileName;
        saveResultData(resultData, savePath);
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
            fileWriter.write("0,0,0,0,0\r\n");
            for (AnalysisContent analysisContent: analysisContents) {
                String str =  analysisContent.getTimeIntervalFromLastCommand() + "," +
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


}

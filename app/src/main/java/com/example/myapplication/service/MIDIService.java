package com.example.myapplication.service;

import java.util.ArrayList;

public interface MIDIService {

    /**
     * obtain the total sequence of the target midi file
     * @param path the path of the target midi file
     * @return the total sequence of the target midi file
     */
    public ArrayList<String> getSequence(String path);

    /**
     * obtain all tracks in the given sequence
     * @param store the given sequence
     * @return all tracks in the given sequence
     */
    public ArrayList<ArrayList<String>> getTracks(ArrayList<String> store);

    /**
     * obtain the delta time  of the first event in the given events
     * @param event an event list
     * @return the delta time  of the first event in the given events
     */
    public ArrayList<Integer> getDeltaTime(ArrayList<String> event);

    /**
     * transform the given code into corresponding music note
     * @param note the given music note code
     * @return music note
     */
    public String getMusicalNote(String note);

    /**
     * return the midi command data length
     * @param command current command
     * @param lastCommand last command
     * @param offset the pos pointing to current command in leftEvents
     * @param leftEvents start with the delta time of current MIDI event
     * @return the midi command data length of current MIDI event
     */
    public ArrayList<String> getEventLen(String command, String lastCommand, int offset, ArrayList<String> leftEvents);

}

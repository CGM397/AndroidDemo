package com.example.myapplication.service;

import java.io.InputStream;
import java.util.ArrayList;

public interface MIDIService {

    /**
     * obtain the total sequence of the target midi file
     * @param midiFileInputStream the inputStream of the target midi file
     * @return the total sequence of the target midi file
     */
    ArrayList<String> getSequence(InputStream midiFileInputStream);

    /**
     * obtain all tracks in the given sequence
     * @param store the given sequence
     * @return all tracks in the given sequence
     */
    ArrayList<ArrayList<String>> getTracks(ArrayList<String> store);

    /**
     * obtain the delta time  of the first event in the given events
     * @param event an event list
     * @return the delta time  of the first event in the given events
     */
    ArrayList<Integer> getDeltaTime(ArrayList<String> event);

    /**
     * transform the given code into corresponding music note
     * @param note the given music note code
     * @return music note
     */
    ArrayList<String> getMusicalNote(String note);

    /**
     * return the midi command data length
     * @param command current command
     * @param lastCommand last command
     * @param offset the pos pointing to current command in leftEvents
     * @param leftEvents start with the delta time of current MIDI event
     * @return the midi command data length of current MIDI event
     */
    ArrayList<String> getEventLen(String command, String lastCommand, int offset, ArrayList<String> leftEvents);

}

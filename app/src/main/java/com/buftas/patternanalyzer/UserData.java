//Nikolaos Katsiopis
//icsd13076
package com.buftas.patternanalyzer;

import java.util.ArrayList;

//This class holds all the data that a single user provides
public class UserData {
    private String username;
    private ArrayList<RawPatternEntry> rawPatterns;
    private ArrayList<PatternMetadata> patternMeta;
    private ArrayList<PatternTouch> pressureMetrics;
    private ArrayList<PairedMetadata> pairedMeta;

    public UserData(String username) {
        this.username = username;
        rawPatterns = new ArrayList<>();
        pressureMetrics = new ArrayList<>();
        pairedMeta = new ArrayList<>();
        patternMeta = new ArrayList<>();
    }


    //Get methods
    public String getUsername() {
        return username;
    }

    public ArrayList<RawPatternEntry> getRawPatterns() {
        return rawPatterns;
    }

    public ArrayList<PatternMetadata> getPatternMeta() {
        return patternMeta;
    }

    public ArrayList<PatternTouch> getTouchMetrics() {
        return pressureMetrics;
    }

    public ArrayList<PairedMetadata> getPairedMeta() {
        return pairedMeta;
    }

    //This method adds new RawPatternEntry object into an arraylist
    public void addRawPattern(RawPatternEntry entry) {
        this.rawPatterns.add(entry);
    }

    public void addPatternMeta(PatternMetadata patternMeta) {
        this.patternMeta.add(patternMeta);
    }

    //This method adds new PatternTouch object into an arraylist
    public void addPatternTouch(PatternTouch entry) {
        this.pressureMetrics.add(entry);
    }

    //This method adds new PairedMetadata object into an arraylist
    public void addPairedPattern(PairedMetadata entry) {
        this.pairedMeta.add(entry);
    }
}

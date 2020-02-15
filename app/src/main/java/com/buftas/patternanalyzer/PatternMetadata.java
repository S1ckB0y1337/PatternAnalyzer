//Nikolaos Katsiopis
//icsd13076
package com.buftas.patternanalyzer;

//This class generates and stores metadata using metrics of RawPatternEntry and PatternTouch class
public class PatternMetadata {
    private String username;
    private int attemptNumber;
    private final int[] sequence;
    private int seqLength;
    private long timeToComplete;
    private double patternLength;
    private double avgSpeed;
    private float highestPressure, lowestPressure;
    private int handNum, fingerNum;

    public PatternMetadata(String username, int attemptNumber, int[] sequence, int seqLength, long timeToComplete, double patternLength, double avgSpeed, float highestPressure, float lowestPressure, int handNum, int fingerNum) {
        this.username = username;
        this.attemptNumber = attemptNumber;
        this.sequence = sequence;
        this.seqLength = seqLength;
        this.timeToComplete = timeToComplete;
        this.patternLength = patternLength;
        this.avgSpeed = avgSpeed;
        this.highestPressure = highestPressure;
        this.lowestPressure = lowestPressure;
        this.handNum = handNum;
        this.fingerNum = fingerNum;
    }

    //Get Methods
    public String getUsername() {
        return username;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public int[] getSequence() {
        return sequence;
    }

    public int getSeqLength() {
        return seqLength;
    }

    public long getTimeToComplete() {
        return timeToComplete;
    }

    public double getPatternLength() {
        return patternLength;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public float getHighestPressure() {
        return highestPressure;
    }

    public float getLowestPressure() {
        return lowestPressure;
    }

    public int getHandNum() {
        return handNum;
    }

    public int getFingerNum() {
        return fingerNum;
    }
}

//Nikolaos Katsiopis
//icsd13076
package com.buftas.patternanalyzer;

//This class contains metrics for the hold duration of each button of the pattern lock
public class PatternTouch {
    private String username;
    private int attemptNumber;
    private int[] sequence;
    private long hold_duration1, hold_duration2, hold_duration3, hold_duration4;

    public PatternTouch(String username, int attemptNumber, int[] sequence, long hold_duration1, long hold_duration2, long hold_duration3, long hold_duration4) {
        this.username = username;
        this.attemptNumber = attemptNumber;
        this.sequence = sequence;
        this.hold_duration1 = hold_duration1;
        this.hold_duration2 = hold_duration2;
        this.hold_duration3 = hold_duration3;
        this.hold_duration4 = hold_duration4;
    }

    public String getUsername() {
        return username;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public int[] getSequence() {
        return sequence;
    }

    public float getHold_duration1() {
        return hold_duration1;
    }

    public float getHold_duration2() {
        return hold_duration2;
    }

    public float getHold_duration3() {
        return hold_duration3;
    }

    public float getHold_duration4() {
        return hold_duration4;
    }
}

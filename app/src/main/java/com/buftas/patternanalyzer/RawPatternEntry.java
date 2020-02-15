//Nikolaos Katsiopis
//icsd13076
package com.buftas.patternanalyzer;

//This class provides the basic structure for the raw entry of a touch event on the pattern board
public class RawPatternEntry {
    private int noOfPoint;
    private long timestamp;
    private int firstXpoint, firstYpoint, lastXpoint, lastYpoint, centerXpoint, centerYpoint;
    private float pressure;

    public RawPatternEntry() {

    }

    public RawPatternEntry(int noOfPoint, long timestamp) {
        this.noOfPoint = noOfPoint;
        this.timestamp = timestamp;
    }


    public RawPatternEntry(int noOfPoint, long timestamp, int firstXpoint, int firstYpoint, float pressure, int lastXpoint, int lastYpoint, int centerXpoint, int centerYpoint) {
        this.noOfPoint = noOfPoint;
        this.timestamp = timestamp;
        this.firstXpoint = firstXpoint;
        this.firstYpoint = firstYpoint;
        this.pressure = pressure;
        this.lastXpoint = lastXpoint;
        this.lastYpoint = lastYpoint;
        this.centerXpoint = centerXpoint;
        this.centerYpoint = centerYpoint;
    }


    //Get Methods
    public int getNoOfPoint() {
        return noOfPoint;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getFirstXpoint() {
        return firstXpoint;
    }

    public int getFirstYpoint() {
        return firstYpoint;
    }

    public float getPressure() {
        return pressure;
    }

    public int getLastXpoint() {
        return lastXpoint;
    }

    public int getLastYpoint() {
        return lastYpoint;
    }

    public int getCenterXpoint() {
        return centerXpoint;
    }

    public int getCenterYpoint() {
        return centerYpoint;
    }

    //Set Methods
    public void setNoOfPoint(int noOfPoint) {
        this.noOfPoint = noOfPoint;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setFirstXpoint(int firstXpoint) {
        this.firstXpoint = firstXpoint;
    }

    public void setFirstYpoint(int firstYpoint) {
        this.firstYpoint = firstYpoint;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public void setLastXpoint(int lastXpoint) {
        this.lastXpoint = lastXpoint;
    }

    public void setLastYpoint(int lastYpoint) {
        this.lastYpoint = lastYpoint;
    }

    public void setCenterXpoint(int centerXpoint) {
        this.centerXpoint = centerXpoint;
    }

    public void setCenterYpoint(int centerYpoint) {
        this.centerYpoint = centerYpoint;
    }

    //Overriding toString for debugging purposes
    @Override
    public String toString() {
        return "Entry -> Point:" + getNoOfPoint() + " Timestamp: " + getTimestamp() + " X and Y: " + String.format("%d,%d ", getFirstXpoint(), getFirstYpoint()) + " Pressure: " + getPressure();
    }
}

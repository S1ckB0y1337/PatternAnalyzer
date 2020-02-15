//Nikolaos Katsiopis
//icsd13076
package com.buftas.patternanalyzer;

//This class creates paired metrics metadata using the saved patterns and each data


public class PairedMetadata {
    private String username;
    private int attemptNumber;
    private String screen_resolution;
    private int pattern_number_A, pattern_number_B;
    private int Xcoord_of_central_Point_of_A, Ycoord_of_central_Point_of_A, Xcoord_of_central_Point_of_B, Ycoord_of_central_Point_of_B;
    private int First_Xcoord_of_A, First_Ycoord_of_A, Last_Xcoord_of_B, Last_Ycoord_of_B;
    private double Distance_AB;
    private long Intertime_AB;
    private double Avg_speeadAB;
    private float Avg_pressure;

    public PairedMetadata(String username, int attemptNumber, String screen_resolution, int pattern_number_A, int pattern_number_B, int Xcoord_of_central_Point_of_A, int Ycoord_of_central_Point_of_A, int Xcoord_of_central_Point_of_B, int Ycoord_of_central_Point_of_B, int first_Xcoord_of_A, int first_Ycoord_of_A, int last_Xcoord_of_B, int last_Ycoord_of_B, double distance_AB, long intertime_AB, double avg_speeadAB, float avg_pressure) {
        this.username = username;
        this.attemptNumber = attemptNumber;
        this.screen_resolution = screen_resolution;
        this.pattern_number_A = pattern_number_A;
        this.pattern_number_B = pattern_number_B;
        this.Xcoord_of_central_Point_of_A = Xcoord_of_central_Point_of_A;
        this.Ycoord_of_central_Point_of_A = Ycoord_of_central_Point_of_A;
        this.Xcoord_of_central_Point_of_B = Xcoord_of_central_Point_of_B;
        this.Ycoord_of_central_Point_of_B = Ycoord_of_central_Point_of_B;
        First_Xcoord_of_A = first_Xcoord_of_A;
        First_Ycoord_of_A = first_Ycoord_of_A;
        Last_Xcoord_of_B = last_Xcoord_of_B;
        Last_Ycoord_of_B = last_Ycoord_of_B;
        Distance_AB = distance_AB;
        Intertime_AB = intertime_AB;
        Avg_speeadAB = avg_speeadAB;
        Avg_pressure = avg_pressure;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public String getScreen_resolution() {
        return screen_resolution;
    }

    public void setScreen_resolution(String screen_resolution) {
        this.screen_resolution = screen_resolution;
    }

    public int getPattern_number_A() {
        return pattern_number_A;
    }

    public void setPattern_number_A(int pattern_number_A) {
        this.pattern_number_A = pattern_number_A;
    }

    public int getPattern_number_B() {
        return pattern_number_B;
    }

    public void setPattern_number_B(int pattern_number_B) {
        this.pattern_number_B = pattern_number_B;
    }

    public int getXcoord_of_central_Point_of_A() {
        return Xcoord_of_central_Point_of_A;
    }

    public void setXcoord_of_central_Point_of_A(int xcoord_of_central_Point_of_A) {
        Xcoord_of_central_Point_of_A = xcoord_of_central_Point_of_A;
    }

    public int getYcoord_of_central_Point_of_A() {
        return Ycoord_of_central_Point_of_A;
    }

    public void setYcoord_of_central_Point_of_A(int ycoord_of_central_Point_of_A) {
        Ycoord_of_central_Point_of_A = ycoord_of_central_Point_of_A;
    }

    public int getXcoord_of_central_Point_of_B() {
        return Xcoord_of_central_Point_of_B;
    }

    public void setXcoord_of_central_Point_of_B(int xcoord_of_central_Point_of_B) {
        Xcoord_of_central_Point_of_B = xcoord_of_central_Point_of_B;
    }

    public int getYcoord_of_central_Point_of_B() {
        return Ycoord_of_central_Point_of_B;
    }

    public void setYcoord_of_central_Point_of_B(int ycoord_of_central_Point_of_B) {
        Ycoord_of_central_Point_of_B = ycoord_of_central_Point_of_B;
    }

    public int getFirst_Xcoord_of_A() {
        return First_Xcoord_of_A;
    }

    public void setFirst_Xcoord_of_A(int first_Xcoord_of_A) {
        First_Xcoord_of_A = first_Xcoord_of_A;
    }

    public int getFirst_Ycoord_of_A() {
        return First_Ycoord_of_A;
    }

    public void setFirst_Ycoord_of_A(int first_Ycoord_of_A) {
        First_Ycoord_of_A = first_Ycoord_of_A;
    }

    public int getLast_Xcoord_of_B() {
        return Last_Xcoord_of_B;
    }

    public void setLast_Xcoord_of_B(int last_Xcoord_of_B) {
        Last_Xcoord_of_B = last_Xcoord_of_B;
    }

    public int getLast_Ycoord_of_B() {
        return Last_Ycoord_of_B;
    }

    public void setLast_Ycoord_of_B(int last_Ycoord_of_B) {
        Last_Ycoord_of_B = last_Ycoord_of_B;
    }

    public double getDistance_AB() {
        return Distance_AB;
    }

    public void setDistance_AB(double distance_AB) {
        Distance_AB = distance_AB;
    }

    public long getIntertime_AB() {
        return Intertime_AB;
    }

    public void setIntertime_AB(long intertime_AB) {
        Intertime_AB = intertime_AB;
    }

    public double getAvg_speeadAB() {
        return Avg_speeadAB;
    }

    public void setAvg_speeadAB(double avg_speeadAB) {
        Avg_speeadAB = avg_speeadAB;
    }

    public float getAvg_pressure() {
        return Avg_pressure;
    }

    public void setAvg_pressure(float avg_pressure) {
        Avg_pressure = avg_pressure;
    }
}

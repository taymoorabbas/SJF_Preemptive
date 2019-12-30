package model;/*
Created by: Taymoor Ghazanfar
R.no: 3625-BSSE-F17-C
Date: 28-Dec-19
Time: 10:44 PM
Lau ji Ghauri aya fir
*/

public class SJFProcess implements Cloneable{

    private int id;
    private float arrivalTime;
    private float burstTime;
    private boolean isCompleted;
    private boolean isArrived;

    public SJFProcess(){}

    public SJFProcess(int id, float arrivalTime, float burstTime, boolean isCompleted, boolean isArrived) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.isCompleted = isCompleted;
        this.isArrived = isArrived;
    }

    public SJFProcess(SJFProcess sjfProcess){

        this.id = sjfProcess.id;
        this.arrivalTime = sjfProcess.arrivalTime;
        this.burstTime = sjfProcess.burstTime;
        this.isCompleted = sjfProcess.isCompleted;
        this.isArrived = sjfProcess.isArrived;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(float arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public float getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(float burstTime) {
        this.burstTime = burstTime;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isArrived() {
        return isArrived;
    }

    public void setArrived(boolean arrived) {
        isArrived = arrived;
    }
}

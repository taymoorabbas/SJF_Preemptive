package model;/*
Created by: Taymoor Ghazanfar
R.no: 3625-BSSE-F17-C
Date: 28-Dec-19
Time: 10:58 PM
Lau ji Ghauri aya fir
*/

public class GanttItem {

    private model.SJFProcess SJFProcess;
    private float startTime;
    private float endTime;

    public GanttItem(){}

    public GanttItem(SJFProcess SJFProcess, float startTime, float endTime) {
        this.SJFProcess = SJFProcess;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public SJFProcess getSJFProcess() {
        return SJFProcess;
    }

    public void setSJFProcess(SJFProcess SJFProcess) {
        this.SJFProcess = SJFProcess;
    }

    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public void setEndTime(float endTime) {
        this.endTime = endTime;
    }
}

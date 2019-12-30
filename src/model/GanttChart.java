package model;/*
Created by: Taymoor Ghazanfar
R.no: 3625-BSSE-F17-C
Date: 30-Dec-19
Time: 6:29 AM
Lau ji Ghauri aya fir
*/

import model.SJFProcess;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GanttChart {

    private ArrayList<GanttItem> ganttItems;

    public GanttChart(){

        this.ganttItems = new ArrayList<>();
    }

    public void addItem(GanttItem ganttItem){

        this.ganttItems.add(ganttItem);
    }

    public void showGanttChart(){

        System.out.format("%-15s %-15s %-15s", "process id", "start time", "end time");
        System.out.println();

        for(GanttItem ganttItem : ganttItems){

            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setMaximumFractionDigits(2);

            System.out.format("%-15d %-15s %-15s",
                    ganttItem.getSJFProcess().getId(),
                    decimalFormat.format(ganttItem.getStartTime()),
                    decimalFormat.format(ganttItem.getEndTime()));

            System.out.println();
        }
    }
    public float getCompletionTime(int processID){

        float completionTime = 0;
        for(GanttItem ganttItem: ganttItems){

            if(ganttItem.getSJFProcess().getId() == processID){

                completionTime = ganttItem.getEndTime();
            }
        }

        return completionTime;
    }
    public float getTurnAroundTime(int processID){

        float turnaroundTime = 0;

        for(GanttItem ganttItem: ganttItems){

            if(ganttItem.getSJFProcess().getId() == processID){

                turnaroundTime = (getCompletionTime(processID) - ganttItem.getSJFProcess().getArrivalTime());
            }
        }

        return turnaroundTime;
    }

    public float getWaitingTime(int processID, ArrayList<SJFProcess> burstList){

        float waitingTime = 0;

        for(GanttItem ganttItem: ganttItems){

            if(ganttItem.getSJFProcess().getId() == processID){

                for(SJFProcess process: burstList){

                    if (process.getId() == processID){

                        waitingTime = (getTurnAroundTime(processID) - process.getBurstTime());
                    }
                }
            }
        }
        return waitingTime;
    }

    public float getScheduleLength(){

        GanttItem firstItem = ganttItems.get(0);
        GanttItem lastItem = ganttItems.get(ganttItems.size() - 1);

        return (lastItem.getEndTime() - firstItem.getStartTime());
    }

    public float getThroughPut(int noOfProcesses){

        String throughPut = String.format("%.2f", noOfProcesses / getScheduleLength());

        return Float.parseFloat(throughPut);
    }
}

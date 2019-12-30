/*
Created by: Taymoor Ghazanfar
R.no: 3625-BSSE-F17-C
Date: 30-Dec-19
Time: 5:09 AM
Lau ji Ghauri aya fir
*/

import controller.SJFPreemptive;
import model.SJFProcess;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        //change or add processes here. leave booleans as they are.
        ArrayList<SJFProcess> processes = new ArrayList<>();
        processes.add(new SJFProcess(1,5,10.2f,false,false));
        processes.add(new SJFProcess(2,3.4f,0.99f,false,false));
        processes.add(new SJFProcess(3,1.3f,1.45f,false,false));
        processes.add(new SJFProcess(4,0.56f,15,false,false));

        SJFPreemptive sjfPreemptive = new SJFPreemptive();

        for(SJFProcess process: processes){

            sjfPreemptive.addProcess(process);
        }

        sjfPreemptive.showGanttChart();

        System.out.println("completion times: " + sjfPreemptive.getCompletionTimes().toString());
        System.out.println("turnaround times: " + sjfPreemptive.getTurnAroundTimes().toString());
        System.out.println("waiting times: " + sjfPreemptive.getWaitingTimes().toString());
        System.out.println("schedule length: " + sjfPreemptive.getScheduleLength());
        System.out.println("throughput: " + sjfPreemptive.getThroughPut());

    }
}

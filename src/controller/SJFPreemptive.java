package controller;/*
Created by: Taymoor Ghazanfar
R.no: 3625-BSSE-F17-C
Date: 30-Dec-19
Time: 4:26 AM
Lau ji Ghauri aya fir
*/

import model.GanttChart;
import model.GanttItem;
import model.SJFProcess;

import java.util.ArrayList;

public class SJFPreemptive {

    private ArrayList<SJFProcess> processes;
    private ArrayList<SJFProcess> arrivedProcesses;
    private ArrayList<SJFProcess> burstList;
    private GanttChart ganttChart;

    public SJFPreemptive(){

        processes = new ArrayList<>();
        burstList = new ArrayList<>();
        arrivedProcesses = new ArrayList<>();
        ganttChart = new GanttChart();
    }

    public void addProcess(SJFProcess process){

        processes.add(process);
        burstList.add(new SJFProcess(process));
    }

    public void showGanttChart(){

        sjf();
        ganttChart.showGanttChart();
    }

    //////////////////////////////////////CALCULATION METHODS//////////////////////////////////////////
    public ArrayList<Float> getCompletionTimes(){

        ArrayList<Float> completionTimes = new ArrayList<>();

        for(SJFProcess process: burstList){

            completionTimes.add(ganttChart.getCompletionTime(process.getId()));
        }

        return completionTimes;
    }

    public ArrayList<Float> getTurnAroundTimes(){

        ArrayList<Float> turnAroundTimes = new ArrayList<>();

        for(SJFProcess process: burstList){

            turnAroundTimes.add(ganttChart.getTurnAroundTime(process.getId()));
        }

        return turnAroundTimes;
    }

    public ArrayList<Float> getWaitingTimes(){

        ArrayList<Float> waitingTimes = new ArrayList<>();

        for(SJFProcess process: burstList){

            waitingTimes.add(ganttChart.getWaitingTime(process.getId(), burstList));
        }

        return waitingTimes;
    }

    public float getScheduleLength(){

        return ganttChart.getScheduleLength();
    }

    public float getThroughPut(){

        return ganttChart.getThroughPut(burstList.size());
    }

    ///////////////////////////////////////SJF INTERNAL METHODS////////////////////////////////////////////////
    private void sjf(){

        boolean allCompleted = false;
        boolean allArrived = false;

        SJFProcess currentProcess = null;
        SJFProcess nextProcess;

        float currentTime = 0;
        float nextTime = 0;
        float idleTime = 0;

        while (!allCompleted){

            while (!allArrived){

                //if there was already a process under execution
                if(currentProcess != null){

                    //if current process has a remaining burst
                    if(!currentProcess.isCompleted()){

                        nextProcess = getNextShortestArrivalProcess();

                        //if current process was the last one
                        if(nextProcess == null){

                            allArrived = true;
                            break;
                        }
                        //if there is a next process on arrival
                        else{

                            //if burst of newly arrived process is < current process burst
                            if(nextProcess.getBurstTime() < currentProcess.getBurstTime()){

                                //preempt current process, assign cpu to newly arrived process
                                currentProcess = nextProcess;

                                //get shortest burst process from arrival list
                                nextProcess = getShortestBurstProcess();

                                //if there exist a incomplete process in arrival list with shortest burst
                                if(nextProcess != null){

                                    //if burst of process in arrival list < burst of newly arrived process
                                    if(nextProcess.getBurstTime() < currentProcess.getBurstTime()){

                                        //execute process from arrival list
                                        currentProcess = nextProcess;
                                    }
                                }
                            }
                            //the newly arrived process was not executed, thus will be added to arrived list
                            else{

                                nextProcess.setArrived(true);
                                updateProcessList(nextProcess);
                                arrivedProcesses.add(nextProcess);
                            }
                        }
                    }

                    //if current process is completed
                    else{

                        //update completion status of current process
                        updateProcessList(currentProcess);

                        //if all processes are completed, exit all loops
                        if(isAllCompleted()){

                            allArrived = true;
                            allCompleted = true;
                            break;
                        }

                        //get next shortest arrival process to check if
                        //they have same arrival time
                        nextProcess = getNextShortestArrivalProcess();

                        //if there is a next process
                        if (nextProcess != null){

                            //if next process have same arrival time as previous one
                            if (nextProcess.getArrivalTime() == currentProcess.getArrivalTime()){

                                //we will continue from previous next time
                                currentTime = nextTime;
                            }
                        }

                        //else get next shortest arrival process (already got it above)
                        currentProcess = nextProcess;

                        //all processes have arrived
                        if(currentProcess == null){

                            //all have arrived, break the loop
                            allArrived = true;
                            break;
                        }

                        //if there is a next arrival process
                        else{

                            //get shortest burst process from arrived list
                            nextProcess = getShortestBurstProcess();

                            //if there exist a incomplete shortest burst process
                            if(nextProcess != null){

                                //if newly arrived process arrived exactly after execution of previous process
                                if(currentProcess.getArrivalTime() == nextTime){

                                    //if burst of previous arrived process <= newly arrived process
                                    //or burst time of previously arrived is greater but it arrived earlier
                                    // (ie. current process has not actually arrived yet)
                                    if(nextProcess.getBurstTime() <= currentProcess.getBurstTime()){

                                        //the newly arrived process was not executed, thus will be added to arrived list
                                        nextProcess.setArrived(true);
                                        updateProcessList(nextProcess);
                                        arrivedProcesses.add(nextProcess);

                                        //execute previously arrived process
                                        currentProcess = nextProcess;
                                    }
                                }

                                //the newly arrived process has technically not arrived yet (making cpu idle)
                                else if(currentProcess.getArrivalTime() == idleTime){

                                    //the newly arrived process was not executed, thus will be added to arrived list
                                    nextProcess.setArrived(true);
                                    updateProcessList(nextProcess);
                                    arrivedProcesses.add(nextProcess);

                                    //execute previously arrived process
                                    currentProcess = nextProcess;
                                }
                            }
                        }
                    }
                }

                //there was no previous process
                else{

                    currentProcess = getNextShortestArrivalProcess();

                    //means all processes have arrived
                    if(currentProcess == null){

                        allArrived = true;
                        break;
                    }
                }
                //execute the currently selected process

                //if process was not previously executed
                if(!currentProcess.isArrived()){

                    currentProcess.setArrived(true);
                    updateProcessList(currentProcess);

                    //if it is the first arrived process
                    if(arrivedProcesses.size() == 0){
                        currentTime = currentProcess.getArrivalTime();
                    }
                    //if the current process's arrival time is same as the previous one,
                    //we have already assigned the current time. checking if time is not assigned,
                    //means if current process have different arrival time
                    else if (currentTime != nextTime){

                        //if current process arrival was later than burst of previous process
                        //this current process made cpu idle.
                        //also checking that maybe during idle time, we picked process from arrived list
                        //and executed it, causing current time to exceed idle time
                        if(currentProcess.getArrivalTime() == idleTime && currentTime <= idleTime){

                            currentTime = idleTime;
                        }
                        else{
                            //current time will be arrival time of newly arrived process(not executed before)
                            currentTime = currentProcess.getArrivalTime();
                        }
                    }
                }

                //current time will be previous next time
                else{
                    currentTime = nextTime;
                }

                nextProcess = getNextShortestArrivalProcess();

                //if current process is the last one
                if(nextProcess == null){

                    //next time will be remaining burst of current process + current time
                    nextTime = currentTime + currentProcess.getBurstTime();

                    //current process burst will be equal to 0 ie (7 - 5) - 2 = 0
                    currentProcess.setBurstTime((nextTime - currentTime) - currentProcess.getBurstTime());
                    currentProcess.setCompleted(true);
                }
                //there is a next process
                else{

                    //if two processes arrived at same time, treat them as FCFS
                    if(currentProcess.getArrivalTime() == nextProcess.getArrivalTime()){

                        nextTime = currentTime + currentProcess.getBurstTime();
                    }
                    else{

                        nextTime = nextProcess.getArrivalTime();
                    }

                    //if current process burst <= arrival of next process
                    if(currentProcess.getBurstTime() <= (nextTime - currentTime)){

                        //burst will be equal to 0 in this case, so process will be completed
                        currentProcess.setCompleted(true);

                        //if next process will not arrive even after completion of current process
                        if(currentProcess.getBurstTime() < (nextTime - currentTime)){

                            nextTime = currentTime + currentProcess.getBurstTime();

                            //we will fast forward time to next process's arrival.
                            // meanwhile cpu may remain idle
                            idleTime = nextProcess.getArrivalTime();
                        }
                    }

                    //if burst > arrival of next. the process will not complete execution
                    currentProcess.setBurstTime(currentProcess.getBurstTime() - (nextTime - currentTime));
                }

                //if process is already in arrived list
                if(searchArrivedList(currentProcess.getId())){

                    //just update its burst value + completion status
                    updateArrivedList(currentProcess);
                }
                else{

                    arrivedProcesses.add(currentProcess);
                }

                //add the process to ganttChart list
                ganttChart.addItem(new GanttItem(currentProcess, currentTime, nextTime));
            }
            if(allCompleted){

                break;
            }

            //execute remaining processes if any
            if(!isAllCompleted()){

                while (true){

                    currentProcess = getShortestBurstProcess();

                    //if all processes are completed
                    if(currentProcess == null){

                        allCompleted = true;
                        break;
                    }

                    //set current time to previous next time
                    currentTime = nextTime;
                    nextTime = currentTime + currentProcess.getBurstTime();

                    currentProcess.setBurstTime(currentProcess.getBurstTime() - (nextTime - currentTime));
                    currentProcess.setCompleted(true);
                    updateArrivedList(currentProcess);
                    updateProcessList(currentProcess);

                    ganttChart.addItem(new GanttItem(currentProcess, currentTime, nextTime));
                }
            }
        }
    }


    private SJFProcess getNextShortestArrivalProcess(){

        if(processes == null || processes.size() == 0){

            return null;
        }
        SJFProcess shortestProcess = null;

        for(SJFProcess process: processes){

            if(!process.isArrived()){

                shortestProcess = process;

                for(SJFProcess searchProcess : processes){

                    if(!searchProcess.isArrived() && searchProcess.getArrivalTime() <= shortestProcess.getArrivalTime()){

                        if(searchProcess.getArrivalTime() == shortestProcess.getArrivalTime()){

                            if(searchProcess.getId() < shortestProcess.getId()){

                                shortestProcess = searchProcess;
                            }
                        }
                        else if(searchProcess.getArrivalTime() < shortestProcess.getArrivalTime()){

                            shortestProcess = searchProcess;
                        }
                    }
                }
            }
        }
        return shortestProcess;
    }

    private SJFProcess getShortestBurstProcess(){

        if (arrivedProcesses == null || arrivedProcesses.size() == 0){

            return null;
        }
        SJFProcess shortestProcess = null;

        for(SJFProcess process : arrivedProcesses){

            if(!process.isCompleted()){

                shortestProcess = process;

                for(SJFProcess searchProcess : arrivedProcesses){

                    if(!searchProcess.isCompleted() && searchProcess.getBurstTime() < shortestProcess.getBurstTime()){

                        shortestProcess = searchProcess;
                    }
                }
            }
        }
        return shortestProcess;
    }

    private boolean isAllCompleted(){

        for(SJFProcess process: processes){

            if (!process.isCompleted()){

                return false;
            }
        }
        return true;
    }

    private boolean searchArrivedList(int id){

        for(SJFProcess process : arrivedProcesses){

            if(process.getId() == id){

                return true;
            }
        }

        return false;
    }

    private void updateArrivedList(SJFProcess updatedProcess){

        for(SJFProcess process: arrivedProcesses){

            if (process.getId() == updatedProcess.getId()){

                process.setBurstTime(updatedProcess.getBurstTime());
                process.setCompleted(updatedProcess.isCompleted());
                break;
            }
        }
    }

    private void updateProcessList(SJFProcess updatedProcess){

        for(SJFProcess process : processes){

            if(process.getId() == updatedProcess.getId()){

                process.setArrived(updatedProcess.isArrived());
                process.setCompleted(updatedProcess.isCompleted());
            }
        }
    }
}

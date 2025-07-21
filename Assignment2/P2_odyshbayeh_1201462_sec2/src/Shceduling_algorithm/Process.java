//odyshbayeh-1201462
//OS task 2

package Shceduling_algorithm;

public class Process{
	public int processid;
    public int arrivaltime;
    public int bursttime;
    public int remainingtime;
    public int completiontime;
    public int turnaroundtime;
    public int priority;
    public int responsetime;
    public int waitingtime;
    public int comesbackafter;

    public Process(int processId, int arrivalTime, int burstTime, int comesbackafter, int priority) {
        this.processid = processId;
        this.arrivaltime = arrivalTime;
        this.bursttime = burstTime;
        this.remainingtime = burstTime;
        this.comesbackafter = comesbackafter;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Process ID: " + processid +
                ", Arrival Time: " + arrivaltime +
                ", Burst Time: " + bursttime +
                ", Priority: " + priority +
                ", Comes Back After: " + comesbackafter;}
}
//odyshbayeh-1201462
//OS task 2

package Shceduling_algorithm;
import java.util.*;

public class CPUScheduler {
	
	public CPUScheduler() {
    }
	
	public List<String> fcfs(Process[] processes) {
        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivaltime));
        int currentTime = 0;
        List<String> ganttChart = new ArrayList<>();
        
        for (Process process : processes) {
            if (currentTime < process.arrivaltime) {
                currentTime = process.arrivaltime;
            }
            process.responsetime = currentTime - process.arrivaltime;
            process.waitingtime = process.responsetime;
            process.completiontime = currentTime + process.bursttime;
            process.turnaroundtime = process.completiontime - process.arrivaltime;
            
            for (int i = currentTime; i < process.completiontime; i++) {
                ganttChart.add("P" + process.processid);
            }
            
            currentTime += process.bursttime;
        }
        return ganttChart;
    }    
    
	public List<String> sjf(Process[] processes) {
	    PriorityQueue<Process> processQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.bursttime));
	    Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivaltime));
	    List<String> ganttChart = new ArrayList<>();
	    
	    int currentTime = 0;
	    int completedProcesses = 0;
	    int i = 0;
	    
	    while (completedProcesses < processes.length) {
	        while (i < processes.length && processes[i].arrivaltime <= currentTime) {
	            processQueue.add(processes[i]);
	            i++;
	        }
	        
	        if (processQueue.isEmpty()) {
	            currentTime = processes[i].arrivaltime;
	            continue;
	        }
	        
	        Process currentProcess = processQueue.poll();
	        currentProcess.responsetime = currentTime - currentProcess.arrivaltime;
	        currentProcess.waitingtime = currentProcess.responsetime;
	        currentTime += currentProcess.bursttime;
	        currentProcess.completiontime = currentTime;
	        currentProcess.turnaroundtime = currentProcess.completiontime - currentProcess.arrivaltime;
	        completedProcesses++;
	        
	        for (int j = 0; j < currentProcess.bursttime; j++) {
	            ganttChart.add("P" + currentProcess.processid);
	        }
	    }
	    
	    return ganttChart;
	}
     
	public List<String> RR(Process[] processes, int timeQuantum) {
	    int n = processes.length;
	    int[] remainingBurstTime = new int[n];
	    List<String> ganttChart = new ArrayList<>();
	    int currentTime = 0;
	    
	    for (int i = 0; i < n; i++) {
	        remainingBurstTime[i] = processes[i].bursttime;
	    }
	    
	    while (true) {
	        boolean done = true;
	        
	        for (int i = 0; i < n; i++) {
	            if (remainingBurstTime[i] > 0) {
	                done = false;
	                
	                int executionTime = Math.min(timeQuantum, remainingBurstTime[i]);
	                
	                for (int j = 0; j < executionTime; j++) {
	                    ganttChart.add("P" + processes[i].processid);
	                    currentTime++;
	                }
	                
	                remainingBurstTime[i] -= executionTime;
	                
	                if (remainingBurstTime[i] == 0) {
	                    processes[i].turnaroundtime = currentTime - processes[i].arrivaltime;
	                    processes[i].waitingtime = processes[i].turnaroundtime - processes[i].bursttime;
	                }
	                
	                processes[i].comesbackafter = Math.max(processes[i].comesbackafter, currentTime);
	            }
	        }
	        
	        if (done) {
	            break;
	        }
	    }
	    
	    return ganttChart;
	}
    
	public List<String> psempitive(Process[] processes) {
        List<String> ganttChart = new ArrayList<>();
        int currentTime = 0;
        int n = processes.length;
        
        PriorityQueue<Process> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.priority));
        
        Arrays.sort(processes, Comparator
        	    .comparingInt((Process p) -> p.arrivaltime)
        	    .thenComparingInt(p -> p.priority)
        	    .thenComparingInt(p -> p.bursttime)
        	);
        int completed = 0;
        int timeInQueue = 0;
        
        while (completed < n || !priorityQueue.isEmpty()) {
            for (Process process : processes) {
                if (process.arrivaltime == currentTime && process.bursttime > 0) {
                    priorityQueue.add(process);
                }
            }
            
            if (!priorityQueue.isEmpty()) {
                Process current = priorityQueue.poll();
                ganttChart.add("P" + current.processid);
                current.remainingtime--;
                currentTime++;
                
                if (current.remainingtime == 0) {
                    completed++;
                    current.turnaroundtime = currentTime - current.arrivaltime;
                    current.waitingtime = current.turnaroundtime - current.bursttime;
                    current.bursttime = 0;
                    timeInQueue = 0;
                } else {
                    timeInQueue++;
                    if (timeInQueue % 5 == 0) {
                        current.priority--;
                        if (current.priority < 0) {
                            current.priority = 0; 
                        }
                    }
                    priorityQueue.add(current);
                }
            } else {
                currentTime++;
            }
        }
        
        return ganttChart;
    }
	
	public List<String> psnonempitive(Process[] processes) {
        List<String> ganttChart = new ArrayList<>();
        int currentTime = 0;
        int n = processes.length;
        
        Arrays.sort(processes, Comparator
                .comparingInt((Process p) -> p.arrivaltime)
                .thenComparingInt(p -> p.priority)
                .thenComparingInt(p -> p.bursttime)
        );
        int completed = 0;
        
        while (completed < n) {
            boolean processAdded = false;
            Process selectedProcess = null;
            
            for (Process process : processes) {
                if (process.arrivaltime <= currentTime && process.bursttime > 0) {
                    if (selectedProcess == null || process.priority < selectedProcess.priority) {
                        selectedProcess = process;
                    }
                }
            }
            
            if (selectedProcess != null) {
                ganttChart.add("P" + selectedProcess.processid);
                currentTime += selectedProcess.bursttime;
                selectedProcess.turnaroundtime = currentTime - selectedProcess.arrivaltime;
                selectedProcess.waitingtime = selectedProcess.turnaroundtime - selectedProcess.bursttime;
                selectedProcess.bursttime = 0;
                completed++;
                
                if (completed % 5 == 0) {
                    selectedProcess.priority--;
                    if (selectedProcess.priority < 0) {
                        selectedProcess.priority = 0;
                    }
                }
                processAdded = true;
            }
            
            if (!processAdded) {
                currentTime++;
            }
        }
        
        return ganttChart;
    }
	
	public List<String> srtf(Process[] processes) {
        List<String> ganttChart = new ArrayList<>();
        int n = processes.length;
        int[] remainingBurstTime = new int[n];
        boolean[] isCompleted = new boolean[n];
        int completed = 0, currentTime = 0, minRemainingTime;
        int shortest = -1;
        
        for (int i = 0; i < n; i++) {
            remainingBurstTime[i] = processes[i].bursttime;
        }
        
        while (completed != n) {
            minRemainingTime = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if ((processes[i].arrivaltime <= currentTime) && (!isCompleted[i]) && (remainingBurstTime[i] < minRemainingTime)) {
                    minRemainingTime = remainingBurstTime[i];
                    shortest = i;
                }
            }
            
            if (shortest == -1) {
                currentTime++;
                continue;
            }
            
            ganttChart.add("P" + processes[shortest].processid);
            remainingBurstTime[shortest]--;
            
            if (remainingBurstTime[shortest] == 0) {
                completed++;
                isCompleted[shortest] = true;
                processes[shortest].completiontime = currentTime + 1;
                processes[shortest].turnaroundtime = processes[shortest].completiontime - processes[shortest].arrivaltime;
                processes[shortest].waitingtime = processes[shortest].turnaroundtime - processes[shortest].bursttime;
            }
            
            currentTime++;
        }
        return ganttChart;
    }
}
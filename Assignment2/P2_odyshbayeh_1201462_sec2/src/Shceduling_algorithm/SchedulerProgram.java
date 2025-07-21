//odyshbayeh-1201462
//OS task 2


package Shceduling_algorithm;
import java.util.List;
import java.util.Scanner;

public class SchedulerProgram {
	
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numprocesses = 0;
        do {
        System.out.println("enter the number of processes: ");
        numprocesses = scanner.nextInt();
        if (numprocesses == 0) {
        	System.out.println("the number of processes can't be 0");
        }else {
        	break;
        }
        } while (numprocesses == 0);
        Process[] processes = new Process[numprocesses];

        for (int i = 0; i < numprocesses; i++) {
            System.out.println("enter details for process " + (i + 1) + ": ");
            int id = i + 1;
            System.out.println("the process ID is : "+id);
            System.out.print("arrival time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("burst time: ");
            int burstTime = 0;
            do {
            	if (burstTime == 0) {
            		System.out.println("\nthe burst time of a process can't be 0");
            		burstTime = scanner.nextInt();
            	}else {
            		break;
            	}
            }while (burstTime == 0);
            System.out.print("priority: ");
            int priority = scanner.nextInt();
            System.out.print("comes back after: ");
            int comesBackAfter = scanner.nextInt();

            processes[i] = new Process(id, arrivalTime, burstTime, comesBackAfter, priority);
        }
        
        CPUScheduler cpuScheduler = new CPUScheduler();
        List<String> ganttChart = null;
        System.out.println("\ndetails for the processes :");
        for (Process process : processes) {
            System.out.println(process.toString() + "\n");
        }
        int choice =0;
     while (choice != 6) {
    	 System.out.println("for better running of the program try to run each method separatly running them sequentially like 1.2.3.4 may cause memory drop");
         System.out.println("Choose a scheduling algorithm:");
         System.out.println("1. FCFS");
         System.out.println("2. SJF");
         System.out.println("3. Round Robin");
         System.out.println("4. Priority scheduling");
         System.out.println("5. SRTF");
         System.out.println("6. exit the program");
    	 choice = scanner.nextInt();
        switch (choice) {
            case 1:
            	System.out.println("scheduling algorithm results : \n");
            	ganttChart = cpuScheduler.fcfs(processes);
            	System.out.println("gantt chart: " + ganttChart.toString());
            	printAverages(processes);
            	ganttChart.clear();
                break;
            case 2:
            	System.out.println("scheduling algorithm results : \n");
            	ganttChart = cpuScheduler.sjf(processes);
            	System.out.println("gantt chart: " + ganttChart.toString());
            	printAverages(processes);
            	ganttChart.clear();
                break;
            case 3:
                System.out.print("enter time quantum for Round Robin: ");
                int timeQuantum = scanner.nextInt();
                System.out.println("scheduling algorithm results : \n");
                ganttChart = cpuScheduler.RR(processes, timeQuantum);
                System.out.println("gantt chart: " + ganttChart.toString());
                printAverages(processes);
                ganttChart.clear();
                break;
            case 4:
            	System.out.println("choose priority scheduling method:");
            	System.out.println("1. Preemptive");
            	System.out.println("2. Non-Preemptive");
            	System.out.println("please consider running the non-preempitive case alone because it uses a lot of memory and may cause crashes ");
            	int priorityMethod = scanner.nextInt();
            	if (priorityMethod == 1) {
            		System.out.println("scheduling algorithm results : \n");
            	    ganttChart = cpuScheduler.psempitive(processes);
            	    System.out.println("gantt chart: " + ganttChart.toString());
            	    printAverages(processes);
            	    ganttChart.clear();
            	} else {
            		System.out.println("scheduling algorithm results : \n");
            	    ganttChart = cpuScheduler.psnonempitive(processes);
            	    System.out.println("gantt chart: " + ganttChart.toString());
            	    printAverages(processes);
            	    ganttChart.clear();
            	}
                break;
            case 5:
            	System.out.println("scheduling algorithm results : \n");
            	ganttChart = cpuScheduler.srtf(processes);
            	System.out.println("gantt chart: " + ganttChart.toString());
            	printAverages(processes);
            	ganttChart.clear();
                break;
            case 6:
            	System.out.println("exiting the program.");
            	break;
            default:
                System.out.println("invalid choice.");
        }     
    }
     scanner.close();
   }
    private static void printAverages(Process[] processes) {
        double totalTurnaroundTime = 0;
        double totalWaitingTime = 0;
        
        for (Process process : processes) {
            totalTurnaroundTime += process.turnaroundtime;
            totalWaitingTime += process.waitingtime;
        }
        
        double avgTurnaroundTime = totalTurnaroundTime / processes.length;
        double avgWaitingTime = totalWaitingTime / processes.length;
        
        System.out.println("\naverage turnaround time: " + avgTurnaroundTime);
        System.out.println("average waiting time: " + avgWaitingTime);
    }
}

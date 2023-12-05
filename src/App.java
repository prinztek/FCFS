import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int numberOfProcess = sc.nextInt();

        // user input = arrival time(at), burst time(bt)
        // automatically set = processes (int[] processId)
        // calculate = completion time(ct), turn around time(tat), waiting time(wt)

        int[] processId = new int[numberOfProcess];
        int[] at = new int[numberOfProcess];
        int[] bt = new int[numberOfProcess];
        int[] ct = new int[numberOfProcess];
        int[] tat = new int[numberOfProcess];
        int[] wt = new int[numberOfProcess];

        // get user input
        for (int i = 0; i < numberOfProcess; i++) {
            processId[i] = i + 1; // (if n = 5) processId[i] = 1, 2, 3, 4, 5
            System.out.println("P" + (processId[i]));
            System.out.print("Process arrival time: ");
            at[i] = sc.nextInt();
            System.out.print("Process burst time: ");
            bt[i] = sc.nextInt();
        }

        // sort processes - from least to greatest
        // for equal arrival time -> put first the first process that is inputted
        sortProcesses(numberOfProcess, processId, at, bt);

        // calculate for completion time(ct)
        getCompletionTime(numberOfProcess, at, bt, ct);

        // calculate for turn around time(tat)
        getTurnAroundTime(numberOfProcess, ct, at, tat);

        // calculate for turn around time(tat)
        getWaitTime(numberOfProcess, tat, bt, wt);
        System.out.println();

        // display table
        displayTable(numberOfProcess, processId, at, bt, ct, tat, wt);
        System.out.println();

        // display gantt chart
        displayGanttChart(numberOfProcess, processId, at, bt, ct);
        System.out.println();

        // display total and average
        int totalWt = getTotalWaitTime(wt);
        System.out.println("Total WT: " + totalWt + "ms");

        int totalTat = getTotalTurnAroundTime(tat);
        System.out.println("Total TAT: " + totalTat + "ms");

        double avglWt = getAvgWaitTime(totalWt, numberOfProcess);
        System.out.println("Average WT: " + avglWt + "ms");

        double avgTat = getAvgTurnAroundTime(totalTat, numberOfProcess);
        System.out.println("Average TAT: " + avgTat + "ms");
        // ****************
        sc.close();
    }

    static void sortProcesses(int n, int[] processId, int[] at, int[] bt) {
        int temp;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // sort processes, arrival time, burst time
                if (at[i] > at[j] || (at[i] == at[j] && processId[i] > processId[j])) {
                    temp = at[i];
                    at[i] = at[j];
                    at[j] = temp;

                    temp = processId[i];
                    processId[i] = processId[j];
                    processId[j] = temp;

                    temp = bt[i];
                    bt[i] = bt[j];
                    bt[j] = temp;
                }
            }
        }
    }

    static void getCompletionTime(int n, int[] at, int[] bt, int[] ct) {
        ct[0] = at[0] + bt[0]; // set the first completion time

        for (int i = 1; i < n; i++) {
            if (ct[i - 1] < at[i]) { // idle
                ct[i] = at[i] + bt[i];
            } else {
                ct[i] = ct[i - 1] + bt[i];
            }
        }
    }

    static void displayTable(int n, int[] p, int[] at, int[] bt, int[] ct, int[] tat, int[] wt) {
        System.out.println("P" + "\t" + "AT" + "\t" + "BT" + "\t" + "CT" + "\t" + "TAT" + "\t" + "WT");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + p[i] + "\t" + at[i] + "\t" + bt[i] + "\t" + ct[i] + "\t" + tat[i] + "\t" + wt[i]);
        }
    }

    static void getTurnAroundTime(int n, int[] ct, int[] at, int[] tat) {
        for (int i = 0; i < n; i++) {
            tat[i] = ct[i] - at[i];
        }
    }

    static void getWaitTime(int n, int[] tat, int[] bt, int[] wt) {
        for (int i = 0; i < n; i++) {
            wt[i] = tat[i] - bt[i];
        }
    }

    static int getTotalTurnAroundTime(int[] tat) {
        int total = 0;
        for (int i : tat) {
            total += i;
        }
        return total;
    }

    static double getAvgTurnAroundTime(int total, int length) {
        double ave = (double) total / length;
        ave = Math.round(ave * 100.0) / 100.0;
        return ave;
    }

    static int getTotalWaitTime(int[] wt) {
        int total = 0;
        for (int i : wt) {
            total += i;
        }
        return total;
    }

    static double getAvgWaitTime(int total, int length) {
        double ave = (double) total / length;
        ave = Math.round(ave * 100.0) / 100.0;
        return ave;
    }

    static void displayGanttChart(int n, int[] processId, int[] at, int[] bt, int[] ct) {
        ArrayList<String> ganttChartProcesses = new ArrayList<String>();
        ArrayList<Integer> ganttChartProcessesTime = new ArrayList<Integer>();

        // Creating Gantt Chart
        for (int i = 0; i < n; i++) {
            if (i == 0) { // first
                // print 0 if there is no 0 in arrival time // idle
                if (at[i] != 0) { // idle
                    ganttChartProcesses.add("//");
                    ganttChartProcessesTime.add(0);
                }
                // print the arrival time and
                // completion time of first process
                ganttChartProcesses.add("P" + processId[i]);
                ganttChartProcessesTime.add(at[i]);
                ganttChartProcessesTime.add(ct[i]);

            } else if (i == n - 1) { // last
                // print at or ct of last process
                if (ct[i - 1] >= at[i]) { // no idle
                    ganttChartProcesses.add("P" + processId[i]);
                    ganttChartProcessesTime.add(ct[i]);
                } else { // idle
                    ganttChartProcesses.add("//"); // idle
                    ganttChartProcesses.add("P" + processId[i]);
                    ganttChartProcessesTime.add(at[i]);
                    ganttChartProcessesTime.add(ct[i]);
                }
            } else {
                // print other processes
                // check the last completion time
                if (ct[i - 1] < at[i]) { // idle
                    ganttChartProcesses.add("//"); // idle
                    ganttChartProcesses.add("P" + processId[i]);
                    ganttChartProcessesTime.add(at[i]);
                    ganttChartProcessesTime.add(ct[i]);
                } else { // no idle
                    ganttChartProcesses.add("P" + processId[i]);
                    ganttChartProcessesTime.add(ct[i]);
                }
            }
        }

        // Printing Gantt Chart
        System.out.println("Gantt Chart");
        System.out.print("|");
        for (String process : ganttChartProcesses) {
            System.out.print("  " + process + "  ");
            System.out.print("|");
        }

        System.out.println();
        for (Integer time : ganttChartProcessesTime) {
            int size = String.valueOf(time).length();
            if (size == 3) {
                System.out.print(time + "    ");
            } else if (size == 2) {
                System.out.print(time + "     ");
            } else {
                System.out.print(time + "      ");
            }
        }
    }
}
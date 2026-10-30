import java.util.*;

public class tugas2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Pilih Algoritma:");
        System.out.println("1. FCFS");
        System.out.println("2. SJF");
        System.out.println("3. Round Robin");
        System.out.print("Pilihan: ");
        int pilih = input.nextInt();

        if (pilih == 1) {
            fcfs();
        } else if (pilih == 2) {
            sjf();
        } else if (pilih == 3) {
            rr();
        }

        input.close();
    }

    public static void fcfs() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Jumlah proses: ");
        int n = sc.nextInt();

        int[] arrival = new int[n];
        int[] burst = new int[n];
        int[] waiting = new int[n];
        int[] turnaround = new int[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Arrival time P" + (i + 1) + ": ");
            arrival[i] = sc.nextInt();
            System.out.print("Burst time P" + (i + 1) + ": ");
            burst[i] = sc.nextInt();
        }

        // FCFS dengan Arrival Time (asumsi input urut kedatangan)
        int currentTime = 0;
        for (int i = 0; i < n; i++) {
            if (currentTime < arrival[i]) {
                currentTime = arrival[i];
            }
            waiting[i] = currentTime - arrival[i];
            currentTime += burst[i];
            turnaround[i] = waiting[i] + burst[i];
            System.out.println("P" + (i + 1) + " w=" + waiting[i] + " t=" + turnaround[i]);
        }

        sc.close();

    }

    public static void sjf() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Jumlah proses: ");
        int n = sc.nextInt();

        int[] arrival = new int[n];
        int[] burst = new int[n];
        int[] waiting = new int[n];
        int[] turnaround = new int[n];
        boolean[] done = new boolean[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Arrival time P" + (i + 1) + ": ");
            arrival[i] = sc.nextInt();
            System.out.print("Burst time P" + (i + 1) + ": ");
            burst[i] = sc.nextInt();
        }

        int currentTime = 0, completed = 0;
        while (completed < n) {
            int idx = -1, minBurst = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (arrival[i] <= currentTime && !done[i] && burst[i] < minBurst) {
                    minBurst = burst[i];
                    idx = i;
                }
            }

            if (idx == -1) {
                currentTime++;
            } else {
                waiting[idx] = currentTime - arrival[idx];
                currentTime += burst[idx];
                turnaround[idx] = waiting[idx] + burst[idx];
                done[idx] = true;
                completed++;
            }
        }

        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1) + " w=" + waiting[i] + " t=" + turnaround[i]);
        }
    }

    public static void rr() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Jumlah proses: ");
        int n = sc.nextInt();

        int[] arrival = new int[n];
        int[] burst = new int[n];
        int[] rem = new int[n];
        int[] waiting = new int[n];
        int[] turnaround = new int[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Arrival time P" + (i + 1) + ": ");
            arrival[i] = sc.nextInt();
            System.out.print("Burst time P" + (i + 1) + ": ");
            burst[i] = sc.nextInt();
            rem[i] = burst[i];
        }

        System.out.print("Time Quantum: ");
        int q = sc.nextInt();

        Queue<Integer> queue = new LinkedList<>();
        boolean[] inQueue = new boolean[n];
        int currentTime = 0, completed = 0;

        for (int i = 0; i < n; i++) {
            if (arrival[i] <= currentTime && !inQueue[i]) {
                queue.add(i);
                inQueue[i] = true;
            }
        }

        while (completed < n) {
            if (queue.isEmpty()) {
                currentTime++;
                for (int i = 0; i < n; i++) {
                    if (arrival[i] <= currentTime && !inQueue[i]) {
                        queue.add(i);
                        inQueue[i] = true;
                    }
                }
                continue;
            }

            int curr = queue.poll();
            int slice = Math.min(rem[curr], q);
            rem[curr] -= slice;
            currentTime += slice;

            for (int i = 0; i < n; i++) {
                if (arrival[i] <= currentTime && !inQueue[i]) {
                    queue.add(i);
                    inQueue[i] = true;
                }
            }

            if (rem[curr] > 0) {
                queue.add(curr);
            } else {
                turnaround[curr] = currentTime - arrival[curr];
                waiting[curr] = turnaround[curr] - burst[curr];
                completed++;
            }
        }

        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1) + " w=" + waiting[i] + " t=" + turnaround[i]);
        }
    }

}

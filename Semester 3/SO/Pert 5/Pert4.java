import java.util.Scanner;

public class Pert4 {
    public static void main(String[] args) {
        fcfs();
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

}

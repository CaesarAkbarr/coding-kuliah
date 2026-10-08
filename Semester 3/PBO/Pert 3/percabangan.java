import java.util.Scanner;

public class percabangan {

     public static void main(String[] args) {

         int nilai;
         Scanner input = new Scanner(System.in);
         System.out.print("Masukkan nilai: ");
         nilai = input.nextInt();

         if (nilai >= 90) {
             System.out.println("Nilai A");
         } else if (nilai >= 80) {
             System.out.println("Nilai B");
         } else if (nilai >= 70) {
             System.out.println("Nilai C");
         } else if (nilai >= 60) {
             System.out.println("Nilai D");
         } else {
             System.out.println("Nilai E");
         }

         input.close();

     }

}

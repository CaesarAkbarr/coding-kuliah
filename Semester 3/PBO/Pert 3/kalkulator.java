import java.util.Scanner;

public class kalkulator {

     public static void main(String[] args) {

         int menu;
         double angka1, angka2, hasil;

         System.out.println("=== Kalkulator ===");
         System.out.println("1. Penjumlahan");
         System.out.println("2. Pengurangan");
         System.out.println("3. Perkalian");
         System.out.println("4. Pembagian");

         Scanner input = new Scanner(System.in);
         System.out.print("Silahkan pilih operator: ");
         menu = input.nextInt();

         if (menu == 1) {
             System.out.print("Masukkan angka pertama: ");
             angka1 = input.nextDouble();
             System.out.print("Masukkan angka kedua: ");
             angka2 = input.nextDouble();
             hasil = angka1 + angka2;
             System.out.print("Hasilnya: " + hasil);
         } else if (menu == 2) {
             System.out.print("Masukkan angka pertama: ");
             angka1 = input.nextDouble();
             System.out.print("Masukkan angka kedua: ");
             angka2 = input.nextDouble();
             hasil = angka1 - angka2;
             System.out.print("Hasilnya: " + hasil);
         } else if (menu == 3) {
             System.out.print("Masukkan angka pertama: ");
             angka1 = input.nextDouble();
             System.out.print("Masukkan angka kedua: ");
             angka2 = input.nextDouble();
             hasil = angka1 * angka2;
             System.out.print("Hasilnya: " + hasil);
         } else if (menu == 4) {
             System.out.print("Masukkan angka pertama: ");
             angka1 = input.nextDouble();
             System.out.print("Masukkan angka kedua: ");
             angka2 = input.nextDouble();
             hasil = angka1 / angka2;
             System.out.print("Hasilnya: " + hasil);
         }

         input.close();
         
     }

}

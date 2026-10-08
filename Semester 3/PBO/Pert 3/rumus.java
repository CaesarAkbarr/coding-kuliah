import java.util.Scanner;

public class rumus {

     public static void main(String[] args) {

         int menu, subMenu;
         double angka1, angka2, angka3;
         double lingkaran, segitiga, persegiPanjang;

         System.out.println("=== Pilih Rumus ===");
         System.out.println("1. Lingkaran");
         System.out.println("2. Segitiga");
         System.out.println("3. Persegi Panjang");

         Scanner input = new Scanner(System.in);
         System.out.print("Silahkan pilih rumus: ");
         menu = input.nextInt();

         if (menu == 1) {

             System.out.println("Rumus lingkaran: ");
             System.out.println("1. Keliling");
             System.out.println("2. Luas");
             System.out.print("Pilih rumus: ");
             subMenu = input.nextInt();

             if (subMenu == 1) {
                 System.out.print("Masukkan angka jari-jari: ");
                 angka1 = input.nextDouble();
                 lingkaran = 3.14 * angka1 * angka1;
                 System.out.print("Hasilnya: " + lingkaran);
             } else if (subMenu == 2) {
                 System.out.print("Masukkan angka jari-jari: ");
                 angka1 = input.nextDouble();
                 lingkaran = 2 * 3.14 * angka1;
                 System.out.print("Hasilnya: " + lingkaran);
             }

         } else if (menu == 2) {

             System.out.println("Rumus segitiga: ");
             System.out.println("1. Keliling");
             System.out.println("2. Luas");
             System.out.print("Pilih rumus: ");
             subMenu = input.nextInt();

             if (subMenu == 1) {
                 System.out.print("Masukkan angka sisi A: ");
                 angka1 = input.nextDouble();
                 System.out.print("Masukkan angka sisi B: ");
                 angka2 = input.nextDouble();
                 System.out.print("Masukkan angka sisi C: ");
                 angka3 = input.nextDouble();
                 segitiga = angka1 + angka2 + angka3;
                 System.out.print("Hasilnya: " + segitiga);
             } else if (subMenu == 2) {
                 System.out.print("Masukkan angka alas: ");
                 angka1 = input.nextDouble();
                 System.out.print("Masukkan angka tinggi: ");
                 angka2 = input.nextDouble();
                 segitiga = 0.5 * angka1 * angka2;
                 System.out.print("Hasilnya: " + segitiga);
             }

         } else if (menu == 3) {

             System.out.println("Rumus persegi panjang: ");
             System.out.println("1. Keliling");
             System.out.println("2. Luas");
             System.out.print("Pilih rumus: ");
             subMenu = input.nextInt();

             if (subMenu == 1) {
                 System.out.print("Masukkan angka panjang: ");
                 angka1 = input.nextDouble();
                 System.out.print("Masukkan angka lebar: ");
                 angka2 = input.nextDouble();
                 persegiPanjang = 2 * (angka1 + angka2);
                 System.out.print("Hasilnya: " + persegiPanjang);
             } else if (subMenu == 2) {
                 System.out.print("Masukkan angka panjang: ");
                 angka1 = input.nextDouble();
                 System.out.print("Masukkan angka lebar: ");
                 angka2 = input.nextDouble();
                 persegiPanjang = angka1 * angka2;
                 System.out.print("Hasilnya: " + persegiPanjang);
             }

         }

         input.close();

     }

}

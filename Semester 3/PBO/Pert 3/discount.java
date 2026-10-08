import java.util.Scanner;

public class discount {

     public static void main(String[] args) {

         Scanner input = new Scanner(System.in);
         System.out.print("Masukkan harga barang: ");
         double hargaBarang = input.nextDouble();
         double diskon;

         if (hargaBarang >= 100000) {
             diskon = 0.2;
             System.out.println("Anda mendapatkan diskon 20%");
         } else if (hargaBarang >=50000) {
             diskon = 0.1;
             System.out.println("Anda mendapatkan diskon 10%");
         } else {
             diskon = 0.0;
             System.out.println("Maaf anda tidak dapat diskon");
         }

         double setelahHargaDiskon = hargaBarang - (hargaBarang * diskon);
         System.out.println("Harga setelah diskon: " + setelahHargaDiskon);

         input.close();

     }

}

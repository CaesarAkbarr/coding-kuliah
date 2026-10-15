import java.util.Scanner;

public class discount {

     public static void main(String[] args) {

         int pelanggan;
         double diskon = 0;

         Scanner input = new Scanner(System.in);
         System.out.println("=== Pilih Tipe Pelanggan ===");
         System.out.println("1. VIP");
         System.out.println("2. Regular");
         System.out.println("3. Non-Member");
         System.out.print("Tipe pelanggan: ");
         pelanggan = input.nextInt();

         System.out.print("Total belanja awal: Rp.");
         double hargaBarang = input.nextDouble();

        if (pelanggan == 1) {

            if (hargaBarang > 1000000) {
                diskon = 0.2;
                System.out.println("Anda mendapatkan diskon sebesar 20% atau: Rp." + hargaBarang * diskon);
            } else if (hargaBarang == 1000000) {
                diskon = 0.15;
                System.out.println("Anda mendapatkan diskon sebesar 15% atau: Rp." + hargaBarang * diskon);
            }

        } else if (pelanggan == 2) {

            if (hargaBarang > 1000000) {
                diskon = 0.1;
                System.out.println("Anda mendapatkan diskon sebesar 10% atau: Rp." + hargaBarang * diskon);
            } else if (hargaBarang == 1000000) {
                diskon = 0.05;
                System.out.println("Anda mendapatkan diskon sebesar 5% atau: Rp." + hargaBarang * diskon);
            }

        } else if (pelanggan == 3) {

            if (hargaBarang > 1000000) {
                diskon = 0.02;
                System.out.println("Anda mendapatkan diskon sebesar 2% atau: Rp." + hargaBarang * diskon);
            } else if (hargaBarang == 1000000) {
                diskon = 0;
                System.out.println("Anda tidak mendapatkan diskon");
            }

        } else {

            System.out.println("Tipe member tidak valid! Dianggap Non-Member!");
            diskon = 0;
            System.out.println("Anda tidak mendapatkan diskon");

        }

        double setelahHargaDiskon = hargaBarang - (hargaBarang * diskon);
        System.out.println("Total bayar akhir: Rp." + setelahHargaDiskon);

        input.close();

    }
}

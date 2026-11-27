javaimport java.util.Scanner;

public class Persediaan {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Masukkan stok awal: ");
        int stok = input.nextInt();

        System.out.print("Masukkan jumlah pengeluaran: ");
        int jumlah = input.nextInt();

        try {
            int sisaStok = kurangiStok(stok, jumlah);
            System.out.println("Sisa stok: " + sisaStok);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Sisa stok: " + stok);
        }

        input.close();
    }

    public static int kurangiStok(int stok, int jumlah) {
        // Aturan validasi:
        // 1. Stok awal tidak boleh negatif (stok < 0)
        // 2. Jumlah pengeluaran harus positif (jumlah <= 0)
        // 3. Jumlah pengeluaran tidak boleh melebihi stok (jumlah > stok)
        if (stok < 0 || jumlah <= 0 || jumlah > stok) {
            throw new IllegalArgumentException("Data tidak valid");
        }
        return stok - jumlah;
    }
}

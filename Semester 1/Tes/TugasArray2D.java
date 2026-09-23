import java.util.Scanner;

public class TugasArray2D {

    public static void main(String[] args) {
        DataPasien();
    }

    static Scanner input = new Scanner(System.in);
    // Array 2D awal (NO RM, Nama Pasien, Usia) - semua baris non-null

    static String[][] pasien = new String[4][3];
    static int idx = 0;

    public static void DataPasien() {
        int pilih = -1;
        while (pilih != 0) {
            System.out.println("\n===== MENU DATA PASIEN =====");
            System.out.println("1. Tampilkan Semua Data");
            System.out.println("2. Tambah Data Pasien");
            System.out.println("3. Cari Data Pasien");
            System.out.println("4. Ubah Data Pasien");
            System.out.println("5. Hapus Data Pasien");
            System.out.println("6. Statistik Pasien");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");
            String line = input.nextLine().trim();
            if (line.equals("")) {
                System.out.println("Masukkan pilihan!");
                continue;
            }
            try {
                pilih = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Input harus angka!");
                continue;
            }

            switch (pilih) {
                case 1:
                    tampilData();
                    break;
                case 2:
                    tambahData();
                    break;
                case 3:
                    cariData();
                    break;
                case 4:
                    ubahData();
                    break;
                case 5:
                    hapusData();
                    break;
                case 6:
                    statistik();
                    break;
                case 0:
                    System.out.println("Keluar...");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    static void tampilData() {
        System.out.println("\n===== DATA PASIEN =====");
        System.out.printf("%-10s %-20s %-5s\n", "NO RM", "Nama", "Usia");
        for (String[] p : pasien) {
            if (p == null)
                continue;
            System.out.printf("%-10s %-20s %-5s\n", safe(p, 0), safe(p, 1), safe(p, 2));
        }
    }

    static String safe(String[] arr, int idx) {
        if (arr == null)
            return "";
        if (idx < 0 || idx >= arr.length)
            return "";
        return arr[idx] == null ? "" : arr[idx];
    }

    static boolean valid(String rm, String nama, String usia) {
        if (rm == null || rm.trim().equals("") || !rm.startsWith("P")) {
            System.out.println("ERROR: NO RM harus diawali huruf 'P' dan tidak boleh kosong!");
            return false;
        }
        if (nama == null || nama.trim().equals("")) {
            System.out.println("ERROR: Nama tidak boleh kosong!");
            return false;
        }
        try {
            int u = Integer.parseInt(usia);
            if (u < 0) {
                System.out.println("ERROR: Usia tidak boleh kurang dari 0!");
                return false;
            }
        } catch (Exception e) {
            System.out.println("ERROR: Usia harus berupa angka!");
            return false;
        }
        return true;
    }

    static void tambahData() {
        System.out.println("\n===== TAMBAH DATA =====");
        String rm, nama, usia;
        while (true) {
            System.out.print("NO RM: ");
            rm = input.nextLine().trim();
            System.out.print("Nama: ");
            nama = input.nextLine().trim();
            System.out.print("Usia: ");
            usia = input.nextLine().trim();

            if (valid(rm, nama, usia))
                break;
            System.out.println("Input tidak valid, coba lagi.\n");
        }

        String[][] temp = new String[pasien.length + 1][3];
        for (int i = 0; i < pasien.length; i++)
            temp[i] = pasien[i];
        temp[temp.length - 1] = new String[] { rm, nama, usia };
        pasien = temp;
        System.out.println("Data berhasil ditambahkan!");
    }

    static void cariData() {
        System.out.print("\nMasukkan NO RM yang dicari: ");
        String cari = input.nextLine().trim();
        for (String[] p : pasien) {
            if (p != null && p[0] != null && p[0].equalsIgnoreCase(cari)) {
                System.out.println("Data ditemukan!");
                System.out.println("NO RM : " + p[0]);
                System.out.println("Nama  : " + p[1]);
                System.out.println("Usia  : " + p[2]);
                return;
            }
        }
        System.out.println("Data tidak ditemukan!");
    }

    static void ubahData() {
        System.out.print("\nMasukkan NO RM yang ingin diubah: ");
        String cari = input.nextLine().trim();
        for (int i = 0; i < pasien.length; i++) {
            if (pasien[i] != null && pasien[i][0] != null && pasien[i][0].equalsIgnoreCase(cari)) {
                System.out.print("Nama baru: ");
                String nama = input.nextLine().trim();
                System.out.print("Usia baru: ");
                String usia = input.nextLine().trim();

                if (!valid(cari, nama, usia)) {
                    System.out.println("Gagal mengubah data (input tidak valid).");
                    return;
                }

                pasien[i][1] = nama;
                pasien[i][2] = usia;
                System.out.println("Data berhasil diubah!");
                return;
            }
        }
        System.out.println("Data tidak ditemukan!");
    }

    static void hapusData() {
        System.out.print("\nNO RM yang ingin dihapus: ");
        String cari = input.nextLine().trim();
        int idx = -1;
        for (int i = 0; i < pasien.length; i++) {
            if (pasien[i] != null && pasien[i][0] != null && pasien[i][0].equalsIgnoreCase(cari)) {
                idx = i;
                break;
            }
        }
        if (idx == -1) {
            System.out.println("Data tidak ditemukan!");
            return;
        }

        String[][] temp = new String[pasien.length - 1][3];
        int k = 0;
        for (int i = 0; i < pasien.length; i++) {
            if (i == idx)
                continue;
            temp[k++] = pasien[i];
        }
        pasien = temp;
        System.out.println("Data berhasil dihapus!");
    }

    static void statistik() {
        System.out.println("\n===== STATISTIK PASIEN =====");
        if (pasien == null || pasien.length == 0) {
            System.out.println("Tidak ada data!");
            return;
        }

        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE, count = 0;
        double total = 0;

        int balita = 0, anak = 0, remaja = 0, dewasa = 0, lansia = 0;

        for (String[] p : pasien) {
            if (p == null)
                continue;
            int u;
            try {
                u = Integer.parseInt(p[2]);
            } catch (Exception e) {
                continue;
            } // skip invalid usia
            count++;
            total += u;
            if (u < min)
                min = u;
            if (u > max)
                max = u;

            if (u <= 5)
                balita++;
            else if (u <= 12)
                anak++;
            else if (u <= 17)
                remaja++;
            else if (u <= 59)
                dewasa++;
            else
                lansia++;
        }

        if (count == 0) {
            System.out.println("Tidak ada data usia valid!");
            return;
        }

        System.out.println("Usia Termuda : " + min);
        System.out.println("Usia Tertua  : " + max);
        System.out.printf("Rata-rata Usia: %.2f\n", total / count);
        System.out.println("Total Pasien : " + count);

        System.out.println("\nKelompok Usia:");
        System.out.println("Balita 0–5   : " + balita);
        System.out.println("Anak 6–12    : " + anak);
        System.out.println("Remaja 13–17 : " + remaja);
        System.out.println("Dewasa 18–59 : " + dewasa);
        System.out.println("Lansia 60+   : " + lansia);
    }
}

// membuat class
class Mahasiswa {

    String nama, nim, tanggal_lahir;

    // Konstruktor
    Mahasiswa(String nama, String nim, String tanggal_lahir){
        this.nama = nama;
        this.nim = nim;
        this.tanggal_lahir = tanggal_lahir;
    }

    // Method
    void info(){
      System.out.println(nama);
      System.out.println(nim);
      System.out.println(tanggal_lahir);
    }

}

class handphone {

    String nm_hp, tp_hp;

    // Konstruktor
    handphone(String nm_hp, String tp_hp){
        this.nm_hp = nm_hp;
        this.tp_hp = tp_hp;
    }

    // Method
    void outputHp(){
      System.out.println("Merk: " + nm_hp);
      System.out.println("Tipenya: " + tp_hp);
    }

}

class buku{

    String judul, penulis, tahun_terbit;
    Integer jumlah_beli, total;
    Integer harga;

// Konstruktor
    buku(String judul, String penulis, String tahun_terbit, int harga, int jumlah_beli){
        this.judul = judul;
        this.penulis = penulis;
        this.tahun_terbit = tahun_terbit;
        this.harga = harga;
        this.jumlah_beli = jumlah_beli;
        this.total = harga * jumlah_beli;
    }

    void total_harga(){
        System.out.println("Total belanja: Rp." + total);
    }

    void output_buku(){
        System.out.println("Nama buku: " + judul);
        System.out.println("Nama pengarang: " + penulis);
        System.out.println("Tahun terbit: " + tahun_terbit);
        System.out.println("Harga buku: Rp." + harga);
        System.out.println("Jumlah beli: " + jumlah_beli);

    }
}

public class main {

    public static void main(String[] args) {

        // memanggil Mahasiswa konvensional
        // Mahasiswa mahasiswa1 = new Mahasiswa();
        // mahasiswa1.nama = "Akbar";
        // mahasiswa1.nim = "3253111006";
        // mahasiswa1.tanggal_lahir = "20 Desember 2006";

        // System.out.println(mahasiswa1.nama);
        // System.out.println(mahasiswa1.nim);
        // System.out.println(mahasiswa1.tanggal_lahir);

        // memanggil Mahasiswa menggunakan Konstruktor dan Method
        // Mahasiswa mahasiswa1 = new Mahasiswa("Akbar", "123", "2006");

        // mahasiswa1.info();

        // handphone xiaomi = new handphone("Xiaomi", "Poco");

        // xiaomi.outputHp();

        buku buku1 = new buku("Bjir", "Jarwo", "2006", 20000, 3);
        buku1.output_buku();
        buku1.total_harga();

    }

}

class produk {
    String nama;
    int harga;

    void infoProduk(){
        System.out.println("Nama barang: " + nama);
        System.out.println("Harga barang: Rp." + harga);
    }
}

class diskon extends produk {
    int potongan;

    void totalHarga(){
        int total = harga - potongan;
        System.out.println("Diskon: Rp." + potongan);
        System.out.println("Total: Rp." + total);
    }
}



public class tantangan {
    public static void main(String[] args) {

        diskon bar1 = new diskon();

        bar1.nama = "Mouse Gaming";
        bar1.harga = 150000;
        bar1.potongan = 30000;

        bar1.infoProduk();
        bar1.totalHarga();

    }
}

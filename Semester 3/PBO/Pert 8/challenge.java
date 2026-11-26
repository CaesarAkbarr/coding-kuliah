public class challenge {
    public static void main(String[] args) {
        bangunDatar[] daftar = {
                new persegi("Persegi", 5),
                new persegiPanjang("Persegi Panjang", 6, 4)
        };

        for (bangunDatar b : daftar) {
            b.tampilkanInfo();
            System.out.println();
        }
    }
}

abstract class bangunDatar {
    private String nama;

    public bangunDatar(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public abstract double hitungLuas();

    public abstract double hitungKeliling();

    public void tampilkanInfo() {
        System.out.println("=== " + getNama() + " ===");
        System.out.println("Luas     : " + hitungLuas());
        System.out.println("Keliling : " + hitungKeliling());
    }
}

class persegi extends bangunDatar {
    private double sisi;

    public persegi(String nama, double sisi) {
        super(nama);
        this.sisi = sisi;
    }

    @Override
    public double hitungLuas() {
        return sisi * sisi;

    }

    @Override
    public double hitungKeliling() {
        return 4 * sisi;

    }
}

class persegiPanjang extends bangunDatar {
    private double panjang;
    private double lebar;

    public persegiPanjang(String nama, double panjang, double lebar) {
        super(nama);
        this.panjang = panjang;
        this.lebar = lebar;
    }

    @Override
    public double hitungLuas() {
        return panjang * lebar;

    }

    @Override
    public double hitungKeliling() {
        return 2 * (panjang + lebar);

    }
}

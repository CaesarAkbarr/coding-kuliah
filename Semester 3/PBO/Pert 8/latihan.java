import java.io.*;

public class latihan {
    public static void main(String[] args) {
        mahlukHidup[] daftar = {
                new manusia("Budi"),
                new hewan("Ikan", "insang", "berenang"),
                new tumbuhan("Mangga", "vegetatif")
        };

        for (mahlukHidup m : daftar) {
            m.cetakCiri();
            System.out.println();
        }
    }
}

abstract class mahlukHidup {
    private String nama;

    public mahlukHidup(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public abstract void cetakCiri();
}

class manusia extends mahlukHidup {
    public manusia(String nama) {
        super(nama);
    }

    public void cetakCiri() {
        System.out.println("=== Ciri-ciri Manusia: " + getNama() + " ===");
        System.out.println("1. Bernafas menggunakan paru-paru.");
        System.out.println("2. Bergerak aktif dengan dua kaki (bipedal).");
        System.out.println("3. Berkembang biak secar vivipar (melahirkan).");
        System.out.println("4. Memiliki akal budi, bahasa verbal, dan kesadaran diri.");
    }
}

class hewan extends mahlukHidup {
    private String alatPernafasan;
    private String caraBergerak;

    public hewan(String nama, String alatPernafasan, String caraBergerak) {
        super(nama);
        this.alatPernafasan = alatPernafasan;
        this.caraBergerak = caraBergerak;
    }

    public void cetakCiri() {
        System.out.println("=== Ciri-ciri Hewan: " + getNama() + " ===");
        System.out.println("1. Bernafas menggunakan: " + alatPernafasan + ".");
        System.out.println("2. Bergerak aktif dengan cara: " + caraBergerak + ".");
        System.out.println("3. Merupakan organisme heterotrof.");
    }
}

class tumbuhan extends mahlukHidup {
    private String berkembangBiak;

    public tumbuhan(String nama, String berkembangBiak) {
        super(nama);
        this.berkembangBiak = berkembangBiak;
    }

    public void cetakCiri() {
        System.out.println("=== Ciri-ciri Tumbuhan: " + getNama() + " ===");
        System.out.println("1. Bernafas menggunakan autotrof yang melakukan fotosintesis.");
        System.out.println("2. Bergerak secara pasif.");
        System.out.println("3. Berkembang biak dengan cara: " + berkembangBiak + ".");
    }
}

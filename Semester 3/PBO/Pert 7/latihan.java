class pegawai {

    String nama;
    int gajiPokok;

    void infoPegawai(){

        System.out.println();
        System.out.println("=== System Gajian Java ===");
        System.out.println("Nama Pegawai: " + nama);
        System.out.printf("Gaji Pegawai: Rp %,d%n", gajiPokok);

    }
}

class manajer extends pegawai {


    void gajiManajer(){

        int tunjangan = (gajiPokok * 20) / 100;
        int totalManajer = tunjangan + gajiPokok;
        System.out.println("Jabatan : Manajer");
        System.out.printf("Tunjangan: Rp %,d%n", tunjangan);
        System.out.printf("Total Gaji: Rp %,d%n", totalManajer);

    }

}

public class latihan {
    public static void main(String[] args) {

        manajer peg1 = new manajer();

        peg1.nama = "Akbar";
        peg1.gajiPokok = 20000000;

        peg1.infoPegawai();
        peg1.gajiManajer();

        pegawai peg2 = new pegawai();

        peg2.nama = "Caesar";
        peg2.gajiPokok = 5000000;

        peg2.infoPegawai();
    }
}

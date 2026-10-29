class Angka {

    private int angka1; //private cuma bisa diakses sama class Angka();
    private int angka2; //private cuma bisa diakses sama class Angka();

    public Angka() {

        this.angka1 = 0;
        this.angka2 = 0;

    }

    public void setAngka1(int angka1) {

        this.angka1 = angka1;

    }
    public void setAngka2(int angka2) {

        this.angka2 = angka2;

    }

    public int getAngka1() {

        return this.angka1;

    }

    public int getAngka2() {

        return this.angka2;

    }

}

public class main {

    public static void main(String[] args) {

        Angka angka = new Angka();
        System.out.println("Sisi: " + angka.getAngka1());
        Angka keliling = new Angka();
        keliling.setAngka1(5);
        System.out.println("Sisi ntah: " + keliling.getAngka1());
        System.out.println("Sisi total: " + keliling.getAngka1() * 4);

    }

}

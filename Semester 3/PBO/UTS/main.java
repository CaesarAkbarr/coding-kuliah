public class main {
    public static void main(String[] args) {
        soal5();
    }

    public static void soal1() {
        int count = 20;

        for (int i = 1; i < count; i++) {
            if (i % 2 == 0) {
                System.out.println("Nomor antrian: " + i);
            }
        }
    }

    public static void soal2() {
        double[][] nilai = { {78, 67, 89}, {78, 90, 87}, {56, 67, 70} };
        String[] nama = {"Amin", "Budi", "Candra"};

        for (int i = 0; i < nilai.length; i++) {
            double na = 0.35 * nilai[i][0] + 0.45 * nilai[i][1] + 0.2 * nilai[i][2];
            System.out.println(nama[i] + " - Na: " + na);
        }
    }

    public static void soal3() {
        int x = 5, y = 10, z = 15;
        boolean b1 = (x++ > 5) && (++y > 10);
        boolean b2 = (z-- < 15) | (x++ == 7);

        // System.out.println(++x > 5);
        System.out.println("b1: " + b1);
        System.out.println("b2: " + b2);
    }

    public static void soal4() {

    }

    public static void soal5() {

        int nilai = 40;

        if (nilai > 80) {
            System.out.println("Nilai A");
        } else if (nilai > 70) {
            System.out.println("Nilai AB");
        } else if (nilai > 65) {
            System.out.println("Nilai B");
        } else if (nilai > 60) {
            System.out.println("Nilai BC");
        } else if (nilai > 55) {
            System.out.println("Nilai C");
        } else if (nilai > 40) {
            System.out.println("Nilai D");
        } else if (nilai <= 40) {
            System.out.println("Nilai E");
        }
    }
}

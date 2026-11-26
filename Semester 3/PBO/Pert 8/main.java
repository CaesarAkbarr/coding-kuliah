import java.io.*;

public class main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        manusia[] m = new manusia[2];

        int x = 0;

        do {
            System.out.println("User " + (x + 1));
            System.out.println("Masukkan tinggi badan anda (cm): ");
            double t = Double.parseDouble(br.readLine());
            System.out.println("Masukkan jenis kelamin anda (L/P) : ");
            String jk = br.readLine();

            if (jk.toUpperCase().equals("L")) {
                m[x] = new cowo(t);
                System.out.println("Berat badan ideal anda sebagai laki-laki adalah: " + m[x].htgBBI());
                System.out.println();
            } else {
                m[x] = new cowo(t);
                System.out.println("Berat badan ideal anda sebagai perempuan adalah: " + m[x].htgBBI());
                System.out.println();
            }
            x++;
        } while (x < 2);
    }
}

abstract class manusia {
    private double tinggiBadan;

    public manusia(double TB) {
        tinggiBadan = TB;
    }

    public double getTB() {
        return tinggiBadan;
    }

    public abstract double htgBBI();
}

class cowo extends manusia {
    public cowo(double TB) {
        super(TB);
    }

    public double htgBBI() {
        return (super.getTB() - 100) * 0.9;
    }
}

class cewe extends manusia {
    public cewe(double TB) {
        super(TB);
    }

    public double htgBBI() {
        return (super.getTB() - 100) * 0.8;
    }
}

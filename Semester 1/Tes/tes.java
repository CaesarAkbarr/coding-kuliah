import java.util.Scanner;

public class tes {
    public static void main(String[] args) {
        tes obj = new tes();
        obj.input();
        obj.tambah(obj.a, obj.b);
    }

    double a;
    double b;

    public void input(){
        Scanner input = new Scanner(System.in);
        System.out.print("Masukkan angka pertama: ");
        a = input.nextDouble();
        System.out.print("Masukkan angka kedua: ");
        b = input.nextDouble();
    }
    
    public void tambah(double data1, double data2){
        double hasil = data1 + data2;
        System.out.println("Hasil: " + hasil);
    }
}

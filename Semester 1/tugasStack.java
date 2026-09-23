
public class tugasStack {

    // 1. Representasi struktur data "nilaiMatKul" (Image 2)
    static class NilaiMatKul {
        String nim;
        String nama;
        double nilai;

        public NilaiMatKul(String nim, String nama, double nilai) {
            this.nim = nim;
            this.nama = nama;
            this.nilai = nilai;
        }
    }

    // 2. Class Stack dan operasinya (Image 2 & 3)
    static class Stack {
        int top;
        NilaiMatKul[] data;
        int maxCapacity = 10; // Sesuai array [1..10] di pseudocode

        // Procedure createEmpty (Image 2)
        public void createEmpty() {
            top = -1;
            data = new NilaiMatKul[maxCapacity];
        }

        // Function isEmpty (Image 2)
        public boolean isEmpty() {
            return top == -1;
        }

        // Function isFull (Image 3)
        public boolean isFull() {
            // Di Java index 0-9, jadi kalau top udah 9 berarti penuh
            return top == maxCapacity - 1;
        }

        // Procedure push (Image 3)
        public void push(String nim, String nama, double nilai) {
            if (isFull()) {
                System.out.println("stack penuh");
            } else {
                // Logic adaptasi: Java mulai dari 0, bukan 1
                top = top + 1;
                data[top] = new NilaiMatKul(nim, nama, nilai);
            }
        }

        // Procedure pop (Image 3)
        public void pop() {
            if (top == -1) { // Stack kosong
                System.out.println("stack kosong");
            } else {
                // Kita turunkan pointer top, data dianggap terhapus/tertimpa nanti
                top = top - 1;
            }
        }

        // Procedure printStack (Image 3)
        public void printStack() {
            if (top != -1) {
                System.out.println("------- ISI STACK -------");
                // Loop dari atas (top) ke bawah (0) -> "downto 1" di pseudocode
                for (int i = top; i >= 0; i--) {
                    System.out.println("=======================");
                    System.out.println("elemen ke : " + (i + 1)); // Biar display-nya tetep 1, 2, 3...
                    System.out.println("nim :   " + data[i].nim);
                    System.out.println("nama :  " + data[i].nama);
                    System.out.println("nilai : " + data[i].nilai);
                }
                System.out.println("=======================");
            } else {
                System.out.println("stack kosong");
            }
        }
    }

    // 3. Algoritma Utama / Main (Image 1)
    public class Main {
        public static void main(String[] args) {
            // Algoritma utama
            Stack S = new Stack();

            S.createEmpty();
            S.printStack(); // Ekspektasi: stack kosong

            System.out.println("=======================");

            // Push data sesuai gambar
            S.push("3095111085", "Ahmad", 84.63);
            S.push("3095111086", "Tri", 74.65);
            S.push("3095111087", "Hidayat", 64.93);

            S.printStack(); // Ekspektasi: Isi 3 data

            System.out.println("=======================");

            // Pop 2 kali sesuai gambar
            S.pop();
            S.pop();

            S.printStack(); // Ekspektasi: Sisa 1 data (Ahmad)

            System.out.println("=======================");
            // End algoritma utama
        }
    }
}

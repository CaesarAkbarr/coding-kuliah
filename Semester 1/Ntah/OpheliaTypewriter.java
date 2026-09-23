package Ntah;

public class OpheliaTypewriter {
    public static void main(String[] args) {

        // 1. DATABASE LIRIK & DELAY
        // Kita ambil bagian Chorus & Verse yang iconic
        String[] lyrics = {
                "All that time",
                "I sat alone in my tower",
                "You were just honing your powers",
                "Now I can see it all",
                "(I can see it all)",
                "",
                "Late one night",
                "You dug me out of my grave and",
                "Saved my heart from the fate of",
                "Ophelia"
        };

        // Delay antar baris (setelah satu baris selesai diketik)
        int[] lineDelays = {
                800, // All that time
                600, // I sat alone in my tower
                500, // You were just honing your powers
                1200, // Now I can see it all
                800, // (I can see it all)
                700, // (Jeda kosong)
                500, // Late one night
                400, // You dug me out of my grave and
                500, // Saved my heart from the fate of
                1200, // Ophelia
        };

        System.out.println("\nNow Playing: Taylor Swift - The Fate of Ophelia\n");

        // 2. OUTER LOOP (Untuk ganti baris)
        for (int i = 0; i < lyrics.length; i++) {

            // Ambil lirik saat ini dan ubah jadi array huruf (char)
            String currentLine = lyrics[i];
            char[] characters = currentLine.toCharArray();

            // 3. INNER LOOP (Untuk ngetik per huruf)
            for (int j = 0; j < characters.length; j++) {
                try {
                    // Print satu huruf TANPA enter (pake print, bukan println)
                    System.out.print(characters[j]);

                    // Efek ngetik: jeda dikit antar huruf (misal 50-80ms)
                    // Semakin kecil angkanya, ngetiknya makin ngebut.
                    Thread.sleep(50);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // 4. PINDAH BARIS & JEDA ANTAR BARIS
            try {
                System.out.println(); // Enter ke bawah setelah satu baris kelar
                Thread.sleep(lineDelays[i]); // Tunggu sebelum mulai baris baru
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n--- End of Story ---\n");
    }
}
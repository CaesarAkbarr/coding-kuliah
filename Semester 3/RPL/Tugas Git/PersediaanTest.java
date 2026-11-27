import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pengujian Unit Kelas Persediaan (Fungsi kurangiStok)")
class PersediaanTest {

    // ========================================================
    // 1. KASUS NORMAL (HAPPY PATH)
    // ========================================================

    @Test
    @DisplayName("TC-01 [Normal]: Pengurangan stok standar dengan input valid")
    void testPenguranganStokNormal() {
        // Arrange & Act: stok awal 10, barang keluar 3
        int hasil = Persediaan.kurangiStok(10, 3);

        // Assert: sisa stok harus 7
        assertEquals(7, hasil, "Stok awal 10 dikurangi 3 harus menghasilkan 7");
    }

    // ========================================================
    // 2. KASUS NILAI BATAS (BOUNDARY CASES)
    // ========================================================

    @Test
    @DisplayName("TC-02 [Batas Atas]: Mengeluarkan seluruh stok hingga habis (stok = jumlah)")
    void testBatasAtasStokHabisTepatNol() {
        // Arrange & Act: stok awal 10, barang keluar 10
        int hasil = Persediaan.kurangiStok(10, 10);

        // Assert: sisa stok harus tepat 0
        assertEquals(0, hasil, "Stok awal 10 dikurangi 10 harus habis (0)");
    }

    @Test
    @DisplayName("TC-03 [Batas Bawah Valid]: Mengeluarkan kuantitas minimum yang sah (jumlah = 1)")
    void testBatasBawahValidSatu() {
        // Arrange & Act: stok awal 10, barang keluar 1
        int hasil = Persediaan.kurangiStok(10, 1);

        // Assert: sisa stok harus 9
        assertEquals(9, hasil, "Stok awal 10 dikurangi 1 harus menghasilkan 9");
    }

    // ========================================================
    // 3. KASUS TIDAK VALID (NEGATIVE / ERROR CASES)
    // ========================================================

    @Test
    @DisplayName("TC-04 [Tidak Valid]: Pengeluaran bernilai nol (jumlah = 0) harus ditolak")
    void testJumlahPengeluaranNolDitolak() {
        // Assert: jumlah = 0 melanggar aturan bisnis (kuantitas harus > 0)
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Persediaan.kurangiStok(10, 0),
            "Pengeluaran kuantitas 0 harus melempar IllegalArgumentException"
        );
        assertEquals("Data tidak valid", exception.getMessage());
    }

    @Test
    @DisplayName("TC-05 [Tidak Valid]: Pengeluaran melebihi stok yang tersedia (jumlah > stok)")
    void testPengeluaranMelebihiStokDitolak() {
        // Assert: stok 10 diminta keluar 11 harus ditolak demi integritas inventaris
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Persediaan.kurangiStok(10, 11),
            "Pengeluaran melebihi stok harus melempar IllegalArgumentException"
        );
        assertEquals("Data tidak valid", exception.getMessage());
    }

    @Test
    @DisplayName("TC-06 [Tidak Valid]: Kuantitas pengeluaran bernilai negatif (jumlah < 0)")
    void testJumlahPengeluaranNegatifDitolak() {
        // Assert: jumlah negatif (-5) harus ditolak
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Persediaan.kurangiStok(10, -5),
            "Pengeluaran negatif harus melempar IllegalArgumentException"
        );
        assertEquals("Data tidak valid", exception.getMessage());
    }

    @Test
    @DisplayName("TC-07 [Tidak Valid]: Saldo stok awal bernilai negatif (stok < 0)")
    void testStokAwalNegatifDitolak() {
        // Assert: stok awal tidak mungkin negatif
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Persediaan.kurangiStok(-1, 5),
            "Stok awal negatif harus melempar IllegalArgumentException"
        );
        assertEquals("Data tidak valid", exception.getMessage());
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tugasakhirv2;

/**
 *
 * @author ROG G513RM
 */
public class Mahasiswa {
    private String nim, nama, alamat, noHp, kota;

    public Mahasiswa(String nim, String nama, String alamat, String noHp, String kota) {
        this.nim = nim;
        this.nama = nama;
        this.alamat = alamat;
        this.noHp = noHp;
        this.kota = kota;
    }

    // Getter wajib ada!
    public String getNim() { return nim; }
    public String getNama() { return nama; }
    public String getAlamat() { return alamat; }
    public String getNoHp() { return noHp; }
    public String getKota() { return kota; }
}
class contohPembayaran {

    int nominal;

    void bayar(){

        System.out.println("Memproses pembayaran sebesar: Rp." + nominal);

    }
}

class ewallet extends contohPembayaran {

    @Override

    void bayar(){

        System.out.println("Pembayaran menggunakan E-Wallet berhasil Rp." + nominal + " mendapatkan cashback sebesar Rp.2000");

    }
}

class transferBank extends contohPembayaran {

    @Override

    void bayar(){

        int totalAdmin = nominal + 2500;

        System.out.println("Pembayaran menggunakan bank berhasil Rp." + totalAdmin + " ini sudah termasuk Admin Rp.2500");

    }
}

public class pembayaran {
    public static void main(String[] args) {

        ewallet transaksi1 = new ewallet();
        transaksi1.nominal = 500000;

        transferBank transaksi2 = new transferBank();
        transaksi2.nominal = 1000000;

        transaksi1.bayar();
        transaksi2.bayar();
    }
}

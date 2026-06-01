package LembarKerja4;

interface Transaksi {
    void catatTransaksi();
}

interface TransaksiDigital extends Transaksi {
    boolean validasiPin();
}

interface LayananInternasional extends Transaksi {
    void konversiKurs(double jumlah);
}

interface TransferGlobal extends TransaksiDigital, LayananInternasional {
    void prosesTransferGlobal(String negaraTujuan, String nomorRekeningTujuan, double jumlah);
}

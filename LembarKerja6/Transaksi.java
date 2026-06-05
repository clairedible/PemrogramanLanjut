package LembarKerja6;

public class Transaksi {
    private String kodeTransaksi;
    private String nipSiswa;
    private String kodeBuku;
    private String tglPinjam;
    private String lamaPinjam;

    public Transaksi(String kodeTransaksi, String nipSiswa, String kodeBuku, String tglPinjam, String lamaPinjam) {
        this.kodeTransaksi = kodeTransaksi;
        this.nipSiswa = nipSiswa;
        this.kodeBuku = kodeBuku;
        this.tglPinjam = tglPinjam;
        this.lamaPinjam = lamaPinjam;
    }

    public String getKodeTransaksi() {
        return kodeTransaksi;
    }

    public String getNipSiswa() {
        return nipSiswa;
    }

    public String getKodeBuku() {
        return kodeBuku;
    }

    public String getTglPinjam() {
        return tglPinjam;
    }

    public String getLamaPinjam() {
        return lamaPinjam;
    }

    public void setKodeTransaksi(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
    }

    public void setNipSiswa(String nipSiswa) {
        this.nipSiswa = nipSiswa;
    }

    public void setKodeBuku(String kodeBuku) {
        this.kodeBuku = kodeBuku;
    }

    public void setTglPinjam(String tglPinjam) {
        this.tglPinjam = tglPinjam;
    }

    public void setLamaPinjam(String lamaPinjam) {
        this.lamaPinjam = lamaPinjam;
    }

}

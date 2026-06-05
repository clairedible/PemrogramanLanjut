package model;

public class Transaksi {
    private String kodeTrx;
    private String nis;
    private String kodeBuku;
    private String tanggalPinjam;
    private String tanggalKembali;
    private boolean sudahDikembalikan;

    public Transaksi(String kodeTrx, String nis, String kodeBuku,
                     String tanggalPinjam, String tanggalKembali, boolean sudahDikembalikan) {
        this.kodeTrx = kodeTrx;
        this.nis = nis;
        this.kodeBuku = kodeBuku;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
        this.sudahDikembalikan = sudahDikembalikan;
    }

    public String getKodeTrx() { return kodeTrx; }
    public String getNis() { return nis; }
    public String getKodeBuku() { return kodeBuku; }
    public String getTanggalPinjam() { return tanggalPinjam; }
    public String getTanggalKembali() { return tanggalKembali; }
    public boolean isSudahDikembalikan() { return sudahDikembalikan; }
    public void setSudahDikembalikan(boolean v) { this.sudahDikembalikan = v; }

    public String getStatusText() {
        return sudahDikembalikan ? "Sudah Dikembalikan" : "Belum Dikembalikan";
    }

    public String toFileString() {
        return kodeTrx + "," + nis + "," + kodeBuku + "," +
               tanggalPinjam + "," + tanggalKembali + "," + (sudahDikembalikan ? "1" : "0");
    }

    public static Transaksi fromFileString(String line) {
        String[] d = line.split(",");
        return new Transaksi(d[0], d[1], d[2], d[3], d[4], d[5].equals("1"));
    }
}

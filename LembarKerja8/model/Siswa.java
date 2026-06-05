package model;

public class Siswa {
    private String nis;
    private String nama;
    private String alamat;

    public Siswa(String nis, String nama, String alamat) {
        this.nis = nis;
        this.nama = nama;
        this.alamat = alamat;
    }

    public String getNis() { return nis; }
    public String getNama() { return nama; }
    public String getAlamat() { return alamat; }
    public void setNama(String nama) { this.nama = nama; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String toFileString() {
        return nis + "," + nama + "," + alamat;
    }

    public static Siswa fromFileString(String line) {
        String[] d = line.split(",");
        return new Siswa(d[0], d[1], d[2]);
    }
}
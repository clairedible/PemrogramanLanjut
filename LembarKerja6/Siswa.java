package LembarKerja6;

public class Siswa{
    private String nama;
    private String nis;
    private String alamat;

    public Siswa(String nama, String nis, String alamat) {
        this.nama = nama;
        this.nis = nis;
        this.alamat = alamat;
    }

    public String getNama() {
        return nama;
    }

    public String getNis() {
        return nis;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
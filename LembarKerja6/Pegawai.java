package LembarKerja6;

public class Pegawai {
    private String nip;
    private String nama;
    private String password;
    private String tglLahir;

    public Pegawai(String nip, String nama, String password, String tglLahir) {
        this.nip = nip;
        this.nama = nama;
        this.password = password;
        this.tglLahir = tglLahir;
    }

    public String getNip() {
        return nip;
    }

    public String getNama() {
        return nama;
    }

    public String getPassword() {
        return password;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }
}

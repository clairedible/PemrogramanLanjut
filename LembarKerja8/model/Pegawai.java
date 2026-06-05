package model;

public class Pegawai {
    private String nip;
    private String nama;
    private String password;
    private String tanggalLahir;

    public Pegawai(String nip, String nama, String password, String tanggalLahir) {
        this.nip = nip;
        this.nama = nama;
        this.password = password;
        this.tanggalLahir = tanggalLahir;
    }

    public String getNip() { return nip; }
    public String getNama() { return nama; }
    public String getPassword() { return password; }
    public String getTanggalLahir() { return tanggalLahir; }

    public String toFileString() {
        return nip + "," + nama + "," + password + "," + tanggalLahir;
    }

    public static Pegawai fromFileString(String line) {
        String[] d = line.split(",");
        return new Pegawai(d[0], d[1], d[2], d.length > 3 ? d[3] : "");
    }
}
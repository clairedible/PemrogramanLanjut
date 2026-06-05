package model;

public class Buku {
    private String kode;
    private String judul;
    private String jenis;

    public Buku(String kode, String judul, String jenis) {
        this.kode = kode;
        this.judul = judul;
        this.jenis = jenis;
    }

    public String getKode() { return kode; }
    public String getJudul() { return judul; }
    public String getJenis() { return jenis; }
    public void setJudul(String judul) { this.judul = judul; }
    public void setJenis(String jenis) { this.jenis = jenis; }

    public String toFileString() {
        return kode + "," + judul + "," + jenis;
    }

    public static Buku fromFileString(String line) {
        String[] d = line.split(",");
        return new Buku(d[0], d[1], d[2]);
    }
}
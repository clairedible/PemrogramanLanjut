package LembarKerja6;

public class Buku {
    private String kodeBuku;
    private String judul;
    private String jenis;

    public Buku(String kodeBuku, String judul, String jenis) {
        this.kodeBuku = kodeBuku;
        this.judul = judul;
        this.jenis = jenis;
    }

    public String getKodeBuku() {
        return kodeBuku;
    }

    public String getJudul() {
        return judul;
    }

    public String getJenis() {
        return jenis;
    }

    public void setKodeBuku(String kodeBuku) {
        this.kodeBuku = kodeBuku;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

}

public class Siswa {
    private String nis;
    private String nama;
    private String alamat;

    public Siswa(String nis, String nama, String alamat) {
        this.nis    = nis;
        this.nama   = nama;
        this.alamat = alamat;
    }

    public String getNis()    { return nis; }
    public String getNama()   { return nama; }
    public String getAlamat() { return alamat; }

    public void setNama(String nama)     { 
        this.nama   = nama; 
    }

    public void setAlamat(String alamat) { 
        this.alamat = alamat; 
    }

    // format untuk save ke CSV
    public String toCsvLine() {
        return nis + "," + nama + "," + alamat;
    }

    // memuat objek Siswa dari CSV ke program 
    public static Siswa fromCsvLine(String csvLine) {
        if (csvLine == null || csvLine.isBlank()) return null;
        String[] parts = csvLine.split(",", 3);
        if (parts.length != 3) return null;
        return new Siswa(parts[0].trim(), parts[1].trim(), parts[2].trim());
    }

    @Override
    public String toString() {
        return "Siswa{nis='" + nis + "', nama='" + nama + "', alamat='" + alamat + "'}";
    }
}

class DuplicateNisException extends Exception {
    public DuplicateNisException(String message) {
        super(message);
    }
}
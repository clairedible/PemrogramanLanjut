import java.io.*;
import java.util.ArrayList;
import java.util.List;

// aturan yang harus dimiliki oleh class yang mendefinisikan bagaimana data disimpan
interface DataRepository<T> {
    List<T> muatData() throws Exception;
    void simpanData(List<T> items) throws Exception;
    boolean isStorageAvailable();
}

// mengatur logika bagaimana data disimpan ke csv
// karena data diwajibkan dalam bentuk CSV, maka class ini mengatur bagaimana data dimasukkan ke CSV
// jika sewaktu waktu meminta perubahan format penyimpanan, maka kita hanya mengubah class ini saja
public class SiswaRepository implements DataRepository<Siswa> {
    private final String filePath;

    public SiswaRepository(String filePath) {
        this.filePath = filePath;
    }

    // membaca data dari csv ke program, lalu mengembalikan dalam bentuk list
    @Override
    public List<Siswa> muatData() throws Exception {
        List<Siswa> daftarSiswa = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) return daftarSiswa; // File belum ada = return data kosong

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Siswa siswa = Siswa.fromCsvLine(line);
                if (siswa != null) daftarSiswa.add(siswa);
            }
        } catch (IOException e) {
            throw new Exception("Gagal membaca data: " + e.getMessage(), e);
        }
        return daftarSiswa;
    }

    // menyimpan data dari program ke csv
    @Override
    public void simpanData(List<Siswa> items) throws Exception {
        File file = new File(filePath);
        // Buat direktori parent jika belum ada
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Siswa siswa : items) {
                bw.write(siswa.toCsvLine());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new Exception("Gagal menyimpan data: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isStorageAvailable() {
        return new File(filePath).exists();
    }
}
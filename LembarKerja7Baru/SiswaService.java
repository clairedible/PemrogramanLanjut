import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// class ini berfungsi untuk menjembatani antara GUI dan Repository
// ia berfungsi untuk memastikan data yang akan dimasukkan ke repository sudah benar atau belum berdasarkan input dari GUI
class SiswaService {
    private final DataRepository<Siswa> aturanSimpan; // untuk memberi aturan bagaimana data disimpan
    private List<Siswa> dataDiMemori; // data aktif yang belum disimpan

public SiswaService(DataRepository<Siswa> aturanSimpan) {
        this.aturanSimpan = aturanSimpan;
        this.dataDiMemori = new ArrayList<>();
    }

    // cek apakah file sudah tersedia, jika belum maka akan dibuat baru
    public boolean isStorageAvailable() {
        return aturanSimpan.isStorageAvailable();
    }

    public void loadData() throws Exception {
        dataDiMemori = aturanSimpan.muatData();
    }

    public List<Siswa> getAll() {
        return new ArrayList<>(dataDiMemori);
    }

    public void tambahSiswa(String nis, String nama, String alamat) throws DuplicateNisException, Exception {
        // jika nis sudah ada, isPresent() return true
        if (findByNis(nis).isPresent()) { throw new DuplicateNisException(
                "Gagal: Data dengan NIS " + nis + " sudah terdaftar!");
        }

        Siswa siswa = new Siswa(nis, nama, alamat);
        dataDiMemori.add(siswa);
        simpanData();
    }

    public boolean updateSiswa(String nis, String namaBaru, String alamatBaru) throws Exception {
        Optional<Siswa> opt = findByNis(nis);
        if (opt.isEmpty()) return false;

        Siswa siswa = opt.get();
        siswa.setNama(namaBaru);
        siswa.setAlamat(alamatBaru);
        simpanData();
        return true;
    }

    // return true jika data berhasil dihapus, false jika tidak ditemukan
    public boolean hapusSiswa(String nis) throws Exception {
        boolean removed = dataDiMemori.removeIf(s -> s.getNis().equals(nis));
        if (removed) simpanData();
        return removed;
    }

    // bisa mengembalikan nilai kosong jika tidak ditemukan
    public Optional<Siswa> findByNis(String nis) {
        return dataDiMemori.stream().filter(s -> s.getNis().equals(nis)).findFirst();
    }

    private void simpanData() throws Exception {
        aturanSimpan.simpanData(dataDiMemori);
    }
}

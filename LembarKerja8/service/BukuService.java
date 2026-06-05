package service;

import model.Buku;
import repository.BukuRepository;
import repository.TransaksiRepository;
import java.util.List;
import java.util.Optional;

public class BukuService {
    private final BukuRepository bukuRepo;
    private final TransaksiRepository transaksiRepo;

    public BukuService(BukuRepository bukuRepo, TransaksiRepository transaksiRepo) {
        this.bukuRepo = bukuRepo;
        this.transaksiRepo = transaksiRepo;
    }

    public List<Buku> semuaBuku() { return bukuRepo.findAll(); }

    public boolean tambahBuku(Buku buku) {
        if (bukuRepo.existsById(buku.getKode())) return false;
        bukuRepo.save(buku);
        return true;
    }

    public boolean updateBuku(String kode, Buku bukuBaru) {
        if (!bukuRepo.existsById(kode)) return false;
        bukuRepo.update(kode, bukuBaru);
        return true;
    }

    public boolean hapusBuku(String kode) {
        if (!bukuRepo.existsById(kode)) return false;
        bukuRepo.deleteById(kode);
        return true;
    }

    public Optional<Buku> cariBuku(String kode) { return bukuRepo.findById(kode); }

    public boolean isTersedia(String kodeBuku) {
        return !transaksiRepo.isBukuSedangDipinjam(kodeBuku);
    }
}

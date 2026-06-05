package service;

import model.Siswa;
import repository.SiswaRepository;
import java.util.List;
import java.util.Optional;

public class SiswaService {
    private final SiswaRepository siswaRepo;

    public SiswaService(SiswaRepository siswaRepo) {
        this.siswaRepo = siswaRepo;
    }

    public List<Siswa> semuaSiswa() { return siswaRepo.findAll(); }

    public boolean tambahSiswa(Siswa siswa) {
        if (siswaRepo.existsById(siswa.getNis())) return false;
        siswaRepo.save(siswa);
        return true;
    }

    public boolean updateSiswa(String nis, Siswa siswaBaru) {
        if (!siswaRepo.existsById(nis)) return false;
        siswaRepo.update(nis, siswaBaru);
        return true;
    }

    public boolean hapusSiswa(String nis) {
        if (!siswaRepo.existsById(nis)) return false;
        siswaRepo.deleteById(nis);
        return true;
    }

    public Optional<Siswa> cariSiswa(String nis) { return siswaRepo.findById(nis); }
}
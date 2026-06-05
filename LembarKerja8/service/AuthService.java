package service;

import model.Pegawai;
import repository.PegawaiRepository;

public class AuthService {
    private final PegawaiRepository pegawaiRepo;
    private Pegawai pegawaiAktif;

    public AuthService(PegawaiRepository pegawaiRepo) {
        this.pegawaiRepo = pegawaiRepo;
    }

    public enum HasilLogin { BERHASIL, SALAH_KREDENSIAL, BELUM_ADA_AKUN }

    public HasilLogin login(String nip, String password) {
        boolean adaData = !pegawaiRepo.findAll().isEmpty();
        return pegawaiRepo.findAll().stream()
                .filter(p -> p.getNip().equals(nip) && p.getPassword().equals(password))
                .findFirst()
                .map(p -> { pegawaiAktif = p; return HasilLogin.BERHASIL; })
                .orElse(adaData ? HasilLogin.SALAH_KREDENSIAL : HasilLogin.BELUM_ADA_AKUN);
    }

    public void daftarPegawai(Pegawai pegawai) {
        pegawaiRepo.save(pegawai);
    }

    public Pegawai getPegawaiAktif() { return pegawaiAktif; }
}

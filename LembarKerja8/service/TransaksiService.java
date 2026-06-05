package service;

import model.Transaksi;
import repository.SiswaRepository;
import repository.BukuRepository;
import repository.TransaksiRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransaksiService {
    private static final int MAKS_PINJAM = 2;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final TransaksiRepository transaksiRepo;
    private final SiswaRepository siswaRepo;
    private final BukuRepository bukuRepo;

    public TransaksiService(TransaksiRepository transaksiRepo,
                             SiswaRepository siswaRepo,
                             BukuRepository bukuRepo) {
        this.transaksiRepo = transaksiRepo;
        this.siswaRepo = siswaRepo;
        this.bukuRepo = bukuRepo;
    }

    public enum HasilPinjam {
        BERHASIL, NIS_TIDAK_ADA, BUKU_TIDAK_ADA,
        BATAS_PINJAM_TERCAPAI, BUKU_TIDAK_TERSEDIA
    }

    public HasilPinjam pinjamBuku(String nis, String kodeBuku, int durasi) {
        if (!siswaRepo.existsById(nis)) return HasilPinjam.NIS_TIDAK_ADA;
        if (!bukuRepo.existsById(kodeBuku)) return HasilPinjam.BUKU_TIDAK_ADA;
        if (transaksiRepo.findAktifByNis(nis).size() >= MAKS_PINJAM) return HasilPinjam.BATAS_PINJAM_TERCAPAI;
        if (transaksiRepo.isBukuSedangDipinjam(kodeBuku)) return HasilPinjam.BUKU_TIDAK_TERSEDIA;

        LocalDate tglPinjam = LocalDate.now();
        LocalDate tglKembali = tglPinjam.plusDays(durasi);
        String kodeTrx = transaksiRepo.generateKodeTrxUrut();
        transaksiRepo.save(new Transaksi(kodeTrx, nis, kodeBuku,
                tglPinjam.format(FMT), tglKembali.format(FMT), false));
        return HasilPinjam.BERHASIL;
    }

    public enum HasilKembali { BERHASIL, TRX_TIDAK_DITEMUKAN }

    public HasilKembali kembalikanBuku(String kodeTrx) {
        return transaksiRepo.findById(kodeTrx)
                .filter(t -> !t.isSudahDikembalikan())
                .map(t -> {
                    t.setSudahDikembalikan(true);
                    transaksiRepo.update(kodeTrx, t);
                    return HasilKembali.BERHASIL;
                })
                .orElse(HasilKembali.TRX_TIDAK_DITEMUKAN);
    }

    public List<Transaksi> semuaTransaksi() { return transaksiRepo.findAll(); }
}


package repository;

import model.Transaksi;
import java.util.List;

public class TransaksiRepository extends FileRepository<Transaksi, String> {

    public TransaksiRepository(String filePath) {
        super(filePath);
    }

    @Override
    protected String toLine(Transaksi e) { return e.toFileString(); }

    @Override
    protected Transaksi fromLine(String line) { return Transaksi.fromFileString(line); }

    @Override
    protected String extractId(Transaksi e) { return e.getKodeTrx(); }

    @Override
    protected String parseId(String idStr) { return idStr; }

    @Override
    protected String idToString(String id) { return id; }

    public List<Transaksi> findAktifByNis(String nis) {
        return findWhere(t -> t.getNis().equals(nis) && !t.isSudahDikembalikan());
    }

    public boolean isBukuSedangDipinjam(String kodeBuku) {
        return findWhere(t -> t.getKodeBuku().equals(kodeBuku) && !t.isSudahDikembalikan())
                .stream().findFirst().isPresent();
    }

    public String generateKodeTrxUrut() {
        int maxId = findAll().stream()
                .filter(t -> t.getKodeTrx().startsWith("TRX-"))
                .mapToInt(t -> {
                    try { return Integer.parseInt(t.getKodeTrx().substring(4)); }
                    catch (NumberFormatException e) { return 0; }
                })
                .max().orElse(0);
        return String.format("TRX-%02d", maxId + 1);
    }
}

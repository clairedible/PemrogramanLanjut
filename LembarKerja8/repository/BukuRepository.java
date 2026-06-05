package repository;

import model.Buku;

public class BukuRepository extends FileRepository<Buku, String> {

    public BukuRepository(String filePath) {
        super(filePath);
    }

    @Override
    protected String toLine(Buku e) { return e.toFileString(); }

    @Override
    protected Buku fromLine(String line) { return Buku.fromFileString(line); }

    @Override
    protected String extractId(Buku e) { return e.getKode(); }

    @Override
    protected String parseId(String idStr) { return idStr; }

    @Override
    protected String idToString(String id) { return id; }
}

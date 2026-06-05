package repository;

import model.Pegawai;

public class PegawaiRepository extends FileRepository<Pegawai, String> {

    public PegawaiRepository(String filePath) {
        super(filePath);
    }

    @Override
    protected String toLine(Pegawai e) { return e.toFileString(); }

    @Override
    protected Pegawai fromLine(String line) { return Pegawai.fromFileString(line); }

    @Override
    protected String extractId(Pegawai e) { return e.getNip(); }

    @Override
    protected String parseId(String idStr) { return idStr; }

    @Override
    protected String idToString(String id) { return id; }
}
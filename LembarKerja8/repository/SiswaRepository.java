package repository;

import model.Siswa;

public class SiswaRepository extends FileRepository<Siswa, String> {

    public SiswaRepository(String filePath) {
        super(filePath);
    }

    @Override
    protected String toLine(Siswa e) { return e.toFileString(); }

    @Override
    protected Siswa fromLine(String line) { return Siswa.fromFileString(line); }

    @Override
    protected String extractId(Siswa e) { return e.getNis(); }

    @Override
    protected String parseId(String idStr) { return idStr; }

    @Override
    protected String idToString(String id) { return id; }
}

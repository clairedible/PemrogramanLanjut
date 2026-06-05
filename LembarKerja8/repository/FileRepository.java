package repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class FileRepository<T, ID> implements Repository<T, ID> {
    protected final String filePath;

    protected FileRepository(String filePath) {
        this.filePath = filePath;
    }

    protected abstract String toLine(T entity);
    protected abstract T fromLine(String line);
    protected abstract ID extractId(T entity);
    protected abstract ID parseId(String idStr);
    protected abstract String idToString(ID id);

    @Override
    public List<T> findAll() {
        List<T> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) result.add(fromLine(line));
            }
        } catch (IOException e) { }
        return result;
    }

    @Override
    public Optional<T> findById(ID id) {
        return findAll().stream()
                .filter(e -> extractId(e).equals(id))
                .findFirst();
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    @Override
    public void save(T entity) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(toLine(entity));
            bw.newLine();
        } catch (IOException e) { }
    }

    @Override
    public void update(ID id, T entity) {
        List<T> all = findAll();
        List<String> lines = new ArrayList<>();
        for (T e : all) {
            if (extractId(e).equals(id)) lines.add(toLine(entity));
            else lines.add(toLine(e));
        }
        overwriteFile(lines);
    }

    @Override
    public void deleteById(ID id) {
        List<T> all = findAll();
        List<String> lines = new ArrayList<>();
        for (T e : all) {
            if (!extractId(e).equals(id)) lines.add(toLine(e));
        }
        overwriteFile(lines);
    }

    protected void overwriteFile(List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) { }
    }

    public List<T> findWhere(Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T e : findAll()) {
            if (predicate.test(e)) result.add(e);
        }
        return result;
    }
}

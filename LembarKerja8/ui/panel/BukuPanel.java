package ui.panel;

import model.Buku;
import service.BukuService;
import ui.component.UIFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class BukuPanel extends JPanel {
    private final BukuService bukuService;

    public BukuPanel(BukuService bukuService) {
        this.bukuService = bukuService;
        setLayout(new GridLayout(2, 2, 20, 20));
        setBorder(new EmptyBorder(50, 80, 50, 80));
        inisialisasiKomponen();
    }

    private void inisialisasiKomponen() {
        JButton bL = UIFactory.createButton("Lihat Buku",   UIFactory.BTN_BLUE);
        JButton bT = UIFactory.createButton("Tambah Buku",  UIFactory.BTN_GREEN);
        JButton bU = UIFactory.createButton("Update Buku",  UIFactory.BTN_YELLOW);
        JButton bH = UIFactory.createButton("Hapus Buku",   UIFactory.BTN_RED);

        bL.addActionListener(e -> tampilkanSemuaBuku());
        bT.addActionListener(e -> tampilkanFormTambah());
        bU.addActionListener(e -> tampilkanFormUpdate());
        bH.addActionListener(e -> tampilkanFormHapus());

        add(bL); add(bT); add(bU); add(bH);
    }

    private void tampilkanSemuaBuku() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Kode", "Judul", "Jenis", "Status"}, 0);
        List<Buku> list = bukuService.semuaBuku();
        for (Buku b : list) {
            String status = bukuService.isTersedia(b.getKode()) ? "Tersedia" : "Tidak Tersedia";
            model.addRow(new Object[]{b.getKode(), b.getJudul(), b.getJenis(), status});
        }
        JOptionPane.showMessageDialog(this, new JScrollPane(new JTable(model)),
                "Data Buku", JOptionPane.PLAIN_MESSAGE);
    }

    private void tampilkanFormTambah() {
        JTextField tK = UIFactory.createTextField("");
        JTextField tJ = UIFactory.createTextField("");
        JTextField tY = UIFactory.createTextField("");
        Object[] fields = {"Kode:", tK, "Judul:", tJ, "Jenis:", tY};
        if (JOptionPane.showConfirmDialog(this, fields, "Tambah Buku",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            if (tK.getText().trim().isEmpty() || tJ.getText().trim().isEmpty() || tY.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Data harus lengkap!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean berhasil = bukuService.tambahBuku(
                    new Buku(tK.getText().trim(), tJ.getText().trim(), tY.getText().trim()));
            if (berhasil) JOptionPane.showMessageDialog(this, "Buku berhasil ditambahkan!");
            else JOptionPane.showMessageDialog(this, "Kode Buku sudah terdaftar!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tampilkanFormUpdate() {
        String kode = JOptionPane.showInputDialog(this, "Masukkan Kode Buku yang akan diupdate:");
        if (kode == null || kode.trim().isEmpty()) return;
        Optional<Buku> opt = bukuService.cariBuku(kode.trim());
        if (opt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode Buku tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Buku lama = opt.get();
        JTextField tJ = UIFactory.createTextField(lama.getJudul());
        JTextField tY = UIFactory.createTextField(lama.getJenis());
        Object[] fields = {"Kode (Tetap): " + kode.trim(), "Judul Baru:", tJ, "Jenis Baru:", tY};
        if (JOptionPane.showConfirmDialog(this, fields, "Update Buku",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            bukuService.updateBuku(kode.trim(),
                    new Buku(kode.trim(), tJ.getText().trim(), tY.getText().trim()));
            JOptionPane.showMessageDialog(this, "Data Buku berhasil diupdate!");
        }
    }

    private void tampilkanFormHapus() {
        String kode = JOptionPane.showInputDialog(this, "Masukkan Kode Buku yang akan dihapus:");
        if (kode == null || kode.trim().isEmpty()) return;
        Optional<Buku> opt = bukuService.cariBuku(kode.trim());
        if (opt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode Buku tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus buku:\n" + opt.get().getJudul() + " ?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            bukuService.hapusBuku(kode.trim());
            JOptionPane.showMessageDialog(this, "Buku berhasil dihapus!");
        }
    }
}

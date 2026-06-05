package ui.panel;

import model.Siswa;
import service.SiswaService;
import ui.component.UIFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Optional;

public class SiswaPanel extends JPanel {
    private final SiswaService siswaService;

    public SiswaPanel(SiswaService siswaService) {
        this.siswaService = siswaService;
        setLayout(new GridLayout(2, 2, 20, 20));
        setBorder(new EmptyBorder(50, 80, 50, 80));
        inisialisasiKomponen();
    }

    private void inisialisasiKomponen() {
        JButton bL = UIFactory.createButton("Lihat Siswa",   UIFactory.BTN_BLUE);
        JButton bT = UIFactory.createButton("Tambah Siswa",  UIFactory.BTN_GREEN);
        JButton bU = UIFactory.createButton("Update Siswa",  UIFactory.BTN_YELLOW);
        JButton bH = UIFactory.createButton("Hapus Siswa",   UIFactory.BTN_RED);

        bL.addActionListener(e -> tampilkanSemuaSiswa());
        bT.addActionListener(e -> tampilkanFormTambah());
        bU.addActionListener(e -> tampilkanFormUpdate());
        bH.addActionListener(e -> tampilkanFormHapus());

        add(bL); add(bT); add(bU); add(bH);
    }

    private void tampilkanSemuaSiswa() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"NIS", "Nama", "Alamat"}, 0);
        siswaService.semuaSiswa().forEach(s ->
                model.addRow(new Object[]{s.getNis(), s.getNama(), s.getAlamat()}));
        JOptionPane.showMessageDialog(this, new JScrollPane(new JTable(model)),
                "Data Siswa", JOptionPane.PLAIN_MESSAGE);
    }

    private void tampilkanFormTambah() {
        JTextField tNis    = UIFactory.createTextField("");
        JTextField tNama   = UIFactory.createTextField("");
        JTextField tAlamat = UIFactory.createTextField("");
        Object[] fields = {"NIS:", tNis, "Nama:", tNama, "Alamat:", tAlamat};
        if (JOptionPane.showConfirmDialog(this, fields, "Tambah Siswa",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            if (tNis.getText().trim().isEmpty() || tNama.getText().trim().isEmpty() || tAlamat.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Data harus lengkap!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean berhasil = siswaService.tambahSiswa(
                    new Siswa(tNis.getText().trim(), tNama.getText().trim(), tAlamat.getText().trim()));
            if (berhasil) JOptionPane.showMessageDialog(this, "Siswa berhasil ditambahkan!");
            else JOptionPane.showMessageDialog(this, "NIS Siswa sudah terdaftar!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tampilkanFormUpdate() {
        String nis = JOptionPane.showInputDialog(this, "Masukkan NIS Siswa yang akan diupdate:");
        if (nis == null || nis.trim().isEmpty()) return;
        Optional<Siswa> opt = siswaService.cariSiswa(nis.trim());
        if (opt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIS Siswa tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Siswa lama = opt.get();
        JTextField tNama   = UIFactory.createTextField(lama.getNama());
        JTextField tAlamat = UIFactory.createTextField(lama.getAlamat());
        Object[] fields = {"NIS (Tetap): " + nis.trim(), "Nama Baru:", tNama, "Alamat Baru:", tAlamat};
        if (JOptionPane.showConfirmDialog(this, fields, "Update Siswa",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            siswaService.updateSiswa(nis.trim(),
                    new Siswa(nis.trim(), tNama.getText().trim(), tAlamat.getText().trim()));
            JOptionPane.showMessageDialog(this, "Data Siswa berhasil diupdate!");
        }
    }

    private void tampilkanFormHapus() {
        String nis = JOptionPane.showInputDialog(this, "Masukkan NIS Siswa yang akan dihapus:");
        if (nis == null || nis.trim().isEmpty()) return;
        Optional<Siswa> opt = siswaService.cariSiswa(nis.trim());
        if (opt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIS Siswa tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus siswa:\n" + opt.get().getNama() + " ?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            siswaService.hapusSiswa(nis.trim());
            JOptionPane.showMessageDialog(this, "Siswa berhasil dihapus!");
        }
    }
}
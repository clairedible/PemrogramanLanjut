package ui.panel;

import service.TransaksiService;
import ui.component.UIFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TransaksiPanel extends JPanel {
    private final TransaksiService transaksiService;
    private final Runnable onTransaksiChanged;

    public TransaksiPanel(TransaksiService transaksiService, Runnable onTransaksiChanged) {
        this.transaksiService = transaksiService;
        this.onTransaksiChanged = onTransaksiChanged;
        setLayout(new GridLayout(1, 2, 30, 0));
        setBorder(new EmptyBorder(150, 50, 150, 50));
        inisialisasiKomponen();
    }

    private void inisialisasiKomponen() {
        JButton bP = UIFactory.createButton("Peminjaman Buku",   UIFactory.BTN_BLUE);
        JButton bK = UIFactory.createButton("Pengembalian Buku", UIFactory.BTN_GREEN);
        bP.addActionListener(e -> tampilkanFormPinjam());
        bK.addActionListener(e -> tampilkanFormKembali());
        add(bP); add(bK);
    }

    private void tampilkanFormPinjam() {
        JTextField tN      = UIFactory.createTextField("");
        JTextField tB      = UIFactory.createTextField("");
        JTextField tDurasi = UIFactory.createTextField("7");
        Object[] fields = {"NIS:", tN, "Kode Buku:", tB, "Lama Pinjam (Hari):", tDurasi};
        if (JOptionPane.showConfirmDialog(this, fields, "Pinjam Buku",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            int durasi = 7;
            try { durasi = Integer.parseInt(tDurasi.getText().trim()); } catch (NumberFormatException ignored) { }
            TransaksiService.HasilPinjam hasil = transaksiService.pinjamBuku(
                    tN.getText().trim(), tB.getText().trim(), durasi);
            switch (hasil) {
                case BERHASIL:
                    onTransaksiChanged.run();
                    JOptionPane.showMessageDialog(this, "Peminjaman Berhasil!");
                    break;
                case NIS_TIDAK_ADA:
                case BUKU_TIDAK_ADA:
                    JOptionPane.showMessageDialog(this, "NIS atau Kode Buku tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case BATAS_PINJAM_TERCAPAI:
                    JOptionPane.showMessageDialog(this, "Meminjam buku maksimal 2, tidak boleh lebih!", "Batas Peminjaman", JOptionPane.ERROR_MESSAGE);
                    break;
                case BUKU_TIDAK_TERSEDIA:
                    JOptionPane.showMessageDialog(this, "Buku tidak tersedia, sudah dipinjam!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    break;
            }
        }
    }

    private void tampilkanFormKembali() {
        String trx = JOptionPane.showInputDialog(this, "Masukkan Kode TRX:");
        if (trx == null || trx.trim().isEmpty()) return;
        TransaksiService.HasilKembali hasil = transaksiService.kembalikanBuku(trx.trim());
        if (hasil == TransaksiService.HasilKembali.BERHASIL) {
            onTransaksiChanged.run();
            JOptionPane.showMessageDialog(this, "Pengembalian Buku Sukses!");
        } else {
            JOptionPane.showMessageDialog(this, "Kode TRX tidak ditemukan atau sudah dikembalikan!");
        }
    }
}

package ui.panel;

import model.Transaksi;
import service.TransaksiService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LaporanPanel extends JPanel {
    private final TransaksiService transaksiService;
    private final DefaultTableModel tableModel;

    public LaporanPanel(TransaksiService transaksiService) {
        this.transaksiService = transaksiService;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        tableModel = new DefaultTableModel(
                new String[]{"TRX", "NIS", "Buku", "Tgl Pinjam", "Batas Kembali", "Status"}, 0);
        add(new JLabel("Histori & Daftar Peminjaman", SwingConstants.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(new JTable(tableModel)), BorderLayout.CENTER);
        refresh();
    }

    public void refresh() {
        tableModel.setRowCount(0);
        for (Transaksi t : transaksiService.semuaTransaksi()) {
            tableModel.addRow(new Object[]{
                    t.getKodeTrx(), t.getNis(), t.getKodeBuku(),
                    t.getTanggalPinjam(), t.getTanggalKembali(), t.getStatusText()
            });
        }
    }
}
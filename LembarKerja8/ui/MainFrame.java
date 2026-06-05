package ui;


import model.Pegawai;
import service.AuthService;
import service.BukuService;
import service.SiswaService;
import service.TransaksiService;
import ui.component.GradientPanel;
import ui.panel.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    private final BukuService bukuService;
    private final SiswaService siswaService;
    private final TransaksiService transaksiService;
    private final Pegawai pegawaiAktif;

    public MainFrame(AuthService authService, BukuService bukuService,
                     SiswaService siswaService, TransaksiService transaksiService) {
        this.bukuService = bukuService;
        this.siswaService = siswaService;
        this.transaksiService = transaksiService;
        this.pegawaiAktif = authService.getPegawaiAktif();
    }

    public void tampilkan() {
        setTitle("Sistem Perpustakaan SMP");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buatHeader(), BorderLayout.NORTH);
        add(buatTabbedPane(), BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel buatHeader() {
        GradientPanel header = new GradientPanel(
                new Color(255, 192, 203), new Color(216, 191, 216));
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel lblApp = new JLabel("SISTEM PERPUSTAKAAN");
        lblApp.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblApp.setForeground(Color.WHITE);
        JLabel lblSub = new JLabel("   Manajemen Data & Transaksi Digital");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSub.setForeground(new Color(230, 240, 255));
        titlePanel.add(lblApp);
        titlePanel.add(lblSub);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        userPanel.setOpaque(false);
        JLabel lblUser = new JLabel("Petugas: " + pegawaiAktif.getNama());
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUser.setForeground(Color.WHITE);
        userPanel.add(lblUser);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(userPanel, BorderLayout.EAST);
        return header;
    }

    private JTabbedPane buatTabbedPane() {
        LaporanPanel laporanPanel = new LaporanPanel(transaksiService);
        Runnable onTransaksiChanged = laporanPanel::refresh;

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabs.addTab("   Kelola Buku   ",   new BukuPanel(bukuService));
        tabs.addTab("   Kelola Siswa   ",  new SiswaPanel(siswaService));
        tabs.addTab("   Transaksi   ",     new TransaksiPanel(transaksiService, onTransaksiChanged));
        tabs.addTab("   Laporan   ",       laporanPanel);
        return tabs;
    }
}

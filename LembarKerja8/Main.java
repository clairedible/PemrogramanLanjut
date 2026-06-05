import repository.*;
import service.*;
import ui.MainFrame;
import ui.panel.LoginPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    private static final String DIR_PATH        = System.getProperty("user.dir") + "/data_perpustakaan";
    private static final String FILE_PEGAWAI    = DIR_PATH + "/pegawai.txt";
    private static final String FILE_SISWA      = DIR_PATH + "/siswa.txt";
    private static final String FILE_BUKU       = DIR_PATH + "/buku.txt";
    private static final String FILE_TRANSAKSI  = DIR_PATH + "/transaksi.txt";

    public static void main(String[] args) {
        inisialisasiFile();

        PegawaiRepository  pegawaiRepo   = new PegawaiRepository(FILE_PEGAWAI);
        BukuRepository     bukuRepo      = new BukuRepository(FILE_BUKU);
        SiswaRepository    siswaRepo     = new SiswaRepository(FILE_SISWA);
        TransaksiRepository transaksiRepo = new TransaksiRepository(FILE_TRANSAKSI);

        AuthService      authService      = new AuthService(pegawaiRepo);
        BukuService      bukuService      = new BukuService(bukuRepo, transaksiRepo);
        SiswaService     siswaService     = new SiswaService(siswaRepo);
        TransaksiService transaksiService = new TransaksiService(transaksiRepo, siswaRepo, bukuRepo);

        UIManager.put("OptionPane.background", new Color(255, 245, 248));
        UIManager.put("Panel.background",      new Color(255, 245, 248));

        SwingUtilities.invokeLater(() -> {
            LoginPanel loginPanel = new LoginPanel(authService, () -> {
                MainFrame frame = new MainFrame(authService, bukuService, siswaService, transaksiService);
                frame.tampilkan();
            });
            loginPanel.tampilkan();
        });
    }

    private static void inisialisasiFile() {
        try {
            File folder = new File(DIR_PATH);
            if (!folder.exists()) folder.mkdirs();
            new File(FILE_PEGAWAI).createNewFile();
            new File(FILE_SISWA).createNewFile();
            new File(FILE_BUKU).createNewFile();
            new File(FILE_TRANSAKSI).createNewFile();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Gagal inisialisasi file: " + e.getMessage());
        }
    }
}
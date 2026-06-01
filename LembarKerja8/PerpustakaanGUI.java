import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Date; 
import java.text.SimpleDateFormat; 

class RoundedButton extends JButton {
    private int radius;
    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g2);
        g2.dispose();
    }
}

class DuplicateDataException extends Exception {
    public DuplicateDataException(String message) {
        super(message);
    }
}

public class PerpustakaanGUI extends JFrame {
    private static final String DIR_PATH = System.getProperty("user.dir") + "/data_perpustakaan"; 
    private static final String FILE_PEGAWAI = DIR_PATH + "/pegawai.txt";
    private static final String FILE_SISWA = DIR_PATH + "/siswa.txt";
    private static final String FILE_BUKU = DIR_PATH + "/buku.txt";
    private static final String FILE_TRANSAKSI = DIR_PATH + "/transaksi.txt";

    private String loggedInPegawai = null;
    private JTabbedPane tabbedPane;
    private DefaultTableModel modelLaporan;

    private final Color bgUtama = new Color(255, 245, 248);
    private final Color teksJudul = new Color(94, 84, 142);
    private final Color bgTabelHeader = new Color(255, 212, 228);
    private final Color btnBlue = new Color(189, 224, 254);    
    private final Color btnGreen = new Color(204, 213, 174);   
    private final Color btnYellow = new Color(254, 250, 224);  
    private final Color btnRed = new Color(250, 210, 225);     
    private final Color COLOR_PRIMARY = new Color(205, 180, 219); 

    private final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 15);

    public PerpustakaanGUI() {
        UIManager.put("OptionPane.background", bgUtama);
        UIManager.put("Panel.background", bgUtama);
        inisialisasiFile();
        tampilkanLogin();
    }

    private void inisialisasiFile() {
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

    private JTextField createBigTextField(String defaultText) {
        JTextField tf = new JTextField(defaultText);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tf.setPreferredSize(new Dimension(280, 35));
        return tf;
    }

    private JPasswordField createBigPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pf.setPreferredSize(new Dimension(280, 35));
        return pf;
    }

    private JButton styleButton(String text, Color bgColor) {
        RoundedButton button = new RoundedButton(text, 40); 
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(new Color(80, 80, 80));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth(); int h = getHeight();
            Color color1 = new Color(255, 192, 203); 
            Color color2 = new Color(216, 191, 216); 
            GradientPaint gp = new GradientPaint(0, 0, color1, w, 0, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    private void tampilkanLogin() {
        JDialog loginDialog = new JDialog(this, "Login Sistem Perpustakaan", true);
        loginDialog.setSize(420, 320); 
        loginDialog.setLocationRelativeTo(null);
        loginDialog.setLayout(new BorderLayout());

        GradientPanel pnlHead = new GradientPanel();
        pnlHead.setLayout(new BorderLayout());
        pnlHead.setBorder(new EmptyBorder(15, 10, 15, 10));
        JLabel lblTitle = new JLabel("LOGIN PETUGAS", SwingConstants.CENTER);
        lblTitle.setFont(FONT_HEADER); lblTitle.setForeground(Color.WHITE);
        pnlHead.add(lblTitle, BorderLayout.CENTER);
        loginDialog.add(pnlHead, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel(new GridLayout(2, 2, 10, 20));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(new EmptyBorder(20, 20, 10, 20));
        pnlForm.add(new JLabel("NIP:")); JTextField txtNip = createBigTextField(""); pnlForm.add(txtNip);
        pnlForm.add(new JLabel("Password:")); JPasswordField txtPass = createBigPasswordField(); pnlForm.add(txtPass);
        loginDialog.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlBtn.setBackground(Color.WHITE);
        
        JButton btnLogin = new RoundedButton("Login", 20); 
        btnLogin.setBackground(COLOR_PRIMARY); 
        btnLogin.setForeground(Color.WHITE);
        
        JButton btnRegister = new RoundedButton("Buat Akun", 20); 
        btnRegister.setBackground(new Color(255, 200, 221)); 
        btnRegister.setForeground(Color.WHITE);
        
        pnlBtn.add(btnLogin); pnlBtn.add(btnRegister);
        loginDialog.add(pnlBtn, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> {
            String nip = txtNip.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();
            if (nip.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(loginDialog, "Silakan masukkan NIP dan Password Anda!");
                return;
            }
            int statusLogin = prosesLogin(nip, pass);
            if (statusLogin == 1) {
                loginDialog.dispose(); bangunAntarmukaUtama();
            } else if (statusLogin == 0) {
                JOptionPane.showMessageDialog(loginDialog, "Data anda tidak ditemukan, silahkan buat akun terlebih dahulu.");
            } else { 
                JOptionPane.showMessageDialog(loginDialog, "NIP/Password Salah!"); 
            }
        });

        btnRegister.addActionListener(e -> tampilkanRegister(loginDialog));
        loginDialog.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); } });
        loginDialog.setVisible(true);
    }

    private void tampilkanRegister(JDialog parent) {
        JDialog regDialog = new JDialog(parent, "Registrasi", true);
        regDialog.setSize(400, 350); regDialog.setLocationRelativeTo(parent);
        regDialog.setLayout(new BorderLayout());
        
        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 5, 10));
        pnlForm.setBorder(new EmptyBorder(20,20,20,20));
        JTextField tNip = createBigTextField(""); JTextField tNama = createBigTextField(""); 
        
        JSpinner tTglLahir = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor de = new JSpinner.DateEditor(tTglLahir, "dd-MM-yyyy");
        tTglLahir.setEditor(de);
        tTglLahir.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tTglLahir.setPreferredSize(new Dimension(280, 35));
        
        JPasswordField tPass = createBigPasswordField();
        
        pnlForm.add(new JLabel("NIP:")); pnlForm.add(tNip);
        pnlForm.add(new JLabel("Nama:")); pnlForm.add(tNama);
        pnlForm.add(new JLabel("Tanggal Lahir:")); pnlForm.add(tTglLahir); 
        pnlForm.add(new JLabel("Password:")); pnlForm.add(tPass);
        
        JButton btnS = new JButton("Simpan");
        JButton btnCancel = new JButton("Cancel");
        
        btnS.addActionListener(e -> {
            String nip = tNip.getText().trim();
            String nama = tNama.getText().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String tglLahir = sdf.format(tTglLahir.getValue());
            String pass = new String(tPass.getPassword()).trim();
            if (nip.isEmpty() || nama.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(regDialog, "Silakan lengkapi semua form pendaftaran Anda!");
                return;
            }
            tulisKeFile(FILE_PEGAWAI, nip + "," + nama + "," + pass + "," + tglLahir);
            regDialog.dispose();
        });
        
        btnCancel.addActionListener(e -> regDialog.dispose());
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlSouth.add(btnS); pnlSouth.add(btnCancel);
        
        regDialog.add(pnlForm, BorderLayout.CENTER); 
        regDialog.add(pnlSouth, BorderLayout.SOUTH);
        regDialog.setVisible(true);
    }

    private int prosesLogin(String nip, String password) {
        boolean adaDataPegawai = false;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PEGAWAI))) {
            String b; while ((b = br.readLine()) != null) {
                if (!b.trim().isEmpty()) {
                    adaDataPegawai = true; 
                    String[] d = b.split(",");
                    if (d[0].equals(nip) && d[2].equals(password)) { loggedInPegawai = d[1]; return 1; }
                }
            }
        } catch (IOException e) {}
        return adaDataPegawai ? -1 : 0;
    }

    private void bangunAntarmukaUtama() {
        setTitle("Sistem Perpustakaan SMP");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        GradientPanel headerPanel = new GradientPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel lblApp = new JLabel("SISTEM PERPUSTAKAAN");
        lblApp.setFont(new Font("Segoe UI", Font.BOLD, 26)); lblApp.setForeground(Color.WHITE);
        JLabel lblSub = new JLabel("   Manajemen Data & Transaksi Digital");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 15)); lblSub.setForeground(new Color(230, 240, 255));
        titlePanel.add(lblApp); titlePanel.add(lblSub);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        userPanel.setOpaque(false);
        JLabel lblUser = new JLabel("Petugas: " + loggedInPegawai);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 16)); lblUser.setForeground(Color.WHITE);
        userPanel.add(lblUser);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabbedPane.addTab("   Kelola Buku   ", panelDashboardBuku());
        tabbedPane.addTab("   Kelola Siswa   ", panelDashboardSiswa());
        tabbedPane.addTab("   Transaksi   ", panelDashboardTransaksi());
        tabbedPane.addTab("   Laporan   ", panelLaporan());

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel panelDashboardBuku() {
        JPanel p = new JPanel(new GridLayout(2, 2, 20, 20));
        p.setBorder(new EmptyBorder(50, 80, 50, 80));
        JButton bL = styleButton("Lihat Buku", btnBlue);
        JButton bT = styleButton("Tambah Buku", btnGreen);
        JButton bU = styleButton("Update Buku", btnYellow);
        JButton bH = styleButton("Hapus Buku", btnRed);
        bL.addActionListener(e -> dialogLihatData("Buku", FILE_BUKU, new String[]{"Kode", "Judul", "Jenis", "Status"}));
        bT.addActionListener(e -> dialogTambahBuku());
        bU.addActionListener(e -> dialogUpdateBuku());
        bH.addActionListener(e -> dialogHapusDataBuku());
        p.add(bL); p.add(bT); p.add(bU); p.add(bH);
        return p;
    }

    private void dialogTambahBuku() {
        JTextField tK = createBigTextField(""); JTextField tJ = createBigTextField(""); JTextField tY = createBigTextField(""); 
        Object[] f = {"Kode:", tK, "Judul:", tJ, "Jenis:", tY};
        if(JOptionPane.showConfirmDialog(this, f, "Tambah Buku", JOptionPane.OK_CANCEL_OPTION) == 0) {
            // --- PERUBAHAN 1: Validasi Data Lengkap ---
            if (tK.getText().trim().isEmpty() || tJ.getText().trim().isEmpty() || tY.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Data harus lengkap! Judul dan Jenis tidak boleh kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (cekDataAda(FILE_BUKU, 0, tK.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Kode Buku sudah terdaftar!", "Error", JOptionPane.ERROR_MESSAGE); return;
            }
            tulisKeFile(FILE_BUKU, tK.getText() + "," + tJ.getText() + "," + tY.getText());
            JOptionPane.showMessageDialog(this, "Buku berhasil ditambahkan!");
        }
    }

    private void dialogUpdateBuku() {
        String kode = JOptionPane.showInputDialog(this, "Masukkan Kode Buku yang akan diupdate:");
        if (kode != null && !kode.trim().isEmpty()) {
            String[] dataLama = ambilSatuData(FILE_BUKU, 0, kode.trim());
            if (dataLama != null) {
                JTextField tJ = createBigTextField(dataLama[1]);
                JTextField tY = createBigTextField(dataLama.length > 2 ? dataLama[2] : "");
                Object[] f = {"Kode (Tetap): " + kode.trim(), "Judul Baru:", tJ, "Jenis Baru:", tY};
                if (JOptionPane.showConfirmDialog(this, f, "Update Buku", JOptionPane.OK_CANCEL_OPTION) == 0) {
                    updateBarisDiFile(FILE_BUKU, kode.trim(), 0, kode.trim() + "," + tJ.getText() + "," + tY.getText());
                    JOptionPane.showMessageDialog(this, "Data Buku berhasil diupdate!");
                }
            } else { JOptionPane.showMessageDialog(this, "Kode Buku tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void dialogHapusDataBuku() {
        String kode = JOptionPane.showInputDialog(this, "Masukkan Kode Buku yang akan dihapus:");
        if (kode != null && !kode.trim().isEmpty()) {
            String[] data = ambilSatuData(FILE_BUKU, 0, kode.trim());
            if (data != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus buku:\n" + data[1] + " ?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    hapusBarisDariFile(FILE_BUKU, kode.trim(), 0);
                    JOptionPane.showMessageDialog(this, "Buku berhasil dihapus!");
                }
            } else { JOptionPane.showMessageDialog(this, "Kode Buku tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private JPanel panelDashboardSiswa() {
        JPanel p = new JPanel(new GridLayout(2, 2, 20, 20));
        p.setBorder(new EmptyBorder(50, 80, 50, 80));
        JButton bL = styleButton("Lihat Siswa", btnBlue);
        JButton bT = styleButton("Tambah Siswa", btnGreen);
        JButton bU = styleButton("Update Siswa", btnYellow); 
        JButton bH = styleButton("Hapus Siswa", btnRed);     
        p.add(bL); p.add(bT); p.add(bU); p.add(bH);
        bL.addActionListener(e -> dialogLihatData("Siswa", FILE_SISWA, new String[]{"NIS", "Nama", "Alamat"}));
        bT.addActionListener(e -> dialogTambahSiswa());
        bU.addActionListener(e -> dialogUpdateSiswa());
        bH.addActionListener(e -> dialogHapusDataSiswa());
        return p;
    }

    private void dialogTambahSiswa() {
        JTextField tNis = createBigTextField(""); JTextField tNama = createBigTextField(""); JTextField tAlamat = createBigTextField(""); 
        Object[] f = {"NIS:", tNis, "Nama:", tNama, "Alamat:", tAlamat};
        if(JOptionPane.showConfirmDialog(this, f, "Tambah Siswa", JOptionPane.OK_CANCEL_OPTION) == 0) {
            // --- PERUBAHAN 1: Validasi Data Lengkap ---
            if (tNis.getText().trim().isEmpty() || tNama.getText().trim().isEmpty() || tAlamat.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Data harus lengkap! Nama dan Alamat tidak boleh kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (cekDataAda(FILE_SISWA, 0, tNis.getText().trim())) {
                JOptionPane.showMessageDialog(this, "NIS Siswa sudah terdaftar!", "Error", JOptionPane.ERROR_MESSAGE); return;
            }
            tulisKeFile(FILE_SISWA, tNis.getText() + "," + tNama.getText() + "," + tAlamat.getText());
            JOptionPane.showMessageDialog(this, "Siswa berhasil ditambahkan!");
        }
    }

    private void dialogUpdateSiswa() {
        String nis = JOptionPane.showInputDialog(this, "Masukkan NIS Siswa yang akan diupdate:");
        if (nis != null && !nis.trim().isEmpty()) {
            String[] dataLama = ambilSatuData(FILE_SISWA, 0, nis.trim());
            if (dataLama != null) {
                JTextField tNama = createBigTextField(dataLama[1]);
                JTextField tAlamat = createBigTextField(dataLama.length > 2 ? dataLama[2] : "");
                Object[] f = {"NIS (Tetap): " + nis.trim(), "Nama Baru:", tNama, "Alamat Baru:", tAlamat};
                if (JOptionPane.showConfirmDialog(this, f, "Update Siswa", JOptionPane.OK_CANCEL_OPTION) == 0) {
                    updateBarisDiFile(FILE_SISWA, nis.trim(), 0, nis.trim() + "," + tNama.getText() + "," + tAlamat.getText());
                    JOptionPane.showMessageDialog(this, "Data Siswa berhasil diupdate!");
                }
            } else { JOptionPane.showMessageDialog(this, "NIS Siswa tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void dialogHapusDataSiswa() {
        String nis = JOptionPane.showInputDialog(this, "Masukkan NIS Siswa yang akan dihapus:");
        if (nis != null && !nis.trim().isEmpty()) {
            String[] data = ambilSatuData(FILE_SISWA, 0, nis.trim());
            if (data != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus siswa:\n" + data[1] + " ?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    hapusBarisDariFile(FILE_SISWA, nis.trim(), 0);
                    JOptionPane.showMessageDialog(this, "Siswa berhasil dihapus!");
                }
            } else { JOptionPane.showMessageDialog(this, "NIS Siswa tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void dialogLihatData(String j, String f, String[] k) {
        DefaultTableModel m = new DefaultTableModel(k, 0);
        JTable t = new JTable(m); muatDataKeTabel(f, m);
        JOptionPane.showMessageDialog(this, new JScrollPane(t), "Data "+j, JOptionPane.PLAIN_MESSAGE);
    }

    private JPanel panelDashboardTransaksi() {
        JPanel p = new JPanel(new GridLayout(1, 2, 30, 0));
        p.setBorder(new EmptyBorder(150, 50, 150, 50));
        JButton bP = styleButton("Peminjaman Buku", btnBlue);
        JButton bK = styleButton("Pengembalian Buku", btnGreen);
        bP.addActionListener(e -> dialogPinjamBuku());
        bK.addActionListener(e -> dialogKembaliBuku());
        p.add(bP); p.add(bK);
        return p;
    }

    private String generateKodeTrxUrut() {
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_TRANSAKSI))) {
            String baris; while ((baris = br.readLine()) != null) {
                String[] data = baris.split(",");
                if (data.length > 0 && data[0].startsWith("TRX-")) {
                    try {
                        int id = Integer.parseInt(data[0].substring(4));
                        if (id > maxId) maxId = id;
                    } catch (NumberFormatException ex) {}
                }
            }
        } catch (IOException e) {}
        return String.format("TRX-%02d", maxId + 1);
    }

    // --- PERUBAHAN 2: Fungsi Hitung Peminjaman Aktif ---
    private int hitungBukuDipinjamSiswa(String nis) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_TRANSAKSI))) {
            String b; while ((b = br.readLine()) != null) {
                String[] d = b.split(",");
                if (d.length >= 6 && d[1].equals(nis) && d[5].equals("0")) count++;
            }
        } catch (IOException e) {}
        return count;
    }

    private void dialogPinjamBuku() {
        JTextField tN = createBigTextField(""); JTextField tB = createBigTextField("");
        JTextField tDurasi = createBigTextField("7"); 
        Object[] f = {"NIS:", tN, "Kode Buku:", tB, "Lama Pinjam (Hari):", tDurasi};
        if(JOptionPane.showConfirmDialog(this, f, "Pinjam Buku", JOptionPane.OK_CANCEL_OPTION) == 0) {
            String nis = tN.getText().trim(); String kodeBuku = tB.getText().trim();
            if(!cekDataAda(FILE_SISWA, 0, nis) || !cekDataAda(FILE_BUKU, 0, kodeBuku)) {
                JOptionPane.showMessageDialog(this, "NIS atau Kode Buku tidak ditemukan di data utama!", "Error", JOptionPane.ERROR_MESSAGE); return;
            }
            // --- PERUBAHAN 2: Limit Pinjam 2 Buku ---
            if (hitungBukuDipinjamSiswa(nis) >= 2) {
                JOptionPane.showMessageDialog(this, "Meminjam buku maksimal 2 tidak boleh lebih!", "Batas Peminjaman", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (isBukuSedangDipinjam(kodeBuku)) {
                JOptionPane.showMessageDialog(this, "Buku tidak tersedia, sudah dipinjam!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int durasi = 7; try { durasi = Integer.parseInt(tDurasi.getText().trim()); } catch (NumberFormatException e) {}
            LocalDate tglPinjam = LocalDate.now();
            LocalDate tglKembali = tglPinjam.plusDays(durasi);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String kTrx = generateKodeTrxUrut(); 
            tulisKeFile(FILE_TRANSAKSI, kTrx + "," + nis + "," + kodeBuku + "," + tglPinjam.format(fmt) + "," + tglKembali.format(fmt) + ",0");
            refreshTabelLaporan(); JOptionPane.showMessageDialog(this, "Peminjaman Berhasil! Kode: " + kTrx);
        }
    }

    private void dialogKembaliBuku() {
        String trx = JOptionPane.showInputDialog("Masukkan Kode TRX:");
        if(trx != null && !trx.trim().isEmpty() && prosesPengembalian(trx)) {
            refreshTabelLaporan(); JOptionPane.showMessageDialog(this, "Pengembalian Buku Sukses!");
        } else if (trx != null) { JOptionPane.showMessageDialog(this, "Kode TRX tidak ditemukan atau sudah dikembalikan!"); }
    }

    private JPanel panelLaporan() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(15, 15, 15, 15));
        p.add(new JLabel("Histori & Daftar Peminjaman", SwingConstants.CENTER), BorderLayout.NORTH);
        // --- PERUBAHAN 3: Tambah Kolom Status di Tabel ---
        modelLaporan = new DefaultTableModel(new String[]{"TRX", "NIS", "Buku", "Tgl Pinjam", "Batas Kembali", "Status"}, 0);
        p.add(new JScrollPane(new JTable(modelLaporan)), BorderLayout.CENTER);
        refreshTabelLaporan();
        return p;
    }

    private void refreshTabelLaporan() {
        if (modelLaporan == null) return;
        modelLaporan.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_TRANSAKSI))) {
            String b; while ((b = br.readLine()) != null) {
                String[] d = b.split(",");
                if (d.length >= 6) {
                    // --- PERUBAHAN 3: Konversi Angka Status ke Teks ---
                    String statusText = d[5].equals("0") ? "Belum Dikembalikan" : "Sudah Dikembalikan";
                    modelLaporan.addRow(new Object[]{d[0], d[1], d[2], d[3], d[4], statusText});
                }
            }
        } catch (IOException e) {}
    }

    private void tulisKeFile(String f, String d) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, true))) {
            bw.write(d); bw.newLine();
        } catch (IOException e) {}
    }

    private void muatDataKeTabel(String f, DefaultTableModel m) {
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String b; while ((b = br.readLine()) != null) {
                String[] data = b.split(",");
                if (f.equals(FILE_BUKU)) {
                    String status = isBukuSedangDipinjam(data[0]) ? "Tidak Tersedia" : "Tersedia";
                    Object[] rowWithStatus = new Object[data.length + 1];
                    System.arraycopy(data, 0, rowWithStatus, 0, data.length);
                    rowWithStatus[data.length] = status;
                    m.addRow(rowWithStatus);
                } else {
                    m.addRow(data);
                }
            }
        } catch (IOException e) {}
    }

    private boolean isBukuSedangDipinjam(String kodeBuku) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_TRANSAKSI))) {
            String b; while ((b = br.readLine()) != null) {
                String[] d = b.split(",");
                if (d.length >= 6 && d[2].equals(kodeBuku) && d[5].equals("0")) return true;
            }
        } catch (IOException e) {}
        return false;
    }

    private boolean cekDataAda(String namaFile, int indexKolom, String target) {
        try (BufferedReader br = new BufferedReader(new FileReader(namaFile))) {
            String baris; while ((baris = br.readLine()) != null) {
                if (baris.split(",")[indexKolom].equals(target)) return true;
            }
        } catch (IOException e) {} 
        return false;
    }

    private String[] ambilSatuData(String namaFile, int indexKolom, String target) {
        try (BufferedReader br = new BufferedReader(new FileReader(namaFile))) {
            String baris; while ((baris = br.readLine()) != null) {
                String[] data = baris.split(",");
                if (data.length > indexKolom && data[indexKolom].equals(target)) return data;
            }
        } catch (IOException e) {} 
        return null;
    }

    private void updateBarisDiFile(String namaFile, String targetId, int indexKolom, String dataBaru) {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(namaFile))) {
            String baris; while ((baris = br.readLine()) != null) {
                if (baris.split(",")[indexKolom].equals(targetId)) list.add(dataBaru);
                else list.add(baris);
            }
        } catch (IOException e) {}
        tulisUlangFile(namaFile, list);
    }

    private void hapusBarisDariFile(String namaFile, String targetId, int indexKolom) {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(namaFile))) {
            String baris; while ((baris = br.readLine()) != null) {
                if (!baris.split(",")[indexKolom].equals(targetId)) list.add(baris);
            }
        } catch (IOException e) {}
        tulisUlangFile(namaFile, list);
    }

    private void tulisUlangFile(String namaFile, List<String> baris) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(namaFile))) {
            for (String b : baris) { bw.write(b); bw.newLine(); }
        } catch (IOException e) {}
    }

    private boolean prosesPengembalian(String kTrx) {
        List<String> list = new ArrayList<>(); boolean ok = false;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_TRANSAKSI))) {
            String b; while ((b = br.readLine()) != null) {
                String[] d = b.split(",");
                // --- PERUBAHAN 3: Ubah status dari 0 ke 1, bukan hapus baris ---
                if (d[0].equalsIgnoreCase(kTrx) && d[5].equals("0")) { 
                    d[5] = "1";
                    list.add(String.join(",", d));
                    ok = true; 
                } else { 
                    list.add(b); 
                }
            }
        } catch (IOException e) { return false; }
        if(ok) {
            tulisUlangFile(FILE_TRANSAKSI, list);
        }
        return ok;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PerpustakaanGUI());
    }
}
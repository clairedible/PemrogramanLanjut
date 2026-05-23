import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;


public class Gui extends JFrame {

    private final SiswaService siswaService;
    private DefaultTableModel tableModel;

    private final Color bgUtama      = new Color(240, 248, 255);
    private final Color teksJudul    = new Color(25, 25, 112);
    private final Color bgTombol     = new Color(135, 206, 235);
    private final Color bgTabelHeader= new Color(173, 216, 230);

    public Gui(SiswaService siswaService) {
        this.siswaService = siswaService;

        UIManager.put("OptionPane.background", bgUtama);
        UIManager.put("Panel.background", bgUtama);

        initFrame();
        initTableModel();
        inisialisasiData();  // muat data + tampilkan dialog jika file belum ada
    }

    private void initFrame() {
        setTitle("Menu Utama - Perpustakaan SMP");
        setSize(480, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bgUtama);

        add(buildPanelAtas(),  BorderLayout.NORTH);
        add(buildPanelMenu(),  BorderLayout.CENTER);
    }

    private JPanel buildPanelAtas() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBackground(bgUtama);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel lblWelcome = new JLabel(
            "✨ Selamat Datang di Sistem Perpustakaan! ✨", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblWelcome.setForeground(teksJudul);

        JLabel lblSub = new JLabel(
            "Silakan pilih menu di bawah ini:", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(teksJudul);

        panel.add(lblWelcome);
        panel.add(lblSub);
        return panel;
    }

    private JPanel buildPanelMenu() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15));
        panel.setBackground(bgUtama);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 40, 30));

        JButton btnLihat  = styleButton("📄 Lihat Semua Data");
        JButton btnCreate = styleButton("➕ Tambah Data Baru");
        JButton btnUpdate = styleButton("✏️ Update Data");
        JButton btnDelete = styleButton("🗑️ Hapus Data");

        // Warna khusus per tombol (sama dengan aslinya)
        btnCreate.setBackground(new Color(153, 255, 153));
        btnUpdate.setBackground(new Color(255, 255, 153));
        btnDelete.setBackground(new Color(255, 153, 153));

        panel.add(btnLihat);
        panel.add(btnCreate);
        panel.add(btnUpdate);
        panel.add(btnDelete);

        btnLihat .addActionListener(e -> showDataDialog());
        btnCreate.addActionListener(e -> showCreateDialog());
        btnUpdate.addActionListener(e -> showUpdateDialog());
        btnDelete.addActionListener(e -> showDeleteDialog());

        return panel;
    }

    private JButton styleButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgTombol);
        button.setForeground(Color.BLACK);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void initTableModel() {
        String[] kolom = { "NIS", "Nama Siswa", "Alamat" };
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    /**
     * Memuat data dari service ke tabel.
     * Jika file belum ada, tampilkan dialog konfirmasi terlebih dahulu.
     */
    private void inisialisasiData() {
        // Jika file belum tersedia, tanya pengguna
        if (!siswaService.isStorageAvailable()) {
            int pilihan = JOptionPane.showConfirmDialog(
                this,
                "File data (siswa.csv) belum ditemukan.\n" +
                "Sistem akan membuat file baru secara otomatis saat Anda menyimpan data nanti.\n\n" +
                "Tetap masuk ke sistem?",
                "Informasi File",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
            );
            if (pilihan != JOptionPane.YES_OPTION) {
                System.exit(0);
            }
            return; 
        }

        try {
            siswaService.loadData();
            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this, "Gagal membaca file: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * untuk menyinkronkan isi tableModel dengan data terkini dari service.
     * Dipanggil setiap kali data berubah (tambah / update / hapus).
     */
    private void refreshTable() {
        tableModel.setRowCount(0); // Kosongkan tabel
        List<Siswa> daftarSiswa = siswaService.getAll();
        for (Siswa s : daftarSiswa) {
            tableModel.addRow(new Object[]{ s.getNis(), s.getNama(), s.getAlamat() });
        }
    }

    /** Menampilkan seluruh data siswa dalam dialog tabel. */
    private void showDataDialog() {
        JTable tabelSiswa = new JTable(tableModel);
        tabelSiswa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelSiswa.setRowHeight(25);
        tabelSiswa.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabelSiswa.getTableHeader().setBackground(bgTabelHeader);

        JScrollPane scrollPane = new JScrollPane(tabelSiswa);
        scrollPane.setPreferredSize(new Dimension(500, 250));

        JOptionPane.showMessageDialog(
            this, scrollPane, "Data Semua Siswa", JOptionPane.PLAIN_MESSAGE);
    }

    // untuk menambah data siswa baru
    private void showCreateDialog() {
        JTextField txtNis    = new JTextField();
        JTextField txtNama   = new JTextField();
        JTextField txtAlamat = new JTextField();

        Object[] formFields = {
            "📌 Masukkan NIS:", txtNis,
            "👤 Masukkan Nama Siswa:", txtNama,
            "🏠 Masukkan Alamat:", txtAlamat
        };

        int option = JOptionPane.showConfirmDialog(
            this, formFields, "Tambah Data Siswa",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option != JOptionPane.OK_OPTION) return;

        String nis    = txtNis.getText().trim();
        String nama   = txtNama.getText().trim();
        String alamat = txtAlamat.getText().trim();

        if (nis.isEmpty() || nama.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            siswaService.tambahSiswa(nis, nama, alamat);
            refreshTable();
            JOptionPane.showMessageDialog(
                this, "Data berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (DuplicateNisException ex) {
            JOptionPane.showMessageDialog(
                this, ex.getMessage(), "Kesalahan Input", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, "Gagal menyimpan data: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //untuk memperbarui data siswa berdasarkan NIS
    private void showUpdateDialog() {
        String inputNis = JOptionPane.showInputDialog(
            this, "Masukkan NIS siswa yang ingin di-update:");
        if (inputNis == null || inputNis.trim().isEmpty()) return;

        Optional<Siswa> opt = siswaService.findByNis(inputNis.trim());
        if (opt.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Data dengan NIS " + inputNis + " tidak ditemukan!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Siswa siswa = opt.get();
        JTextField txtNama   = new JTextField(siswa.getNama());
        JTextField txtAlamat = new JTextField(siswa.getAlamat());

        Object[] formFields = {
            "📌 NIS: " + siswa.getNis() + " (Tidak dapat diubah)",
            "👤 Nama Siswa Baru:", txtNama,
            "🏠 Alamat Baru:", txtAlamat
        };

        int option = JOptionPane.showConfirmDialog(
            this, formFields, "Update Data",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option != JOptionPane.OK_OPTION) return;

        try {
            siswaService.updateSiswa(
                siswa.getNis(),
                txtNama.getText().trim(),
                txtAlamat.getText().trim());
            refreshTable();
            JOptionPane.showMessageDialog(
                this, "Data berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, "Gagal memperbarui data: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // untuk menghapus data siswa berdasarkan NIS
    private void showDeleteDialog() {
        String inputNis = JOptionPane.showInputDialog(
            this, "Masukkan NIS siswa yang ingin dihapus:");
        if (inputNis == null || inputNis.trim().isEmpty()) return;

        Optional<Siswa> opt = siswaService.findByNis(inputNis.trim());
        if (opt.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Data tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Siswa siswa = opt.get();
        int confirm = JOptionPane.showConfirmDialog(
            this, "Hapus data '" + siswa.getNama() + "'?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            siswaService.hapusSiswa(siswa.getNis());
            refreshTable();
            JOptionPane.showMessageDialog(
                this, "Data berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, "Gagal menghapus data: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        DataRepository<Siswa> repository = new SiswaRepository("siswa.csv");
        SiswaService service = new SiswaService(repository);

        SwingUtilities.invokeLater(() -> new Gui(service).setVisible(true));
    }
}
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class Gui extends JFrame {

    private final SiswaService siswaService;
    private DefaultTableModel tableModel;

    private final Color bgUtama = new Color(240, 248, 255); //biru muda
    private final Color teksJudul = new Color(25, 25, 112); //biru tua
    private final Color bgTombol = new Color(135, 206, 235); //biru langit
    private final Color bgTabelHeader = new Color(173, 216, 230); //biru pucet

    public Gui(SiswaService siswaService) {
        this.siswaService = siswaService; // menghubungkan gui dengan siswaService agar bisa mengakses data Siswa

        UIManager.put("OptionPane.background", bgUtama); //option pane pop up dialog
        UIManager.put("Panel.background", bgUtama); //panel

        initFrame(); // inisialisasi frame utama
        initTableModel(); // inisialisasi model untuk tabel, agar bisa menampilkan data siswa
        inisialisasiData(); // muat data + tampilkan dialog jika file belum ada
    }

    private void initFrame() { 
        setTitle("Menu Utama - Perpustakaan SMP"); //set judul
        setSize(480, 380); // size dari frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //jika frame diclose maka program langsung berhenti
        setLocationRelativeTo(null); // agar frame ditengah layar
        setLayout(new BorderLayout()); // layout utama
        getContentPane().setBackground(bgUtama); // set background frame dengan warna bgUtama

        // border layout digunakan untuk membagi menjadi 5 layout berdasarkan north, center, south, west, east
        add(buildPanelAtas(), BorderLayout.NORTH); //judul diset di bagian atas
        add(buildPanelMenu(), BorderLayout.CENTER); //judul diset di bagian tengah
    }

    private JPanel buildPanelAtas(){
        JPanel panel = new JPanel(new GridLayout(2, 1)); //ngatur layout 2 baris 1 kolom
        panel.setBackground(bgUtama); //set background
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); //jarak padding atas bawah 20px, kiri kanan 0px

        JLabel lblWelcome = new JLabel(
                "Selamat Datang di Sistem Perpustakaan!", SwingConstants.CENTER); // label yang diposisikan di rata tengah
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 19)); 
       

        JLabel lblSub = new JLabel(
                "Silakan pilih menu di bawah ini:", SwingConstants.CENTER); // label yang diposisikan di rata tengah
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // font yang reguler dari judul utama
        lblSub.setForeground(teksJudul); //judul depan

        panel.add(lblWelcome); //menambahkan label welcome ke panel
        panel.add(lblSub); //menambahkan label sub ke panel
        return panel; //mengembalikan panel yang sudah dibuat
    }

    private JPanel buildPanelMenu() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15)); 
        panel.setBackground(bgUtama);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 40, 30)); //jarak padding atas 10px, kiri kanan 30px, bawah 40px

        JButton btnLihat = styleButton("Lihat Semua Data");
        JButton btnCreate = styleButton("Tambah Data Baru");
        JButton btnUpdate = styleButton("Update Data");
        JButton btnDelete = styleButton("Hapus Data");

        // Warna khusus per tombol (sama dengan aslinya)
        btnCreate.setBackground(new Color(153, 255, 153)); //hijau muda
        btnUpdate.setBackground(new Color(255, 255, 153)); //kuning muda
        btnDelete.setBackground(new Color(255, 153, 153)); //merah muda

        panel.add(btnLihat);
        panel.add(btnCreate);
        panel.add(btnUpdate);
        panel.add(btnDelete);

        btnLihat.addActionListener(e -> showDataDialog()); //ketika tombol lihat diklik, maka akan memanggil method show
        btnCreate.addActionListener(e -> showCreateDialog()); //ketika tombol create diklik, maka akan memanggil method showCreateDialog
        btnUpdate.addActionListener(e -> showUpdateDialog()); //ketika tombol update diklik, maka akan memanggil method showUpdateDialog
        btnDelete.addActionListener(e -> showDeleteDialog()); //ketika tombol delete diklik, maka akan memanggil method showDeleteDialog

        return panel;
    }

    private JButton styleButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgTombol); //warna tombol default, bisa diubah per tombol jika ingin warna khusus
        button.setForeground(Color.BLACK); //text tombol berwarna hitam
        button.setOpaque(true); //agar background bisa terlihat disemua device
        button.setBorderPainted(false); //menghilangkan border default tombol agar terlihat lebih modern
        button.setFocusPainted(false); //fokus ke border text
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); //mengubah cursor menjadi tangan ketika hover di tombol, agar terlihat lebih interaktif
        return button;
    }

    private void initTableModel() {
        String[] kolom = { "NIS", "Nama Siswa", "Alamat" }; //nama kolom untuk tabel, harus sesuai dengan atribut Siswa
        tableModel = new DefaultTableModel(kolom, 0) { //0 berarti awalnya tidak ada baris data, nanti akan diisi saat refreshTable()
            @Override
            public boolean isCellEditable(int row, int column) { //agar tabel tidak bisa diedit langsung oleh user, karena kita ingin mengontrol perubahan data melalui dialog update
                return false;
            }
        };
    }

    /**
     * Memuat data dari service ke tabel.
     * Jika file belum ada, tampilkan dialog konfirmasi terlebih dahulu.
     */
    private void inisialisasiData() {
        // Jika file belum tersedia, tanya pengguna
        if (!siswaService.isStorageAvailable()) { //cek apakah file sudah tersedia, jika belum maka akan dibuat baru
            int pilihan = JOptionPane.showConfirmDialog( //pop up
                    this, //this sebagai parent component untuk dialog
                    "File data (siswa.csv) belum ditemukan.\n" +
                            "Sistem akan membuat file baru secara otomatis saat Anda menyimpan data nanti.\n\n" +
                            "Tetap masuk ke sistem?",
                    "Informasi File",
                    JOptionPane.YES_NO_OPTION, //yes no option untuk pilihan dialog
                    JOptionPane.INFORMATION_MESSAGE); //icon informasi untuk dialog
            if (pilihan != JOptionPane.YES_OPTION) { //jika pengguna memilih tidak, maka program akan langsung berhenti
                System.exit(0);
            }
            return; //jika file belum tersedia, maka tidak perlu memuat data, karena nanti akan dibuat baru saat menyimpan data pertama kali
        }

        try {
            siswaService.loadData(); //memuat data dari file ke memori, jika file sudah tersedia
            refreshTable(); //menyinkronkan isi tableModel dengan data terkini dari service, agar data yang ditampilkan di tabel adalah data terbaru
        } catch (Exception e) {
            JOptionPane.showMessageDialog( //jika terjadi error saat memuat data, maka akan menampilkan dialog error
                    this, "Gagal membaca file: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE); //icon error untuk dialog
        }
    }

    // untuk menyinkronkan isi tableModel dengan data terkini dari service.
    // dipanggil setiap kali data berubah (tambah / update / hapus).
    private void refreshTable() {
        tableModel.setRowCount(0); // Kosongkan tabel biar tidak terjadi duplikasi
        List<Siswa> daftarSiswa = siswaService.getAll(); //ambil semua data siswa dari service, lalu ditampilkan di tabel
        for (Siswa s : daftarSiswa) { //iterasi setiap siswa dalam daftarSiswa, lalu menambahkan baris baru ke tableModel dengan data siswa tersebut
            tableModel.addRow(new Object[] { s.getNis(), s.getNama(), s.getAlamat() }); //nambah baris baru ke tableModel
        }
    }

    /** Menampilkan seluruh data siswa dalam dialog tabel. */
    private void showDataDialog() { //lihat semua data siswa
        JTable tabelSiswa = new JTable(tableModel); 
        tabelSiswa.setFont(new Font("Segoe UI", Font.PLAIN, 14)); //plain reguler
        tabelSiswa.setRowHeight(25); //tinggi baris tabel 
        tabelSiswa.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14)); //tabel header
        tabelSiswa.getTableHeader().setBackground(bgTabelHeader); //warna biru

        JScrollPane scrollPane = new JScrollPane(tabelSiswa); //tabel bisa discroll
        scrollPane.setPreferredSize(new Dimension(500, 250)); //

        JOptionPane.showMessageDialog(
                this, scrollPane, "Data Semua Siswa", JOptionPane.PLAIN_MESSAGE); //plain message
    }

    // untuk menambah data siswa baru
    private void showCreateDialog() { //dialog untuk menambah data siswa baru, dengan input NIS, Nama, Alamat
        JTextField txtNis = new JTextField();
        JTextField txtNama = new JTextField();
        JTextField txtAlamat = new JTextField();

        Object[] formFields = {
                "📌 Masukkan NIS:", txtNis,
                "👤 Masukkan Nama Siswa:", txtNama,
                "🏠 Masukkan Alamat:", txtAlamat
        };

        int option = JOptionPane.showConfirmDialog(
                this, formFields, "Tambah Data Siswa",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option != JOptionPane.OK_OPTION)
            return;

        String nis = txtNis.getText().trim(); 
        String nama = txtNama.getText().trim();
        String alamat = txtAlamat.getText().trim();

        if (nis.isEmpty() || nama.isEmpty() || alamat.isEmpty()) { //validasi input, jika ada yang kosong maka akan menampilkan peringatan
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

    // untuk memperbarui data siswa berdasarkan NIS
    private void showUpdateDialog() {
        String inputNis = JOptionPane.showInputDialog(
                this, "Masukkan NIS siswa yang ingin di-update:");
        if (inputNis == null || inputNis.trim().isEmpty()) //validasi input, jika kosong maka akan menampilkan peringatan
            return;

        Optional<Siswa> opt = siswaService.findByNis(inputNis.trim()); //mencari data siswa berdasarkan NIS yang dimasukkan, jika tidak ditemukan maka akan menampilkan dialog error, jika ditemukan maka akan menampilkan dialog untuk mengubah nama dan alamat siswa tersebut
        if (opt.isEmpty()) {
            JOptionPane.showMessageDialog( //jika data tidak ditemukan, maka akan menampilkan dialog error
                    this, "Data dengan NIS " + inputNis + " tidak ditemukan!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Siswa siswa = opt.get();
        JTextField txtNama = new JTextField(siswa.getNama()); //input nama baru, defaultnya diisi dengan nama lama
        JTextField txtAlamat = new JTextField(siswa.getAlamat()); //input alamat baru, defaultnya diisi dengan alamat lama

        Object[] formFields = {
                "📌 NIS: " + siswa.getNis() + " (Tidak dapat diubah)",
                "👤 Nama Siswa Baru:", txtNama, 
                "🏠 Alamat Baru:", txtAlamat
        };

        int option = JOptionPane.showConfirmDialog( //menampilkan dialog untuk mengubah nama dan alamat siswa, dengan informasi NIS yang tidak bisa diubah
                this, formFields, "Update Data", //formFields sebagai isi dialog, "Update Data" sebagai judul dialog
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option != JOptionPane.OK_OPTION)
            return;

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
        if (inputNis == null || inputNis.trim().isEmpty())
            return;

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

        if (confirm != JOptionPane.YES_OPTION)
            return;

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
        DataRepository<Siswa> repository = new SiswaRepository("siswa.csv"); //menggunakan SiswaRepository sebagai implementasi DataRepository, dengan file path "siswa.csv" untuk menyimpan data siswa
        SiswaService service = new SiswaService(repository); //membuat instance SiswaService dengan repository yang sudah dibuat, agar bisa mengelola data siswa melalui service

        SwingUtilities.invokeLater(() -> new Gui(service).setVisible(true)); //menjalankan GUI di thread yang aman untuk Swing, dengan membuat instance Gui dan menampilkannya
    }
}
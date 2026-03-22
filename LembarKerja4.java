import java.util.Scanner;

interface Transaksi {
    void catatTransaksi();
}

interface TransaksiDigital extends Transaksi {
    boolean validasiPin();
}

interface LayananInternasional extends Transaksi {
    void konversiKurs(double jumlah);
}

interface TransferGlobal extends TransaksiDigital, LayananInternasional {
    void prosesTransferGlobal(String negaraTujuan, String nomorRekeningTujuan, double jumlah);
}

class Rekening {
    protected String nomorRekening;
    protected double saldo;
    protected String pin;
    Scanner in = new Scanner(System.in);

    public Rekening() {
    }

    public void displayInfo() {
        System.out.println("Nomor Rekening : " + nomorRekening);
        System.out.println("Saldo          : " + saldo);
    }
}

class RekeningValas extends Rekening implements TransferGlobal {
    private String kodeValas;
    private boolean isSafe;
    Scanner in = new Scanner(System.in);

    public RekeningValas() {

        System.out.println("=== PENDAFTARAN REKENING VALAS ===");
        System.out.print("Masukkan Nomor Rekening : ");
        String noRek = in.nextLine();

        System.out.print("Kode Valas (USD/EUR)    : ");
        String valas = in.nextLine();

        System.out.print("Masukkan Saldo Awal     : ");
        double saldoAwal = in.nextDouble();
        in.nextLine(); //

        System.out.print("Buat PIN (6 digit)      : ");
        String pinBaru = in.nextLine();

        this.nomorRekening = noRek;
        this.saldo = saldoAwal;
        this.pin = pinBaru;
        this.kodeValas = valas;

        System.out.println("[INFO] Rekening Valas dengan kode valas " + this.kodeValas + " telah dibuat.");
    }

    public String getKodeValas() {
        return kodeValas;
    }

    public void setKodeValas(String kodeValas) {
        this.kodeValas = kodeValas.toUpperCase();
    }

    public void setSafe(boolean status) {
        this.isSafe = status;
    }

    @Override
    public void displayInfo() {
        System.out.println("Nomor Rekening : " + nomorRekening);
        System.out.println("Saldo          : " + saldo + " " + kodeValas);
        System.out.println("---------------------------------");
    }

    @Override
    public void catatTransaksi() {
        boolean valid = validasiPin();
        if (valid) {
            System.out.print("Tipe (Masuk/Keluar): "); 
            String tipe = in.nextLine();
            System.out.print("Jumlah             : "); 
            double jml = in.nextDouble(); 
            in.nextLine();

            if (tipe.equalsIgnoreCase("masuk")) {
                saldo += jml;
                System.out.println("[SUCCESS] Transaksi masuk sebesar " + jml + " " + kodeValas + " berhasil dicatat.");
            } else if (tipe.equalsIgnoreCase("keluar")) {
                if (saldo >= jml) {
                    saldo -= jml;
                    System.out.println("[SUCCESS] Transaksi keluar sebesar " + jml + " " + kodeValas + " berhasil dicatat.");
                } else {
                    System.out.println("[FAILED] Saldo tidak mencukupi untuk transaksi keluar.");
                }
            } 
        } else {
            System.out.println("[FAILED] Pin tidak valid. Transaksi dibatalkan.");
        }
        System.out.println("---------------------------------");
    }

    @Override
    public boolean validasiPin() {
        System.out.print("Masukkan PIN untuk validasi: ");
        String inputPin = in.nextLine();
        if (this.pin.equals(inputPin)) {
            return true;
        }
        return false;

    }

    @Override
    public void konversiKurs(double jumlah) {
        double hasil = 0;
        if (this.kodeValas.equals("USD")) {
            hasil = jumlah * 15700;
        } else if (this.kodeValas.equals("EUR")) {
            hasil = jumlah * 17000;
        } else {
            hasil = jumlah * 1.0;
        }
        System.out.println("[KONVERSI] " + jumlah + " " + kodeValas + " = " + hasil + " IDR");
        System.out.println("---------------------------------");
    }

    @Override
    public void prosesTransferGlobal(String negaraTujuan, String nomorRekeningTujuan, double jumlah) {
        if (this.saldo >= jumlah) {
            this.saldo -= jumlah;
            System.out.println("[SUCCESS] Berhasil mengirim " + jumlah + " " + kodeValas + " ke " + negaraTujuan);
        } else {
            System.out.println("[FAILED] Saldo tidak mencukupi untuk transfer global.");
            System.out.println("---------------------------------");
        }
    }
}

final class ProtokolKeamanan {
    public final String ID_SERVER;
    private boolean cek;

    public ProtokolKeamanan(String ID_SERVER) {
        this.ID_SERVER = ID_SERVER;
    }

    public boolean validasiKeamanan(RekeningValas rek) {
        System.out.println("Memproses di Server: " + ID_SERVER);
        boolean valid = rek.validasiPin();
        if (valid) {
            System.out.println("Status: TERVERIFIKASI");
            System.out.println("---------------------------------");
            this.cek = true;
            return true;
        } else {
            System.out.println("Status: DITOLAK");
            System.out.println("---------------------------------");
            this.cek = false;
            return false;
        }
        
    }
}

public class LembarKerja4 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // Membuat protokol keamanan
        ProtokolKeamanan protokol = new ProtokolKeamanan("SERVER-001");
        RekeningValas akun = null; 
        boolean berjalan = true;
        while (berjalan) {
            System.out.println("\n========= M-BANKING VALAS =========");
            System.out.println("1. Buka Rekening Baru");
            System.out.println("2. Cek Saldo & Info");
            System.out.println("3. Setor / Tarik Tunai");
            System.out.println("4. Transfer Global (Luar Negeri)");
            System.out.println("5. Kalkulator Kurs (Ke IDR)");
            System.out.println("0. Keluar");
            System.out.print("Pilih Menu (0-5): ");
            
            int pilihan = in.nextInt();
            in.nextLine(); 
            switch (pilihan) {
                case 1:
                    akun = new RekeningValas();
                    break;

                case 2:
                    if (akun != null) {
                        akun.displayInfo();
                    }
                    break;

                case 3:
                    if (akun != null) {
                        akun.catatTransaksi();
                    } else{
                        System.out.println("[FAILED] Validasi Keamanan Gagal. Silakan coba lagi.");
                    }
                    break;

                case 4:
                    if (akun != null && protokol.validasiKeamanan(akun)) {
                        System.out.print("Negara Tujuan      : "); String neg = in.nextLine();
                        System.out.print("Rekening Tujuan    : "); String rekT = in.nextLine();
                        System.out.print("Jumlah Transfer    : "); double jm = in.nextDouble();
                        akun.prosesTransferGlobal(neg, rekT, jm);
                    }else{
                        System.out.println("[FAILED] Validasi Keamanan Gagal. Silakan coba lagi.");
                    }
                    break;

                case 5:
                    System.out.print("Jumlah yang dikonversi: ");
                    double konv = in.nextDouble();
                    akun.konversiKurs(konv);
                    break;

                case 0:
                    System.out.println("Terima kasih telah menggunakan layanan kami.");
                    berjalan = false;
                    break;

                default:
                    System.out.println("❌ Pilihan tidak valid.");
            
            }
        }
    }
}


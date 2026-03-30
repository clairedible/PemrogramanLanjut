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

class Rekening implements TransaksiDigital {
    protected String nomorRekening;
    protected double saldo;
    protected String pin;
    protected boolean isVerified = false;
    ProtokolKeamanan protokolReguler = new ProtokolKeamanan("SERVER-REGULER");
    Scanner in = new Scanner(System.in);

    public Rekening(String nomorRekening, double saldo, String pin) {
        this.nomorRekening = nomorRekening;
        this.saldo = saldo;
        this.pin = pin;
    }

    public void displayInfo() {
        System.out.println("Nomor Rekening : " + nomorRekening);
        System.out.println("Saldo          : " + saldo);
    }

    public void setIsVerified(boolean status) {
        this.isVerified = status;
    }

    @Override
    public void catatTransaksi() {
        if (isVerified) {
            System.out.print("Tipe (Masuk/Keluar): ");
            String tipe = in.nextLine();
            System.out.print("Jumlah             : ");
            double jml = in.nextDouble();
            in.nextLine();

            if (tipe.equalsIgnoreCase("masuk")) {
                saldo += jml;
                System.out.println("[SUCCESS] Transaksi masuk sebesar Rp" + jml + " " + " berhasil dicatat.");
            } else if (tipe.equalsIgnoreCase("keluar")) {
                if (saldo >= jml) {
                    saldo -= jml;
                    System.out.println("[SUCCESS] Transaksi keluar sebesar Rp" + jml + " " + " berhasil dicatat.");
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
}

class RekeningValas extends Rekening implements TransferGlobal {
    private String kodeValas;
    Scanner in = new Scanner(System.in);
    ProtokolKeamanan protokolValas = new ProtokolKeamanan("SERVER-VALAS");

    public RekeningValas(String nomorRekening, double saldo, String pin, String kodeValas) {
        super(nomorRekening, saldo, pin);
        this.kodeValas = kodeValas.toUpperCase();
    }

    public String getKodeValas() {
        return kodeValas;
    }

    public void setKodeValas(String kodeValas) {
        this.kodeValas = kodeValas.toUpperCase();
    }

    public void setIsVerified(boolean status) {
        this.isVerified = status;
    }

    @Override
    public void displayInfo() {
        System.out.println("Nomor Rekening : " + nomorRekening);
        System.out.println("Saldo          : " + saldo + " " + kodeValas);
        System.out.println("---------------------------------");
    }

    @Override
    public void catatTransaksi() {
        if (isVerified) {
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
                    System.out.println(
                            "[SUCCESS] Transaksi keluar sebesar " + jml + " " + kodeValas + " berhasil dicatat.");
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
        if (isVerified) {
            if (this.saldo >= jumlah) {
                this.saldo -= jumlah;
                System.out.println("[SUCCESS] Berhasil mengirim " + jumlah + " " + kodeValas + " ke " + negaraTujuan);
            } else {
                System.out.println("[FAILED] Saldo tidak mencukupi untuk transfer global.");
                System.out.println("---------------------------------");
            }
        } else {
            System.out.println("[FAILED] Pin tidak valid. Transfer global dibatalkan.");
            System.out.println("---------------------------------");
        }
    }
}

final class ProtokolKeamanan {
    public final String ID_SERVER;

    public ProtokolKeamanan(String ID_SERVER) {
        this.ID_SERVER = ID_SERVER;
    }

    public void validasiKeamanan(Rekening rek) {
        System.out.println("Memproses di Server: " + ID_SERVER);
        boolean valid = rek.validasiPin();
        if (valid) {
            System.out.println("Status: TERVERIFIKASI");
            System.out.println("---------------------------------");
            rek.setIsVerified(true);
        } else {
            System.out.println("Status: DITOLAK");
            System.out.println("---------------------------------");
            rek.setIsVerified(false);
        }
    }
}

public class LembarKerja4 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // Membuat protokol keamanan
        boolean berjalan = false;
        boolean berjalanValas = false;
        Rekening reguler = null;
        RekeningValas valas = null;

        System.out.println("=== SELAMAT DATANG DI M-BANKING ===");
        System.out.println("1. Rekening Reguler");
        System.out.println("2. Rekening Valas");
        System.out.print("Pilih Jenis Rekening (1/2): ");
        int jenisRek = in.nextInt();
        in.nextLine();

        if (jenisRek == 1) {
            berjalan = true;
            System.out.println("=== PENDAFTARAN REKENING REGULER ===");
            System.out.print("Masukkan Nomor Rekening : ");
            String noRek = in.nextLine();
            System.out.println("[INFO] Rekening reguler akan menggunakan IDR sebagai mata uang default.");
            System.out.println("[PERHATIAN] Wajib melakukan deposit awal minimal Rp50.000 untuk aktivasi rekening.");
            System.out.print("Masukkan Saldo Awal     : ");
            double saldoAwal = in.nextDouble();
            while (saldoAwal < 50000) {
                System.out.println("[ERROR] Saldo awal minimal Rp50.000. Silakan masukkan jumlah yang valid.");
                System.out.print("Masukkan Saldo Awal     : ");
                saldoAwal = in.nextDouble();
            }
            in.nextLine();

            System.out.print("Buat PIN (6 digit)      : ");
            String pinBaru = in.nextLine();

            reguler = new Rekening(noRek, saldoAwal, pinBaru);
            System.out.println("[INFO] Rekening reguler telah dibuat.");

        } else if (jenisRek == 2) {
            berjalanValas = true;
            System.out.println("=== PENDAFTARAN REKENING VALAS ===");
            System.out.print("Masukkan Nomor Rekening : ");
            String noRek = in.nextLine();

            System.out.print("Kode Valas (USD/EUR)    : ");
            String valass = in.nextLine();

            System.out.print("Masukkan Saldo Awal     : ");
            double saldoAwal = in.nextDouble();
            in.nextLine(); //

            System.out.print("Buat PIN (6 digit)      : ");
            String pinBaru = in.nextLine();

            valas = new RekeningValas(noRek, saldoAwal, pinBaru, valass);
            System.out.println("[INFO] Rekening Valas dengan kode valas " + valas.getKodeValas() + " telah dibuat.");

        } else {
            System.out.println("❌ Pilihan tidak valid. Program akan keluar.");
            return;
        }

        while (berjalan) {
            System.out.println("\n========= M-BANKING REGULER =========");
            System.out.println("1. Cek Saldo & Info");
            System.out.println("2. Setor / Tarik Tunai");
            System.out.println("0. Keluar");
            System.out.print("Pilih Menu (0-2): ");

            int pilihan = in.nextInt();
            in.nextLine();
            switch (pilihan) {
                case 1:
                    if (reguler != null) {
                        reguler.displayInfo();
                    } else {
                        System.out.println("[INFO] Silakan buat rekening terlebih dahulu.");
                    }
                    break;

                case 2:
                    if (reguler != null) {
                        reguler.protokolReguler.validasiKeamanan(reguler);
                        if (reguler.isVerified) {
                            reguler.catatTransaksi();
                        } else {
                            System.out.println("[FAILED] Validasi Keamanan Gagal. Silakan coba lagi.");
                        }
                    } else {
                        System.out.println("[INFO] Silakan buat rekening terlebih dahulu.");
                    }
                    break;

                case 0:
                    System.out.println("Terima kasih telah menggunakan layanan kami.");
                    berjalan = false;
                    break;

                default:
                    System.out.println("❌ Pilihan tidak valid.");

            }
        }

        while (berjalanValas) {
            System.out.println("\n========= M-BANKING VALAS =========");
            System.out.println("1. Cek Saldo & Info");
            System.out.println("2. Setor / Tarik Tunai");
            System.out.println("3. Transfer Global (Luar Negeri)");
            System.out.println("4. Kalkulator Kurs (Ke IDR)");
            System.out.println("0. Keluar");
            System.out.print("Pilih Menu (0-4): ");

            int pilihan1 = in.nextInt();
            in.nextLine();
            switch (pilihan1) {
                case 1:
                    if (valas != null) {
                        valas.displayInfo();
                    } else {
                        System.out.println("[INFO] Silakan buat rekening terlebih dahulu.");
                    }
                    break;

                case 2:
                    if (valas != null) {
                        valas.protokolValas.validasiKeamanan(valas);
                        if (valas.isVerified) {
                            valas.catatTransaksi();
                        } else {
                            System.out.println("[FAILED] Validasi Keamanan Gagal. Silakan coba lagi.");
                        }
                    } else {
                        System.out.println("[INFO] Silakan buat rekening terlebih dahulu.");
                    }
                    break;

                case 3:
                    if (valas != null) {
                        valas.protokolValas.validasiKeamanan(valas);
                        System.out.print("Negara Tujuan      : ");
                        String neg = in.nextLine();
                        System.out.print("Rekening Tujuan    : ");
                        String rekT = in.nextLine();
                        System.out.print("Jumlah Transfer    : ");
                        double jm = in.nextDouble();
                        valas.prosesTransferGlobal(neg, rekT, jm);
                    } else {
                        System.out.println("[FAILED] Validasi Keamanan Gagal. Silakan coba lagi.");
                    }
                    break;

                case 4:
                    System.out.print("Jumlah yang dikonversi: ");
                    double konv = in.nextDouble();
                    valas.konversiKurs(konv);
                    break;

                case 0:
                    System.out.println("Terima kasih telah menggunakan layanan kami.");
                    berjalanValas = false;
                    break;

                default:
                    System.out.println("❌ Pilihan tidak valid.");

            }
        }
    }
}
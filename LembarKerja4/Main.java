package LembarKerja4;

import java.util.Scanner;

public class Main {
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
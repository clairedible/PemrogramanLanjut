package LembarKerja4;

import java.util.*;

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


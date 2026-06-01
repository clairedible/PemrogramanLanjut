package LembarKerja4;

import java.util.*;

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


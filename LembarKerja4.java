interface Transaksi {
    void catatTransaksi(double jumlah, String reason);
}

interface TransaksiDigital extends Transaksi {
    boolean validasiToken(String token);
}

interface LayananInternasional extends Transaksi {
    void konversiKurs(double jumlah);
}

interface TransferGlobal extends TransaksiDigital, LayananInternasional {
    void prosesTransferGlobal(String negaraTujuan, double jumlah);
}

class Rekening {
    protected String nomorRekening;
    protected double saldo;

    public Rekening(String nomorRekening, double saldo) {
        this.nomorRekening = nomorRekening;
        this.saldo = saldo;
    }

    public void displayInfo() {
        System.out.println("Nomor Rekening : " + nomorRekening);
        System.out.println("Saldo          : " + saldo);
    }
}

class RekeningValas extends Rekening implements TransferGlobal {
    private String kodeValas;

    public RekeningValas(String nomorRekening, double saldo, String kodeValas) {
        super(nomorRekening, saldo);
        this.kodeValas = kodeValas.toUpperCase();
        System.out.println("[INFO] Rekening Valas dengan kode valas " + this.kodeValas + " telah dibuat.");
    }

    @Override
    public void displayInfo() {
        System.out.println("Nomor Rekening : " + nomorRekening);
        System.out.println("Saldo          : " + saldo + " " + kodeValas);
        System.out.println("---------------------------------");
    }

    @Override
    public void catatTransaksi(double jumlah, String reason) {
        if (reason.equalsIgnoreCase("masuk")) {
            System.out.println("[LOG] Dana Masuk: +" + jumlah + " " + kodeValas);
        } else if (reason.equalsIgnoreCase("keluar")) {
            if (jumlah > this.saldo) {
                System.out.println("[FAILED] Saldo tidak mencukupi untuk transaksi keluar.");
                return;
            }
            else {
                this.saldo -= jumlah;
                System.out.println("[LOG] Dana Keluar: " + jumlah + " " + kodeValas);
            }
        } else {
            System.out.println("[LOG] Transaksi Tidak Dikenal: " + jumlah + " " + kodeValas);
        }
        System.out.println("---------------------------------");
}

    @Override
    public boolean validasiToken(String token) {
        if (token != null && token.length() == 6) {
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
    public void prosesTransferGlobal(String negaraTujuan, double jumlah) {
        if (this.saldo >= jumlah) {
            this.saldo -= jumlah;
            System.out.println("[SUCCESS] Berhasil mengirim " + jumlah + " " + kodeValas + " ke " + negaraTujuan);
            catatTransaksi(jumlah, "keluar");
        } else {
            System.out.println("[FAILED] Saldo tidak mencukupi untuk transfer global.");
            System.out.println("---------------------------------");
        }
    }
}

final class ProtokolKeamanan {
    public final String ID_SERVER;

    public ProtokolKeamanan(String ID_SERVER) {
        this.ID_SERVER = ID_SERVER;
    }

    public void validasiKeamanan(RekeningValas rek, String token) {
        System.out.println("Enkripsi Server: " + ID_SERVER);
        if (rek.validasiToken(token)) {
            System.out.println("Status Keamanan: TERVERIFIKASI");
        } else {
            System.out.println("Status Keamanan: DITOLAK (Token Salah)");
        }
        System.out.println("---------------------------------");
    }
}

public class LembarKerja4 {
    public static void main(String[] args) {
        // Membuat protokol keamanan
        ProtokolKeamanan protokol = new ProtokolKeamanan("SERVER-001");
        
        RekeningValas usd = new RekeningValas("12345", 1000, "USD");
        RekeningValas eur = new RekeningValas("67890", 1500, "EUR");

        System.out.println();
        System.out.println("--- PROSES TRANSAKSI GLOBAL ---");
        usd.displayInfo();
        eur.displayInfo();
        protokol.validasiKeamanan(usd, "CDE123");
        protokol.validasiKeamanan(usd, "ABC1234");
        protokol.validasiKeamanan(eur, "ABC456");

        eur.catatTransaksi(150, "masuk");
        usd.catatTransaksi(60, "keluar");

        eur.konversiKurs(50);
        usd.konversiKurs(100);

        usd.prosesTransferGlobal("Indonesia", 1000);
        usd.prosesTransferGlobal("Malaysia", 10);
        eur.prosesTransferGlobal("Jerman", 175);
    }
}
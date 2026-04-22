package LembarKerja3;

import java.util.ArrayList;

public class ShopManagementSystem {

    // Menyimpan semua objek transaksi yang dibuat selama program berjalan
    public static ArrayList<Transaction> listTransactions = new ArrayList<>();

    public static void main(String[] args) {

        // Inisialisasi katalog dengan 6 produk dari 3 jenis berbeda (polymorphism)
        Product[] catalog = {
            new FoodProduct("F01", "Roti Tawar", 15000, 11, "2026-04-01"),
            new FoodProduct("F02", "Susu UHT", 20000, 5, "2026-05-10"),
            new ElectronicProduct("E01", "Mouse Wireless", 150000, 10, "1 Year"),
            new ElectronicProduct("E02", "Keyboard Mech", 600000, 3, "2 Years"),
            new ClothingProduct("C01", "Kaos Polos L", 75000, 15, "L", "Filkom-Wear"),
            new ClothingProduct("C02", "Kaos Polos S", 75000, 10, "S", "Filkom-Wear")
        };

        // Tampilkan seluruh katalog produk sebelum transaksi dimulai
        System.out.println("====================================== KATALOG PRODUK FILKOM MART ======================================");
        for (Product p : catalog) {
            System.out.println(p.getProductInfo());
        }

        // Transaksi 1 — demonstrasi stok makanan dan stok tidak cukup
        // Roti awal 11 unit, beli 3 → sisa 8 → diskon FoodProduct hangus di transaksi berikutnya
        // Kaos L stok 15, beli 16 → gagal karena stok tidak cukup
        Transaction trx1 = new Transaction("TRX-001");
        listTransactions.add(trx1);
        trx1.addItem(catalog[0], 3);
        trx1.addItem(catalog[3]);
        trx1.addItem(catalog[4], 16);
        double total1 = trx1.processSale();

        // Transaksi 2 — demonstrasi perbedaan diskon ukuran baju (L dapat diskon, S tidak)
        Transaction trx2 = new Transaction("TRX-002");
        listTransactions.add(trx2);
        trx2.addItem(catalog[4], 2);
        trx2.addItem(catalog[5]);
        double total2 = trx2.processSale();

        // Tampilkan stok semua produk setelah transaksi penjualan
        System.out.println("\n==================================== STOK PRODUK SETELAH TRANSAKSI ====================================");
        for (Product p : catalog) {
            System.out.println(p.getProductInfo());
        }

        // Transaksi restock — tambah stok Roti Tawar sebanyak 5 unit
        Transaction trxin1 = new Transaction("TRX-IN-001");
        trxin1.restockItem(catalog[0], 5);

        // Tampilkan stok semua produk setelah restock
        System.out.println("\n==================================== STOK PRODUK SETELAH INPUT BARANG ====================================");
        for (Product p : catalog) {
            System.out.println(p.getProductInfo());
        }

        // Cetak laporan penjualan harian dengan varargs (bisa terima berapa pun jumlah total)
        laporanPenjualanHarian(total1, total2);
    }

    // Hitung grand total dan tampilkan laporan harian
    // Menggunakan varargs agar bisa menerima berapa pun jumlah transaksi
    public static void laporanPenjualanHarian(double... totals) {
        double grandTotal = 0;

        for (double total : totals) {
            grandTotal += total;
        }

        System.out.println("\n==============================================");
        System.out.println("          LAPORAN PENJUALAN HARIAN");
        System.out.println("==============================================");
        System.out.printf("Jumlah Transaksi      : %d transaksi\n", totals.length);
        System.out.printf("Total Pendapatan      : Rp%,15.2f\n", grandTotal);
        System.out.println("Produk Terlaris       : " + produkTerlaris().getName());
        System.out.println("Status                : CLOSED");
        System.out.println("==============================================");
    }

    // Cari produk terlaris berdasarkan frekuensi kemunculan di allItemsSold
    public static Product produkTerlaris() {
        ArrayList<Product> allItems = Transaction.getAllItems();

        int rekorPenjualan = 0;
        Product pemenang = null;

        for (int i = 0; i < allItems.size(); i++) {
            Product p = allItems.get(i);
            int hitungSekarang = 0;

            // Hitung berapa kali produk ini muncul di seluruh daftar penjualan
            for (int j = 0; j < allItems.size(); j++) {
                if (p.getName().equals(allItems.get(j).getName())) {
                    hitungSekarang++;
                }
            }

            // Update pemenang jika produk ini terjual lebih banyak dari rekor sebelumnya
            if (hitungSekarang > rekorPenjualan) {
                rekorPenjualan = hitungSekarang;
                pemenang = p;
            }
        }

        return pemenang;
    }
}
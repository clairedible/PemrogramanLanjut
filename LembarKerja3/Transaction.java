package LembarKerja3;

import java.util.ArrayList;

public class Transaction {

    // Identitas unik transaksi
    private String transactionId;

    // Dua ArrayList paralel — indeks yang sama merujuk ke produk dan diskonnya
    private ArrayList<Product> items = new ArrayList<>();
    private ArrayList<Double> itemDiscounts = new ArrayList<>();

    // Static — dipakai bersama oleh semua objek Transaction untuk mencatat semua produk terjual
    private static ArrayList<Product> allItemsSold = new ArrayList<>();

    // Konstruktor — cetak notifikasi saat transaksi berhasil dibuat
    public Transaction(String transactionId) {
        this.transactionId = transactionId;
        System.out.println("\n[INFO] Transaksi dengan id " + transactionId + " berhasil dibuat.");
    }

    // Overload versi 1 — tambah 1 unit produk ke transaksi
    public void addItem(Product item) {
        if (item.getStockQuantity() > 0) {
            // Hitung dan simpan diskon saat item ditambahkan
            double diskonSaatIni = item.calculateDiscount();
            items.add(item);
            itemDiscounts.add(diskonSaatIni);

            // Kurangi stok 1 unit, cetak pesan otomatis
            item.updateStock(-1);
        } else {
            System.out.println("[Gagal] Stok " + item.getName() + " habis!");
        }
    }

    // Overload versi 2 — tambah sejumlah unit produk ke transaksi sekaligus
    public void addItem(Product item, int quantity) {
        if (item.getStockQuantity() >= quantity) {
            // Diskon dihitung sekali, berlaku sama untuk semua unit dalam pembelian ini
            double diskonSaatIni = item.calculateDiscount();

            for (int i = 0; i < quantity; i++) {
                items.add(item);
                itemDiscounts.add(diskonSaatIni);

                // Catat ke daftar global semua produk terjual
                allItemsSold.add(item);
            }

            // Kurangi stok sekaligus dan catat alasannya
            item.updateStock(-quantity, "Pembelian " + transactionId);
        } else {
            System.out.println("[Gagal] Stok " + item.getName() + " tidak cukup!");
        }
    }

    // Tambah stok produk dengan keterangan alasan restock
    public void restockItem(Product item, int quantity) {
        item.updateStock(quantity, "Restock " + transactionId);
    }

    // Getter static — ambil semua produk yang pernah terjual lintas transaksi
    public static ArrayList<Product> getAllItems() {
        return allItemsSold;
    }

    // Cetak struk penjualan dan kembalikan total yang harus dibayar
    public double processSale() {
        double totalSemua = 0;

        // Mencegah produk yang sama muncul lebih dari satu baris di struk
        ArrayList<String> sudahDiproses = new ArrayList<>();

        // Header struk
        System.out.println("\n===============================================================");
        System.out.println("                 STRUK PENJUALAN: " + transactionId);
        System.out.println("===============================================================");
        System.out.printf("%-18s %-3s %-13s %-12s %-10s\n",
                "Produk", "Qty", "Harga Satuan", "Tot. Diskon", "Subtotal");
        System.out.println("---------------------------------------------------------------");

        for (int i = 0; i < items.size(); i++) {
            Product p = items.get(i);

            // Lewati jika produk ini sudah dicetak sebelumnya
            if (!sudahDiproses.contains(p.getName())) {
                int qty = 0;
                double totalDiskonProduk = 0;

                // Hitung total qty dan diskon untuk produk yang sama
                for (int j = 0; j < items.size(); j++) {
                    if (items.get(j).getName().equals(p.getName())) {
                        qty++;
                        totalDiskonProduk += itemDiscounts.get(j);
                    }
                }

                // Hitung subtotal: (harga × qty) - total diskon
                double hargaSatuan = p.getPrice();
                double subtotal = (hargaSatuan * qty) - totalDiskonProduk;
                totalSemua += subtotal;

                // Cetak satu baris per produk
                System.out.printf("%-18s %-3d Rp%,-12.0f -Rp%,-10.0f Rp%,-10.0f\n",
                        p.getName(), qty, hargaSatuan, totalDiskonProduk, subtotal);

                sudahDiproses.add(p.getName());
            }
        }

        // Footer struk
        System.out.println("---------------------------------------------------------------");
        System.out.printf("TOTAL BAYAR:                                  Rp%,12.2f\n", totalSemua);
        System.out.println("===============================================================");
        System.out.println("          Terima Kasih Telah Berbelanja di Filkom Mart!        ");
        System.out.println("===============================================================\n");

        return totalSemua;
    }
}
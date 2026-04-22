package LembarKerja3;

// Kelas abstrak Product sebagai superclass dari semua jenis produk
// Tidak dapat diinstansiasi langsung, hanya bisa digunakan sebagai blueprint
public abstract class Product {

    // Atribut private sebagai penerapan enkapsulasi
    // Hanya bisa diakses dari dalam kelas ini melalui getter/setter
    private String productId;
    private String name;
    private double price;
    private int stockQuantity;

    // Konstruktor untuk menginisialisasi semua atribut dasar produk
    // Dipanggil oleh subclass melalui super(...)
    public Product(String productId, String name, double price, int stockQuantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Method abstrak yang wajib diimplementasikan oleh setiap subclass
    // Setiap jenis produk memiliki aturan diskon yang berbeda-beda
    public abstract double calculateDiscount();

    // Method untuk menampilkan informasi dasar produk dalam format tabel
    // Menggunakan String.format() agar tampilan setiap kolom seragam dan rapi
    public String getProductInfo() {
        return String.format("ID: %-5s | Nama: %-20s | Harga: Rp%,10.2f | Stok: %-3d",
                productId, name, price, stockQuantity);
    }

    // Overload versi 1: hanya menerima jumlah perubahan stok
    // quantity positif = restock (stok bertambah)
    // quantity negatif = penjualan (stok berkurang)
    public void updateStock(int quantity) {
        this.stockQuantity += quantity;

        // Deteksi arah perubahan stok secara otomatis lalu cetak pesan yang sesuai
        if (quantity < 0) {
            System.out.println("[INFO] Stok " + name + " berkurang: " + quantity + " unit");
        } else {
            System.out.println("[INFO] Stok " + name + " bertambah: " + quantity + " unit");
        }
    }

    // Overload versi 2: menerima jumlah perubahan stok beserta alasan perubahannya
    // Digunakan saat transaksi pembelian dalam jumlah tertentu perlu dicatat
    // Contoh reason: "Pembelian TRX-001"
    public void updateStock(int quantity, String reason) {
        this.stockQuantity += quantity;
        System.out.println("[INFO] Stok " + name + " diperbarui: " + reason);
    }

    // Getter dan setter sebagai penerapan enkapsulasi
    // Satu-satunya cara untuk mengakses atribut private dari luar kelas
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
}

// Subclass untuk produk makanan/minuman
// Mewarisi semua atribut dan method dari Product
class FoodProduct extends Product {

    // Atribut tambahan khusus produk makanan
    private String expiryDate;

    // Konstruktor meneruskan 4 parameter ke induk via super()
    // lalu menginisialisasi atribut tambahan expiryDate
    public FoodProduct(String id, String name, double price, int stock, String expiry) {
        super(id, name, price, stock);
        this.expiryDate = expiry;
    }

    // Implementasi aturan diskon untuk produk makanan:
    // Diskon 10% jika stok lebih dari 10 unit, tidak ada diskon jika stok <= 10
    // Diskon bersifat dinamis — nilainya bergantung pada stok saat ini
    @Override
    public double calculateDiscount() {
        return (getStockQuantity() > 10) ? getPrice() * 0.10 : 0;
    }

    // Menambahkan informasi tanggal kedaluwarsa di belakang info dasar dari induk
    // super.getProductInfo() dipakai agar tidak perlu menulis ulang format dari awal
    @Override
    public String getProductInfo() {
        return super.getProductInfo() + " | Exp: " + expiryDate;
    }
}

// Subclass untuk produk elektronik
// Mewarisi semua atribut dan method dari Product
class ElectronicProduct extends Product {

    // Atribut tambahan khusus produk elektronik
    private String warrantyPeriod;

    // Konstruktor meneruskan 4 parameter ke induk via super()
    // lalu menginisialisasi atribut tambahan warrantyPeriod
    public ElectronicProduct(String id, String name, double price, int stock, String warranty) {
        super(id, name, price, stock);
        this.warrantyPeriod = warranty;
    }

    // Implementasi aturan diskon untuk produk elektronik:
    // Selalu mendapat diskon dasar 5%
    // Jika harga > Rp500.000, ditambah bonus diskon 2% (total jadi 7%)
    // Diskon tidak bergantung pada stok, melainkan pada harga produk
    @Override
    public double calculateDiscount() {
        double discount = getPrice() * 0.05;                        // diskon dasar 5%
        if (getPrice() > 500000) discount += getPrice() * 0.02;    // bonus 2% untuk produk premium
        return discount;
    }

    // Menambahkan informasi masa garansi di belakang info dasar dari induk
    @Override
    public String getProductInfo() {
        return super.getProductInfo() + " | Garansi: " + warrantyPeriod;
    }
}

// Subclass untuk produk pakaian
// Mewarisi semua atribut dan method dari Product
class ClothingProduct extends Product {

    // Atribut tambahan khusus produk pakaian
    // size menentukan apakah produk mendapat diskon atau tidak
    private String size, brand;

    // Konstruktor meneruskan 4 parameter ke induk via super()
    // lalu menginisialisasi dua atribut tambahan size dan brand
    public ClothingProduct(String id, String name, double price, int stock, String size, String brand) {
        super(id, name, price, stock);
        this.size = size;
        this.brand = brand;
    }

    // Implementasi aturan diskon untuk produk pakaian:
    // Diskon 15% hanya untuk ukuran L dan XL
    // Ukuran lain (S, M, dll) tidak mendapat diskon
    // equalsIgnoreCase() agar tidak sensitif terhadap huruf besar/kecil
    @Override
    public double calculateDiscount() {
        if (size.equalsIgnoreCase("L") || size.equalsIgnoreCase("XL")) {
            return getPrice() * 0.15;
        }
        return 0;
    }

    // Menambahkan informasi ukuran dan merek di belakang info dasar dari induk
    // ClothingProduct menambahkan dua informasi sekaligus dibanding subclass lain
    @Override
    public String getProductInfo() {
        return super.getProductInfo() + " | Size: " + size + " | Brand: " + brand;
    }
}
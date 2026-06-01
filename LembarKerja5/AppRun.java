package LembarKerja5;

import java.util.List;
import java.util.Arrays;

public class AppRun {
    public static void main(String[] args) {
        System.out.println("╔═════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                MEDIGUARD INTEGRATION GATEWAY - SIMULASI (AppRun)                ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════════════╝\n");
        
        // inisialisasi level akses untuk skenario
        int aksesRendah = 1;
        int aksesTinggi = 4;
        
        // membuat data pasien untuk versi 1 dan versi 2
        PatientProfileV1 pasienV1 = new PatientProfileV1("P-01", "Budi", "357123456");
        PatientProfileV2 pasienV2 = new PatientProfileV2("P-02", "Siti", "357987654", "Kacang", "HIV/AIDS");

        System.out.println("  [ MENGAKSES DATA PASIEN VERSI 1 DENGAN AKSES RENDAH ]\n");

        // membuat gateway khusus untuk pasien versi 1 yang bernama gatewayRendah
        // memasukkan arraylist yang berisi pasienV1 ke dalam gatewayRendah
        IntegrationGateway<PatientProfileV1> gatewayRendah = new IntegrationGateway<>(Arrays.asList(pasienV1));
        
        gatewayRendah.fetchData("P-01", aksesRendah).displayInfo(
            "SKENARIO 1: Akses Rendah minta P-01 (Budi)",
            "Melihat nama pasien, namun KTP tertutup."
        );

        gatewayRendah.fetchData("fP-01", aksesTinggi).displayInfo(
            "SKENARIO 2: Akses Tinggi minta P-01 (Budi)",
            "Melihat KTP Budi secara utuh." 
        );

    
        System.out.println("  [ MENGAKSES DATA PASIEN VERSI 2 DENGAN AKSES TINGGI ]\n");
        IntegrationGateway<PatientProfileV2> gatewayTinggi = new IntegrationGateway<>(Arrays.asList(pasienV2));

        gatewayTinggi.fetchData("P-02", aksesRendah).displayInfo(
            "SKENARIO 3: Akses Rendah minta P-02 (Siti)",
            "Melihat nama & alergi, namun KTP & Diagnosa tertutup."
        );

        gatewayTinggi.fetchData("P-02", aksesTinggi).displayInfo(
            "SKENARIO 4: Akses Tinggi minta P-02 (Siti)",
            "Melihat seluruh data secara utuh, termasuk KTP dan Diagnosa." 
        );
        
        List<UniversalRecord> mixData = Arrays.asList(pasienV1, pasienV2);
        IntegrationGateway<UniversalRecord> gatewayUniversal = new IntegrationGateway<>(mixData);

        System.out.println("  [ MENGAKSES DATA V1 & V2 MENGGUNAKAN GATEWAY YANG SAMA ]\n");

        gatewayUniversal.fetchData("P-01", aksesRendah).displayInfo(
            "SKENARIO 5: Akses Rendah dengan 1 Gateway meminta Data V1 (P-01)",
            "Melihat nama pasien, namun KTP tertutup."
        );
        gatewayUniversal.fetchData("P-01", aksesTinggi).displayInfo(
            "SKENARIO 6: Akses Tinggi dengan 1 Gateway meminta Data V1 (P-01)",
            "Melihat KTP Budi secara utuh." 
        );

        gatewayUniversal.fetchData("P-02", aksesRendah).displayInfo(
            "SKENARIO 7: Akses Rendah dengan 1 Gateway meminta Data V2 (P-02)",
            "Melihat nama & alergi, KTP & Diagnosa tersensor."
        );
        
        gatewayUniversal.fetchData("P-02", aksesTinggi).displayInfo(
            "SKENARIO 8: Akses Tinggi dengan 1 Gateway meminta Data V2 (P-02)",
            "Melihat seluruh data Siti secara utuh (KTP & Diagnosa terbuka)."
        );

        System.out.println("═════════════════════════════════ END OF SIMULATION ═════════════════════════════════\n");
    }
}

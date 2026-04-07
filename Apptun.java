interface MedicalRecord {
    String getPatientId();
}

interface Versioned {
    int getVersion();
}

interface Confidential {
    int getSecurityLevel(); 
    void maskSensitiveData(); 
}

class PatientProfileV1 implements MedicalRecord, Versioned, Confidential {
    private String patientId;
    private String name;
    private String ssn; // KTP
    private int securityLevel = 2;

    public PatientProfileV1(String patientId, String name, String ssn) {
        this.patientId = patientId;
        this.name = name;
        this.ssn = ssn;
    }

    @Override public String getPatientId() { return patientId; }
    @Override public int getVersion() { return 1; }
    @Override public int getSecurityLevel() { return securityLevel; }

    @Override
    public void maskSensitiveData() {
        if (this.ssn != null) this.ssn = "******"; // Masking KTP
    }

    @Override
    public String toString() {
        return String.format("PatientV1 { ID: %s | Nama: %-4s | KTP: %-9s }", patientId, name, ssn);
    }
}

class PatientProfileV2 implements MedicalRecord, Versioned, Confidential {
    private String patientId;
    private String name;
    private String ssn; // KTP
    private String allergyHistory; 
    private String specialDiagnosis;
    private int securityLevel = 3;

    public PatientProfileV2(String patientId, String name, String ssn, String allergyHistory, String specialDiagnosis) {
        this.patientId = patientId;
        this.name = name;
        this.ssn = ssn;
        this.allergyHistory = allergyHistory;
        this.specialDiagnosis = specialDiagnosis;
    }

    @Override public String getPatientId() { return patientId; }
    @Override public int getVersion() { return 2; }
    @Override public int getSecurityLevel() { return securityLevel; }

    @Override
    public void maskSensitiveData() {
        if (this.ssn != null) this.ssn = "******"; // Masking KTP
        if (this.specialDiagnosis != null) this.specialDiagnosis = "[DISENSOR]"; // Masking Diagnosa
    }

    @Override
    public String toString() {
        return String.format("PatientV2 { ID: %s | Nama: %-4s | KTP: %-9s | Alergi: %-6s | Diagnosa: %s }", 
                patientId, name, ssn, allergyHistory, specialDiagnosis);
    }
}

class SecureResponse<T extends MedicalRecord & Confidential> {
    private boolean success;
    private T data;
    private String warningMessage;

    public SecureResponse(boolean success, T data, String warningMessage) {
        this.success = success;
        this.data = data;
        this.warningMessage = warningMessage;
    }

    public void displayInfo (String title, String expectation) {
        System.out.println(" ╭─── " + title + " ");
        System.out.println(" │ Tujuan     : " + expectation);
        System.out.println(" ├─────────────────────────────────────────────────────────────────────────────────");
        System.out.println(" │ Status API : " + (success ? "[ SUCCESS ]" : "[ FAILED ]"));
        
        if (warningMessage != null && !warningMessage.isEmpty()) {
            System.out.println(" │ Warning    : (!) " + warningMessage);
        }
        
        System.out.println(" │ Payload    : " + (data != null ? data.toString() : "null"));
        System.out.println(" ╰─────────────────────────────────────────────────────────────────────────────────\n");
    }
}

class IntegrationGateway<T extends MedicalRecord & Versioned & Confidential> {
    private T mockDatabaseRecord;

    public IntegrationGateway(T record) {
        this.mockDatabaseRecord = record;
    }

    public SecureResponse<T> fetchData(String id, int requesterClearanceLevel) {
        if (!mockDatabaseRecord.getPatientId().equals(id)) {
            return new SecureResponse<>(false, null, "ID tidak ditemukan");
        }

        String warning = "";
        
        if (requesterClearanceLevel < mockDatabaseRecord.getSecurityLevel()) {
            mockDatabaseRecord.maskSensitiveData();
            warning = "Akses rendah! Beberapa field sensitif di-masking secara otomatis.";
        }

        return new SecureResponse<>(true, mockDatabaseRecord, warning);
    }
}

public class Simulation {
    public static void main(String[] args) {
        System.out.println("╔═════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                MEDIGUARD INTEGRATION GATEWAY - SIMULASI (AppTun)                ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════════════╝\n");

        int aksesRendah = 1;
        int aksesTinggi = 3;

        System.out.println("  [ MENGAKSES DATA VERSI 1 ]\n");

        PatientProfileV1 dataV1_Rendah = new PatientProfileV1("P-01", "Budi", "357123456");
        IntegrationGateway<PatientProfileV1> gatewayV1_1 = new IntegrationGateway<>(dataV1_Rendah);
        gatewayV1_1.fetchData("P-01", aksesRendah).displayInfo(
            "SKENARIO 1: Dokter Akses Rendah (Level 1) meminta Data V1", 
            "Melihat nama pasien, namun KTP tertutup."
        );

        PatientProfileV1 dataV1_Tinggi = new PatientProfileV1("P-01", "Budi", "357123456");
        IntegrationGateway<PatientProfileV1> gatewayV1_2 = new IntegrationGateway<>(dataV1_Tinggi);
        gatewayV1_2.fetchData("P-01", aksesTinggi).displayInfo(
            "SKENARIO 2: Dokter Akses Tinggi (Level 3) meminta Data V1", 
            "Melihat seluruh data secara utuh tanpa ada sensor."
        );

        System.out.println("  [ MENGAKSES DATA VERSI 2 (MENGGUNAKAN GATEWAY YANG SAMA) ]\n");

        PatientProfileV2 dataV2_Rendah = new PatientProfileV2("P-02", "Siti", "357987654", "Kacang", "HIV/AIDS");
        IntegrationGateway<PatientProfileV2> gatewayV2_1 = new IntegrationGateway<>(dataV2_Rendah);
        gatewayV2_1.fetchData("P-02", aksesRendah).displayInfo(
            "SKENARIO 3: Dokter Akses Rendah (Level 1) meminta Data V2", 
            "Melihat nama & alergi, namun KTP & Diagnosa tertutup."
        );

        PatientProfileV2 dataV2_Tinggi = new PatientProfileV2("P-02", "Siti", "357987654", "Kacang", "HIV/AIDS");
        IntegrationGateway<PatientProfileV2> gatewayV2_2 = new IntegrationGateway<>(dataV2_Tinggi);
        gatewayV2_2.fetchData("P-02", aksesTinggi).displayInfo(
            "SKENARIO 4: Dokter Akses Tinggi (Level 3) meminta Data V2", 
            "Melihat seluruh data secara utuh, termasuk KTP dan Diagnosa."
        );
        
        System.out.println("═════════════════════════════════ END OF SIMULATION ═════════════════════════════════\n");
    }
}
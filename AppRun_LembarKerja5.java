import java.util.List;
import java.util.Arrays;

interface MedicalRecord {
    String getPatientId();
}

interface Versioned {
    int getVersion();
}

interface Confidential {
    int getSecurityLevel();
    String maskSensitiveData(int requesterClearance); 
}

interface UniversalRecord extends MedicalRecord, Versioned, Confidential {
}

class PatientProfileV1 implements UniversalRecord {
    private String patientId;
    private String name;
    private String ssn; 
    private int securityLevel = 2;

    public PatientProfileV1(String patientId, String name, String ssn) {
        this.patientId = patientId;
        this.name = name;
        this.ssn = ssn;
    }

    @Override 
    public String getPatientId() { 
        return patientId; 
    }

    @Override 
    public int getVersion() { 
        return 1; 
    }

    @Override 
    public int getSecurityLevel() { 
        return securityLevel; 
    }

    @Override
    public String maskSensitiveData(int requesterClearance) {
        String displaySsn = (requesterClearance < this.securityLevel) ? "******" : this.ssn;
        
        return String.format("PatientV1 { ID: %s | Nama: %-4s | KTP: %-9s }", 
                patientId, name, displaySsn);
    }
}

class PatientProfileV2 implements UniversalRecord {
    private String patientId;
    private String name;
    private String ssn; 
    private String allergyHistory;
    private String specialDiagnosis;
    private final int securityLevel = 3;

    public PatientProfileV2(String patientId, String name, String ssn, String allergyHistory, String specialDiagnosis) {
        this.patientId = patientId;
        this.name = name;
        this.ssn = ssn;
        this.allergyHistory = allergyHistory;
        this.specialDiagnosis = specialDiagnosis;
    }

    @Override 
    public String getPatientId() { 
        return patientId; 
    }

    @Override 
    public int getVersion() { 
        return 2; 
    }

    @Override 
    public int getSecurityLevel() { 
        return securityLevel; 
    }

    @Override
    public String maskSensitiveData(int requesterClearance) {
        String displaySsn = (requesterClearance < this.securityLevel) ? "******" : this.ssn;
        String displayDiagnosis = (requesterClearance < this.securityLevel) ? "[DISENSOR]" : this.specialDiagnosis;
        return String.format("PatientV2 { ID: %s | Nama: %-4s | KTP: %-9s | Alergi: %-6s | Diagnosa: %s }",
                patientId, name, displaySsn, allergyHistory, displayDiagnosis);
    }
}

class SecureResponse<T extends UniversalRecord> {
    private boolean success;
    private T data; 
    private String warningMessage;
    private final int clearanceUsed;

    public SecureResponse(boolean success, T data, String warningMessage, int clearanceUsed) {
        this.success = success;
        this.data = data;
        this.warningMessage = warningMessage;
        this.clearanceUsed = clearanceUsed;
    }

    public void displayInfo (String title, String expectation) {
        System.out.println(" ╭─── " + title + " ");
        System.out.println(" │ Tujuan     : " + expectation);
        System.out.println(" ├─────────────────────────────────────────────────────────────────────────────────");
        System.out.println(" │ Status API : " + (success ? "[ SUCCESS ]" : "[ FAILED ]"));
        
        if (warningMessage != null && !warningMessage.isEmpty()) {
            System.out.println(" │ Warning    : (!) " + warningMessage);
        }
        String payloadStr = (data != null) ? data.maskSensitiveData(clearanceUsed) : "null";
        System.out.println(" │ Payload    : " + payloadStr);
        System.out.println(" ╰─────────────────────────────────────────────────────────────────────────────────\n");
    }
}

class IntegrationGateway<T extends UniversalRecord> {
    private List<T> mockDatabase;

    public IntegrationGateway(List<T> records) {
        this.mockDatabase = records;
    }

    public SecureResponse<T> fetchData(String id, int requesterClearanceLevel) {
        T foundRecord = null;
        for (T record : mockDatabase) {
            if (record.getPatientId().equals(id)) {
                foundRecord = record;
                break;
            }
        }

        if (foundRecord == null) {
            return new SecureResponse<>(false, null, "ID pasien tidak ditemukan", requesterClearanceLevel);
        }

        String warning = "";
        if (requesterClearanceLevel < foundRecord.getSecurityLevel()) {
            warning = "Akses rendah! Beberapa field sensitif disamarkan pada tampilan.";
        }

        return new SecureResponse<>(true, foundRecord, warning, requesterClearanceLevel);
    }
}

public class AppRun_LembarKerja5 {
    public static void main(String[] args) {
        System.out.println("╔═════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                MEDIGUARD INTEGRATION GATEWAY - SIMULASI (AppRun)                ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════════════╝\n");

        int aksesRendah = 1;
        int aksesTinggi = 4;
        
        PatientProfileV1 pasienV1 = new PatientProfileV1("P-01", "Budi", "357123456");
        PatientProfileV2 pasienV2 = new PatientProfileV2("P-02", "Siti", "357987654", "Kacang", "HIV/AIDS");

        System.out.println("  [ MENGAKSES DATA PASIEN VERSI 1 DENGAN AKSES RENDAH ]\n");
        IntegrationGateway<PatientProfileV1> gatewayRendah = new IntegrationGateway<>(Arrays.asList(pasienV1));

        gatewayRendah.fetchData("P-01", aksesRendah).displayInfo(
            "SKENARIO 1: Akses Rendah minta P-01 (Budi)",
            "Melihat nama pasien, namun KTP tertutup."
        );

        gatewayRendah.fetchData("P-01", aksesTinggi).displayInfo(
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
package LembarKerja5;


// profil pasien versi 2 dengan level keamanan 3 (tinggi)
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

    /* jika nilai requester lebih rendah dari security level, maka nilai dikembalikan dengan nilai *****
    jika nilai requester sama atau lebih tinggi dari security level, maka nilai ssn dikembalikan tanpa disensor */
    @Override
    public String maskSensitiveData(int nilaiRequester) {
        String displaySsn = (nilaiRequester < this.securityLevel) ? "******" : this.ssn;
        String displayDiagnosis = (nilaiRequester < this.securityLevel) ? "[DISENSOR]" : this.specialDiagnosis;
        return String.format("PatientV2 { ID: %s | Nama: %-4s | KTP: %-9s | Alergi: %-6s | Diagnosa: %s }",
                patientId, name, displaySsn, allergyHistory, displayDiagnosis);
    }
}


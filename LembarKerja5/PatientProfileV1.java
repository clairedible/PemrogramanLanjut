package LembarKerja5;

/* semakin tinggi level keamanan, makin sulit diakses
    contoh: level 3 tidak bisa diakses oleh level 1 dan 2 */

// profil pasien versi 1 dengan level keamanan 2 (sedang)
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

    /* jika nilai requester lebih rendah dari security level, maka nilai dikembalikan dengan nilai *****
    jika nilai requester sama atau lebih tinggi dari security level, maka nilai ssn dikembalikan tanpa disensor */
    @Override
    public String maskSensitiveData(int nilaiRequester) {
        String displaySsn = (nilaiRequester < this.securityLevel) ? "******" : this.ssn;
        
        return String.format("PatientV1 { ID: %s | Nama: %-4s | KTP: %-9s }", 
                patientId, name, displaySsn);
    }
}


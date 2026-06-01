package LembarKerja5;

interface MedicalRecord {
    String getPatientId();
}

interface Versioned {
    int getVersion();
}

interface Confidential {
    int getSecurityLevel();
    String maskSensitiveData(int nilaiRequester); 
}

interface UniversalRecord extends MedicalRecord, Versioned, Confidential {
}
package LembarKerja5;

import java.util.List;

/* class ini berfungsi untuk mengelola pintu masuk
kita bisa instansiasi gateway dengan tipe data berbeda (baik V1 atau V2)
*/

class IntegrationGateway<T extends UniversalRecord> {
    private List<T> daftarPasien;

    public IntegrationGateway(List<T> records) {
        this.daftarPasien = records;
    }

    public SecureResponse<T> fetchData(String id, int nilaiRequester) {
        T dataPasien = null;
        for (T record : daftarPasien) {
            if (record.getPatientId().equals(id)) {
                dataPasien = record;
                break;
            }
        }

        if (dataPasien == null) {
            return new SecureResponse<>(false, null, "ID pasien tidak ditemukan", nilaiRequester);
        }

        String warning = "";
        if (nilaiRequester < dataPasien.getSecurityLevel()) {
            warning = "Akses rendah! Beberapa field sensitif disamarkan pada tampilan.";
        }

        return new SecureResponse<>(true, dataPasien, warning, nilaiRequester);
    }
}
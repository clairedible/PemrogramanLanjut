package LembarKerja5;

/* class ini berfungsi untuk menyimpan respons aman dari API 
class ini bisa digunakan untuk pasien V1 maupun V2
*/

class SecureResponse<T extends UniversalRecord> {
    private boolean success;
    private T data; // bisa digunakan oleh V1 atau V2
    private String warningMessage;
    private final int nilaiPengakses; // level akses yang digunakan untuk request ini

    /* untuk menginstansiasi SecureResponse, kita perlu memberikan informasi apakah request berhasil, 
    data yang ditemukan (jika ada), pesan peringatan (jika akses rendah), dan level akses yang digunakan untuk request tersebut.
    */
    public SecureResponse(boolean success, T data, String warningMessage, int nilaiPengakses) {
        this.success = success; // jika data sukses diambil tanpa dimasking = true
        this.data = data;
        this.warningMessage = warningMessage; // jika data tidak bisa diambil, berisi pesan peringatan
        this.nilaiPengakses = nilaiPengakses; // nilai akses yang digunakan untuk melakukan request
    }

    public void displayInfo (String title, String expectation) {
        System.out.println(" ╭─── " + title + " ");
        System.out.println(" │ Tujuan     : " + expectation);
        System.out.println(" ├─────────────────────────────────────────────────────────────────────────────────");
        System.out.println(" │ Status API : " + (success ? "[ SUCCESS ]" : "[ FAILED ]"));
        
        if (warningMessage != null && !warningMessage.isEmpty()) {
            System.out.println(" │ Warning    : (!) " + warningMessage);
        }
        String payloadStr = (data != null) ? data.maskSensitiveData(nilaiPengakses) : "null";
        System.out.println(" │ Payload    : " + payloadStr);
        System.out.println(" ╰─────────────────────────────────────────────────────────────────────────────────\n");
    }
}


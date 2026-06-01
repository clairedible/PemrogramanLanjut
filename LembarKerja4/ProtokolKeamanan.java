package LembarKerja4;

final class ProtokolKeamanan {
    public final String ID_SERVER;

    public ProtokolKeamanan(String ID_SERVER) {
        this.ID_SERVER = ID_SERVER;
    }

    public void validasiKeamanan(Rekening rek) {
        System.out.println("Memproses di Server: " + ID_SERVER);
        boolean valid = rek.validasiPin();
        if (valid) {
            System.out.println("Status: TERVERIFIKASI");
            System.out.println("---------------------------------");
            rek.setIsVerified(true);
        } else {
            System.out.println("Status: DITOLAK");
            System.out.println("---------------------------------");
            rek.setIsVerified(false);
        }
    }
}

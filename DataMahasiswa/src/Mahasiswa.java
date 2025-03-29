public class Mahasiswa {
    private String nim;
    private String nama;
    private String jenisKelamin;
    private double IPKsemester;
    public Mahasiswa(String nim, String nama, String jenisKelamin, double IPKsemester) {
        this.nim = nim;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.IPKsemester =IPKsemester;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }
    public void setIPKsemester(double IPKsemester) {
        this.IPKsemester = IPKsemester;
    }

    public String getNim() {
        return this.nim;
    }

    public String getNama() {
        return this.nama;
    }

    public String getJenisKelamin() {
        return this.jenisKelamin;
    }

    public double getIPKsemester() {
        return this.IPKsemester;
    }
}

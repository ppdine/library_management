package model;

public class DocGia {
    private int maDocGia;
    private String tenDocGia;
    private String soDienThoai;
    private String email;

    public DocGia(int maDocGia, String tenDocGia, String soDienThoai, String email) {
        this.maDocGia = maDocGia;
        this.tenDocGia = tenDocGia;
        this.soDienThoai = soDienThoai;
        this.email = email;
    }

    // Getters và setters
    public int getMaDocGia() { return maDocGia; }
    public void setMaDocGia(int maDocGia) { this.maDocGia = maDocGia; }

    public String getTenDocGia() { return tenDocGia; }
    public void setTenDocGia(String tenDocGia) { this.tenDocGia = tenDocGia; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

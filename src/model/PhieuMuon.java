package model;

import java.util.Date;

public class PhieuMuon {
    private int maPhieuMuon;
    private int maDocGia;
    private int maSach;
    private Date ngayMuon;
    private Date ngayTra;
    private int soLuongMuon;

    public PhieuMuon(int maPhieuMuon, int maDocGia, int maSach, Date ngayMuon, Date ngayTra, int soLuongMuon) {
        this.maPhieuMuon = maPhieuMuon;
        this.maDocGia = maDocGia;
        this.maSach = maSach;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
        this.soLuongMuon = soLuongMuon;
    }

    // Getters và setters
    public int getMaPhieuMuon() { return maPhieuMuon; }
    public void setMaPhieuMuon(int maPhieuMuon) { this.maPhieuMuon = maPhieuMuon; }

    public int getMaDocGia() { return maDocGia; }
    public void setMaDocGia(int maDocGia) { this.maDocGia = maDocGia; }

    public int getMaSach() { return maSach; }
    public void setMaSach(int maSach) { this.maSach = maSach; }

    public Date getNgayMuon() { return ngayMuon; }
    public void setNgayMuon(Date ngayMuon) { this.ngayMuon = ngayMuon; }

    public Date getNgayTra() { return ngayTra; }
    public void setNgayTra(Date ngayTra) { this.ngayTra = ngayTra; }

    public int getSoLuongMuon() { return soLuongMuon; }
    public void setSoLuongMuon(int soLuongMuon) { this.soLuongMuon = soLuongMuon; }
}

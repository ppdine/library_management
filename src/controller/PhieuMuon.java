package controller;

import dao.DAO;
import dao.PhieuMuonDAO;
import view.PhieuMuonView;

import javax.swing.*;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhieuMuon {
    private PhieuMuonDAO phieuMuonDAO;
    private PhieuMuonView view;

    public PhieuMuon(PhieuMuonView view) {
        this.phieuMuonDAO = new PhieuMuonDAO();
        this.view = view;
    }

    public List<model.PhieuMuon> getAllPhieuMuon() {
        return phieuMuonDAO.getAllPhieuMuon();
    }

    public List<model.PhieuMuon> searchPhieuMuonByMaDocGia(int maDocGia) {
        return phieuMuonDAO.searchPhieuMuonByMaDocGia(maDocGia);
    }

    public model.PhieuMuon getPhieuMuonById(int maPhieuMuon) {
        return phieuMuonDAO.getPhieuMuonById(maPhieuMuon);
    }

    public void deletePhieuMuon(int maPhieuMuon) {
        phieuMuonDAO.deletePhieuMuon(maPhieuMuon);
    }

    public void addPhieuMuon(int maDocGia, int maSach, Date ngayMuon, Date ngayTra, int soLuongMuon) throws Exception {
        // Kiểm tra tình trạng sách
        if (isBookStatusPending(maSach)) {
            JOptionPane.showMessageDialog(view, "Sách hiện đang tạm hết do đang nhập thêm.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        validatePhieuMuonData(maDocGia, maSach, soLuongMuon);
        model.PhieuMuon phieuMuon = new model.PhieuMuon(0, maDocGia, maSach, ngayMuon, ngayTra, soLuongMuon);
        phieuMuonDAO.addPhieuMuon(phieuMuon);
    }

    public void updatePhieuMuon(model.PhieuMuon phieuMuon, int maDocGia, int maSach, Date ngayMuon, Date ngayTra, int soLuongMuon) throws Exception {
        // Kiểm tra tình trạng sách
        if (isBookStatusPending(maSach)) {
            JOptionPane.showMessageDialog(view, "Sách hiện đang tạm hết do đang nhập thêm.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        validatePhieuMuonData(maDocGia, maSach, soLuongMuon);
        phieuMuon.setMaDocGia(maDocGia);
        phieuMuon.setMaSach(maSach);
        phieuMuon.setNgayMuon(ngayMuon);
        phieuMuon.setNgayTra(ngayTra);
        phieuMuon.setSoLuongMuon(soLuongMuon);
        phieuMuonDAO.updatePhieuMuon(phieuMuon);
    }

    private void validatePhieuMuonData(int maDocGia, int maSach, int soLuongMuon) throws Exception {
        if (maDocGia <= 0 || maSach <= 0 || soLuongMuon <= 0) {
            throw new Exception("Vui lòng điền đầy đủ thông tin hợp lệ!");
        }
    }

    public void handleSearch(String searchText) {
        try {
            int maDocGia = Integer.parseInt(searchText.trim());
            List<model.PhieuMuon> searchResults = searchPhieuMuonByMaDocGia(maDocGia);
            view.updateTableData(searchResults);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập mã độc giả hợp lệ!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            view.clearSearch();
            view.updateTableData(getAllPhieuMuon());
        }
    }

    // Thêm phương thức kiểm tra tình trạng sách
    public boolean isBookStatusPending(int maSach) {
        // Giả sử bạn có cơ sở dữ liệu và có phương thức kiểm tra tình trạng sách
        // Ví dụ: Truy vấn cơ sở dữ liệu để kiểm tra tình trạng sách
        String query = "SELECT tinh_trang FROM sach WHERE ma_sach = ?";
        try (Connection conn = DAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, maSach);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String tinhTrang = rs.getString("tinh_trang");
                return "Đang nhập".equals(tinhTrang);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Nếu không tìm thấy sách hoặc xảy ra lỗi, mặc định trả về false
    }
}

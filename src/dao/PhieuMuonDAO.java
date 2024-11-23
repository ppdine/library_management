package dao;

import model.PhieuMuon;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuMuonDAO {
    // Lấy danh sách tất cả phiếu mượn
    public List<PhieuMuon> getAllPhieuMuon() {
        List<PhieuMuon> phieuMuonList = new ArrayList<>();
        String query = "SELECT * FROM phieu_muon";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PhieuMuon phieuMuon = new PhieuMuon(
                        rs.getInt("ma_phieu_muon"),
                        rs.getInt("ma_doc_gia"),
                        rs.getInt("ma_sach"),
                        rs.getDate("ngay_muon"),
                        rs.getDate("ngay_tra"),
                        rs.getInt("so_luong_muon")
                );
                phieuMuonList.add(phieuMuon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phieuMuonList;
    }

    // Thêm phiếu mượn mới
    public void addPhieuMuon(PhieuMuon phieuMuon) {
        // Kiểm tra sự tồn tại của độc giả
        String readerQuery = "SELECT COUNT(*) FROM doc_gia WHERE ma_doc_gia = ?";
        String availabilityQuery = "SELECT so_luong, tinh_trang FROM sach WHERE ma_sach = ?";

        try (Connection conn = DAO.getConnection();
             PreparedStatement readerStmt = conn.prepareStatement(readerQuery)) {

            // Kiểm tra sự tồn tại của độc giả
            readerStmt.setInt(1, phieuMuon.getMaDocGia());
            ResultSet readerRs = readerStmt.executeQuery();

            if (readerRs.next() && readerRs.getInt(1) == 0) {
                throw new SQLException("Mã độc giả " + phieuMuon.getMaDocGia() + " không tồn tại trong hệ thống.");
            }

            // Kiểm tra sách
            try (PreparedStatement availabilityStmt = conn.prepareStatement(availabilityQuery)) {
                availabilityStmt.setInt(1, phieuMuon.getMaSach());
                ResultSet rs = availabilityStmt.executeQuery();

                if (rs.next()) {
                    int availableBooks = rs.getInt("so_luong");
                    String bookStatus = rs.getString("tinh_trang");

                    // Kiểm tra tình trạng sách (Đang nhập)
                    if (bookStatus.equals("Đang nhập")) {
                        // Thông báo tạm hết sách
                        throw new SQLException("Sách hiện đang tạm hết do đang nhập thêm.");
                    }

                    // Kiểm tra số lượng sách có sẵn
                    if (availableBooks < phieuMuon.getSoLuongMuon()) {
                        throw new SQLException("Không đủ sách để mượn. Chỉ còn " + availableBooks + " cuốn.");
                    }
                } else {
                    throw new SQLException("Sách không tồn tại.");
                }

                // Nếu mọi kiểm tra đều thành công, tiến hành thêm phiếu mượn
                String query = "INSERT INTO phieu_muon (ma_doc_gia, ma_sach, ngay_muon, ngay_tra, so_luong_muon) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setInt(1, phieuMuon.getMaDocGia());
                    ps.setInt(2, phieuMuon.getMaSach());
                    ps.setDate(3, new java.sql.Date(phieuMuon.getNgayMuon().getTime()));
                    ps.setDate(4, new java.sql.Date(phieuMuon.getNgayTra().getTime()));
                    ps.setInt(5, phieuMuon.getSoLuongMuon());
                    ps.executeUpdate();

                    // Giảm số lượng sách và cập nhật trạng thái
                    updateBookStatus(conn, phieuMuon.getMaSach(), phieuMuon.getSoLuongMuon(), false);
                }
            }
        } catch (SQLException e) {
            // Xử lý lỗi và ném exception với thông báo rõ ràng
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    // Sửa thông tin phiếu mượn
    public void updatePhieuMuon(PhieuMuon phieuMuon) {
        String query = "UPDATE phieu_muon SET ma_doc_gia = ?, ma_sach = ?, ngay_muon = ?, ngay_tra = ?, so_luong_muon = ? WHERE ma_phieu_muon = ?";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, phieuMuon.getMaDocGia());
            ps.setInt(2, phieuMuon.getMaSach());
            ps.setDate(3, new java.sql.Date(phieuMuon.getNgayMuon().getTime()));
            ps.setDate(4, new java.sql.Date(phieuMuon.getNgayTra().getTime()));
            ps.setInt(5, phieuMuon.getSoLuongMuon());
            ps.setInt(6, phieuMuon.getMaPhieuMuon());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa phiếu mượn
    public void deletePhieuMuon(int maPhieuMuon) {
        // Lấy thông tin phiếu mượn trước khi xóa
        PhieuMuon phieuMuon = getPhieuMuonById(maPhieuMuon);

        if (phieuMuon == null) {
            System.out.println("Phiếu mượn không tồn tại.");
            return;
        }

        String query = "DELETE FROM phieu_muon WHERE ma_phieu_muon = ?";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, maPhieuMuon);
            ps.executeUpdate();

            // Tăng lại số lượng sách và cập nhật trạng thái
            updateBookStatus(conn, phieuMuon.getMaSach(), phieuMuon.getSoLuongMuon(), true);
            System.out.println("Phiếu mượn đã được xóa và sách đã được trả lại.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Lấy phiếu mượn theo mã phiếu mượn
    public PhieuMuon getPhieuMuonById(int maPhieuMuon) {
        String query = "SELECT * FROM phieu_muon WHERE ma_phieu_muon = ?";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, maPhieuMuon);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new PhieuMuon(
                        rs.getInt("ma_phieu_muon"),
                        rs.getInt("ma_doc_gia"),
                        rs.getInt("ma_sach"),
                        rs.getDate("ngay_muon"),
                        rs.getDate("ngay_tra"),
                        rs.getInt("so_luong_muon")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm kiếm phiếu mượn theo mã độc giả
    public List<PhieuMuon> searchPhieuMuonByMaDocGia(int maDocGia) {
        List<PhieuMuon> phieuMuonList = new ArrayList<>();
        String query = "SELECT * FROM phieu_muon WHERE ma_doc_gia = ?";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, maDocGia);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PhieuMuon phieuMuon = new PhieuMuon(
                        rs.getInt("ma_phieu_muon"),
                        rs.getInt("ma_doc_gia"),
                        rs.getInt("ma_sach"),
                        rs.getDate("ngay_muon"),
                        rs.getDate("ngay_tra"),
                        rs.getInt("so_luong_muon")
                );
                phieuMuonList.add(phieuMuon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phieuMuonList;
    }

    // Cập nhật số lượng và tình trạng sách
    private void updateBookStatus(Connection conn, int maSach, int soLuong, boolean isReturn) throws SQLException {
        // Đầu tiên kiểm tra số lượng sách hiện tại
        String checkQuery = "SELECT so_luong FROM sach WHERE ma_sach = ?";
        int currentQuantity;

        try (PreparedStatement checkPs = conn.prepareStatement(checkQuery)) {
            checkPs.setInt(1, maSach);
            ResultSet rs = checkPs.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Không tìm thấy sách với mã " + maSach);
            }
            currentQuantity = rs.getInt("so_luong");
        }

        // Tính toán số lượng mới
        int newQuantity = isReturn ? currentQuantity + soLuong : currentQuantity - soLuong;

        // Cập nhật số lượng và tình trạng
        String updateQuery = "UPDATE sach " +
                "SET so_luong = ?, " +
                "tinh_trang = CASE " +
                "    WHEN ? = 0 THEN 'Hết' " +
                "    WHEN ? > 0 THEN 'Còn' " +
                "    ELSE 'Hết' " +
                "END " +
                "WHERE ma_sach = ?";

        try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            ps.setInt(1, newQuantity);    // Cập nhật số lượng mới
            ps.setInt(2, newQuantity);    // Kiểm tra số lượng = 0
            ps.setInt(3, newQuantity);    // Kiểm tra số lượng > 0
            ps.setInt(4, maSach);         // Mã sách
            ps.executeUpdate();
        }
    }



    // Kiểm tra phiếu mượn quá hạn
    public List<PhieuMuon> getPhieuMuonQuaHan() {
        List<PhieuMuon> phieuMuonQuaHan = new ArrayList<>();
        String query = "SELECT * FROM phieu_muon WHERE ngay_tra < CURRENT_DATE";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PhieuMuon phieuMuon = new PhieuMuon(
                        rs.getInt("ma_phieu_muon"),
                        rs.getInt("ma_doc_gia"),
                        rs.getInt("ma_sach"),
                        rs.getDate("ngay_muon"),
                        rs.getDate("ngay_tra"),
                        rs.getInt("so_luong_muon")
                );
                phieuMuonQuaHan.add(phieuMuon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phieuMuonQuaHan;
    }
}
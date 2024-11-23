package dao;

import model.Sach;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

public class SachDAO {
    // Lấy danh sách tất cả sách
    public List<Sach> getAllSach() {
        List<Sach> sachList = new ArrayList<>();
        String query = "SELECT * FROM sach";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Sach sach = new Sach(
                        rs.getInt("ma_sach"),
                        rs.getString("ten_sach"),
                        rs.getString("ten_tac_gia"),
                        rs.getString("nha_xuat_ban"),
                        rs.getInt("so_luong"),
                        rs.getString("tinh_trang")
                );
                sachList.add(sach);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sachList;
    }

    // Thêm sách
    public void addSach(Sach sach) {
        String query = "INSERT INTO sach (ten_sach, ten_tac_gia, nha_xuat_ban, so_luong, tinh_trang) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, sach.getTenSach());
            ps.setString(2, sach.getTenTacGia());
            ps.setString(3, sach.getNhaXuatBan());
            ps.setInt(4, sach.getSoLuong());
            ps.setString(5, sach.getTinhTrang());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Sửa thông tin sách
    public void updateSach(Sach sach) {
        String query = "UPDATE sach SET ten_sach = ?, ten_tac_gia = ?, nha_xuat_ban = ?, so_luong = ?, tinh_trang = ? WHERE ma_sach = ?";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, sach.getTenSach());
            ps.setString(2, sach.getTenTacGia());
            ps.setString(3, sach.getNhaXuatBan());
            ps.setInt(4, sach.getSoLuong());
            ps.setString(5, sach.getTinhTrang());
            ps.setInt(6, sach.getMaSach());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa sách
    public void deleteSach(int maSach) {
        String query = "DELETE FROM sach WHERE ma_sach = ?";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, maSach);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tìm sách theo tên
    public List<Sach> searchSachByName(String tenSach) {
        List<Sach> sachList = new ArrayList<>();
        String query = "SELECT * FROM sach WHERE ten_sach LIKE ?";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + tenSach + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Sach sach = new Sach(
                        rs.getInt("ma_sach"),
                        rs.getString("ten_sach"),
                        rs.getString("ten_tac_gia"),
                        rs.getString("nha_xuat_ban"),
                        rs.getInt("so_luong"),
                        rs.getString("tinh_trang")
                );
                sachList.add(sach);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sachList;
    }
    // Phương thức lấy tổng số sách
    public int getTotalBooks() {
        String query = "SELECT COUNT(*) FROM sach";
        try (Connection conn = DAO.getConnection(); // Sử dụng kết nối từ getConnection()
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1); // Trả về tổng số sách
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Phương thức lấy tổng số người dùng
    public int getTotalUsers() {
        String query = "SELECT COUNT(*) FROM doc_gia";
        try (Connection conn = DAO.getConnection(); // Sử dụng kết nối từ getConnection()
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1); // Trả về tổng số người dùng
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Phương thức lấy tổng số phiếu mượn
    public int getTotalLoanTickets() {
        String query = "SELECT COUNT(*) FROM phieu_muon";
        try (Connection conn = DAO.getConnection(); // Sử dụng kết nối từ getConnection()
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1); // Trả về tổng số phiếu mượn
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}

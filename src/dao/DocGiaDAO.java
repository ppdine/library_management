package dao;

import model.DocGia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocGiaDAO {
    // Lấy danh sách tất cả độc giả
    public List<DocGia> getAllDocGia() {
        List<DocGia> docGiaList = new ArrayList<>();
        String query = "SELECT * FROM doc_gia";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DocGia docGia = new DocGia(
                        rs.getInt("ma_doc_gia"),
                        rs.getString("ten_doc_gia"),
                        rs.getString("so_dien_thoai"),
                        rs.getString("email")
                );
                docGiaList.add(docGia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return docGiaList;
    }

    // Thêm độc giả mới
    public void addDocGia(DocGia docGia) {
        String query = "INSERT INTO doc_gia (ten_doc_gia, so_dien_thoai, email) VALUES (?, ?, ?)";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, docGia.getTenDocGia());
            ps.setString(2, docGia.getSoDienThoai());
            ps.setString(3, docGia.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Sửa thông tin độc giả
    public void updateDocGia(DocGia docGia) {
        String query = "UPDATE doc_gia SET ten_doc_gia = ?, so_dien_thoai = ?, email = ? WHERE ma_doc_gia = ?";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, docGia.getTenDocGia());
            ps.setString(2, docGia.getSoDienThoai());
            ps.setString(3, docGia.getEmail());
            ps.setInt(4, docGia.getMaDocGia());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa độc giả theo mã độc giả
    public void deleteDocGia(int maDocGia) {
        String query = "DELETE FROM doc_gia WHERE ma_doc_gia = ?";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, maDocGia);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tìm kiếm độc giả theo tên
    public List<DocGia> searchDocGiaByName(String tenDocGia) {
        List<DocGia> docGiaList = new ArrayList<>();
        String query = "SELECT * FROM doc_gia WHERE ten_doc_gia LIKE ?";
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + tenDocGia + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DocGia docGia = new DocGia(
                        rs.getInt("ma_doc_gia"),
                        rs.getString("ten_doc_gia"),
                        rs.getString("so_dien_thoai"),
                        rs.getString("email")
                );
                docGiaList.add(docGia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return docGiaList;
    }
}

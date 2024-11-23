package controller;

import dao.SachDAO;
import view.LoginFormView;
import view.SachDialog;
import view.SachView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class SachC {
    private final SachView sachView;
    private final SachDAO sachDAO;
    private DefaultTableModel tableModel;

    public SachC(SachView sachView) {
        this.sachView = sachView;
        this.sachDAO = new SachDAO();
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public void loadSachData() {
        tableModel.setRowCount(0);
        List<model.Sach> sachList = sachDAO.getAllSach();
        for (model.Sach sach : sachList) {
            addSachToTable(sach);
        }
    }

    private void addSachToTable(model.Sach sach) {
        Object[] row = {
                sach.getMaSach(),
                sach.getTenSach(),
                sach.getTenTacGia(),
                sach.getNhaXuatBan(),
                sach.getSoLuong(),
                sach.getTinhTrang()
        };
        tableModel.addRow(row);
    }

    public void handleAddSach() {
        SachDialog dialog = new SachDialog(
                SwingUtilities.getWindowAncestor(sachView),
                "Thêm Sách Mới",
                null,
                this
        );
        dialog.setVisible(true);
        if (dialog.isDataSubmitted()) {
            loadSachData();
        }
    }

    public void handleEditSach(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int maSach = (int) table.getValueAt(selectedRow, 0);
            model.Sach sach = sachDAO.getAllSach().stream()
                    .filter(s -> s.getMaSach() == maSach)
                    .findFirst()
                    .orElse(null);

            if (sach != null) {
                SachDialog dialog = new SachDialog(
                        SwingUtilities.getWindowAncestor(sachView),
                        "Sửa Thông Tin Sách",
                        sach,
                        this
                );
                dialog.setVisible(true);
                if (dialog.isDataSubmitted()) {
                    loadSachData();
                }
            }
        } else {
            showWarningMessage("Vui lòng chọn sách cần sửa!");
        }
    }

    public void handleDeleteSach(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int maSach = (int) table.getValueAt(selectedRow, 0);
            int confirm = showConfirmDialog("Bạn có chắc chắn muốn xóa sách này?");

            if (confirm == JOptionPane.YES_OPTION) {
                sachDAO.deleteSach(maSach);
                loadSachData();
            }
        } else {
            showWarningMessage("Vui lòng chọn sách cần xóa!");
        }
    }

    public void handleSearch(String searchTerm) {
        tableModel.setRowCount(0);
        if (!searchTerm.trim().isEmpty()) {
            List<model.Sach> searchResults = sachDAO.searchSachByName(searchTerm);
            for (model.Sach sach : searchResults) {
                addSachToTable(sach);
            }
        } else {
            loadSachData();
        }
    }

    public boolean validateSachData(String tenSach, String tacGia, String nxb) {
        if (tenSach.trim().isEmpty() || tacGia.trim().isEmpty() || nxb.trim().isEmpty()) {
            showErrorMessage("Vui lòng điền đầy đủ thông tin!");
            return false;
        }
        return true;
    }

    public void handleSubmitSach(model.Sach sachEdit, String tenSach, String tacGia, String nxb, int soLuong, String tinhTrang) {
        try {
            if (sachEdit == null) {
                // Thêm mới
                model.Sach sach = new model.Sach(0, tenSach, tacGia, nxb, soLuong, tinhTrang);
                sachDAO.addSach(sach);
            } else {
                // Cập nhật
                sachEdit.setTenSach(tenSach);
                sachEdit.setTenTacGia(tacGia);
                sachEdit.setNhaXuatBan(nxb);
                sachEdit.setSoLuong(soLuong);
                sachEdit.setTinhTrang(tinhTrang);
                sachDAO.updateSach(sachEdit);
            }
        } catch (Exception ex) {
            showErrorMessage("Có lỗi xảy ra: " + ex.getMessage());
            throw ex;
        }
    }

    private void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(sachView, message, "Thông báo", JOptionPane.WARNING_MESSAGE);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(sachView, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private int showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(sachView,
                message,
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFormView().setVisible(true);
        });
    }
}
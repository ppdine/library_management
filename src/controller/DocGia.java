package controller;

import dao.DocGiaDAO;
import view.DocGiaView;
import view.DocGiaDialog;
import javax.swing.*;
import java.util.List;

public class DocGia {
    private DocGiaDAO docGiaDAO;
    private DocGiaView docGiaView;
    private DocGiaDialog dialog;
    private model.DocGia docGiaEdit;

    public DocGia(DocGiaView docGiaView) {
        this.docGiaDAO = new DocGiaDAO();
        this.docGiaView = docGiaView;
    }

    // Main Controller Methods
    public List<model.DocGia> getAllDocGia() {
        return docGiaDAO.getAllDocGia();
    }

    public List<model.DocGia> searchDocGiaByName(String searchTerm) {
        return docGiaDAO.searchDocGiaByName(searchTerm);
    }

    public void addDocGia(model.DocGia docGia) throws Exception {
        docGiaDAO.addDocGia(docGia);
    }

    public void updateDocGia(model.DocGia docGia) throws Exception {
        docGiaDAO.updateDocGia(docGia);
    }

    public void deleteDocGia(int maDocGia) {
        docGiaDAO.deleteDocGia(maDocGia);
    }

    public model.DocGia findDocGiaById(int maDocGia) {
        return docGiaDAO.getAllDocGia().stream()
                .filter(dg -> dg.getMaDocGia() == maDocGia)
                .findFirst()
                .orElse(null);
    }

    public boolean validateDocGiaInput(String tenDocGia, String soDienThoai, String email) {
        return !tenDocGia.isEmpty() && !soDienThoai.isEmpty() && !email.isEmpty();
    }

    // Dialog Controller Methods
    public void setDialog(DocGiaDialog dialog) {
        this.dialog = dialog;
    }

    public void setDocGiaEdit(model.DocGia docGiaEdit) {
        this.docGiaEdit = docGiaEdit;
    }

    public model.DocGia getDocGiaEdit() {
        return docGiaEdit;
    }

    public boolean submitData(String tenDocGia, String soDienThoai, String email) {
        if (!validateDocGiaInput(tenDocGia, soDienThoai, email)) {
            showError("Vui lòng điền đầy đủ thông tin!");
            return false;
        }

        try {
            if (docGiaEdit == null) {
                // Thêm mới
                model.DocGia docGia = new model.DocGia(0, tenDocGia, soDienThoai, email);
                addDocGia(docGia);
            } else {
                // Cập nhật
                docGiaEdit.setTenDocGia(tenDocGia);
                docGiaEdit.setSoDienThoai(soDienThoai);
                docGiaEdit.setEmail(email);
                updateDocGia(docGiaEdit);
            }
            return true;
        } catch (Exception ex) {
            showError("Có lỗi xảy ra: " + ex.getMessage());
            return false;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(dialog,
                message,
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
    }
}
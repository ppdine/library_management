package view;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import controller.PhieuMuon;

public class PhieuMuonDialog extends JDialog {
    private JTextField txtMaDocGia;
    private JTextField txtMaSach;
    private JSpinner spnNgayMuon;
    private JSpinner spnNgayTra;
    private JSpinner spnSoLuong;
    private JButton btnSubmit;
    private JButton btnCancel;
    private boolean dataSubmitted = false;
    private PhieuMuon controller;
    private model.PhieuMuon phieuMuonEdit;

    public PhieuMuonDialog(Window owner, String title, model.PhieuMuon phieuMuon, PhieuMuon controller) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        this.controller = controller;
        this.phieuMuonEdit = phieuMuon;
        initComponents();
        if (phieuMuon != null) {
            loadPhieuMuonData(phieuMuon);
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel chứa form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblMaDocGia = new JLabel("Mã độc giả:");
        JLabel lblMaSach = new JLabel("Mã sách:");
        JLabel lblNgayMuon = new JLabel("Ngày mượn:");
        JLabel lblNgayTra = new JLabel("Ngày trả:");
        JLabel lblSoLuong = new JLabel("Số lượng mượn:");

        txtMaDocGia = new JTextField(20);
        txtMaSach = new JTextField(20);
        spnNgayMuon = new JSpinner(new SpinnerDateModel());
        spnNgayTra = new JSpinner(new SpinnerDateModel());
        spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        // Định dạng cho spinner ngày
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnNgayMuon, "dd/MM/yyyy");
        spnNgayMuon.setEditor(dateEditor);
        dateEditor = new JSpinner.DateEditor(spnNgayTra, "dd/MM/yyyy");
        spnNgayTra.setEditor(dateEditor);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblMaDocGia, gbc);
        gbc.gridx = 1;
        formPanel.add(txtMaDocGia, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblMaSach, gbc);
        gbc.gridx = 1;
        formPanel.add(txtMaSach, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblNgayMuon, gbc);
        gbc.gridx = 1;
        formPanel.add(spnNgayMuon, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lblNgayTra, gbc);
        gbc.gridx = 1;
        formPanel.add(spnNgayTra, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(lblSoLuong, gbc);
        gbc.gridx = 1;
        formPanel.add(spnSoLuong, gbc);

        // Panel chứa buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSubmit = new JButton(phieuMuonEdit == null ? "Thêm" : "Cập nhật");
        btnCancel = new JButton("Hủy");

        btnSubmit.setBackground(new Color(92, 184, 92));
        btnSubmit.setForeground(Color.WHITE);
        btnCancel.setBackground(new Color(217, 83, 79));
        btnCancel.setForeground(Color.WHITE);

        buttonPanel.add(btnSubmit);
        buttonPanel.add(btnCancel);

        btnSubmit.addActionListener(e -> submitData());
        btnCancel.addActionListener(e -> dispose());

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void loadPhieuMuonData(model.PhieuMuon phieuMuon) {
        txtMaDocGia.setText(String.valueOf(phieuMuon.getMaDocGia()));
        txtMaSach.setText(String.valueOf(phieuMuon.getMaSach()));
        spnNgayMuon.setValue(phieuMuon.getNgayMuon());
        spnNgayTra.setValue(phieuMuon.getNgayTra());
        spnSoLuong.setValue(phieuMuon.getSoLuongMuon());
    }

    private void submitData() {
        try {
            // Lấy dữ liệu từ form
            int maDocGia = Integer.parseInt(txtMaDocGia.getText().trim());
            int maSach = Integer.parseInt(txtMaSach.getText().trim());
            Date ngayMuon = (Date) spnNgayMuon.getValue();
            Date ngayTra = (Date) spnNgayTra.getValue();
            int soLuongMuon = (int) spnSoLuong.getValue();

            // Kiểm tra dữ liệu
            if (!validateInput(maDocGia, maSach, ngayMuon, ngayTra)) {
                return;
            }

            // Kiểm tra tình trạng sách
            if (controller.isBookStatusPending(maSach)) { // Giả sử bạn có phương thức isBookStatusPending() trong controller
                JOptionPane.showMessageDialog(this, "Sách hiện đang tạm hết do đang nhập thêm.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Xử lý thêm hoặc cập nhật
            if (phieuMuonEdit == null) {
                controller.addPhieuMuon(maDocGia, maSach, ngayMuon, ngayTra, soLuongMuon);
            } else {
                controller.updatePhieuMuon(phieuMuonEdit, maDocGia, maSach, ngayMuon, ngayTra, soLuongMuon);
            }

            dataSubmitted = true;
            JOptionPane.showMessageDialog(this, "Lưu dữ liệu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đúng định dạng số cho mã độc giả và mã sách!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private boolean validateInput(int maDocGia, int maSach, Date ngayMuon, Date ngayTra) {
        if (txtMaDocGia.getText().trim().isEmpty() || txtMaSach.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã độc giả và mã sách không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (ngayMuon.after(ngayTra)) {
            JOptionPane.showMessageDialog(this, "Ngày mượn không thể sau ngày trả!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isDataSubmitted() {
        return dataSubmitted;
    }
}

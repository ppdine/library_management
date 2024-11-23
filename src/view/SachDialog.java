package view;

import controller.SachC;
import javax.swing.*;
import java.awt.*;

public class SachDialog extends JDialog {
    private JTextField txtTenSach;
    private JTextField txtTacGia;
    private JTextField txtNXB;
    private JSpinner spnSoLuong;
    private JComboBox<String> cbbTinhTrang;
    private JButton btnSubmit;
    private JButton btnCancel;
    private boolean dataSubmitted = false;
    private model.Sach sachEdit;
    private SachC controller;

    public SachDialog(Window owner, String title, model.Sach sach, SachC controller) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        this.sachEdit = sach;
        this.controller = controller;
        initComponents();
        if (sach != null) {
            loadSachData(sach);
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel chứa form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tạo các components
        txtTenSach = new JTextField(20);
        txtTacGia = new JTextField(20);
        txtNXB = new JTextField(20);
        spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
        cbbTinhTrang = new JComboBox<>(new String[]{"Còn"});

        // Thêm components vào form
        addFormComponents(formPanel, gbc);

        // Panel chứa buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSubmit = new JButton(sachEdit == null ? "Thêm" : "Cập nhật");
        btnCancel = new JButton("Hủy");

        styleButtons();
        buttonPanel.add(btnSubmit);
        buttonPanel.add(btnCancel);

        // Thêm sự kiện cho buttons
        btnSubmit.addActionListener(e -> submitData());
        btnCancel.addActionListener(e -> dispose());

        // Thêm panels vào dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Cấu hình dialog
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void addFormComponents(JPanel formPanel, GridBagConstraints gbc) {
        JLabel[] labels = {
                new JLabel("Tên sách:"),
                new JLabel("Tác giả:"),
                new JLabel("Nhà xuất bản:"),
                new JLabel("Số lượng:"),
                new JLabel("Tình trạng:")
        };

        Component[] fields = {
                txtTenSach,
                txtTacGia,
                txtNXB,
                spnSoLuong,
                cbbTinhTrang
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(labels[i], gbc);
            gbc.gridx = 1;
            formPanel.add(fields[i], gbc);
        }
    }

    private void styleButtons() {
        btnSubmit.setBackground(new Color(92, 184, 92));
        btnSubmit.setForeground(Color.WHITE);
        btnCancel.setBackground(new Color(217, 83, 79));
        btnCancel.setForeground(Color.WHITE);
    }

    private void loadSachData(model.Sach sach) {
        txtTenSach.setText(sach.getTenSach());
        txtTacGia.setText(sach.getTenTacGia());
        txtNXB.setText(sach.getNhaXuatBan());
        spnSoLuong.setValue(sach.getSoLuong());
        cbbTinhTrang.setSelectedItem(sach.getTinhTrang());
    }

    private void submitData() {
        try {
            String tenSach = txtTenSach.getText().trim();
            String tacGia = txtTacGia.getText().trim();
            String nxb = txtNXB.getText().trim();
            int soLuong = (int) spnSoLuong.getValue();
            String tinhTrang = (String) cbbTinhTrang.getSelectedItem();

            // Kiểm tra dữ liệu đầu vào
            if (!validateSachData(tenSach, tacGia, nxb)) {
                return;
            }

            // Gọi controller để xử lý dữ liệu
            controller.handleSubmitSach(sachEdit, tenSach, tacGia, nxb, soLuong, tinhTrang);

            dataSubmitted = true;
            JOptionPane.showMessageDialog(this, "Lưu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception e) {
            // Hiển thị lỗi chi tiết cho người dùng
            handleError(e);
        }
    }

    private boolean validateSachData(String tenSach, String tacGia, String nxb) {
        if (tenSach.isEmpty() || tacGia.isEmpty() || nxb.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền đầy đủ thông tin (Tên sách, Tác giả, Nhà xuất bản).",
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void handleError(Exception e) {
        if (e instanceof NumberFormatException) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi: Số lượng không hợp lệ. Vui lòng nhập số nguyên dương.",
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE);
        } else if (e instanceof IllegalArgumentException) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi: " + e.getMessage(),
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Đã xảy ra lỗi: " + e.getMessage() + "\nVui lòng thử lại sau.",
                    "Lỗi hệ thống",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public boolean isDataSubmitted() {
        return dataSubmitted;
    }
}

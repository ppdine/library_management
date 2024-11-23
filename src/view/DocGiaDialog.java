package view;

import controller.DocGia;

import javax.swing.*;
import java.awt.*;

public class DocGiaDialog extends JDialog {
    private JTextField txtTenDocGia;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    private JButton btnSubmit;
    private JButton btnCancel;
    private boolean dataSubmitted = false;
    private DocGia controller;

    public DocGiaDialog(Window owner, String title, model.DocGia docGia, DocGia controller) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        this.controller = controller;
        controller.setDialog(this);
        controller.setDocGiaEdit(docGia);
        initComponents();
        if (docGia != null) {
            loadDocGiaData(docGia);
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
        JLabel lblTenDocGia = new JLabel("Tên độc giả:");
        JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
        JLabel lblEmail = new JLabel("Email:");

        txtTenDocGia = new JTextField(20);
        txtSoDienThoai = new JTextField(20);
        txtEmail = new JTextField(20);

        // Thêm components vào form
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblTenDocGia, gbc);
        gbc.gridx = 1;
        formPanel.add(txtTenDocGia, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblSoDienThoai, gbc);
        gbc.gridx = 1;
        formPanel.add(txtSoDienThoai, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblEmail, gbc);
        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);

        // Panel chứa buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSubmit = new JButton(controller.getDocGiaEdit() == null ? "Thêm" : "Cập nhật");
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

    private void loadDocGiaData(model.DocGia docGia) {
        txtTenDocGia.setText(docGia.getTenDocGia());
        txtSoDienThoai.setText(docGia.getSoDienThoai());
        txtEmail.setText(docGia.getEmail());
    }

    private boolean validateInput(String tenDocGia, String soDienThoai, String email) {
        if (tenDocGia.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên độc giả không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (soDienThoai.isEmpty() || !soDienThoai.matches("\\d{10,15}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (email.isEmpty() || !email.matches("^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "Email không đúng định dạng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void submitData() {
        String tenDocGia = txtTenDocGia.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();
        String email = txtEmail.getText().trim();

        if (!validateInput(tenDocGia, soDienThoai, email)) {
            return;
        }

        if (controller.submitData(tenDocGia, soDienThoai, email)) {
            dataSubmitted = true;
            JOptionPane.showMessageDialog(this, "Lưu dữ liệu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi lưu dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }


    public boolean isDataSubmitted() {
        return dataSubmitted;
    }
}
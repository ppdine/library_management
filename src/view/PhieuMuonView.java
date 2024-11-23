package view;

import controller.PhieuMuon;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class PhieuMuonView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton btnThem, btnSua, btnXoa;
    private PhieuMuon controller;
    private SimpleDateFormat dateFormat;

    public PhieuMuonView() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        setLayout(new BorderLayout());
        controller = new PhieuMuon(this);
        initComponents();
        loadPhieuMuonData();
    }

    private void initComponents() {
        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Tìm kiếm theo mã độc giả:"));
        searchField = new JTextField(20);
        searchField.addActionListener(e -> controller.handleSearch(searchField.getText()));
        searchPanel.add(searchField);

        // Tạo bảng
        createTable();
        JScrollPane scrollPane = new JScrollPane(table);

        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");

        // Thiết lập màu cho các nút
        btnThem.setBackground(new Color(92, 184, 92));
        btnThem.setForeground(Color.WHITE);
        btnSua.setBackground(new Color(240, 173, 78));
        btnSua.setForeground(Color.WHITE);
        btnXoa.setBackground(new Color(217, 83, 79));
        btnXoa.setForeground(Color.WHITE);

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);

        // Thêm các component vào panel chính
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Thêm sự kiện cho các nút
        btnThem.addActionListener(e -> showAddDialog());
        btnSua.addActionListener(e -> showEditDialog());
        btnXoa.addActionListener(e -> deletePhieuMuon());
    }

    private void createTable() {
        String[] columns = {"Mã phiếu", "Mã độc giả", "Mã sách", "Ngày mượn", "Ngày trả", "Số lượng"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
    }

    public void loadPhieuMuonData() {
        updateTableData(controller.getAllPhieuMuon());
    }

    public void updateTableData(List<model.PhieuMuon> phieuMuonList) {
        tableModel.setRowCount(0);
        for (model.PhieuMuon phieuMuon : phieuMuonList) {
            Object[] row = {
                    phieuMuon.getMaPhieuMuon(),
                    phieuMuon.getMaDocGia(),
                    phieuMuon.getMaSach(),
                    dateFormat.format(phieuMuon.getNgayMuon()),
                    dateFormat.format(phieuMuon.getNgayTra()),
                    phieuMuon.getSoLuongMuon()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddDialog() {
        PhieuMuonDialog dialog = new PhieuMuonDialog(
                SwingUtilities.getWindowAncestor(this),
                "Thêm Phiếu Mượn Mới",
                null,
                controller
        );
        dialog.setVisible(true);
        if (dialog.isDataSubmitted()) {
            loadPhieuMuonData();
        }
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int maPhieuMuon = (int) table.getValueAt(selectedRow, 0);
            model.PhieuMuon phieuMuon = controller.getPhieuMuonById(maPhieuMuon);

            if (phieuMuon != null) {
                PhieuMuonDialog dialog = new PhieuMuonDialog(
                        SwingUtilities.getWindowAncestor(this),
                        "Sửa Thông Tin Phiếu Mượn",
                        phieuMuon,
                        controller
                );
                dialog.setVisible(true);
                if (dialog.isDataSubmitted()) {
                    loadPhieuMuonData();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn phiếu mượn cần sửa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deletePhieuMuon() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int maPhieuMuon = (int) table.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa phiếu mượn này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                controller.deletePhieuMuon(maPhieuMuon);
                loadPhieuMuonData();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn phiếu mượn cần xóa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public void clearSearch() {
        searchField.setText("");
    }
}
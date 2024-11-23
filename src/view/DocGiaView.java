package view;

import controller.DocGia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DocGiaView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton btnThem, btnSua, btnXoa;
    private DocGia controller;

    public DocGiaView() {
        controller = new DocGia(this);
        initComponents();
        loadDocGiaData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchField = new JTextField(30);
        searchField.addActionListener(e -> performSearch());
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
        btnXoa.addActionListener(e -> deleteDocGia());
    }

    private void createTable() {
        String[] columns = {"Mã độc giả", "Tên độc giả", "Số điện thoại", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
    }

    public void loadDocGiaData() {
        tableModel.setRowCount(0);
        List<model.DocGia> docGiaList = controller.getAllDocGia();
        for (model.DocGia docGia : docGiaList) {
            addDocGiaToTable(docGia);
        }
    }

    private void addDocGiaToTable(model.DocGia docGia) {
        Object[] row = {
                docGia.getMaDocGia(),
                docGia.getTenDocGia(),
                docGia.getSoDienThoai(),
                docGia.getEmail()
        };
        tableModel.addRow(row);
    }

    private void showAddDialog() {
        DocGiaDialog dialog = new DocGiaDialog(SwingUtilities.getWindowAncestor(this),
                "Thêm Độc Giả Mới", null, controller);
        dialog.setVisible(true);
        if (dialog.isDataSubmitted()) {
            loadDocGiaData();
        }
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int maDocGia = (int) table.getValueAt(selectedRow, 0);
            model.DocGia docGia = controller.findDocGiaById(maDocGia);

            if (docGia != null) {
                DocGiaDialog dialog = new DocGiaDialog(SwingUtilities.getWindowAncestor(this),
                        "Sửa Thông Tin Độc Giả", docGia, controller);
                dialog.setVisible(true);
                if (dialog.isDataSubmitted()) {
                    loadDocGiaData();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn độc giả cần sửa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteDocGia() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int maDocGia = (int) table.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa độc giả này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                controller.deleteDocGia(maDocGia);
                loadDocGiaData();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn độc giả cần xóa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        tableModel.setRowCount(0);
        List<model.DocGia> searchResults = searchTerm.isEmpty() ?
                controller.getAllDocGia() :
                controller.searchDocGiaByName(searchTerm);

        for (model.DocGia docGia : searchResults) {
            addDocGiaToTable(docGia);
        }
    }
}
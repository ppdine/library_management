package view;

import controller.SachC;
import dao.SachDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SachView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton btnThem, btnSua, btnXoa;
    private SachC controller;
    private SachDAO sachDAO;

    public SachView() {
        sachDAO = new SachDAO();
        setLayout(new BorderLayout());
        initComponents();
        controller = new SachC(this);
        controller.setTableModel(tableModel);
        controller.loadSachData();
    }

    private void initComponents() {
        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchField = new JTextField(30);
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
        btnThem.addActionListener(e -> controller.handleAddSach());
        btnSua.addActionListener(e -> controller.handleEditSach(table));
        btnXoa.addActionListener(e -> controller.handleDeleteSach(table));
    }

    private void createTable() {
        String[] columns = {"Mã sách", "Tên sách", "Tên tác giả", "Nhà xuất bản", "Số lượng", "Tình trạng"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
            }
        });
    }
    void loadSachData() {
        tableModel.setRowCount(0);
        List<model.Sach> sachList = sachDAO.getAllSach();
        for (model.Sach sach : sachList) {
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
    }

}
package view;

import dao.SachDAO;

import javax.swing.*;
import java.awt.*;

public class QuanLyThuVienView extends JFrame {
    private JTabbedPane tabbedPane;
    private SachView sachView;
    private DocGiaView docGiaView;
    private PhieuMuonView phieuMuonView;
    private JPanel statisticsPanel;

    public QuanLyThuVienView() {
        setTitle("Quản Lý Thư Viện");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        sachView = new SachView();
        docGiaView = new DocGiaView();
        phieuMuonView = new PhieuMuonView();

        // Khởi tạo panel "Thống Kê"
        statisticsPanel = createStatisticsPanel();

        tabbedPane.addTab("Quản Lý Sách", sachView);
        tabbedPane.addTab("Quản Lý Độc Giả", docGiaView);
        tabbedPane.addTab("Quản Lý Phiếu Mượn", phieuMuonView);
        tabbedPane.addTab("Thống Kê", statisticsPanel);

        // Xử lý sự kiện chuyển tab
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            String selectedTab = tabbedPane.getTitleAt(selectedIndex);

            if ("Quản Lý Sách".equals(selectedTab)) {
                sachView.loadSachData(); // Tải lại dữ liệu sách
            } else if ("Thống Kê".equals(selectedTab)) {
                refreshStatisticsPanel(); // Làm mới dữ liệu "Thống Kê"
            }
        });

        add(tabbedPane, BorderLayout.CENTER);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();

        JMenu menuHeThong = new JMenu("Hệ Thống");
        JMenuItem menuItemDangXuat = new JMenuItem("Đăng Xuất");
        JMenuItem menuItemThoat = new JMenuItem("Thoát");

        menuItemDangXuat.addActionListener(e -> logout());
        menuItemThoat.addActionListener(e -> System.exit(0));

        menuHeThong.add(menuItemDangXuat);
        menuHeThong.addSeparator();
        menuHeThong.add(menuItemThoat);

        menuBar.add(menuHeThong);

        JMenu menuTroGiup = new JMenu("Trợ Giúp");
        JMenuItem menuItemHuongDan = new JMenuItem("Hướng Dẫn Sử Dụng");
        JMenuItem menuItemThongTin = new JMenuItem("Thông Tin Phần Mềm");

        menuItemHuongDan.addActionListener(e -> showHelp());
        menuItemThongTin.addActionListener(e -> showAbout());

        menuTroGiup.add(menuItemHuongDan);
        menuTroGiup.add(menuItemThongTin);

        menuBar.add(menuTroGiup);

        setJMenuBar(menuBar);
    }

    // Phương thức làm mới dữ liệu "Thống Kê"
    private void refreshStatisticsPanel() {
        tabbedPane.setComponentAt(tabbedPane.indexOfTab("Thống Kê"), createStatisticsPanel());
    }

    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10)); // Sử dụng GridLayout để sắp xếp 3 hàng với khoảng cách giữa các ô

        // Tạo DAO để lấy dữ liệu
        SachDAO sachDAO = new SachDAO();
        int totalBooks = sachDAO.getTotalBooks();
        int totalUsers = sachDAO.getTotalUsers();
        int totalLoanTickets = sachDAO.getTotalLoanTickets();

        JPanel bookPanel = createStatPanel(
                new Color(255, 102, 51), // Màu cam
                totalBooks + "",
                "Số sách hiện có"
        );

        JPanel userPanel = createStatPanel(
                new Color(51, 153, 102), // Màu xanh lá
                totalUsers + "",
                "Số độc giả"

        );

        JPanel loanPanel = createStatPanel(
                new Color(204, 51, 51), // Màu đỏ
                totalLoanTickets + "",
                "Số phiếu mượn"

        );

        panel.add(bookPanel);
        panel.add(userPanel);
        panel.add(loanPanel);

        return panel;
    }

    private JPanel createStatPanel(Color bgColor, String statValue, String statLabel) {
        JPanel statPanel = new JPanel();
        statPanel.setBackground(bgColor);
        statPanel.setLayout(new BorderLayout(10, 10));

        JLabel valueLabel = new JLabel(statValue, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 40));
        valueLabel.setForeground(Color.WHITE);

        JLabel label = new JLabel(statLabel, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);

        JPanel textPanel = new JPanel();
        textPanel.setBackground(bgColor);
        textPanel.setLayout(new GridLayout(2, 1));
        textPanel.add(valueLabel);
        textPanel.add(label);

        statPanel.add(textPanel, BorderLayout.CENTER);

        return statPanel;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFormView().setVisible(true);
        }
    }

    private void showHelp() {
        String helpMessage = "HƯỚNG DẪN SỬ DỤNG\n\n" +
                "1. Quản Lý Sách:\n" +
                "- Thêm, Sửa, Xóa, Tìm kiếm.\n\n" +
                "2. Quản Lý Độc Giả:\n" +
                "- Thêm, Sửa, Xóa, Tìm kiếm.\n\n" +
                "3. Quản Lý Phiếu Mượn:\n" +
                "- Thêm, Sửa, Xóa, Tìm kiếm.\n\n" +
                "4. Hệ Thống:\n" +
                "- Đăng Xuất, Thoát.\n\n" +
                "5. Trợ Giúp:\n" +
                "- Hướng Dẫn, Thông Tin.\n\n" +
                "Hỗ trợ: lyhotuanan2004@gmail.com";

        JOptionPane.showMessageDialog(this, helpMessage, "Hướng Dẫn Sử Dụng", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this, "Phần mềm quản lý thư viện\nPhiên bản 1.0\nTác giả: Tuấn An", "Thông Tin Phần Mềm", JOptionPane.INFORMATION_MESSAGE);
    }
}
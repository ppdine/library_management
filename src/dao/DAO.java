package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {
    private static final String DATABASE = "quanlythuvien";
    private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE + "?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    private static boolean isConnected = false;

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (!isConnected) {
                System.out.println("Kết nối tới cơ sở dữ liệu thành công!");
                isConnected = true;
            }
        } catch (SQLException e) {
            System.out.println("Kết nối tới cơ sở dữ liệu thất bại: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}
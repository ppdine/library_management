## Yêu cầu hệ thống
- Java 23: https://www.oracle.com/cis/java/technologies/downloads/#java23
- MySQL: https://dev.mysql.com/downloads/installer/
- IntelliJ IDEA: https://www.jetbrains.com/idea/download/?section=windows
- MySql JDBC Driver: https://dbschema.com/jdbc-drivers/MySqlJdbcDriver.zip

## Chức năng
- Đăng ký và đăng nhập tài khoản (dùng MySQL để lưu trữ thông tin).
- Tìm kiếm
- Quản lý sách
- Quản lý người dùng
- Quản lý phiếu mượn sách
- Thống kê dữ liệu

## Hướng dẫn cài đặt
1. Clone repository:

    ```bash
    https://github.com/ppdine/library_management.git
    ```

2. Cấu hình MySQL:
    - Tạo một database trong MySQL (ví dụ: `quanlythuvien`).
    - Chạy các lệnh SQL dưới đây trong file `quanlythuvien.sql`.<br><br>
      ```sql
        CREATE DATABASE quanlythuvien;
        USE quanlythuvien;
        CREATE TABLE user (
        ID int AUTO_INCREMENT PRIMARY KEY,
        username varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs UNIQUE,
        password varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs
        );
        CREATE TABLE sach (
        ma_sach INT AUTO_INCREMENT PRIMARY KEY,
        ten_sach VARCHAR(255) NOT NULL,
        ten_tac_gia VARCHAR(255) NOT NULL,
        nha_xuat_ban VARCHAR(255),
        so_luong INT NOT NULL,
        tinh_trang VARCHAR(255) NOT NULL
        );
        CREATE TABLE doc_gia (
        ma_doc_gia INT AUTO_INCREMENT PRIMARY KEY,
        ten_doc_gia VARCHAR(255) NOT NULL,
        so_dien_thoai VARCHAR(15),
        email VARCHAR(255)
        );
        CREATE TABLE phieu_muon (
        ma_phieu_muon INT AUTO_INCREMENT PRIMARY KEY,
        ma_doc_gia INT NOT NULL,
        ma_sach INT NOT NULL,
        ngay_muon DATE NOT NULL,
        ngay_tra DATE,
        so_luong_muon INT NOT NULL,
        FOREIGN KEY (ma_doc_gia) REFERENCES doc_gia(ma_doc_gia) ON DELETE CASCADE,
        FOREIGN KEY (ma_sach) REFERENCES sach(ma_sach) ON DELETE CASCADE
        );

3. Import dự án vào IntelliJ
    - Thêm MySql JDBC Driver vào modules
    - Cấu hình kết nối MySQL ở `Dao.java`. <br><br>
      ```java
      final String DATABASE_NAME = "database_name";
      ...
      final String JDBC_USER = "username";
      final String JDBC_PASSWORD = "password";
      

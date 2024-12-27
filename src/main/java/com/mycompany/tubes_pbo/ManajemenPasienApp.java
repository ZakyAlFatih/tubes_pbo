package com.mycompany.tubes_pbo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ManajemenPasienApp extends JFrame {
    private Connection connection;

    public ManajemenPasienApp() {
        setTitle("Manajemen Pasien");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);  // Ukuran yang lebih pas untuk fullscreen
        setLocationRelativeTo(null);

        // Koneksi ke Database
        connectToDatabase();

        // Tampilkan halaman login sebagai default
        showLoginPage();

    }

    private void connectToDatabase() {
        try {
            // Driver MySQL JDBC
            String url = "jdbc:mysql://localhost:3308/hos5"; // Ganti localhost dengan alamat server database Anda jika perlu
            String username = "root"; // Ganti dengan username database Anda
            String password = ""; // Ganti dengan password database Anda jika diperlukan

            // Membuat koneksi
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Koneksi ke database berhasil!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal terhubung ke database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    private void showLoginPage() {
        getContentPane().removeAll();
        JPanel loginPanel = new JPanel(new BorderLayout(20, 20));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Judul
        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginPanel.add(titleLabel, BorderLayout.NORTH);

        // Form Login
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblEmail = new JLabel("Email:");
        JTextField tfEmail = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField pfPassword = new JPasswordField();
        JButton btnLogin = new JButton("Login");
        JButton btnGoToRegister = new JButton("Belum punya akun? Register");

        formPanel.add(lblEmail);
        formPanel.add(tfEmail);
        formPanel.add(lblPassword);
        formPanel.add(pfPassword);
        formPanel.add(btnLogin);
        formPanel.add(btnGoToRegister);

        loginPanel.add(formPanel, BorderLayout.CENTER);

        // Event Listener
        btnLogin.addActionListener(e -> loginPatient(tfEmail.getText(), new String(pfPassword.getPassword())));
        btnGoToRegister.addActionListener(e -> showRegisterPage());

        // Tambahkan panel login ke frame
        add(loginPanel);
        revalidate();
        repaint();
    }

    private void showRegisterPage() {
        getContentPane().removeAll();
        JPanel registerPanel = new JPanel(new BorderLayout(20, 20));
        registerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Judul
        JLabel titleLabel = new JLabel("Register", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        registerPanel.add(titleLabel, BorderLayout.NORTH);

        // Form Register
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblEmail = new JLabel("Email:");
        JTextField tfEmail = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField pfPassword = new JPasswordField();
        JLabel lblNama = new JLabel("Nama:");
        JTextField tfNama = new JTextField();
        JLabel lblUsia = new JLabel("Usia:");
        JTextField tfUsia = new JTextField();
        JLabel lblJenisKelamin = new JLabel("Jenis Kelamin:");
        JTextField tfJenisKelamin = new JTextField();
        JLabel lblNoTelepon = new JLabel("No Telepon:");
        JTextField tfNoTelepon = new JTextField();

        JButton btnRegister = new JButton("Register");
        JButton btnGoToLogin = new JButton("Sudah punya akun? Login");

        formPanel.add(lblEmail);
        formPanel.add(tfEmail);
        formPanel.add(lblPassword);
        formPanel.add(pfPassword);
        formPanel.add(lblNama);
        formPanel.add(tfNama);
        formPanel.add(lblUsia);
        formPanel.add(tfUsia);
        formPanel.add(lblJenisKelamin);
        formPanel.add(tfJenisKelamin);
        formPanel.add(lblNoTelepon);
        formPanel.add(tfNoTelepon);
        formPanel.add(btnRegister);
        formPanel.add(btnGoToLogin);

        registerPanel.add(formPanel, BorderLayout.CENTER);

        // Event Listener
        btnRegister.addActionListener(e -> registerPatient(tfEmail.getText(), new String(pfPassword.getPassword()), tfNama.getText(),
                tfUsia.getText(), tfJenisKelamin.getText(), tfNoTelepon.getText()));
        btnGoToLogin.addActionListener(e -> showLoginPage());

        // Tambahkan panel register ke frame
        add(registerPanel);
        revalidate();
        repaint();
    }

    private void registerPatient(String email, String password, String nama, String usia, String jenisKelamin, String noTelepon) {
        if (email.isEmpty() || password.isEmpty() || nama.isEmpty() || usia.isEmpty() || jenisKelamin.isEmpty() || noTelepon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String sql = "INSERT INTO pasien (email, password, nama, usia, jenis_kelamin, no_telepon) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, nama);
            statement.setInt(4, Integer.parseInt(usia));
            statement.setString(5, jenisKelamin);
            statement.setString(6, noTelepon);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Pendaftaran berhasil!");
            showLoginPage();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mendaftarkan pasien: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // modif by jak
    private void loginPatient(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email dan password harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String sql = "SELECT * FROM pasien WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(this, "Login berhasil! Selamat datang, " + resultSet.getString("nama"));

                // Setelah login berhasil, buka View dan tutup ManajemenPasienApp
                this.dispose(); // Menutup ManajemenPasienApp
                new View().setVisible(true); // Membuka tampilan View
            } else {
                JOptionPane.showMessageDialog(this, "Email atau password salah!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal melakukan login: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }





}
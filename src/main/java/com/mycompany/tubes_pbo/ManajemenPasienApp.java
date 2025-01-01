package com.mycompany.tubes_pbo;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class ManajemenPasienApp extends JFrame {
    private Connection connection;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public ManajemenPasienApp() {
        setTitle("Manajemen Pasien");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        connectToDatabase();

        // Menyiapkan CardLayout untuk navigasi antar halaman
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        add(cardPanel);

        showDashboardPage();
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3308/hos5";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Koneksi ke database berhasil!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal terhubung ke database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showDashboardPage() {
        JPanel dashboardPanel = createStyledPanel();

        // Judul
        JLabel titleLabel = createStyledTitle("Welcome to HospitalFivee");
        dashboardPanel.add(titleLabel, BorderLayout.NORTH);

        // Teks "Login as ..."
        JLabel loginAsLabel = new JLabel("Login as ...", SwingConstants.CENTER);
        loginAsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dashboardPanel.add(loginAsLabel, BorderLayout.CENTER);

        // Tombol Pilihan
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(Color.decode("#cfeef0"));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnAdmin = createStyledButton("Admin");
        JButton btnPasient = createStyledButton("Pasien");

        buttonPanel.add(btnAdmin);
        buttonPanel.add(btnPasient);

        dashboardPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Event Listener
        btnAdmin.addActionListener(e -> showAdminLoginPage());
        btnPasient.addActionListener(e -> showLoginPage());

        cardPanel.add(dashboardPanel, "Dashboard");

        cardLayout.show(cardPanel, "Dashboard");
    }

    private void showAdminLoginPage() {
        JPanel adminLoginPanel = createStyledPanel();

        // Judul
        JLabel titleLabel = createStyledTitle("Admin Login");
        adminLoginPanel.add(titleLabel, BorderLayout.NORTH);

        // Form Login
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBackground(Color.decode("#cfeef0"));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblUsername = new JLabel("Username:");
        JTextField tfUsername = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField pfPassword = new JPasswordField();

        JButton btnLogin = createStyledButton("Login");

        formPanel.add(lblUsername);
        formPanel.add(tfUsername);
        formPanel.add(lblPassword);
        formPanel.add(pfPassword);
        formPanel.add(new JLabel()); // Placeholder
        formPanel.add(btnLogin);

        adminLoginPanel.add(formPanel, BorderLayout.CENTER);

        // Add Back Button to the top-left corner
        addBackButton(adminLoginPanel);

        // Event Listener
        btnLogin.addActionListener((ActionEvent e) -> {
            String username = tfUsername.getText();
            String password = new String(pfPassword.getPassword());

            if ("admin".equals(username) && "admin1234".equals(password)) {
                JOptionPane.showMessageDialog(this, "Admin successfully logged In!");
                this.dispose(); // Menutup ManajemenPasienApp mod by jak
                new View().setVisible(true); // Membuka tampilan View
            } else {
                JOptionPane.showMessageDialog(this, "Username or password is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cardPanel.add(adminLoginPanel, "AdminLogin");
        cardLayout.show(cardPanel, "AdminLogin");
    }

    private void showLoginPage() {
        JPanel loginPanel = createStyledPanel();

        // Judul
        JLabel titleLabel = createStyledTitle("Login");
        loginPanel.add(titleLabel, BorderLayout.NORTH);

        // Form Login
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(Color.decode("#cfeef0"));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblEmail = new JLabel("Email:");
        JTextField tfEmail = new JTextField(15);
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField pfPassword = new JPasswordField(15);
        JButton btnLogin = createStyledButton("Login");
        JButton btnGoToRegister = createStyledButton("Register");

        formPanel.add(lblEmail);
        formPanel.add(tfEmail);
        formPanel.add(lblPassword);
        formPanel.add(pfPassword);
        formPanel.add(btnLogin);
        formPanel.add(btnGoToRegister);

        loginPanel.add(formPanel, BorderLayout.CENTER);

        // Add Back Button to the top-left corner
        addBackButton(loginPanel);

        // Event Listener
        btnLogin.addActionListener(e -> loginPatient(tfEmail.getText(), new String(pfPassword.getPassword())));
        btnGoToRegister.addActionListener(e -> showRegisterPage());

        cardPanel.add(loginPanel, "Login");
        cardLayout.show(cardPanel, "Login");
    }


    //mod by jak
    private void showRegisterPage() {
        JPanel registerPanel = createStyledPanel();
        registerPanel.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = createStyledTitle("Register");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x004B6B));  // Color for the title
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        registerPanel.add(titleLabel, BorderLayout.NORTH);

        // Form Register (using GridBagLayout for better control over alignment)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.decode("#cfeef0"));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));  // Add some padding around the form
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);  // Space between components

        // Labels and Text Fields
        JLabel lblEmail = new JLabel("Email:");
        JTextField tfEmail = new JTextField(20);
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField pfPassword = new JPasswordField(20);
        JLabel lblNama = new JLabel("Nama:");
        JTextField tfNama = new JTextField(20);
        JLabel lblUsia = new JLabel("Usia:");
        JTextField tfUsia = new JTextField(20);
        JLabel lblJenisKelamin = new JLabel("Jenis Kelamin:");
        JTextField tfJenisKelamin = new JTextField(20);
        JLabel lblNoTelepon = new JLabel("No Telepon:");
        JTextField tfNoTelepon = new JTextField(20);

        // Buttons
        JButton btnRegister = createStyledButton("Register");
        JButton btnGoToLogin = createStyledButton("Login");

// Set both buttons to have the same size
        Dimension buttonSize = new Dimension(100, 40); // Same width and height for both buttons
        btnRegister.setPreferredSize(buttonSize);
        btnGoToLogin.setPreferredSize(buttonSize);

        // Layout the form components
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblEmail, gbc);
        gbc.gridx = 1;
        formPanel.add(tfEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblPassword, gbc);
        gbc.gridx = 1;
        formPanel.add(pfPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblNama, gbc);
        gbc.gridx = 1;
        formPanel.add(tfNama, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(lblUsia, gbc);
        gbc.gridx = 1;
        formPanel.add(tfUsia, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(lblJenisKelamin, gbc);
        gbc.gridx = 1;
        formPanel.add(tfJenisKelamin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(lblNoTelepon, gbc);
        gbc.gridx = 1;
        formPanel.add(tfNoTelepon, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(16, 8, 16, 8);  // Add extra space for buttons
        formPanel.add(btnRegister, gbc);

        gbc.gridy = 7;
        formPanel.add(btnGoToLogin, gbc);

        // Add formPanel to registerPanel center
        registerPanel.add(formPanel, BorderLayout.CENTER);

        // Add Back Button to the top-left corner
        addBackButton(registerPanel);

        // Event Listeners
        btnRegister.addActionListener(e -> registerPatient(tfEmail.getText(), new String(pfPassword.getPassword()), tfNama.getText(),
                tfUsia.getText(), tfJenisKelamin.getText(), tfNoTelepon.getText()));
        btnGoToLogin.addActionListener(e -> showLoginPage());

        // Make sure cardPanel uses CardLayout
        cardPanel.setLayout(cardLayout);
        cardPanel.add(registerPanel, "Register");
        cardLayout.show(cardPanel, "Register");
    }


    private void addBackButton(JPanel panel) {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.decode("#cfeef0"));

        JButton btnBack = createStyledButton("Back");
        btnBack.setPreferredSize(new Dimension(100, 40)); // Ukuran sama dengan tombol lainnya
        btnBack.addActionListener(e -> cardLayout.show(cardPanel, "Dashboard"));

        topPanel.add(btnBack, BorderLayout.WEST); // Posisi di kiri atas
        panel.add(topPanel, BorderLayout.NORTH);
    }

    private void registerPatient(String email, String password, String nama, String usia,String jenis_kelamin, String noTelepon) {
        if (email.isEmpty() || password.isEmpty() || nama.isEmpty() || usia.isEmpty() || jenis_kelamin.isEmpty()|| noTelepon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String sql = "INSERT INTO pasien (email, password, nama, usia,jenis_kelamin, no_telepon) VALUES (?, ?, ?,?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, nama);
            statement.setInt(4, Integer.parseInt(usia));
            statement.setString(5, jenis_kelamin);
            statement.setString(6, noTelepon);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Successfully registered!");
            showLoginPage();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to register: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //mod by Jak
    // Class-level declaration of idPasien
    private int idPasien = -1; // Initialize with -1 or a value indicating no patient is logged in

    private void loginPatient(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email and password must be filled!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String sql = "SELECT * FROM pasien WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Get patient ID from the database
                this.idPasien = resultSet.getInt("id"); // Assuming the column for the ID is named "id"
                JOptionPane.showMessageDialog(this, "Login Success! Welcome, " + resultSet.getString("nama"));

                // After login success, open the patient view and close the current app
                this.dispose();
                new ViewPasien(idPasien).setVisible(true); // Passing idPasien to the next page

            } else {
                JOptionPane.showMessageDialog(this, "Email or password is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to login: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private JPanel createStyledPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.decode("#cfeef0"));
        return panel;
    }

    private JLabel createStyledTitle(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.decode("#6fb9bd"));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManajemenPasienApp app = new ManajemenPasienApp();
            app.setVisible(true);
        });
    }
}
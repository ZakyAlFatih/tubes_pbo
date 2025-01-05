package com.mycompany.tubes_pbo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ViewPasien extends JFrame {
    private int idPasien; // Declare idPasien field to hold the patient ID
    private String hospitalName = "Hospital Fivee";
    private String hospitalAddress = "Jl. Sehat No. 123, Jakarta";
    private List<String> hospitalPoli = new ArrayList<>();

    public ViewPasien(int idPasien) {
        this.idPasien = idPasien; // Assign the passed idPasien to the class field

        // Load data poli from database
        loadDataPoliFromDatabase();

        setTitle("Menu Pasien");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama dengan layout BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0xEAF6F6));

        // Header dengan desain yang lebih menarik
        JLabel header = new JLabel("Welcome to Patient Management", SwingConstants.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 28));
        header.setForeground(new Color(0x004B6B));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(header, BorderLayout.NORTH);

        // Panel untuk konten utama
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.setBackground(new Color(0xEAF6F6));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Bagian kiri: Tombol navigasi
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(0xEAF6F6));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Tombol menuju Appointment
        JButton btnAppointment = createStyledButton("Appointment");
        btnAppointment.addActionListener(this::goToAppointment);
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(btnAppointment, gbc);

        // Tombol menuju MedicalReport
        JButton btnMedicalReport = createStyledButton("Medical Report");
        btnMedicalReport.addActionListener(this::goToMedicalReport);
        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(btnMedicalReport, gbc);

        // Tombol kembali ke login
        JButton btnBack = createStyledButton("Back to Login");
        btnBack.addActionListener(this::goBackToLogin);
        gbc.gridx = 0;
        gbc.gridy = 2;
        buttonPanel.add(btnBack, gbc);

        contentPanel.add(buttonPanel);

        // Bagian kanan: Informasi Profil Rumah Sakit
        JPanel profilPanel = new JPanel();
        profilPanel.setLayout(new BoxLayout(profilPanel, BoxLayout.Y_AXIS));
        profilPanel.setBackground(new Color(0xEAF6F6));
        profilPanel.setBorder(BorderFactory.createTitledBorder("Profil Rumah Sakit"));

        profilPanel.add(createStyledLabel("Nama: " + hospitalName));
        profilPanel.add(createStyledLabel("Alamat: " + hospitalAddress));
        profilPanel.add(createStyledLabel("Daftar Poli:"));

        // Daftar Poli
        JList<String> poliList = new JList<>(hospitalPoli.toArray(new String[0]));
        poliList.setBackground(new Color(0xEAF6F6));
        poliList.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(poliList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0x004B6B), 1));
        profilPanel.add(scrollPane);

        contentPanel.add(profilPanel);

        // Footer dengan informasi tambahan
        JLabel footer = new JLabel("\u00A9 2024 HospitalFivee - All Rights Reserved", SwingConstants.CENTER);
        footer.setFont(new Font("Arial", Font.ITALIC, 12));
        footer.setForeground(Color.GRAY);
        footer.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        mainPanel.add(footer, BorderLayout.SOUTH);

        // Menambahkan panel utama ke frame
        add(mainPanel);
    }

    private void loadDataPoliFromDatabase() {
        String url = "jdbc:mysql://localhost:3308/hos5";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT nama_poli FROM poli"; // Query untuk mengambil data nama_poli
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                hospitalPoli.add(rs.getString("nama_poli"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metode untuk membuat tombol dengan gaya yang seragam
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40));
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(0x70B9BE));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x004B6B), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        return button;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(new Color(0x004B6B));
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return label;
    }

    private void goToAppointment(ActionEvent e) {
        this.dispose(); // Menutup ViewPasien
        new Appointment().setVisible(true); // Membuka halaman Appointment
    }

    private void goToMedicalReport(ActionEvent e) {
        // Use the class field idPasien instead of hardcoding it
        new Diagnosa(idPasien).setVisible(true); // Membuka halaman Diagnosa
        this.dispose(); // Menutup halaman saat ini jika diperlukan
    }

    private void goBackToLogin(ActionEvent e) {
        this.dispose(); // Menutup ViewPasien
        new ManajemenPasienApp().setVisible(true); // Membuka halaman login
    }
}

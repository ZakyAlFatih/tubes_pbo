package com.mycompany.tubes_pbo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class View extends JFrame {

    public View() {
        setTitle("Menu Utama");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama dengan layout BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0xEAF6F6));

        // Header dengan desain yang lebih menarik
        JLabel header = new JLabel("Welcome to HospitalFivee", SwingConstants.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 28));
        header.setForeground(new Color(0x004B6B));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(header, BorderLayout.NORTH);

        // Panel untuk tombol dengan GridBagLayout agar lebih terorganisir
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(0xEAF6F6));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Tombol menuju ManDokter
        JButton btnManDokter = createStyledButton("Manajemen Dokter");
        btnManDokter.addActionListener(this::goToManDokter);
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(btnManDokter, gbc);

        // Tombol menuju JadwalPraktikCRUD
        JButton btnJadwalPraktik = createStyledButton("Manajemen Jadwal Praktik");
        btnJadwalPraktik.addActionListener(this::goToJadwalPraktik);
        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(btnJadwalPraktik, gbc);

        // Tombol menuju ManPoli
        JButton btnManPoli = createStyledButton("Manajemen Poli");
        btnManPoli.addActionListener(this::goToManPoli);
        gbc.gridx = 0;
        gbc.gridy = 2;
        buttonPanel.add(btnManPoli, gbc);

        // Menambahkan panel tombol ke panel utama
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Footer dengan informasi tambahan
        JLabel footer = new JLabel("\u00A9 2024 HospitalFivee - All Rights Reserved", SwingConstants.CENTER);
        footer.setFont(new Font("Arial", Font.ITALIC, 12));
        footer.setForeground(Color.GRAY);
        footer.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        mainPanel.add(footer, BorderLayout.SOUTH);

        // Menambahkan panel utama ke frame
        add(mainPanel);
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

    private void goToManDokter(ActionEvent e) {
        this.dispose(); // Menutup View
        new ManDokter().setVisible(true); // Membuka halaman ManDokter
    }

    private void goToJadwalPraktik(ActionEvent e) {
        this.dispose(); // Menutup View
        new JadwalPraktikCRUD().setVisible(true); // Membuka halaman JadwalPraktikCRUD
    }

    private void goToManPoli(ActionEvent e) {
        this.dispose(); // Menutup View
        new ManPoli().setVisible(true); // Membuka halaman ManPoli
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new View().setVisible(true);
        });
    }
}

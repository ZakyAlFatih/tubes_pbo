package com.mycompany.tubes_pbo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Diagnosa extends JFrame {

    private final int idPasien; // ID pasien yang sedang login

    public Diagnosa(int idPasien) {
        this.idPasien = idPasien;

        setTitle("Hasil Diagnosa");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama dengan layout BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0xEAF6F6));

        // Header dengan desain menarik
        JLabel header = new JLabel("Hasil Diagnosa Anda", SwingConstants.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 28));
        header.setForeground(new Color(0x004B6B));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(header, BorderLayout.NORTH);

        // Panel untuk menampilkan hasil diagnosa
        JTextArea diagnosaArea = new JTextArea();
        diagnosaArea.setEditable(false);
        diagnosaArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        diagnosaArea.setForeground(new Color(0x004B6B));
        diagnosaArea.setBackground(new Color(0xFFFFFF));
        diagnosaArea.setBorder(BorderFactory.createLineBorder(new Color(0x004B6B), 2));
        JScrollPane scrollPane = new JScrollPane(diagnosaArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Tombol kembali
        JButton btnBack = createStyledButton("Kembali");
        btnBack.addActionListener(this::goBack);
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(0xEAF6F6));
        footerPanel.add(btnBack);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Menambahkan panel utama ke frame
        add(mainPanel);

        // Load data hasil diagnosa
        loadDiagnosa(diagnosaArea);
    }

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

    private void loadDiagnosa(JTextArea diagnosaArea) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/hos5", "root", "");
            String query = "SELECT hasil_diagnosa FROM medicalreport WHERE idPasien = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idPasien);
            ResultSet rs = pstmt.executeQuery();

            StringBuilder diagnosaText = new StringBuilder();
            while (rs.next()) {
                diagnosaText.append("- ").append(rs.getString("hasil_diagnosa")).append("\n");
            }

            if (diagnosaText.length() == 0) {
                diagnosaText.append("Tidak ada hasil diagnosa tersedia.");
            }

            diagnosaArea.setText(diagnosaText.toString());

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data diagnosa: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }


    }

    private void goBack(ActionEvent e) {
        this.dispose(); // Menutup halaman Diagnosa
        new ViewPasien(idPasien).setVisible(true); // Membuka halaman utama pasien
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Diagnosa(1).setVisible(true); // Contoh: Menjalankan untuk idPasien = 1
        });
    }
}

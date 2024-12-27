/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tubes_pbo;

/**
 *
 * @author User
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class View extends JFrame {

    public View() {
        setTitle("Menu Utama");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Header
        JLabel header = new JLabel("Welcome to HospitalFivee", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(header, BorderLayout.NORTH);

        // Panel tombol
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Tombol menuju ManDokter
        JButton btnManDokter = new JButton("Manajemen Dokter");
        btnManDokter.setPreferredSize(new Dimension(150, 30));
        btnManDokter.addActionListener(this::goToManDokter);

        // Tombol menuju JadwalPraktikCRUD
        JButton btnJadwalPraktik = new JButton("Manajemen Jadwal Praktik");
        btnJadwalPraktik.setPreferredSize(new Dimension(150, 30));
        btnJadwalPraktik.addActionListener(this::goToJadwalPraktik);

        // Tombol menuju ManPoli
        JButton btnManPoli = new JButton("Manajemen Poli");
        btnManPoli.setPreferredSize(new Dimension(150, 30));
        btnManPoli.addActionListener(this::goToManPoli);

        // Menambahkan tombol ke panel tombol
        buttonPanel.add(btnManDokter);
        buttonPanel.add(btnJadwalPraktik);
        buttonPanel.add(btnManPoli);

        // Menambahkan panel tombol ke panel utama
        panel.add(buttonPanel, BorderLayout.CENTER);

        // Menambahkan panel utama ke frame
        add(panel);
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


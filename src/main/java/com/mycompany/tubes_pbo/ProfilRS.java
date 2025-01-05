package com.mycompany.tubes_pbo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProfilRS extends JFrame {
    private String nama;
    private String alamat;
    private List<String> listPoli; // Daftar nama Poli

    private ManPoli manPoli;

    public ProfilRS(ManPoli manPoli) {
        this.manPoli = manPoli;
        this.nama = "Hospital Fivee"; // Contoh nama rumah sakit
        this.alamat = "Jl. Sehat No. 123, Jakarta"; // Contoh alamat
        this.listPoli = new ArrayList<>();

        // Ambil data Poli dari ManPoli
        loadDataPoli();

        setTitle("Profil Rumah Sakit");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel Utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0xEAF6F6));
        add(mainPanel);

        // Header
        JLabel header = new JLabel("Profil Rumah Sakit", SwingConstants.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 24));
        header.setForeground(new Color(0x004B6B));
        mainPanel.add(header, BorderLayout.NORTH);

        // Konten
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(0xEAF6F6));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Tampilkan informasi Rumah Sakit
        contentPanel.add(createStyledLabel("Nama: " + nama));
        contentPanel.add(createStyledLabel("Alamat: " + alamat));
        contentPanel.add(createStyledLabel("Daftar Poli:"));

        // Tampilkan daftar Poli
        JList<String> poliList = new JList<>(listPoli.toArray(new String[0]));
        poliList.setBackground(new Color(0xEAF6F6));
        poliList.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(poliList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0x004B6B), 1));
        contentPanel.add(scrollPane);

        // Tombol Kembali
        JButton backButton = new JButton("Kembali");
        backButton.setBackground(new Color(0x70B9BE));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> kembaliKeManPoli());
        mainPanel.add(backButton, BorderLayout.SOUTH);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(new Color(0x004B6B));
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return label;
    }

    private void loadDataPoli() {
        for (int i = 0; i < manPoli.getTableModel().getRowCount(); i++) {
            String namaPoli = manPoli.getTableModel().getValueAt(i, 2).toString();
            listPoli.add(namaPoli);
        }
    }

    private void kembaliKeManPoli() {
        this.dispose();
        manPoli.setVisible(true); // Kembali ke jendela ManPoli
    }


}

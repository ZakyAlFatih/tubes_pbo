package com.mycompany.tubes_pbo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class MedicalReport extends JFrame {
    private JComboBox<String> cbIdPasien;
    private JTextField tfNamaPasien, tfHasilDiagnosa;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;

    public MedicalReport() {
        setTitle("Manajemen Medical Report");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama dengan BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(0xEAF6F6));

        // Header dengan desain modern
        JLabel header = new JLabel("Manajemen Medical Report", SwingConstants.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 28));
        header.setForeground(new Color(0x004B6B));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(header, BorderLayout.NORTH);

        // Panel Input dengan GridLayout
        JPanel panelInput = new JPanel(new GridLayout(3, 2, 10, 10));
        panelInput.setBackground(new Color(0xEAF6F6));
        panelInput.setBorder(BorderFactory.createTitledBorder("Input Medical Report"));

        panelInput.add(new JLabel("ID Pasien:", SwingConstants.RIGHT));
        cbIdPasien = new JComboBox<>();
        cbIdPasien.addActionListener(e -> updateNamaPasien());
        panelInput.add(cbIdPasien);

        panelInput.add(new JLabel("Nama Pasien:", SwingConstants.RIGHT));
        tfNamaPasien = new JTextField();
        tfNamaPasien.setEditable(false);
        panelInput.add(tfNamaPasien);

        panelInput.add(new JLabel("Hasil Diagnosa:", SwingConstants.RIGHT));
        tfHasilDiagnosa = new JTextField();
        panelInput.add(tfHasilDiagnosa);

        mainPanel.add(panelInput, BorderLayout.WEST);

        // Panel Tombol
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelButton.setBackground(new Color(0xEAF6F6));

        JButton btnTambah = createStyledButton("Tambah");
        JButton btnBack = createStyledButton("Kembali");

        panelButton.add(btnTambah);
        panelButton.add(btnBack);

        mainPanel.add(panelButton, BorderLayout.SOUTH);

        // Tabel untuk menampilkan data
        String[] columnNames = {"ID MR", "ID Pasien", "Nama Pasien", "Hasil Diagnosa"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // Event Listeners
        btnTambah.addActionListener(e -> tambahMedicalReport());
        btnBack.addActionListener(e -> backToMainMenu());

        // Database Connection
        connection = DatabaseConnection.getConnection();
        loadIdPasien();
        loadTableData();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(0x70B9BE));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x004B6B), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        return button;
    }

    private void loadIdPasien() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, nama FROM pasien");
            cbIdPasien.removeAllItems();
            while (rs.next()) {
                cbIdPasien.addItem(rs.getInt("id") + " - " + rs.getString("nama"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat ID Pasien: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateNamaPasien() {
        if (cbIdPasien.getSelectedItem() == null) return;
        String selectedItem = cbIdPasien.getSelectedItem().toString();
        String[] parts = selectedItem.split(" - ", 2);
        if (parts.length == 2) {
            tfNamaPasien.setText(parts[1]);
        }
    }

    private void loadTableData() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT mr.id_mr, p.id, p.nama, mr.hasil_diagnosa FROM medicalreport mr JOIN pasien p ON mr.idPasien = p.id");
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_mr"),
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("hasil_diagnosa")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tambahMedicalReport() {
        if (cbIdPasien.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih ID Pasien!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedItem = cbIdPasien.getSelectedItem().toString();
        String[] parts = selectedItem.split(" - ", 2);
        int idPasien = Integer.parseInt(parts[0]);
        String hasilDiagnosa = tfHasilDiagnosa.getText();

        if (hasilDiagnosa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hasil Diagnosa tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO medicalreport (idPasien, hasil_diagnosa) VALUES (?, ?)")) {
            ps.setInt(1, idPasien);
            ps.setString(2, hasilDiagnosa);
            ps.executeUpdate();
            loadTableData();
            tfHasilDiagnosa.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menambah Medical Report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backToMainMenu() {
        this.dispose();
        new View().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MedicalReport().setVisible(true));
    }
}

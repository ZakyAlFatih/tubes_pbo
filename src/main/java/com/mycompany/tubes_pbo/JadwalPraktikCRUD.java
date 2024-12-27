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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JadwalPraktikCRUD extends JFrame {
    private JTextField tfTanggal, tfIdPoli, tfRuang, tfIdDokter;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;

    public JadwalPraktikCRUD() {
        // Set up UI
        setTitle("Manajemen Jadwal Praktik");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Input Panel
        JPanel panelInput = new JPanel(new GridLayout(4, 2, 5, 5));
        panelInput.setBorder(BorderFactory.createTitledBorder("Data Jadwal Praktik"));

        panelInput.add(new JLabel("Tanggal:"));
        tfTanggal = new JTextField();
        panelInput.add(tfTanggal);

        panelInput.add(new JLabel("ID Poli:"));
        tfIdPoli = new JTextField();
        panelInput.add(tfIdPoli);

        panelInput.add(new JLabel("Ruang:"));
        tfRuang = new JTextField();
        panelInput.add(tfRuang);

        panelInput.add(new JLabel("ID Dokter:"));
        tfIdDokter = new JTextField();
        panelInput.add(tfIdDokter);

        // Button Panel
        JPanel panelButton = new JPanel();
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnSimpan = new JButton("Simpan");
        JButton btnHapus = new JButton("Hapus");
        JButton btnBack = new JButton("Back");  // New Back button

        panelButton.add(btnTambah);
        panelButton.add(btnEdit);
        panelButton.add(btnSimpan);
        panelButton.add(btnHapus);
        panelButton.add(btnBack); // Add the back button to the panel

        // Table
        String[] columnNames = {"ID Jadwal Praktik", "Tanggal", "ID Poli", "Ruang", "ID Dokter"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add components to frame
        setLayout(new BorderLayout(10, 10));
        add(panelInput, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);

        // Event Listeners
        btnTambah.addActionListener(e -> tambahJadwal());
        btnEdit.addActionListener(e -> editJadwal());
        btnSimpan.addActionListener(e -> simpanJadwal());
        btnHapus.addActionListener(e -> hapusJadwal());
        btnBack.addActionListener(e -> backToMainMenu());

        // Database Connection
        connection = DatabaseConnection.getConnection();
        loadTableData();
    }

    private void loadTableData() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM jadwalPraktik");
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("idJadwalPraktik"),
                        rs.getString("tanggal"),
                        rs.getInt("idPoli"),
                        rs.getString("ruang"),
                        rs.getInt("idDokter")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tambahJadwal() {
        String tanggal = tfTanggal.getText();
        String idPoli = tfIdPoli.getText();
        String ruang = tfRuang.getText();
        String idDokter = tfIdDokter.getText();

        if (tanggal.isEmpty() || idPoli.isEmpty() || ruang.isEmpty() || idDokter.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO jadwalPraktik (tanggal, idPoli, ruang, idDokter) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, tanggal);
            ps.setInt(2, Integer.parseInt(idPoli));
            ps.setString(3, ruang);
            ps.setInt(4, Integer.parseInt(idDokter));
            ps.executeUpdate();
            loadTableData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menambah data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editJadwal() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tfTanggal.setText(tableModel.getValueAt(selectedRow, 1).toString());
        tfIdPoli.setText(tableModel.getValueAt(selectedRow, 2).toString());
        tfRuang.setText(tableModel.getValueAt(selectedRow, 3).toString());
        tfIdDokter.setText(tableModel.getValueAt(selectedRow, 4).toString());
    }

    private void simpanJadwal() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin disimpan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idJadwalPraktik = (int) tableModel.getValueAt(selectedRow, 0);
        String tanggal = tfTanggal.getText();
        String idPoli = tfIdPoli.getText();
        String ruang = tfRuang.getText();
        String idDokter = tfIdDokter.getText();

        if (tanggal.isEmpty() || idPoli.isEmpty() || ruang.isEmpty() || idDokter.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (PreparedStatement ps = connection.prepareStatement("UPDATE jadwalPraktik SET tanggal = ?, idPoli = ?, ruang = ?, idDokter = ? WHERE idJadwalPraktik = ?")) {
            ps.setString(1, tanggal);
            ps.setInt(2, Integer.parseInt(idPoli));
            ps.setString(3, ruang);
            ps.setInt(4, Integer.parseInt(idDokter));
            ps.setInt(5, idJadwalPraktik);
            ps.executeUpdate();
            loadTableData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusJadwal() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idJadwalPraktik = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM jadwalPraktik WHERE idJadwalPraktik = ?")) {
            ps.setInt(1, idJadwalPraktik);
            ps.executeUpdate();
            loadTableData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void backToMainMenu() {
        this.dispose(); // Close the current JadwalPraktikCRUD window
        new View().setVisible(true); // Open the main menu or previous screen (replace 'View' with the actual class for your main menu)
    }


}


package com.mycompany.tubes_pbo;

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
        setTitle("Manajemen Jadwal Praktik");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama dengan BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(0xEAF6F6));

        // Header dengan desain modern
        JLabel header = new JLabel("Manajemen Jadwal Praktik", SwingConstants.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 28));
        header.setForeground(new Color(0x004B6B));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(header, BorderLayout.NORTH);

        // Panel Input dengan GridLayout
        JPanel panelInput = new JPanel(new GridLayout(4, 2, 10, 10));
        panelInput.setBackground(new Color(0xEAF6F6));
        panelInput.setBorder(BorderFactory.createTitledBorder("Data Jadwal Praktik"));

        panelInput.add(new JLabel("Tanggal:", SwingConstants.RIGHT));
        tfTanggal = new JTextField();
        panelInput.add(tfTanggal);

        panelInput.add(new JLabel("ID Poli:", SwingConstants.RIGHT));
        tfIdPoli = new JTextField();
        panelInput.add(tfIdPoli);

        panelInput.add(new JLabel("Ruang:", SwingConstants.RIGHT));
        tfRuang = new JTextField();
        panelInput.add(tfRuang);

        panelInput.add(new JLabel("ID Dokter:", SwingConstants.RIGHT));
        tfIdDokter = new JTextField();
        panelInput.add(tfIdDokter);

        mainPanel.add(panelInput, BorderLayout.WEST);

        // Panel Tombol
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelButton.setBackground(new Color(0xEAF6F6));

        JButton btnTambah = createStyledButton("Tambah");
        JButton btnEdit = createStyledButton("Edit");
        JButton btnSimpan = createStyledButton("Simpan");
        JButton btnHapus = createStyledButton("Hapus");
        JButton btnBack = createStyledButton("Kembali");

        panelButton.add(btnTambah);
        panelButton.add(btnEdit);
        panelButton.add(btnSimpan);
        panelButton.add(btnHapus);
        panelButton.add(btnBack);

        mainPanel.add(panelButton, BorderLayout.SOUTH);

        // Tabel untuk menampilkan data
        String[] columnNames = {"ID Jadwal Praktik", "Tanggal", "ID Poli", "Ruang", "ID Dokter"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

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
        this.dispose();
        new View().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JadwalPraktikCRUD().setVisible(true));
    }
}

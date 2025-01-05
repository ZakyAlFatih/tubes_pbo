package com.mycompany.tubes_pbo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ManPoli extends JFrame {

    private DefaultTableModel tableModel;
    private JTextField idPoliField;
    private JTextField namaDokterField;
    private JTextField namaPoliField;
    private JTable dokterTable;
    private Connection connection;

    public ManPoli() {
        connectToDatabase();

        setTitle("Manajemen Poli");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Color bgColor = new Color(0xCFEEF0);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Data Poli"));
        formPanel.setBackground(bgColor);

        JLabel idDokterLabel = new JLabel("ID Poli:");
        idPoliField = new JTextField();

        JLabel namaDokterLabel = new JLabel("Nama Dokter:");
        namaDokterField = new JTextField();

        JLabel spesialisLabel = new JLabel("Nama Poli:");
        namaPoliField = new JTextField();

        formPanel.add(idDokterLabel);
        formPanel.add(idPoliField);
        formPanel.add(namaDokterLabel);
        formPanel.add(namaDokterField);
        formPanel.add(spesialisLabel);
        formPanel.add(namaPoliField);

        add(formPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columnNames = {"ID Poli", "Nama Dokter", "Nama Poli"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dokterTable = new JTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(dokterTable);
        tableScrollPane.getViewport().setBackground(bgColor); // Table background
        add(tableScrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(bgColor);

        JButton tambahButton = new JButton("Tambah");
        JButton editButton = new JButton("Edit");
        JButton simpanButton = new JButton("Simpan");
        JButton hapusButton = new JButton("Hapus");
        JButton btnBack = new JButton("Back");

        tambahButton.addActionListener(e -> tambahData());
        editButton.addActionListener(e -> editData());
        simpanButton.addActionListener(e -> simpanData());
        hapusButton.addActionListener(e -> hapusData());
        btnBack.addActionListener(e -> kembaliKeHalamanUtama());

        buttonPanel.add(tambahButton);
        buttonPanel.add(editButton);
        buttonPanel.add(simpanButton);
        buttonPanel.add(hapusButton);
        buttonPanel.add(btnBack);

        add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().setBackground(bgColor); // Frame background

        // Load data saat aplikasi pertama kali dijalankan
        loadTableData();
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

    private void loadTableData() {
        try {
            tableModel.setRowCount(0); // Bersihkan tabel sebelum memuat data baru
            String query = "SELECT * FROM poli";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String idPoli = resultSet.getString("id_poli");
                String namaDokter = resultSet.getString("nama_dokter");
                String namaPoli = resultSet.getString("nama_poli");
                tableModel.addRow(new Object[]{idPoli, namaDokter, namaPoli});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void tambahData() {
        String idPoli = idPoliField.getText();
        String namaDokter = namaDokterField.getText();
        String namaPoli = namaPoliField.getText();

        if (!idPoli.isEmpty() && !namaDokter.isEmpty() && !namaPoli.isEmpty()) {
            try {
                String query = "INSERT INTO poli (id_poli, nama_dokter, nama_poli) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, idPoli);
                statement.setString(2, namaDokter);
                statement.setString(3, namaPoli);
                statement.executeUpdate();

                loadTableData(); // Refresh tabel setelah menambahkan data
                clearFields();
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editData() {
        int selectedRow = dokterTable.getSelectedRow();
        if (selectedRow != -1) {
            idPoliField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            namaDokterField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            namaPoliField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        } else {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void simpanData() {
        int selectedRow = dokterTable.getSelectedRow();
        if (selectedRow != -1) {
            String idPoli = idPoliField.getText();
            String namaDokter = namaDokterField.getText();
            String namaPoli = namaPoliField.getText();

            try {
                String query = "UPDATE poli SET nama_dokter = ?, nama_poli = ? WHERE id_poli = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, namaDokter);
                statement.setString(2, namaPoli);
                statement.setString(3, idPoli);
                statement.executeUpdate();

                loadTableData(); // Refresh tabel setelah menyimpan data
                clearFields();
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin disimpan", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData() {
        int selectedRow = dokterTable.getSelectedRow();
        if (selectedRow != -1) {
            String idPoli = tableModel.getValueAt(selectedRow, 0).toString();

            try {
                String query = "DELETE FROM poli WHERE id_poli = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, idPoli);
                statement.executeUpdate();

                loadTableData(); // Refresh tabel setelah menghapus data
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void kembaliKeHalamanUtama() {
        this.dispose();
        new View().setVisible(true); // Buka jendela utama
    }

    private void clearFields() {
        idPoliField.setText("");
        namaDokterField.setText("");
        namaPoliField.setText("");
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}

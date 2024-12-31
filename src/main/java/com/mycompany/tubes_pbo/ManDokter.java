/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tubes_pbo;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ManDokter extends JFrame {
    private JTextField tfID, tfNama, tfSpesialisasi, tfPoli, tfNoTelepon;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;

    public ManDokter() {
        setTitle("Manajemen Dokter");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Koneksi ke database
        connectToDatabase();

        // Panel Input Data Dokter
        JPanel panelInput = new JPanel(new GridLayout(5, 2, 5, 5));
        panelInput.setBorder(BorderFactory.createTitledBorder("Data Dokter"));
        panelInput.setBackground(Color.decode("#CFEEF0")); // Set background color

        panelInput.add(new JLabel("ID Dokter:"));
        tfID = new JTextField();
        panelInput.add(tfID);

        panelInput.add(new JLabel("Dokter:"));
        tfNama = new JTextField();
        panelInput.add(tfNama);

        panelInput.add(new JLabel("Spesialisasi:"));
        tfSpesialisasi = new JTextField();
        panelInput.add(tfSpesialisasi);

        panelInput.add(new JLabel("Poli:"));
        tfPoli = new JTextField();
        panelInput.add(tfPoli);

        panelInput.add(new JLabel("No Telepon:"));
        tfNoTelepon = new JTextField();
        panelInput.add(tfNoTelepon);

        // Panel Tombol
        JPanel panelButton = new JPanel();
        panelButton.setBackground(Color.decode("#CFEEF0"));
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnSimpan = new JButton("Simpan");
        JButton btnHapus = new JButton("Hapus");
        JButton btnBack = new JButton("Back");

        btnTambah.setBackground(Color.decode("#70B9BE"));
        btnEdit.setBackground(Color.decode("#70B9BE"));
        btnSimpan.setBackground(Color.decode("#70B9BE"));
        btnHapus.setBackground(Color.decode("#70B9BE"));
        btnBack.setBackground(Color.decode("#70B9BE"));

        btnTambah.setForeground(Color.WHITE);
        btnEdit.setForeground(Color.WHITE);
        btnSimpan.setForeground(Color.WHITE);
        btnHapus.setForeground(Color.WHITE);
        btnBack.setForeground(Color.WHITE);

        panelButton.add(btnTambah);
        panelButton.add(btnEdit);
        panelButton.add(btnSimpan);
        panelButton.add(btnHapus);
        panelButton.add(btnBack);

        // Tabel Dokter
        String[] columnNames = {"ID Dokter", "Dokter", "Spesialisasi", "Poli", "No Telepon"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setBackground(Color.decode("#CFEEF0"));

        // Tambahkan Komponen ke Frame
        setLayout(new BorderLayout(10, 10));
        add(panelInput, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);

        // Event Listener
        btnTambah.addActionListener(e -> tambahDokter());
        btnEdit.addActionListener(e -> editDokter());
        btnSimpan.addActionListener(e -> simpanDokter());
        btnHapus.addActionListener(e -> hapusDokter());
        btnBack.addActionListener(e -> backToMainMenu());

        loadData();
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3308/hos5?useSSL=false";
            String user = "root";
            String password = "";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Koneksi ke database gagal!", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void loadData() {
        try {
            String query = "SELECT * FROM dokter";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nama = resultSet.getString("nama");
                String spesialisasi = resultSet.getString("spesialisasi");
                String poli = resultSet.getString("poli");
                String noTelepon = resultSet.getString("no_telepon");

                tableModel.addRow(new Object[]{id, nama, spesialisasi, poli, noTelepon});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data!", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void tambahDokter() {
        try {
            int id = Integer.parseInt(tfID.getText());
            String nama = tfNama.getText();
            String spesialisasi = tfSpesialisasi.getText();
            String poli = tfPoli.getText();
            String noTelepon = tfNoTelepon.getText();

            if (nama.isEmpty() || spesialisasi.isEmpty() || poli.isEmpty() || noTelepon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Simpan data ke database
            String query = "INSERT INTO dokter (id, nama, spesialisasi, poli, no_telepon) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, nama);
            preparedStatement.setString(3, spesialisasi);
            preparedStatement.setString(4, poli);
            preparedStatement.setString(5, noTelepon);

            preparedStatement.executeUpdate();

            // Tambahkan data ke tabel
            tableModel.addRow(new Object[]{id, nama, spesialisasi, poli, noTelepon});

            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID harus berupa angka!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan data ke database!", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void editDokter() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tfID.setText(tableModel.getValueAt(selectedRow, 0).toString());
        tfNama.setText(tableModel.getValueAt(selectedRow, 1).toString());
        tfSpesialisasi.setText(tableModel.getValueAt(selectedRow, 2).toString());
        tfPoli.setText(tableModel.getValueAt(selectedRow, 3).toString());
        tfNoTelepon.setText(tableModel.getValueAt(selectedRow, 4).toString());
    }

    private void simpanDokter() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin disimpan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(tfID.getText());
            String nama = tfNama.getText();
            String spesialisasi = tfSpesialisasi.getText();
            String poli = tfPoli.getText();
            String noTelepon = tfNoTelepon.getText();

            String query = "UPDATE dokter SET nama = ?, spesialisasi = ?, poli = ?, no_telepon = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nama);
            preparedStatement.setString(2, spesialisasi);
            preparedStatement.setString(3, poli);
            preparedStatement.setString(4, noTelepon);
            preparedStatement.setInt(5, id);

            preparedStatement.executeUpdate();

            tableModel.setValueAt(nama, selectedRow, 1);
            tableModel.setValueAt(spesialisasi, selectedRow, 2);
            tableModel.setValueAt(poli, selectedRow, 3);
            tableModel.setValueAt(noTelepon, selectedRow, 4);

            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID harus berupa angka!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan perubahan ke database!", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void hapusDokter() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String query = "DELETE FROM dokter WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

            tableModel.removeRow(selectedRow);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data dari database!", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        tfID.setText("");
        tfNama.setText("");
        tfSpesialisasi.setText("");
        tfPoli.setText("");
        tfNoTelepon.setText("");
    }
    private void backToMainMenu() {
        this.dispose();
        new View().setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ManDokter().setVisible(true);
        });
    }
}

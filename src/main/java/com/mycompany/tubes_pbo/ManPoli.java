package com.mycompany.tubes_pbo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ManPoli extends JFrame {
    private JTextField idPoliField;
    private JTextField namaDokterField;
    private JTextField namaPoliField;
    private DefaultTableModel tableModel;
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

        tambahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

                        tableModel.addRow(new Object[]{idPoli, namaDokter, namaPoli});
                        clearFields();
                        JOptionPane.showMessageDialog(ManPoli.this, "Data berhasil ditambahkan.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(ManPoli.this, "Gagal menambahkan data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(ManPoli.this, "Semua field harus diisi", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = dokterTable.getSelectedRow();
                if (selectedRow != -1) {
                    idPoliField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    namaDokterField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    namaPoliField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                } else {
                    JOptionPane.showMessageDialog(ManPoli.this, "Pilih baris yang ingin diedit", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        simpanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = dokterTable.getSelectedRow();
                if (selectedRow != -1) {
                    String idPoli = idPoliField.getText();
                    String namaDokter = namaDokterField.getText();
                    String namaPoli = namaPoliField.getText();

                    try {
                        String query = "UPDATE poli SET nama_dokter = ?, nama_poli = ? WHERE id_poli = ?";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setString(1, idPoli);
                        statement.setString(2, namaDokter);
                        statement.setString(3, namaPoli);
                        statement.executeUpdate();

                        tableModel.setValueAt(idPoli, selectedRow, 0);
                        tableModel.setValueAt(namaDokter, selectedRow, 1);
                        tableModel.setValueAt(namaPoli, selectedRow, 2);

                        clearFields();
                        JOptionPane.showMessageDialog(ManPoli.this, "Data berhasil disimpan.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(ManPoli.this, "Gagal menyimpan data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(ManPoli.this, "Pilih baris yang ingin disimpan", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = dokterTable.getSelectedRow();
                if (selectedRow != -1) {
                    String idPoli = tableModel.getValueAt(selectedRow, 0).toString();

                    try {
                        String query = "DELETE FROM poli WHERE id_poli = ?";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setString(1, idPoli);
                        statement.executeUpdate();

                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(ManPoli.this, "Data berhasil dihapus.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(ManPoli.this, "Gagal menghapus data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(ManPoli.this, "Pilih baris yang ingin dihapus", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Menutup jendela ManPoli
                ManPoli.this.dispose();

                // Membuka jendela utama (View)
                new View().setVisible(true);
            }
        });


        buttonPanel.add(tambahButton);
        buttonPanel.add(editButton);
        buttonPanel.add(simpanButton);
        buttonPanel.add(hapusButton);
        buttonPanel.add(btnBack);




        add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().setBackground(bgColor); // Frame background
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

    private void clearFields() {
        idPoliField.setText("");
        namaDokterField.setText("");
        namaPoliField.setText("");
    }


    }

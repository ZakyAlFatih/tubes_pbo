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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManDokter extends JFrame {
    private JTextField tfID, tfNama, tfSpesialis;
    private JTable table;
    private DefaultTableModel tableModel;

    public ManDokter(){
        setTitle("Manajemen Dokter");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel Input Data Dokter
        JPanel panelInput = new JPanel(new GridLayout(3, 2, 5, 5));
        panelInput.setBorder(BorderFactory.createTitledBorder("Data Dokter"));

        panelInput.add(new JLabel("ID Dokter:"));
        tfID = new JTextField();
        panelInput.add(tfID);

        panelInput.add(new JLabel("Dokter:"));
        tfNama = new JTextField();
        panelInput.add(tfNama);

        panelInput.add(new JLabel("Spesialis:"));
        tfSpesialis = new JTextField();
        panelInput.add(tfSpesialis);

        // Panel Tombol
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

        // Tabel Dokter
        String[] columnNames = {"ID Dokter", "Dokter", "Spesialis"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

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
    }

    private void tambahDokter() {
        String id = tfID.getText();
        String nama = tfNama.getText();
        String spesialis = tfSpesialis.getText();

        if (id.isEmpty() || nama.isEmpty() || spesialis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.addRow(new Object[]{id, nama, spesialis});
        clearFields();
    }

    private void editDokter() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tfID.setText(tableModel.getValueAt(selectedRow, 0).toString());
        tfNama.setText(tableModel.getValueAt(selectedRow, 1).toString());
        tfSpesialis.setText(tableModel.getValueAt(selectedRow, 2).toString());
    }

    private void simpanDokter() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin disimpan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setValueAt(tfID.getText(), selectedRow, 0);
        tableModel.setValueAt(tfNama.getText(), selectedRow, 1);
        tableModel.setValueAt(tfSpesialis.getText(), selectedRow, 2);
        clearFields();
    }

    private void hapusDokter() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.removeRow(selectedRow);
    }

    private void clearFields() {
        tfID.setText("");
        tfNama.setText("");
        tfSpesialis.setText("");
    }
    private void backToMainMenu() {
        this.dispose(); // Close the current JadwalPraktikCRUD window
        new View().setVisible(true); // Open the main menu or previous screen (replace 'View' with the actual class for your main menu)
    }

   
}

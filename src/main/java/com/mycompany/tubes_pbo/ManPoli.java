package com.mycompany.tubes_pbo;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManPoli extends JFrame {
    private JTextField idPoliField;
    private JTextField namaDokterField;
    private JTextField namaPoliField;
    private DefaultTableModel tableModel;
    private JTable dokterTable;

    public ManPoli() {
        setTitle("Manajemen Poli");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Data Poli"));

        JLabel idPoliLabel = new JLabel("ID Poli:");
        idPoliField = new JTextField();

        JLabel namaDokterLabel = new JLabel("Nama Dokter:");
        namaDokterField = new JTextField();

        JLabel spesialisLabel = new JLabel("Nama Poli");
        namaPoliField = new JTextField();

        formPanel.add(idPoliLabel);
        formPanel.add(idPoliField);
        formPanel.add(namaDokterLabel);
        formPanel.add(namaDokterField);
        formPanel.add(spesialisLabel);
        formPanel.add(namaPoliField);

        add(formPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columnNames = {"ID Poli", "Dokter", "Nama Poli"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dokterTable = new JTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(dokterTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton tambahButton = new JButton("Tambah");
        JButton editButton = new JButton("Edit");
        JButton simpanButton = new JButton("Simpan");
        JButton hapusButton = new JButton("Hapus");

        tambahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idPoli = idPoliField.getText();
                String namaDokter = namaDokterField.getText();
                String spesialis = namaPoliField.getText();

                if (!idPoli.isEmpty() && !namaDokter.isEmpty() && !spesialis.isEmpty()) {
                    tableModel.addRow(new Object[]{idPoli, namaDokter, spesialis});
                    clearFields();
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
                    tableModel.setValueAt(idPoliField.getText(), selectedRow, 0);
                    tableModel.setValueAt(namaDokterField.getText(), selectedRow, 1);
                    tableModel.setValueAt(namaPoliField.getText(), selectedRow, 2);
                    clearFields();
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
                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(ManPoli.this, "Pilih baris yang ingin dihapus", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(tambahButton);
        buttonPanel.add(editButton);
        buttonPanel.add(simpanButton);
        buttonPanel.add(hapusButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void clearFields() {
        idPoliField.setText("");
        namaDokterField.setText("");
        namaPoliField.setText("");
    }

    private void backToMainMenu() {
        this.dispose(); // Close the current JadwalPraktikCRUD window
        new View().setVisible(true); // Open the main menu or previous screen (replace 'View' with the actual class for your main menu)
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManPoli app = new ManPoli();
            app.setVisible(true);
        });
    }
}
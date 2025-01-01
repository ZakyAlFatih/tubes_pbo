package com.mycompany.tubes_pbo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class Appointment extends JFrame {
    private JComboBox<String> cbPoliklinik, cbDokter, cbTanggal;
    private JTextArea taKeluhan;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private Connection connection;

    public Appointment() {
        // Koneksi ke Database
        connectToDatabase();

        setTitle("Buat Jadwal Janji Temu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(0xEAF6F6));

        JLabel header = new JLabel("Buat Jadwal Janji Temu", SwingConstants.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 28));
        header.setForeground(new Color(0x004B6B));
        mainPanel.add(header, BorderLayout.NORTH);

        // Panel input
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(new Color(0xEAF6F6));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Data Janji Temu"));

        inputPanel.add(new JLabel("Poliklinik:", SwingConstants.RIGHT));
        cbPoliklinik = new JComboBox<>();
        cbPoliklinik.addActionListener(e -> loadDokter());
        inputPanel.add(cbPoliklinik);

        inputPanel.add(new JLabel("Dokter:", SwingConstants.RIGHT));
        cbDokter = new JComboBox<>();
        cbDokter.addActionListener(e -> loadTanggal());
        inputPanel.add(cbDokter);

        inputPanel.add(new JLabel("Tanggal:", SwingConstants.RIGHT));
        cbTanggal = new JComboBox<>();
        inputPanel.add(cbTanggal);

        inputPanel.add(new JLabel("Keluhan:", SwingConstants.RIGHT));
        taKeluhan = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(taKeluhan);
        inputPanel.add(scrollPane);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Panel tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(0xEAF6F6));

        JButton btnSimpan = createStyledButton("Simpan");
        btnSimpan.addActionListener(e -> simpanAppointment());

        JButton btnKembali = createStyledButton("Kembali");
        btnKembali.addActionListener(e -> kembaliKeMenuUtama());

        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnKembali);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Menampilkan Table appointment
        String[] columnNames = {"Keluhan", "Poliklinik", "Dokter", "Tanggal"};
        tableModel = new DefaultTableModel(columnNames, 0);
        appointmentTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(appointmentTable);
        mainPanel.add(tableScrollPane, BorderLayout.EAST);
        add(mainPanel);

        // Load data awal
        loadPoliklinik();
        loadAppointments();
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
        }
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

    private void loadPoliklinik() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT nama_poli FROM poli");
            cbPoliklinik.removeAllItems();
            while (rs.next()) {
                cbPoliklinik.addItem(rs.getString("nama_poli"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat poliklinik: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDokter() {
        String selectedPoli = (String) cbPoliklinik.getSelectedItem();
        if (selectedPoli == null) return;

        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT dokter.nama FROM dokter JOIN poli ON dokter.poli = poli.id_poli WHERE poli.nama_poli = ?")) {
            ps.setString(1, selectedPoli);
            ResultSet rs = ps.executeQuery();
            cbDokter.removeAllItems();
            while (rs.next()) {
                cbDokter.addItem(rs.getString("nama"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat dokter: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTanggal() {
        String selectedDokter = (String) cbDokter.getSelectedItem();
        if (selectedDokter == null) return;

        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT jadwalpraktik.tanggal FROM jadwalpraktik JOIN dokter ON jadwalpraktik.idDokter = dokter.id WHERE dokter.nama = ?")) {
            ps.setString(1, selectedDokter);
            ResultSet rs = ps.executeQuery();
            cbTanggal.removeAllItems();
            while (rs.next()) {
                cbTanggal.addItem(rs.getString("tanggal"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat tanggal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void simpanAppointment() {
        String selectedPoliklinik = (String) cbPoliklinik.getSelectedItem();
        String selectedDokter = (String) cbDokter.getSelectedItem();
        String selectedTanggal = (String) cbTanggal.getSelectedItem();
        String keluhan = taKeluhan.getText();

        if (selectedPoliklinik == null || selectedDokter == null || selectedTanggal == null || keluhan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua data harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO appointment (keluhan, id_poli, id, idJadwalPraktik) VALUES (?, " +
                        "(SELECT id_poli FROM poli WHERE nama_poli = ?), " +
                        "(SELECT id FROM dokter WHERE nama = ?), " +
                        "(SELECT idJadwalPraktik FROM jadwalpraktik WHERE tanggal = ?))")) {
            ps.setString(1, keluhan);
            ps.setString(2, selectedPoliklinik);
            ps.setString(3, selectedDokter);
            ps.setString(4, selectedTanggal);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Janji temu berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadAppointments(); // Refresh appointment table after successful save
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan janji temu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAppointments() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT a.keluhan, p.nama_poli AS poli, d.nama AS dokter, j.tanggal FROM appointment a " +
                    "JOIN poli p ON a.id_poli = p.id_poli " +
                    "JOIN dokter d ON a.id = d.id " +
                    "JOIN jadwalpraktik j ON a.idJadwalPraktik = j.idJadwalPraktik");
            tableModel.setRowCount(0); // Clear existing data
            while (rs.next()) {
                String keluhan = rs.getString("keluhan");
                String poliklinik = rs.getString("poli");
                String dokter = rs.getString("dokter");
                String tanggal = rs.getString("tanggal");
                tableModel.addRow(new Object[]{keluhan, poliklinik, dokter, tanggal});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, " Gagal memuat data janji temu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   private void kembaliKeMenuUtama() {
    this.dispose();
       new View().setVisible(true);
   }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new Appointment().setVisible(true));
//    }
}


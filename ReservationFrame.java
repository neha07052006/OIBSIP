
package com.neha.reservation;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ThreadLocalRandom;

public class ReservationFrame extends JFrame {

    private JTextField passengerField, trainNumberField, trainNameField;
    private JTextField dateField, sourceField, destinationField;
    private JComboBox<String> classBox;

    private final Color navy = new Color(20, 43, 80);
    private final Color blue = new Color(37, 105, 190);
    private final Color background = new Color(244, 247, 252);

    public ReservationFrame() {

        setTitle("Railway Reservation - Book Ticket");
        setSize(720, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(background);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(navy);
        header.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("  BOOK YOUR JOURNEY");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Railway Reservation System");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(new Color(210, 225, 245));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(title);
        heading.add(Box.createVerticalStrut(7));
        heading.add(subtitle);

        header.add(heading, BorderLayout.CENTER);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new CompoundBorder(
            new LineBorder(new Color(225, 231, 240)),
            new EmptyBorder(20, 25, 20, 25)
        ));

        passengerField = new JTextField(20);
        trainNumberField = new JTextField(20);
        trainNameField = new JTextField(20);
        trainNameField.setEditable(false);
        trainNameField.setBackground(new Color(240, 245, 252));

        dateField = new JTextField(LocalDate.now().plusDays(1).toString(), 20);
        sourceField = new JTextField(20);
        destinationField = new JTextField(20);

        classBox = new JComboBox<>(new String[]{
            "Sleeper", "AC 3 Tier", "AC 2 Tier", "AC First Class"
        });
        classBox.setFont(new Font("Arial", Font.PLAIN, 14));

        addFormRow(form, 0, "Passenger Name", passengerField);
        addFormRow(form, 1, "Train Number", trainNumberField);
        addFormRow(form, 2, "Train Name", trainNameField);
        addFormRow(form, 3, "Travel Class", classBox);
        addFormRow(form, 4, "Journey Date (YYYY-MM-DD)", dateField);
        addFormRow(form, 5, "From Station", sourceField);
        addFormRow(form, 6, "To Station", destinationField);

        // Buttons
        JButton bookButton = new JButton("BOOK TICKET");
        styleButton(bookButton, blue);

        JButton clearButton = new JButton("CLEAR FORM");
        styleButton(clearButton, new Color(95, 110, 130));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttons.setBackground(background);
        buttons.add(bookButton);
        buttons.add(clearButton);

        JPanel center = new JPanel(new BorderLayout(0, 15));
        center.setBackground(background);
        center.setBorder(new EmptyBorder(20, 25, 20, 25));
        center.add(form, BorderLayout.CENTER);
        center.add(buttons, BorderLayout.SOUTH);

        JLabel footer = new JLabel(
            "Have a safe and comfortable journey!",
            SwingConstants.CENTER
        );
        footer.setForeground(navy);
        footer.setBorder(new EmptyBorder(0, 10, 15, 10));

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        add(root);

        trainNumberField.addActionListener(e -> fetchTrainName());
        trainNumberField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                fetchTrainName();
            }
        });

        bookButton.addActionListener(e -> bookTicket());
        clearButton.addActionListener(e -> clearForm());
    }

    private void addFormRow(
        JPanel panel, int row, String labelText, JComponent field
    ) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(navy);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;

        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(260, 32));
        panel.add(field, gbc);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(160, 42));
    }

    private void fetchTrainName() {
        String text = trainNumberField.getText().trim();

        if (text.isEmpty()) {
            trainNameField.setText("");
            return;
        }

        String sql = "SELECT train_name FROM trains WHERE train_number = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(text));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    trainNameField.setText(rs.getString("train_name"));
                } else {
                    trainNameField.setText("");
                    JOptionPane.showMessageDialog(
                        this, "Train number not found!"
                    );
                }
            }

        } catch (NumberFormatException e) {
            trainNameField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this, "Database error: " + e.getMessage()
            );
        }
    }

    private void bookTicket() {

        String passenger = passengerField.getText().trim();
        String trainText = trainNumberField.getText().trim();
        String journeyDate = dateField.getText().trim();
        String source = sourceField.getText().trim();
        String destination = destinationField.getText().trim();
        String trainName = trainNameField.getText().trim();

        if (passenger.isEmpty() || trainText.isEmpty() || trainName.isEmpty()
                || journeyDate.isEmpty() || source.isEmpty()
                || destination.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please fill all required fields."
            );
            return;
        }

        if (source.equalsIgnoreCase(destination)) {
            JOptionPane.showMessageDialog(
                this, "Source and destination must be different."
            );
            return;
        }

        LocalDate date;
        int trainNumber;

        try {
            date = LocalDate.parse(journeyDate);

            if (date.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(
                    this, "Journey date cannot be in the past."
                );
                return;
            }

            trainNumber = Integer.parseInt(trainText);

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(
                this, "Date must be in YYYY-MM-DD format."
            );
            return;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                this, "Train number must be numeric."
            );
            return;
        }

        String sql = "INSERT INTO reservations "
            + "(pnr, passenger_name, train_number, class_type, "
            + "journey_date, source_station, destination_station) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            long pnr = 0;
            boolean saved = false;

            for (int attempt = 0; attempt < 5 && !saved; attempt++) {
                pnr = ThreadLocalRandom.current()
                    .nextLong(1000000000L, 10000000000L);

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setLong(1, pnr);
                    ps.setString(2, passenger);
                    ps.setInt(3, trainNumber);
                    ps.setString(4, (String) classBox.getSelectedItem());
                    ps.setDate(5, Date.valueOf(date));
                    ps.setString(6, source);
                    ps.setString(7, destination);
                    ps.executeUpdate();
                    saved = true;

                } catch (SQLIntegrityConstraintViolationException e) {
                    if (attempt == 4) {
                        throw e;
                    }
                }
            }

            if (saved) {
                showDigitalTicket(
                    pnr, passenger, trainName,
                    trainNumber, journeyDate, source, destination,
                    (String) classBox.getSelectedItem()
                );
                clearForm();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this, "Booking failed: " + e.getMessage()
            );
        }
    }

    private void showDigitalTicket(
        long pnr, String passenger, String trainName,
        int trainNumber, String date, String source,
        String destination, String travelClass
    ) {

        JDialog dialog = new JDialog(
            this, "Ticket Booking Confirmed", true
        );
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel ticket = new JPanel(new BorderLayout(10, 15));
        ticket.setBackground(Color.WHITE);
        ticket.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBackground(navy);
        top.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel success = new JLabel("  TICKET BOOKED SUCCESSFULLY!");
        success.setFont(new Font("Arial", Font.BOLD, 17));
        success.setForeground(Color.WHITE);

        JLabel pnrLabel = new JLabel("  PNR: " + pnr);
        pnrLabel.setFont(new Font("Arial", Font.BOLD, 22));
        pnrLabel.setForeground(new Color(255, 220, 100));

        top.add(success);
        top.add(Box.createVerticalStrut(12));
        top.add(pnrLabel);

        JPanel details = new JPanel(new GridLayout(0, 2, 10, 12));
        details.setBackground(Color.WHITE);
        details.setBorder(new EmptyBorder(15, 5, 15, 5));

        addTicketDetail(details, "Passenger", passenger);
        addTicketDetail(details, "Train", trainName);
        addTicketDetail(details, "Train Number", String.valueOf(trainNumber));
        addTicketDetail(details, "Journey Date", date);
        addTicketDetail(details, "From", source);
        addTicketDetail(details, "To", destination);
        addTicketDetail(details, "Class", travelClass);

        JButton close = new JButton("DONE");
        styleButton(close, blue);
        close.addActionListener(e -> dialog.dispose());

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.add(close);

        ticket.add(top, BorderLayout.NORTH);
        ticket.add(details, BorderLayout.CENTER);
        ticket.add(bottom, BorderLayout.SOUTH);

        dialog.add(ticket);
        dialog.setVisible(true);
    }

    private void addTicketDetail(
        JPanel panel, String title, String value
    ) {
        JLabel key = new JLabel(title + ":");
        key.setFont(new Font("Arial", Font.BOLD, 12));
        key.setForeground(navy);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Arial", Font.PLAIN, 12));
        val.setForeground(new Color(55, 65, 80));

        panel.add(key);
        panel.add(val);
    }

    private void clearForm() {
        passengerField.setText("");
        trainNumberField.setText("");
        trainNameField.setText("");
        dateField.setText(LocalDate.now().plusDays(1).toString());
        sourceField.setText("");
        destinationField.setText("");
        classBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new ReservationFrame().setVisible(true)
        );
    }
}

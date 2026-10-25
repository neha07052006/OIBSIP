
package com.neha.reservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class CancellationFrame extends JFrame {

    private JTextField pnrField;
    private JTextArea detailsArea;
    private JButton cancelButton;

    private static final Color NAVY = new Color(20, 42, 75);
    private static final Color BLUE = new Color(38, 110, 210);
    private static final Color RED = new Color(200, 55, 65);
    private static final Color LIGHT_BG = new Color(242, 246, 252);

    public CancellationFrame() {
        setTitle("Online Reservation System - Cancel Ticket");
        setSize(680, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 550));

        JPanel mainPanel = new JPanel(new BorderLayout(0, 18));
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(new EmptyBorder(22, 28, 25, 28));
        setContentPane(mainPanel);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(20, 22, 20, 22));

        JLabel title = new JLabel("CANCEL YOUR TICKET");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));

        JLabel subtitle = new JLabel(
            "Find your booking and confirm cancellation"
        );
        subtitle.setForeground(new Color(215, 225, 240));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(7));
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(header, BorderLayout.NORTH);

        // Center content
        JPanel content = new JPanel();
        content.setBackground(LIGHT_BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel pnrLabel = new JLabel("Enter your 10-digit PNR");
        pnrLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnrLabel.setForeground(NAVY);
        pnrLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel searchPanel = new JPanel(new BorderLayout(12, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(new EmptyBorder(18, 18, 18, 18));
        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        pnrField = new JTextField();
        pnrField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pnrField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 220, 235)),
            new EmptyBorder(10, 12, 10, 12)
        ));
        pnrField.setToolTipText("Enter the PNR received after booking");

        JButton fetchButton = new JButton("Find Booking");
        styleButton(fetchButton, BLUE);
        fetchButton.addActionListener(e -> fetchBooking());

        searchPanel.add(pnrField, BorderLayout.CENTER);
        searchPanel.add(fetchButton, BorderLayout.EAST);

        JLabel detailsTitle = new JLabel("BOOKING DETAILS");
        detailsTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        detailsTitle.setForeground(NAVY);
        detailsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFocusable(false);
        detailsArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        detailsArea.setForeground(new Color(45, 58, 75));
        detailsArea.setBackground(Color.WHITE);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setMargin(new Insets(18, 18, 18, 18));
        detailsArea.setText(
            "  Your booking details will appear here.\n\n"
            + "  1. Enter your PNR.\n"
            + "  2. Click Find Booking.\n"
            + "  3. Review the details before cancelling."
        );

        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(
            new Color(220, 228, 238)
        ));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        cancelButton = new JButton("Cancel Ticket");
        styleButton(cancelButton, RED);
        cancelButton.setEnabled(false);
        cancelButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        cancelButton.addActionListener(e -> cancelBooking());

        content.add(pnrLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(searchPanel);
        content.add(Box.createVerticalStrut(20));
        content.add(detailsTitle);
        content.add(Box.createVerticalStrut(10));
        content.add(scrollPane);
        content.add(Box.createVerticalStrut(16));
        content.add(cancelButton);

        mainPanel.add(content, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel(
            "Please verify your booking details before cancellation."
        );
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footer.setForeground(new Color(100, 110, 125));
        mainPanel.add(footer, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(fetchButton);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(11, 18, 11, 18));
        button.setOpaque(true);
    }

    private void fetchBooking() {
        detailsArea.setText("");
        cancelButton.setEnabled(false);

        String pnrText = pnrField.getText().trim();

        if (!pnrText.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter a valid 10-digit numeric PNR.",
                "Invalid PNR",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String sql = "SELECT r.*, t.train_name FROM reservations r "
                   + "JOIN trains t ON r.train_number = t.train_number "
                   + "WHERE r.pnr = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, Long.parseLong(pnrText));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    detailsArea.setText(
                        "  PNR              : " + rs.getLong("pnr") + "\n\n"
                        + "  Passenger Name   : "
                        + rs.getString("passenger_name") + "\n\n"
                        + "  Train Number     : "
                        + rs.getInt("train_number") + "\n\n"
                        + "  Train Name       : "
                        + rs.getString("train_name") + "\n\n"
                        + "  Travel Class     : "
                        + rs.getString("class_type") + "\n\n"
                        + "  Journey Date     : "
                        + rs.getDate("journey_date") + "\n\n"
                        + "  From             : "
                        + rs.getString("source_station") + "\n\n"
                        + "  To               : "
                        + rs.getString("destination_station")
                    );

                    cancelButton.setEnabled(true);
                } else {
                    detailsArea.setText(
                        "  No booking found for this PNR.\n\n"
                        + "  Please check the PNR and try again."
                    );

                    JOptionPane.showMessageDialog(
                        this,
                        "No booking found for this PNR.",
                        "Booking Not Found",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Database error: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cancelBooking() {
        String pnrText = pnrField.getText().trim();

        if (!pnrText.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(
                this, "Please enter a valid 10-digit PNR."
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to cancel this ticket?\n"
            + "This action cannot be undone.",
            "Confirm Ticket Cancellation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM reservations WHERE pnr = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, Long.parseLong(pnrText));

            int deleted = ps.executeUpdate();

            if (deleted > 0) {
                detailsArea.setText(
                    "  TICKET CANCELLED SUCCESSFULLY\n\n"
                    + "  PNR: " + pnrText + "\n\n"
                    + "  Your reservation has been removed."
                );

                pnrField.setText("");
                cancelButton.setEnabled(false);

                JOptionPane.showMessageDialog(
                    this,
                    "Ticket cancelled successfully!",
                    "Cancellation Successful",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                detailsArea.setText("  Booking not found.");
                cancelButton.setEnabled(false);

                JOptionPane.showMessageDialog(
                    this, "Booking not found."
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Cancellation failed: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new CancellationFrame().setVisible(true)
        );
    }
}

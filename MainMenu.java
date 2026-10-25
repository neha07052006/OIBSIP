
package com.neha.reservation;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class MainMenu extends JFrame {

    private final Color navy = new Color(18, 38, 72);
    private final Color blue = new Color(39, 105, 190);
    private final Color background = new Color(244, 247, 252);
    private final Color textColor = new Color(45, 58, 78);

    private JPanel contentPanel;

    public MainMenu() {

        setTitle("Railway Reservation System");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(850, 600));

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(background);

        // LEFT SIDEBAR
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(navy);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(
            new EmptyBorder(25, 15, 20, 15)
        );

        JLabel logo = new JLabel("  RAILWAY");
        logo.setFont(new Font("Arial", Font.BOLD, 25));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logoSub = new JLabel("  RESERVATION SYSTEM");
        logoSub.setFont(new Font("Arial", Font.PLAIN, 11));
        logoSub.setForeground(new Color(190, 210, 240));
        logoSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(logoSub);
        sidebar.add(Box.createVerticalStrut(45));

        JButton homeBtn = createSideButton("   🏠   Home");
        JButton bookBtn = createSideButton("   🎫   Book Ticket");
        JButton cancelBtn = createSideButton("   ✖   Cancel Ticket");
        JButton exitBtn = createSideButton("   ↪   Exit");

        sidebar.add(homeBtn);
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(bookBtn);
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(cancelBtn);
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(exitBtn);

        sidebar.add(Box.createVerticalGlue());

        JLabel sideFooter = new JLabel("  Java Swing + MySQL");
        sideFooter.setForeground(new Color(180, 195, 220));
        sideFooter.setFont(new Font("Arial", Font.PLAIN, 11));
        sideFooter.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(sideFooter);

        // RIGHT CONTENT AREA
        contentPanel = new JPanel(new BorderLayout(0, 18));
        contentPanel.setBackground(background);
        contentPanel.setBorder(
            new EmptyBorder(25, 28, 20, 28)
        );

        // Top heading
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(background);

        JPanel heading = new JPanel();
        heading.setLayout(
            new BoxLayout(heading, BoxLayout.Y_AXIS)
        );
        heading.setBackground(background);

        JLabel welcome = new JLabel("Welcome to Railway Reservation");
        welcome.setFont(new Font("Arial", Font.BOLD, 25));
        welcome.setForeground(navy);

        JLabel description = new JLabel(
            "Plan your journey. Book your ticket. Travel comfortably."
        );
        description.setFont(new Font("Arial", Font.PLAIN, 14));
        description.setForeground(Color.GRAY);

        heading.add(welcome);
        heading.add(Box.createVerticalStrut(8));
        heading.add(description);

        JLabel userLabel = new JLabel("  USER  ");
        userLabel.setOpaque(true);
        userLabel.setBackground(new Color(222, 236, 255));
        userLabel.setForeground(blue);
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userLabel.setBorder(new EmptyBorder(8, 10, 8, 10));

        top.add(heading, BorderLayout.CENTER);
        top.add(userLabel, BorderLayout.EAST);

        // Train image banner
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(navy);
        banner.setBorder(new LineBorder(Color.WHITE, 2));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(700, 250));

        ImageIcon original = new ImageIcon("images/train.jpg");

        if (original.getIconWidth() > 0) {
            Image scaled = original.getImage().getScaledInstance(
                700, 250, Image.SCALE_SMOOTH
            );
            imageLabel.setIcon(new ImageIcon(scaled));
        } else {
            imageLabel.setText("Train image not found");
            imageLabel.setForeground(Color.WHITE);
        }

        banner.add(imageLabel, BorderLayout.CENTER);

        // Action cards
        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
        cards.setBackground(background);
        cards.setPreferredSize(new Dimension(700, 125));

        JPanel bookCard = createCard(
            "BOOK TICKET",
            "Reserve your next journey",
            "Open booking form",
            blue
        );

        JPanel cancelCard = createCard(
            "CANCEL TICKET",
            "Manage an existing booking",
            "Open cancellation form",
            new Color(205, 75, 75)
        );

        JPanel infoCard = createCard(
            "TRAVEL EASY",
            "Simple and quick reservation",
            "Your journey starts here",
            new Color(35, 145, 115)
        );

        cards.add(bookCard);
        cards.add(cancelCard);
        cards.add(infoCard);

        JPanel center = new JPanel(new BorderLayout(0, 18));
        center.setBackground(background);
        center.add(banner, BorderLayout.CENTER);
        center.add(cards, BorderLayout.SOUTH);

        JLabel footer = new JLabel(
            "ONLINE RESERVATION SYSTEM  |  Have a safe journey!",
            SwingConstants.CENTER
        );
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.setForeground(Color.GRAY);
        footer.setBorder(new EmptyBorder(5, 0, 0, 0));

        contentPanel.add(top, BorderLayout.NORTH);
        contentPanel.add(center, BorderLayout.CENTER);
        contentPanel.add(footer, BorderLayout.SOUTH);

        root.add(sidebar, BorderLayout.WEST);
        root.add(contentPanel, BorderLayout.CENTER);

        add(root);

        // NAVIGATION
        homeBtn.addActionListener(e -> showHome());

        bookBtn.addActionListener(e -> {
            new ReservationFrame().setVisible(true);
        });

        cancelBtn.addActionListener(e -> {
            new CancellationFrame().setVisible(true);
        });

        exitBtn.addActionListener(e -> exitApplication());

        // Card clicks
        bookCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookCard.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new ReservationFrame().setVisible(true);
            }
        });

        cancelCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelCard.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new CancellationFrame().setVisible(true);
            }
        });
    }

    private JButton createSideButton(String text) {

        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(navy);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMaximumSize(new Dimension(200, 46));
        button.setPreferredSize(new Dimension(190, 46));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(blue);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(navy);
            }
        });

        return button;
    }

    private JPanel createCard(
        String title, String description, String detail, Color accent
    ) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(225, 231, 240), 1),
            new EmptyBorder(14, 12, 12, 12)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        titleLabel.setForeground(accent);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(textColor);

        JLabel detailLabel = new JLabel(detail);
        detailLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        detailLabel.setForeground(Color.GRAY);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(descLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(detailLabel);

        return card;
    }

    private void showHome() {
        // The dashboard is already displayed.
        repaint();
    }

    private void exitApplication() {

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new MainMenu().setVisible(true)
        );
    }
}

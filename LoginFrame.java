
package com.neha.reservation;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {

        setTitle("Online Reservation System - Login");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setPreferredSize(new Dimension(350, 140));

        JLabel title = new JLabel("ONLINE RESERVATION LOGIN");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(25, 65, 120));

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField(18);

        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField(18);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(25, 90, 160));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        formPanel.add(userLabel);
        formPanel.add(usernameField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);
        formPanel.add(new JLabel(""));
        formPanel.add(loginButton);

        JPanel wrapper = new JPanel(new BorderLayout(10, 20));
        wrapper.setBackground(new Color(240, 248, 255));
        wrapper.add(title, BorderLayout.NORTH);
        wrapper.add(formPanel, BorderLayout.CENTER);

        mainPanel.add(wrapper);
        add(mainPanel);

        loginButton.addActionListener(e -> login());

        getRootPane().setDefaultButton(loginButton);
    }

    private void login() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter username and password."
            );
            return;
        }

        String sql =
            "SELECT id FROM users WHERE username = ? AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Login Successful!"
                    );

                    new MainMenu().setVisible(true);
                    dispose();

                } else {

                    JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password!"
                    );
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Database error: " + ex.getMessage()
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
            new LoginFrame().setVisible(true)
        );
    }
}

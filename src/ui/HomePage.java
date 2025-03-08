package ui; // Ensure this package name matches your folder structure

import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {
    public HomePage() {
        // Set title
        setTitle("Expense Tracker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(230, 240, 250)); // Light background color

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome to Expense Tracker", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.DARK_GRAY);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        // Style buttons
        loginButton.setPreferredSize(new Dimension(100, 40));
        registerButton.setPreferredSize(new Dimension(100, 40));

        // Add event listeners
        loginButton.addActionListener(e -> {
            dispose(); // Close HomePage
            new LoginPage(); // Open Login Page (Make sure LoginPage.java exists)
        });

        registerButton.addActionListener(e -> {
            dispose(); // Close HomePage
            new RegisterPage(); // Open Register Page (Make sure RegisterPage.java exists)
        });

        // Add components to panels
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        // Add panel to frame
        add(panel);
        setVisible(true);
    }
}

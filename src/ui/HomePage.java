package ui;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {
    public HomePage() {
        setTitle("Expense Tracker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the panel and apply styles
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Expense Tracker", JLabel.CENTER);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        // Apply styles from HomePageStyle
        HomePageStyle.applyStyles(welcomeLabel, loginButton, registerButton, panel);

        // Create button panel and add buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Button actions
        loginButton.addActionListener(e -> {
            dispose();
            new LoginPage(); // Open Login Page
        });

        registerButton.addActionListener(e -> {
            dispose();
            new RegisterPage(); // Open Register Page
        });

        // Add components to the main panel
        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        // Add panel to the frame
        add(panel);
        setVisible(true);
    }
}

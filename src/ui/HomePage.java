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
        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Expense Tracker", JLabel.CENTER);
        JLabel welcomeLabel = new JLabel("Welcome to Expense Tracker", JLabel.CENTER);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        // Apply styles from HomePageStyle
        HomePageStyle.applyStyles(titleLabel, welcomeLabel, loginButton, registerButton, mainPanel);

        // Create button panel and add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);
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
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to the frame
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        // Run the HomePage
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HomePage();
            }
        });
    }
}
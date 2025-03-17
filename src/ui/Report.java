package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Report extends JFrame {
    private String token;

    public Report(String token) {
        this.token = token; // Store the token

        // Set the JFrame properties
        setTitle("Report Page");
        setSize(800, 600); // Adjusted size for better visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to the Report Page!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StartPage(token);  // Navigate back to StartPage with token
                dispose();  // Close current window
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame and make it visible
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        // Example JWT (replace with actual JWT generated during registration)
        String exampleToken = "token";  // Pass your generated token here
        new Report(exampleToken);
    }
}
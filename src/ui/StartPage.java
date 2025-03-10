package ui;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPage extends JFrame {

    public StartPage(String token) {
        // Decode the token to get the email
        String email = getEmailFromToken(token);

        // Extract the username part of the email (before '@')
        String username = email != null ? email.split("@")[0] : "Guest";

        // Set the JFrame properties
        setTitle("Welcome - Expense Tracker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Welcome label with the username or "Guest" if email is null
        JLabel welcomeLabel = new JLabel("Welcome to My Expense, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));

        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Start button to navigate to Expense page
        JButton startButton = new JButton("Start My Expense");
        StartPageStyle.styleButton(startButton);  // Apply styling from your custom method

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Expense();  // Navigate to Expense page
                dispose();  // Close current window
            }
        });

        // Button panel for layout
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.add(startButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add the main panel to the frame and make it visible
        add(mainPanel);
        setVisible(true);
    }

    private String getEmailFromToken(String token) {
        try {
            // Decode JWT token to get the email claim
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim("email").asString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Return null if there is any error decoding the token
        }
    }

    public static void main(String[] args) {
        // Example JWT (replace with actual JWT generated during registration)
        String exampleToken = "token";  // Pass your generated token here
        new StartPage(exampleToken);
    }
}

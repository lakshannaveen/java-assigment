package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;

    public LoginPage() {
        setTitle("Login - Expense Tracker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the panel and apply styles
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        // Create and add the components
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        backButton = new JButton("Back");

        // Apply styles using the LoginPageStyle class
        LoginPageStyle.applyStyles(panel, emailField, passwordField, loginButton, backButton);

        // Button actions
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                char[] password = passwordField.getPassword();

                // Placeholder validation logic
                if (email.equals("user@example.com") && String.valueOf(password).equals("password123")) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    // Navigate to the next page (e.g., HomePage or Dashboard)
                    dispose();
                    new HomePage(); // Open Home Page or Dashboard
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid email or password.");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new HomePage(); // Go back to HomePage
            }
        });

        // Add components to the panel
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(backButton);

        // Add panel to the frame
        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        // Run the LoginPage
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginPage();
            }
        });
    }
}

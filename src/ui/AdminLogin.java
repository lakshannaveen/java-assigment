package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class AdminLogin extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private static final String USERNAME_PLACEHOLDER = "Enter Username";
    private static final String PASSWORD_PLACEHOLDER = "Enter Password";

    public AdminLogin() {
        setTitle("Admin Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel and form panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Admin Login"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create and add labels and fields
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        usernameField.setText(USERNAME_PLACEHOLDER); // Set placeholder text
        usernameField.setForeground(Color.GRAY); // Set placeholder text color
        usernameField.setToolTipText("Enter Username");

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        passwordField.setText(PASSWORD_PLACEHOLDER); // Set placeholder text
        passwordField.setForeground(Color.GRAY); // Set placeholder text color
        passwordField.setToolTipText("Enter Password");

        loginButton = new JButton("Login");

        // Apply styles using AdminStyles
        AdminStyles.applyStyles(formPanel, usernameField, passwordField, loginButton);

        // Add focus listeners to simulate placeholder behavior
        usernameField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals(USERNAME_PLACEHOLDER)) {
                    usernameField.setText("");
                    usernameField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText(USERNAME_PLACEHOLDER);
                    usernameField.setForeground(Color.GRAY);
                }
            }
        });

        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(PASSWORD_PLACEHOLDER)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText(PASSWORD_PLACEHOLDER);
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Validate username and password
                if (username.length() < 5 || username.equals(USERNAME_PLACEHOLDER)) {
                    JOptionPane.showMessageDialog(null, "Username must be at least 5 characters long.");
                    return;
                }

                if (password.length() < 8 || password.equals(PASSWORD_PLACEHOLDER)) {
                    JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long.");
                    return;
                }

                JOptionPane.showMessageDialog(null, "Login Successful!");
                // After successful login, you can proceed to the next page or action.
            }
        });

        // Add components to the form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add main panel to the frame
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminLogin();
            }
        });
    }
}

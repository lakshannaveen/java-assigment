package ui;

import controllers.AdminController;
import models.AdminModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminRegister extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private AdminController adminController;

    public AdminRegister() {
        setTitle("Admin Register");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        adminController = new AdminController(); // Initialize controller

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Admin Register"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        registerButton = new JButton("Register");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.length() < 5) {
                    JOptionPane.showMessageDialog(null, "Username must be at least 5 characters long.");
                    return;
                }

                if (password.length() < 8) {
                    JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long.");
                    return;
                }

                AdminModel newAdmin = new AdminModel(username, password);
                boolean success = adminController.registerAdmin(newAdmin);

                if (success) {
                    JOptionPane.showMessageDialog(null, "Registration Successful!");
                } else {
                    JOptionPane.showMessageDialog(null, "Username already exists. Try another.");
                }
            }
        });

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
        formPanel.add(registerButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminRegister());
    }
}

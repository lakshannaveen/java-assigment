package ui;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;
import controllers.UserController;
import models.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class RegisterPage extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton backButton;
    private UserController userController;
    private ResourceBundle bundle;

    public RegisterPage() {
        try {
            // Get the current locale from HomePage
            bundle = ResourceBundle.getBundle("resources/messages",
                    HomePage.getCurrentLocale(),
                    new UTF8ResourceBundleControl());
        } catch (Exception e) {
            e.printStackTrace();
            bundle = ResourceBundle.getBundle("resources/messages", Locale.ENGLISH);
        }

        userController = new UserController();

        setTitle(bundle.getString("title") + " - " + bundle.getString("register"));
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel and apply styles
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("register") + " " + bundle.getString("form")));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create and add the components
        JLabel nameLabel = new JLabel(bundle.getString("name") + ":");
        nameField = new JTextField(20);

        JLabel emailLabel = new JLabel(bundle.getString("email") + ":");
        emailField = new JTextField(20);

        JLabel passwordLabel = new JLabel(bundle.getString("password") + ":");
        passwordField = new JPasswordField(20);

        registerButton = new JButton(bundle.getString("register"));
        backButton = new JButton(bundle.getString("back"));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);

        // Apply styles using the RegisterPageStyle class
        RegisterPageStyle.applyStyles(formPanel, nameField, emailField, passwordField, registerButton, backButton);

        // Register button action
        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, bundle.getString("fill.all.fields"), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (name.length() > 25) {
                JOptionPane.showMessageDialog(this, bundle.getString("name.length"), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, bundle.getString("invalid.email"), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.length() < 8) {
                JOptionPane.showMessageDialog(this, bundle.getString("password.length"), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create the user object
            User user = new User(name, email, password);
            if (userController.registerUser(user)) {
                JOptionPane.showMessageDialog(this, bundle.getString("register.success"), "Success", JOptionPane.INFORMATION_MESSAGE);

                // Log the registration event
                Logger.logRegister(email, "user");

                dispose(); // Close the register page

                // Generate JWT Token
                String token = generateJWT(email);

                // Pass the token to the StartPage
                new StartPage(token); // Use the generated token here
            } else {
                JOptionPane.showMessageDialog(this, bundle.getString("register.failed"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Back button action
        backButton.addActionListener(e -> {
            dispose(); // Close register page
            new HomePage(); // Navigate back to HomePage
        });

        // Add components to the form panel with GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(registerButton, gbc);

        // Add back button to the top-left corner of the main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);

        // Add panels to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add main panel to the frame
        add(mainPanel);
        setVisible(true);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private String generateJWT(String email) {
        try {
            // Define the JWT signing algorithm
            Algorithm algorithm = Algorithm.HMAC256("1020"); // Use a secret key for signing
            String token = JWT.create()
                    .withClaim("email", email)  // Add email claim to the token
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // Expiration time: 1 hour
                    .sign(algorithm); // Sign and generate the token
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        // Run the RegisterPage
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegisterPage();
            }
        });
    }
}
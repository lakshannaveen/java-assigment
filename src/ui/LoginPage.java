package ui;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import java.util.Date;

public class LoginPage extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;

    private static final String CONNECTION_STRING = "mongodb+srv://naveen:uD4DxPM4lBhZ4gOH@cluster0.lbyqk.mongodb.net/test?retryWrites=true&w=majority&appName=Cluster0";
    private static final String DATABASE_NAME = "test";
    private static final String COLLECTION_NAME = "users";
    private static final String SECRET_KEY = "1020"; // Secret key for JWT

    private int loginAttempts = 0; // Counter for login attempts

    public LoginPage() {
        setTitle("Login - Expense Tracker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Login Form"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        loginButton = new JButton("Login");
        backButton = new JButton("Back");
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);

        LoginPageStyle.applyStyles(formPanel, emailField, passwordField, loginButton, backButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (email.equals("admin123")) {
                    loginAttempts++; // Increment the counter for admin login attempts
                    if (loginAttempts >= 3) {
                        // Navigate to AdminLogin after 3 attempts with the email "admin123"
                        JOptionPane.showMessageDialog(null, "Navigating to Admin Login...");
                        dispose();
                        new AdminLogin(); // Open AdminLogin page
                        return;
                    }
                } else {
                    // Perform regular validation for non-admin users
                    if (!isValidEmail(email)) {
                        JOptionPane.showMessageDialog(null, "Invalid email format.");
                        return;
                    }

                    if (password.length() < 8) {
                        JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long.");
                        return;
                    }

                    String token = authenticateUser(email, password);
                    if (token != null) {
                        JOptionPane.showMessageDialog(null, "Login Successful!");

                        // Log the login information
                        Logger.logLogin(email);

                        // Pass token to StartPage
                        dispose();
                        new StartPage(token); // Pass the token to StartPage constructor
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid email or password.");
                    }
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new HomePage(); // Navigate to HomePage.java instead of StartPage.java
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(emailField, gbc);

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

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private String authenticateUser(String email, String password) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document query = new Document("email", email).append("password", password);
            Document user = collection.find(query).first();

            if (user != null) {
                // Generate JWT token after successful authentication
                return generateJwtToken(email);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateJwtToken(String email) {
        try {
            long expirationTime = 365 * 24 * 60 * 60 * 1000L; // 1 year in milliseconds
            Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            return JWT.create()
                    .withSubject(email)
                    .withClaim("email", email)
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginPage();
            }
        });
    }
}
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
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginPage extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;
    private ResourceBundle bundle;

    private static final String CONNECTION_STRING = "mongodb+srv://naveen:uD4DxPM4lBhZ4gOH@cluster0.lbyqk.mongodb.net/test?retryWrites=true&w=majority&appName=Cluster0";
    private static final String DATABASE_NAME = "test";
    private static final String COLLECTION_NAME = "users";
    private static final String SECRET_KEY = "1020"; // Secret key for JWT

    private int loginAttempts = 0; // Counter for login attempts

    public LoginPage() {
        try {
            // Get the current locale from HomePage
            bundle = ResourceBundle.getBundle("resources/messages",
                    HomePage.getCurrentLocale(),
                    new UTF8ResourceBundleControl());
        } catch (Exception e) {
            e.printStackTrace();
            bundle = ResourceBundle.getBundle("resources/messages", Locale.ENGLISH);
        }

        initializeUI();
    }

    private void initializeUI() {
        setTitle(getLocalizedString("title") + " - " + getLocalizedString("login"));
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(getLocalizedString("login") + " " + getLocalizedString("form")));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel(getLocalizedString("email") + ":");
        emailField = new JTextField(20);

        JLabel passwordLabel = new JLabel(getLocalizedString("password") + ":");
        passwordField = new JPasswordField(20);

        loginButton = new JButton(getLocalizedString("login"));
        backButton = new JButton(getLocalizedString("back"));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);

        LoginPageStyle.applyStyles(formPanel, emailField, passwordField, loginButton, backButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (email.equals("admin123")) {
                    loginAttempts++; // Increment counter
                    if (loginAttempts >= 3) {
                        JOptionPane.showMessageDialog(null, "Navigating to admin login...");
                        dispose();
                        new AdminLogin();
                    } else {
                        JOptionPane.showMessageDialog(null, "Admin click count: " + loginAttempts);
                    }
                    return;
                } else {
                    if (!isValidEmail(email)) {
                        JOptionPane.showMessageDialog(null, getLocalizedString("invalid.email"));
                        return;
                    }

                    if (password.length() < 8) {
                        JOptionPane.showMessageDialog(null, getLocalizedString("password.length"));
                        return;
                    }

                    String token = authenticateUser(email, password);
                    if (token != null) {
                        showSuccessDialog(getLocalizedString("login.success", "Login successful!"));

                        Logger.logLogin(email, "user");

                        dispose();
                        new StartPage(token);
                    } else {
                        JOptionPane.showMessageDialog(null, getLocalizedString("invalid.credentials", "Invalid email or password"));
                    }
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new HomePage();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
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

    private void showSuccessDialog(String message) {
        JDialog dialog = new JDialog(this, "Success", true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setForeground(new Color(0, 128, 0)); // Green text
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton okButton = new JButton("OK");
        okButton.setBackground(new Color(0, 128, 0)); // Green background
        okButton.setForeground(Color.WHITE); // White text
        okButton.setFocusPainted(false);
        okButton.setFont(new Font("Arial", Font.BOLD, 12));
        okButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);

        dialog.add(messageLabel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private String getLocalizedString(String key) {
        return getLocalizedString(key, key);
    }

    private String getLocalizedString(String key, String defaultValue) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private String authenticateUser(String email, String password) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document query = new Document("email", email).append("password", password);
            Document user = collection.find(query).first();

            if (user != null) {
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
            long expirationTime = 365L * 24 * 60 * 60 * 1000; // 1 year
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
        SwingUtilities.invokeLater(() -> new LoginPage());
    }
}

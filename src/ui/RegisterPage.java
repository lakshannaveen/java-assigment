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

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("register") + " " + bundle.getString("form")));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

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

        RegisterPageStyle.applyStyles(formPanel, nameField, emailField, passwordField, registerButton, backButton);

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

            User user = new User(name, email, password);
            if (userController.registerUser(user)) {
                JOptionPane.showMessageDialog(this,
                        bundle.getString("register.success") + "\n" +
                                bundle.getString("email.sent.notification"),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                Logger.logRegister(email, "user");
                dispose();
                String token = generateJWT(email);
                new StartPage(token);
            } else {
                JOptionPane.showMessageDialog(this, bundle.getString("register.failed"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            new HomePage();
        });

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

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

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
            Algorithm algorithm = Algorithm.HMAC256("1020");
            return JWT.create()
                    .withClaim("email", email)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterPage());
    }
}
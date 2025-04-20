package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.io.IOException;
import java.lang.IllegalAccessException;

public class HomePage extends JFrame {
    private ResourceBundle bundle;
    private JLabel titleLabel;
    private JLabel welcomeLabel;
    private JButton loginButton;
    private JButton registerButton;
    private JComboBox<String> languageComboBox;
    private static Locale currentLocale = Locale.ENGLISH; // Track current locale

    public HomePage() {
        try {
            // Load default bundle with UTF-8 support
            bundle = ResourceBundle.getBundle("resources/messages",
                    currentLocale,
                    new UTF8ResourceBundleControl());
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to default loading
            bundle = ResourceBundle.getBundle("resources/messages", currentLocale);
            JOptionPane.showMessageDialog(this,
                    "Error loading language resources. Using English as fallback.",
                    "Resource Error",
                    JOptionPane.WARNING_MESSAGE);
        }

        initializeUI();
    }

    private void initializeUI() {
        setTitle(bundle.getString("title"));
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        JPanel mainPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel(bundle.getString("title"), JLabel.CENTER);
        welcomeLabel = new JLabel(bundle.getString("welcome"), JLabel.CENTER);
        loginButton = new JButton(bundle.getString("login"));
        registerButton = new JButton(bundle.getString("register"));

        // Language selection
        String[] languages = {"English", "Singlish"};
        languageComboBox = new JComboBox<>(languages);
        // Set the selected item based on current locale
        languageComboBox.setSelectedItem(currentLocale.equals(new Locale("si")) ? "Singlish" : "English");
        JLabel languageLabel = new JLabel(bundle.getString("language") + ": ");

        JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        languagePanel.setOpaque(false);
        languagePanel.add(languageLabel);
        languagePanel.add(languageComboBox);

        // Apply styles
        HomePageStyle.applyStyles(titleLabel, welcomeLabel, loginButton, registerButton, mainPanel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Button actions
        loginButton.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        registerButton.addActionListener(e -> {
            dispose();
            new RegisterPage();
        });

        // Language selection action with error handling
        languageComboBox.addActionListener(e -> {
            String selectedLanguage = (String) languageComboBox.getSelectedItem();
            currentLocale = "Singlish".equals(selectedLanguage) ? new Locale("si")
                    : Locale.ENGLISH;
            try {
                bundle = ResourceBundle.getBundle("resources/messages", currentLocale, new UTF8ResourceBundleControl());
                updateTexts();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error loading " + selectedLanguage + " resources",
                        "Language Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Layout components
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(languagePanel, BorderLayout.PAGE_START);

        add(mainPanel);
        setVisible(true);
    }

    private void updateTexts() {
        try {
            setTitle(bundle.getString("title"));
            titleLabel.setText(bundle.getString("title"));
            welcomeLabel.setText(bundle.getString("welcome"));
            loginButton.setText(bundle.getString("login"));
            registerButton.setText(bundle.getString("register"));
            ((JLabel) ((JPanel) languageComboBox.getParent()).getComponent(0)).setText(bundle.getString("language") + ": ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new HomePage();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Failed to initialize application",
                        "Startup Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
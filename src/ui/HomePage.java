package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

public class HomePage extends JFrame {
    private ResourceBundle bundle;
    private JLabel titleLabel;
    private JLabel welcomeLabel;
    private JButton loginButton;
    private JButton registerButton;
    private JComboBox<String> languageComboBox;
    private static Locale currentLocale = Locale.ENGLISH;

    private String fullWelcomeText = "Welcome to Expense Tracker";
    private int welcomeTextIndex = 0;
    private Timer typingTimer;

    public HomePage() {
        try {
            bundle = ResourceBundle.getBundle("resources/messages", currentLocale, new UTF8ResourceBundleControl());
        } catch (Exception e) {
            e.printStackTrace();
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

        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());

        titleLabel = new JLabel(bundle.getString("title"), JLabel.CENTER);
        welcomeLabel = new JLabel("", JLabel.CENTER); // Start empty for animation
        loginButton = new JButton(bundle.getString("login"));
        registerButton = new JButton(bundle.getString("register"));

        loginButton.setPreferredSize(new Dimension(150, 40));
        registerButton.setPreferredSize(new Dimension(150, 40));

        String[] languages = {"English", "Singlish"};
        languageComboBox = new JComboBox<>(languages);
        languageComboBox.setSelectedItem(currentLocale.equals(new Locale("si")) ? "Singlish" : "English");
        JLabel languageLabel = new JLabel(bundle.getString("language") + ": ");

        JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        languagePanel.setOpaque(false);
        languagePanel.add(languageLabel);
        languagePanel.add(languageComboBox);

        HomePageStyle.applyStyles(titleLabel, welcomeLabel, loginButton, registerButton, mainPanel, languageComboBox);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        loginButton.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        registerButton.addActionListener(e -> {
            dispose();
            new RegisterPage();
        });

        languageComboBox.addActionListener(e -> {
            String selectedLanguage = (String) languageComboBox.getSelectedItem();
            currentLocale = "Singlish".equals(selectedLanguage) ? new Locale("si") : Locale.ENGLISH;
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

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(languagePanel, BorderLayout.PAGE_START);

        add(mainPanel);
        setVisible(true);

        startTypingAnimation();
    }

    private void startTypingAnimation() {
        fullWelcomeText = bundle.getString("welcome");
        welcomeLabel.setText("");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomeLabel.setForeground(Color.DARK_GRAY);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        welcomeTextIndex = 0;

        if (typingTimer != null && typingTimer.isRunning()) {
            typingTimer.stop();
        }

        typingTimer = new Timer(100, e -> {
            if (welcomeTextIndex < fullWelcomeText.length()) {
                welcomeLabel.setText(welcomeLabel.getText() + fullWelcomeText.charAt(welcomeTextIndex));
                welcomeTextIndex++;
            } else {
                ((Timer) e.getSource()).stop();
            }
        });

        typingTimer.setInitialDelay(300); // delay before typing starts
        typingTimer.start();
    }

    private void updateTexts() {
        try {
            setTitle(bundle.getString("title"));
            titleLabel.setText(bundle.getString("title"));
            loginButton.setText(bundle.getString("login"));
            registerButton.setText(bundle.getString("register"));
            ((JLabel) ((JPanel) languageComboBox.getParent()).getComponent(0)).setText(bundle.getString("language") + ": ");

            startTypingAnimation(); // restart typing with new language
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage());
    }
}

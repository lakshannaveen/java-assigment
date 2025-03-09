package ui;

import javax.swing.*;
import java.awt.*;

public class StartPage extends JFrame {

    public StartPage() {
        setTitle("Welcome - Expense Tracker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel and apply styles
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to the Start Page!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));

        // Add the welcome label to the main panel
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Add main panel to the frame
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        // Run the StartPage
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartPage();
            }
        });
    }
}
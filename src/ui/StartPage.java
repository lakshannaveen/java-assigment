package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPage extends JFrame {

    public StartPage() {
        setTitle("Welcome - Expense Tracker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel and apply styles
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to My Expense!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));

        // Add the welcome label to the main panel
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Create and style the button
        JButton startButton = new JButton("Start My Expense");
        StartPageStyle.styleButton(startButton);

        // Add action listener to the button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to Expense page
                new Expense(); // Assuming Expense.java is another JFrame class
                dispose(); // Close the current window
            }
        });

        // Create a panel for the button and add the button to it
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.add(startButton);

        // Add the button panel to the main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

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
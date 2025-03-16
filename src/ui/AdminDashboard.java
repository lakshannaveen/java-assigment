package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AdminDashboard extends JFrame {

    private static final String LOG_FILE_PATH = "login_logs.txt";

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome to Admin Dashboard", SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        JButton showLogsButton = new JButton("Show Logs");
        add(showLogsButton, BorderLayout.SOUTH);

        showLogsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLogs();
            }
        });

        setVisible(true);
    }

    private void showLogs() {
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            textArea.setText("Failed to load logs.");
        }

        JOptionPane.showMessageDialog(this, scrollPane, "Log Contents", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminDashboard();
            }
        });
    }
}
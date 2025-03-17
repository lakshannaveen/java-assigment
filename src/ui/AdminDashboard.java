package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Admin Dashboard", SwingConstants.CENTER);
        AdminDashboardStyle.applyStyle(welcomeLabel);
        add(welcomeLabel, BorderLayout.NORTH);

        JButton showLogsButton = new JButton("Show Logs");
        JButton accountsButton = new JButton("Accounts");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(showLogsButton);
        buttonPanel.add(accountsButton);
        add(buttonPanel, BorderLayout.CENTER);

        showLogsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLogs();
            }
        });

        accountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminUserAccounts();
                dispose(); // Close the current frame
            }
        });

        AdminDashboardStyle.applyStyle(this);
        setVisible(true);
    }

    private void showLogs() {
        String[] columnNames = {"Event", "UserType", "Username", "Time"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table read-only
            }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] logParts = line.split(", ");
                String[] rowData = new String[4];
                for (String part : logParts) {
                    String[] keyValue = part.split(": ");
                    if (keyValue.length == 2) {
                        switch (keyValue[0]) {
                            case "Event":
                                rowData[0] = keyValue[1];
                                break;
                            case "UserType":
                                rowData[1] = keyValue[1];
                                break;
                            case "Username":
                                rowData[2] = keyValue[1];
                                break;
                            case "Time":
                                rowData[3] = keyValue[1];
                                break;
                        }
                    }
                }
                tableModel.addRow(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load logs.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
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
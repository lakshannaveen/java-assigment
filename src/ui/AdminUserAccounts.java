package ui;

import controllers.UserController;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminUserAccounts extends JFrame {

    private final UserController userController;

    public AdminUserAccounts() {
        userController = new UserController();

        setTitle("Admin User Accounts");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Label for the title
        JLabel accountsLabel = new JLabel("User Accounts", SwingConstants.CENTER);
        AdminUserAccountsStyle.applyStyle(accountsLabel); // Apply label style
        accountsLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Set font size and bold
        accountsLabel.setForeground(new Color(30, 144, 255)); // Set color for title (blue)
        add(accountsLabel, BorderLayout.NORTH);

        // Table to display user accounts
        String[] columnNames = {"Name", "Email"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table read-only
            }
        };
        JTable table = new JTable(tableModel);
        AdminUserAccountsStyle.applyTableStyle(table); // Apply custom table styles
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load user accounts and populate the table
        List<User> users = userController.getAllUsers();
        for (User user : users) {
            Object[] rowData = {user.getName(), user.getEmail()};
            tableModel.addRow(rowData);
        }

        // Back button to return to AdminDashboard
        JButton backButton = new JButton("Back");
        AdminUserAccountsStyle.applyButtonStyle(backButton); // Apply button styles
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminDashboard(); // Open Admin Dashboard
                dispose(); // Close the current frame
            }
        });

        JPanel buttonPanel = new JPanel();
        AdminUserAccountsStyle.applyPanelStyle(buttonPanel); // Apply style to the panel
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        AdminUserAccountsStyle.applyStyle(this); // Apply JFrame styles
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminUserAccounts();
            }
        });
    }
}

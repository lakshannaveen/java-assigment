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

        JLabel accountsLabel = new JLabel("User Accounts", SwingConstants.CENTER);
        AdminUserAccountsStyle.applyStyle(accountsLabel);
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
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminDashboard();
                dispose(); // Close the current frame
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        AdminUserAccountsStyle.applyStyle(this);
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
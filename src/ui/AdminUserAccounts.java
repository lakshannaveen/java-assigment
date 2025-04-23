package ui;

import controllers.UserController;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title Label
        JLabel accountsLabel = new JLabel("User Accounts", SwingConstants.CENTER);
        AdminUserAccountsStyle.applyModernStyle(accountsLabel);
        accountsLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        accountsLabel.setForeground(new Color(70, 130, 180)); // Soft blue
        add(accountsLabel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Name", "Email"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        AdminUserAccountsStyle.applyModernTableStyle(table);

        // Center cell content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load user data
        List<User> users = userController.getAllUsers();
        for (User user : users) {
            Object[] rowData = {user.getName(), user.getEmail()};
            tableModel.addRow(rowData);
        }

        // Back Button
        JButton backButton = new JButton("Back");
        AdminUserAccountsStyle.applyModernButtonStyle(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminDashboard();
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        AdminUserAccountsStyle.applyPanelStyle(buttonPanel);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        AdminUserAccountsStyle.applyStyle(this);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminUserAccounts());
    }
}

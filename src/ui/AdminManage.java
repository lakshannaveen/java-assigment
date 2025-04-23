package ui;

import controllers.AdminController;
import models.AdminModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class AdminManage extends JFrame {
    public AdminManage() {
        setTitle("Manage Admins");
        setSize(700, 450); // Increased window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(230, 240, 255);
                Color color2 = new Color(180, 200, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Title label
        JLabel titleLabel = new JLabel("Admin Management", SwingConstants.CENTER);
        AdminDashboardStyle.applyHeaderStyle(titleLabel);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table to display admins
        String[] columnNames = {"Username", "Password"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable adminTable = new JTable(tableModel);
        adminTable.setRowHeight(28); // Increase row height
        adminTable.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Increase table font
        adminTable.setSelectionBackground(new Color(180, 200, 255));
        adminTable.setSelectionForeground(Color.BLACK);

        JTableHeader header = adminTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(100, 30));

        JScrollPane scrollPane = new JScrollPane(adminTable);
        scrollPane.setPreferredSize(new Dimension(650, 250)); // Set preferred size for better content layout
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        JButton addButton = createStyledButton("Add Admin");
        JButton refreshButton = createStyledButton("Refresh");
        JButton deleteButton = createStyledButton("Delete Admin");
        JButton backButton = createStyledButton("Back to Dashboard");

        int maxWidth = Math.max(addButton.getPreferredSize().width,
                Math.max(refreshButton.getPreferredSize().width,
                        Math.max(deleteButton.getPreferredSize().width,
                                backButton.getPreferredSize().width)));

        Dimension uniformSize = new Dimension(maxWidth, 35);
        addButton.setPreferredSize(uniformSize);
        refreshButton.setPreferredSize(uniformSize);
        deleteButton.setPreferredSize(uniformSize);
        backButton.setPreferredSize(uniformSize);

        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load admin data
        refreshAdminTable(tableModel);

        // Button actions
        refreshButton.addActionListener(e -> refreshAdminTable(tableModel));

        backButton.addActionListener(e -> {
            new AdminDashboard();
            dispose();
        });

        addButton.addActionListener(e -> showAddAdminDialog(tableModel));

        deleteButton.addActionListener(e -> {
            int selectedRow = adminTable.getSelectedRow();
            if (selectedRow >= 0) {
                String username = (String) tableModel.getValueAt(selectedRow, 0);
                deleteAdmin(username, tableModel);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an admin to delete", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(mainPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                if (getModel().isRollover()) {
                    g2.setColor(new Color(70, 130, 180));
                } else {
                    g2.setColor(new Color(100, 149, 237));
                }
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                setForeground(Color.WHITE);
                super.paintComponent(g);
            }
        };

        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);

        return button;
    }

    private void refreshAdminTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        AdminController adminController = new AdminController();
        List<AdminModel> admins = adminController.getAllAdmins();

        for (AdminModel admin : admins) {
            tableModel.addRow(new Object[]{admin.getUsername(), admin.getPassword()});
        }
    }

    private void showAddAdminDialog(DefaultTableModel tableModel) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Admin", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            AdminController adminController = new AdminController();
            boolean success = adminController.registerAdmin(new AdminModel(username, password));

            if (success) {
                JOptionPane.showMessageDialog(this, "Admin added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshAdminTable(tableModel);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add admin. Username may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteAdmin(String username, DefaultTableModel tableModel) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete admin '" + username + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            AdminController adminController = new AdminController();
            boolean success = adminController.deleteAdmin(username);

            if (success) {
                JOptionPane.showMessageDialog(this, "Admin deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshAdminTable(tableModel);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete admin", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

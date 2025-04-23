package ui;

import controllers.AdminController;
import models.AdminModel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AdminManage extends JFrame {
    private JTable adminTable;
    private DefaultTableModel tableModel;

    public AdminManage() {
        setTitle("Manage Admins");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

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

        JLabel titleLabel = new JLabel("Admin Management", SwingConstants.CENTER);
        AdminDashboardStyle.applyHeaderStyle(titleLabel);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Username", "Password", "Action"};
        tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };

        adminTable = new JTable(tableModel);
        adminTable.setRowHeight(30);
        adminTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        adminTable.setSelectionBackground(new Color(180, 200, 255));
        adminTable.setSelectionForeground(Color.BLACK);

        JTableHeader header = adminTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(100, 30));

        // Set custom cell renderer/editor for Action column
        adminTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        adminTable.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(adminTable);
        scrollPane.setPreferredSize(new Dimension(700, 250));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        JButton addButton = createStyledButton("Add Admin");
        JButton refreshButton = createStyledButton("Refresh");
        JButton backButton = createStyledButton("Back to Dashboard");

        int maxWidth = Math.max(addButton.getPreferredSize().width,
                Math.max(refreshButton.getPreferredSize().width, backButton.getPreferredSize().width));
        Dimension uniformSize = new Dimension(maxWidth, 35);
        addButton.setPreferredSize(uniformSize);
        refreshButton.setPreferredSize(uniformSize);
        backButton.setPreferredSize(uniformSize);

        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        refreshAdminTable();

        refreshButton.addActionListener(e -> refreshAdminTable());

        backButton.addActionListener(e -> {
            new AdminDashboard();
            dispose();
        });

        addButton.addActionListener(e -> showAddAdminDialog());

        add(mainPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(getModel().isRollover() ? new Color(70, 130, 180) : new Color(100, 149, 237));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                setForeground(Color.WHITE);
                super.paintComponent(g);
            }
        };

        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);
        return button;
    }

    private void refreshAdminTable() {
        tableModel.setRowCount(0);
        AdminController adminController = new AdminController();
        List<AdminModel> admins = adminController.getAllAdmins();

        for (AdminModel admin : admins) {
            tableModel.addRow(new Object[]{admin.getUsername(), admin.getPassword(), "Delete"});
        }
    }

    private void showAddAdminDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Admin", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            AdminController adminController = new AdminController();
            if (adminController.registerAdmin(new AdminModel(username, password))) {
                JOptionPane.showMessageDialog(this, "Admin added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshAdminTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add admin. Username may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int col) {
            setText("Delete");
            setBackground(isSelected ? new Color(220, 50, 50) : new Color(200, 0, 0));
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private final JButton button = new JButton("Delete");
        private String currentUser;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button.setOpaque(true);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("SansSerif", Font.BOLD, 12));
            button.setBorderPainted(false);
            button.setFocusPainted(false);

            button.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(button, "Delete admin '" + currentUser + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    AdminController adminController = new AdminController();
                    if (adminController.deleteAdmin(currentUser)) {
                        JOptionPane.showMessageDialog(button, "Admin deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshAdminTable();
                    } else {
                        JOptionPane.showMessageDialog(button, "Failed to delete admin", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentUser = (String) table.getValueAt(row, 0);
            button.setBackground(new Color(200, 0, 0));
            return button;
        }
    }
}

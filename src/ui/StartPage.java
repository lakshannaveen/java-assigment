package ui;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import controllers.ExpenseController;
import models.ExpenseModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StartPage extends JFrame {
    private ExpenseController expenseController;
    private String token;
    private JTabbedPane tabbedPane;

    public StartPage(String token) {
        this.token = token; // Store the token
        // Decode the token to get the email
        String email = getEmailFromToken(token);

        // Extract the username part of the email (before '@')
        String username = email != null ? email.split("@")[0] : "Guest";

        // Set the JFrame properties
        setTitle("Welcome - Expense Tracker");
        setSize(800, 600); // Adjusted size for better visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize the expense controller
        expenseController = new ExpenseController();

        // Main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Welcome label with the username or "Guest" if email is null
        JLabel welcomeLabel = new JLabel("Welcome to My Expense, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));

        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Tabbed pane to display expenses by pocket name
        tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Fetch and display expenses
        loadExpenses(email);

        // Start and Refresh buttons panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JButton startButton = new JButton("Start My Expense");
        StartPageStyle.styleGreenButton(startButton);  // Apply styling for green button

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Expense(token);  // Pass the token to Expense
                dispose();  // Close current window
            }
        });

        JButton refreshButton = new JButton("Refresh");
        StartPageStyle.styleGreenButton(refreshButton);  // Apply styling for green button

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadExpenses(email);  // Reload expenses
            }
        });

        gbc.gridx = 0;
        buttonPanel.add(startButton, gbc);
        gbc.gridx = 1;
        buttonPanel.add(refreshButton, gbc);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame and make it visible
        add(mainPanel);
        setVisible(true);
    }

    private void loadExpenses(String email) {
        tabbedPane.removeAll();  // Clear existing tabs

        List<ExpenseModel> expenses = expenseController.getExpensesByEmail(email);

        // Group expenses by pocket name
        Map<String, List<ExpenseModel>> expensesByPocketName = expenses.stream()
                .collect(Collectors.groupingBy(ExpenseModel::getPocketName));

        // Create a table for each pocket name and add it to the tabbed pane
        for (Map.Entry<String, List<ExpenseModel>> entry : expensesByPocketName.entrySet()) {
            String pocketName = entry.getKey();
            List<ExpenseModel> pocketExpenses = entry.getValue();

            String[] columnNames = {"Month", "Expense Name", "Amount", "Date", "Actions"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            for (ExpenseModel expense : pocketExpenses) {
                Object[] rowData = {
                        expense.getSelectedMonth(),
                        expense.getExpenseName(),
                        expense.getAmount(),
                        expense.getDate(),
                        "Delete" // Placeholder for delete button
                };
                tableModel.addRow(rowData);
            }

            JTable expenseTable = new JTable(tableModel);
            expenseTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
            expenseTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

            StartPageStyle.styleTable(expenseTable);  // Apply custom styles to the table

            JScrollPane scrollPane = new JScrollPane(expenseTable);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(scrollPane, BorderLayout.CENTER);

            // Add expense button
            JButton addExpenseButton = new JButton("Add Expense");
            StartPageStyle.styleGreenButton(addExpenseButton);  // Apply custom styles to the button
            addExpenseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new AddExpense(token, pocketName);  // Navigate to AddExpense form with token and pocketName
                }
            });

            // Delete pocket button
            JButton deletePocketButton = new JButton("Delete Pocket");
            StartPageStyle.styleRedButton(deletePocketButton);  // Apply custom styles to the button
            deletePocketButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Placeholder for delete pocket functionality
                    JOptionPane.showMessageDialog(panel, "Pocket '" + pocketName + "' deleted.");
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(addExpenseButton);
            buttonPanel.add(deletePocketButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            tabbedPane.addTab(pocketName, panel);
        }
    }

    private String getEmailFromToken(String token) {
        try {
            // Decode JWT token to get the email claim
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim("email").asString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Return null if there is any error decoding the token
        }
    }

    public static void main(String[] args) {
        // Example JWT (replace with actual JWT generated during registration)
        String exampleToken = "token";  // Pass your generated token here
        new StartPage(exampleToken);
    }

    // Custom ButtonRenderer class
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Custom ButtonEditor class
    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private JButton button;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                // Placeholder for delete functionality
                JOptionPane.showMessageDialog(button, label + ": Deleted");
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
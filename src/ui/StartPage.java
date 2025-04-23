package ui;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import controllers.ExpenseController;
import models.ExpenseModel;
import org.apache.commons.text.similarity.LevenshteinDistance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StartPage extends JFrame {
    private ExpenseController expenseController;
    private String token;
    private JTabbedPane tabbedPane;
    private JTextField searchField;
    private JButton searchButton;

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

        // Create clock panel
        JPanel clockPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel clockLabel = new JLabel();
        clockLabel.setFont(new Font("Serif", Font.BOLD, 16));
        clockPanel.add(clockLabel);

        // Initialize and start the clock timer
        Timer clockTimer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            clockLabel.setText( sdf.format(new Date()));
        });
        clockTimer.start();

        // Welcome label with the username or "Guest" if email is null
        JLabel welcomeLabel = new JLabel("Welcome to My Expense, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));

        // Create north panel to hold both welcome label and clock
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(welcomeLabel, BorderLayout.NORTH);
        northPanel.add(clockPanel, BorderLayout.SOUTH);
        mainPanel.add(northPanel, BorderLayout.NORTH);

        // Tabbed pane to display expenses by pocket name
        tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Fetch and display expenses
        loadExpenses(email);

        // Start and Refresh buttons panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JButton startButton = new JButton("Create a pocket");
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

        // Search field and button
        searchField = new JTextField("Search by expense name", 15); // Reduced column count
        StartPageStyle.styleSearchField(searchField);  // Apply styling for search field
        searchField.setForeground(Color.GRAY);  // Set initial placeholder color
        // Add FocusListener
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search by expense name")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search by expense name");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        searchButton = new JButton("Search");
        StartPageStyle.styleBlueButton(searchButton);  // Apply styling for blue button

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchExpenses(email, searchField.getText().trim());
            }
        });

        gbc.gridx = 2;
        buttonPanel.add(searchField, gbc);
        gbc.gridx = 3;
        buttonPanel.add(searchButton, gbc);

        // Add Report button
        JButton reportButton = new JButton("Report");
        StartPageStyle.styleBlueButton(reportButton);  // Apply styling for blue button

        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Report(token);  // Navigate to Report page with token
                dispose();  // Close current window
            }
        });

        gbc.gridx = 4;
        buttonPanel.add(reportButton, gbc);

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

            String[] columnNames = {"Month", "Expense Name", "Amount", "Date", "Actions", "Update"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            double totalAmount = 0;
            for (ExpenseModel expense : pocketExpenses) {
                Object[] rowData = {
                        expense.getSelectedMonth(),
                        expense.getExpenseName(),
                        expense.getAmount(),
                        expense.getDate(),
                        "Delete", // Placeholder for delete button
                        "Update"  // Placeholder for update button
                };
                tableModel.addRow(rowData);
                totalAmount += expense.getAmount();
            }

            JTable expenseTable = new JTable(tableModel);
            expenseTable.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));
            expenseTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
            expenseTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), expenseTable, email, pocketName, false));
            expenseTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
            expenseTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), expenseTable, email, pocketName, true));

            StartPageStyle.styleTable(expenseTable);  // Apply custom styles to the table

            JScrollPane scrollPane = new JScrollPane(expenseTable);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(scrollPane, BorderLayout.CENTER);

            // Add total amount label at the bottom
            JLabel totalAmountLabel = new JLabel("Total Amount: " + totalAmount);
            totalAmountLabel.setFont(new Font("Serif", Font.BOLD, 16));
            JPanel totalAmountPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            totalAmountPanel.add(totalAmountLabel);
            panel.add(totalAmountPanel, BorderLayout.SOUTH);

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
                    int response = JOptionPane.showConfirmDialog(panel, "Are you sure you want to delete pocket '" + pocketName + "'?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        expenseController.deletePocket(email, pocketName);
                        loadExpenses(email);  // Reload expenses to update UI
                        JOptionPane.showMessageDialog(panel, "Pocket '" + pocketName + "' deleted successfully.");
                    }
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(addExpenseButton);
            buttonPanel.add(deletePocketButton);
            panel.add(buttonPanel, BorderLayout.NORTH);

            tabbedPane.addTab(pocketName, panel);
        }
    }

    private void searchExpenses(String email, String searchTerm) {
        tabbedPane.removeAll();  // Clear existing tabs

        List<ExpenseModel> expenses = expenseController.getExpensesByEmail(email);

        // Filter expenses by the search term using fuzzy matching
        LevenshteinDistance levenshtein = new LevenshteinDistance();
        List<ExpenseModel> filteredExpenses = expenses.stream()
                .filter(expense -> levenshtein.apply(expense.getExpenseName().toLowerCase(), searchTerm.toLowerCase()) <= 3)
                .collect(Collectors.toList());

        if (filteredExpenses.isEmpty()) {
            // Show a message if no expenses are found
            JLabel noExpensesLabel = new JLabel("No expenses found", SwingConstants.CENTER);
            noExpensesLabel.setFont(new Font("Serif", Font.BOLD, 24));
            tabbedPane.addTab("Search Results", noExpensesLabel);
            return;
        }

        // Group filtered expenses by pocket name
        Map<String, List<ExpenseModel>> expensesByPocketName = filteredExpenses.stream()
                .collect(Collectors.groupingBy(ExpenseModel::getPocketName));

        // Create a table for each pocket name and add it to the tabbed pane
        for (Map.Entry<String, List<ExpenseModel>> entry : expensesByPocketName.entrySet()) {
            String pocketName = entry.getKey();
            List<ExpenseModel> pocketExpenses = entry.getValue();

            String[] columnNames = {"Month", "Expense Name", "Amount", "Date", "Actions", "Update"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            double totalAmount = 0;
            for (ExpenseModel expense : pocketExpenses) {
                Object[] rowData = {
                        expense.getSelectedMonth(),
                        expense.getExpenseName(),
                        expense.getAmount(),
                        expense.getDate(),
                        "Delete", // Placeholder for delete button
                        "Update"  // Placeholder for update button
                };
                tableModel.addRow(rowData);
                totalAmount += expense.getAmount();
            }

            JTable expenseTable = new JTable(tableModel);
            expenseTable.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));
            expenseTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
            expenseTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), expenseTable, email, pocketName, false));
            expenseTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
            expenseTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), expenseTable, email, pocketName, true));

            StartPageStyle.styleTable(expenseTable);  // Apply custom styles to the table

            JScrollPane scrollPane = new JScrollPane(expenseTable);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(scrollPane, BorderLayout.CENTER);

            // Add total amount label at the bottom
            JLabel totalAmountLabel = new JLabel("Total Amount: " + totalAmount);
            totalAmountLabel.setFont(new Font("Serif", Font.BOLD, 16));
            JPanel totalAmountPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            totalAmountPanel.add(totalAmountLabel);
            panel.add(totalAmountPanel, BorderLayout.SOUTH);

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
                    int response = JOptionPane.showConfirmDialog(panel, "Are you sure you want to delete pocket '" + pocketName + "'?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        expenseController.deletePocket(email, pocketName);
                        loadExpenses(email);  // Reload expenses to update UI
                        JOptionPane.showMessageDialog(panel, "Pocket '" + pocketName + "' deleted successfully.");
                    }
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(addExpenseButton);
            buttonPanel.add(deletePocketButton);
            panel.add(buttonPanel, BorderLayout.NORTH);

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
            if (value != null && value.toString().equals("Update")) {
                StartPageStyle.styleBlueButton(this);  // Apply blue button style for update
            } else if (value != null && value.toString().equals("Delete")) {
                StartPageStyle.styleRedButton(this);  // Apply red button style for delete
            }
            return this;
        }
    }

    // Custom ButtonEditor class
    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private JButton button;
        private boolean isPushed;
        private JTable table;
        private String email;
        private String pocketName;
        private boolean isUpdate;

        public ButtonEditor(JCheckBox checkBox, JTable table, String email, String pocketName, boolean isUpdate) {
            super(checkBox);
            this.table = table;
            this.email = email;
            this.pocketName = pocketName;
            this.isUpdate = isUpdate;
            button = new JButton();
            button.setOpaque(true);
            if (isUpdate) {
                StartPageStyle.styleBlueButton(button);  // Apply blue button style for update
            } else {
                StartPageStyle.styleRedButton(button);  // Apply red button style for delete
            }
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
                if (isUpdate) {
                    // Handle update functionality
                    int selectedRow = table.getSelectedRow();
                    String selectedMonth = table.getValueAt(selectedRow, 0).toString();
                    String expenseName = table.getValueAt(selectedRow, 1).toString();
                    double amount = Double.parseDouble(table.getValueAt(selectedRow, 2).toString());

                    ExpenseModel updatedExpense = new ExpenseModel(pocketName, selectedMonth, expenseName, amount, new java.util.Date(), email);
                    expenseController.updateExpense(updatedExpense);
                    JOptionPane.showMessageDialog(button, "Expense updated successfully!");
                } else {
                    // Handle delete functionality
                    int response = JOptionPane.showConfirmDialog(button, "Are you sure you want to delete this expense?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        int selectedRow = table.getSelectedRow();
                        String selectedMonth = table.getValueAt(selectedRow, 0).toString();
                        String expenseName = table.getValueAt(selectedRow, 1).toString();
                        expenseController.deleteExpense(email, expenseName, selectedMonth);
                        ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
                        JOptionPane.showMessageDialog(button, "Expense deleted successfully!");
                    }
                }
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
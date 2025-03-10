package ui;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import controllers.ExpenseController;
import models.ExpenseModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

public class Expense extends JFrame {

    private JComboBox<String> monthComboBox;
    private JSpinner dateSpinner;
    private JTextField pocketNameField;
    private JTextField expenseNameField;
    private JTextField amountField; // Add amount field
    private ExpenseController expenseController;
    private String token;

    public Expense(String token) {
        this.token = token;
        setTitle("Expense Tracker");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        expenseController = new ExpenseController();

        JPanel mainPanel = new JPanel(new BorderLayout());

        JButton backButton = new JButton("Back");
        ExpenseStyle.styleButton(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StartPage(token);
                dispose();
            }
        });

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.add(backButton);
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10)); // Update grid layout to 5 rows
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel pocketNameLabel = new JLabel("Pocket Name:");
        pocketNameField = new JTextField();
        ExpenseStyle.addPlaceholder(pocketNameField, "Enter pocket name");
        formPanel.add(pocketNameLabel);
        formPanel.add(pocketNameField);

        JLabel monthLabel = new JLabel("Select Month:");
        monthComboBox = new JComboBox<>();
        populateMonthComboBox();
        formPanel.add(monthLabel);
        formPanel.add(monthComboBox);

        JLabel expenseNameLabel = new JLabel("Expense Name (optional):");
        expenseNameField = new JTextField();
        ExpenseStyle.addPlaceholder(expenseNameField, "Enter expense name");
        formPanel.add(expenseNameLabel);
        formPanel.add(expenseNameField);

        JLabel amountLabel = new JLabel("Amount (optional):"); // Add amount label
        amountField = new JTextField();
        ExpenseStyle.addPlaceholder(amountField, "Enter amount");
        formPanel.add(amountLabel);
        formPanel.add(amountField);

        JLabel dateLabel = new JLabel("Select Date (optional):");
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy");
        dateSpinner.setEditor(dateEditor);
        setDateRange();
        formPanel.add(dateLabel);
        formPanel.add(dateSpinner);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        ExpenseStyle.styleButton(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPlaceholders();
                validateAndSubmitForm();
            }
        });

        JPanel submitButtonPanel = new JPanel();
        submitButtonPanel.add(submitButton);
        mainPanel.add(submitButtonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void populateMonthComboBox() {
        LocalDate now = LocalDate.now();
        YearMonth currentYearMonth = YearMonth.of(now.getYear(), now.getMonth());
        monthComboBox.addItem(currentYearMonth.getMonth().name() + " " + currentYearMonth.getYear());
        monthComboBox.setSelectedIndex(0);
        monthComboBox.setEnabled(false);
    }

    private void setDateRange() {
        LocalDate now = LocalDate.now();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, now.getYear());
        calendar.set(Calendar.MONTH, now.getMonthValue() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();
        ((SpinnerDateModel) dateSpinner.getModel()).setStart(startDate);
        ((SpinnerDateModel) dateSpinner.getModel()).setEnd(endDate);
        dateSpinner.setValue(startDate);
    }

    private void clearPlaceholders() {
        if (pocketNameField.getForeground() == Color.GRAY) {
            pocketNameField.setText("");
            pocketNameField.setForeground(Color.BLACK);
        }
        if (expenseNameField.getForeground() == Color.GRAY) {
            expenseNameField.setText("");
            expenseNameField.setForeground(Color.BLACK);
        }
        if (amountField.getForeground() == Color.GRAY) { // Clear amount placeholder
            amountField.setText("");
            amountField.setForeground(Color.BLACK);
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

    private void validateAndSubmitForm() {
        String pocketName = pocketNameField.getText();
        String selectedMonth = (String) monthComboBox.getSelectedItem();
        String expenseName = expenseNameField.getText();
        String amountText = amountField.getText(); // Get amount text
        Date selectedDate = (Date) dateSpinner.getValue();
        String email = getEmailFromToken(token);

        if (pocketName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pocket Name is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedMonth == null) {
            JOptionPane.showMessageDialog(null, "Month selection is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount = 0;
        if (!amountText.isEmpty()) {
            try {
                amount = Double.parseDouble(amountText); // Parse amount
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid amount", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        ExpenseModel expense = new ExpenseModel(pocketName, selectedMonth, expenseName, amount, selectedDate, email);
        expenseController.addExpense(expense);

        JOptionPane.showMessageDialog(null, "Expense added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Expense("token"); // Replace with actual token logic
            }
        });
    }
}
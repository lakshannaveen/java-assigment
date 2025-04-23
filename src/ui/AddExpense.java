package ui;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import controllers.ExpenseController;
import models.ExpenseModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

public class AddExpense extends JFrame {

    private JComboBox<String> monthComboBox;
    private JSpinner dateSpinner;
    private JTextField expenseNameField;
    private JTextField amountField;
    private ExpenseController expenseController;
    private String token;
    private String pocketName;

    public AddExpense(String token, String pocketName) {
        this.token = token;
        this.pocketName = pocketName;
        setTitle("Welcome to Add Expense");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        expenseController = new ExpenseController();

        JPanel mainPanel = new JPanel(new BorderLayout());

        JButton backButton = new JButton("Back");
        AddExpenseStyle.styleButton(backButton);
        backButton.addActionListener(e -> {
            new StartPage(token);
            dispose();
        });

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.add(backButton);
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel monthLabel = new JLabel("Select Month:");
        monthComboBox = new JComboBox<>();
        populateMonthComboBox();
        formPanel.add(monthLabel);
        formPanel.add(monthComboBox);

        JLabel expenseNameLabel = new JLabel("Expense Name:");
        expenseNameField = new JTextField();
        AddExpenseStyle.addPlaceholder(expenseNameField, "Enter expense name");
        formPanel.add(expenseNameLabel);
        formPanel.add(expenseNameField);

        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField();
        AddExpenseStyle.addPlaceholder(amountField, "Enter amount");
        formPanel.add(amountLabel);
        formPanel.add(amountField);

        JLabel dateLabel = new JLabel("Select Date:");
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy");
        dateSpinner.setEditor(dateEditor);
        setDateRange();
        formPanel.add(dateLabel);
        formPanel.add(dateSpinner);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        AddExpenseStyle.styleButton(submitButton);
        submitButton.addActionListener(e -> {
            clearPlaceholders();
            validateAndSubmitForm();
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

        dateSpinner.setValue(new Date());
    }

    private void clearPlaceholders() {
        if (expenseNameField.getForeground() == Color.GRAY) {
            expenseNameField.setText("");
            expenseNameField.setForeground(Color.BLACK);
        }
        if (amountField.getForeground() == Color.GRAY) {
            amountField.setText("");
            amountField.setForeground(Color.BLACK);
        }
    }

    private String getEmailFromToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim("email").asString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void validateAndSubmitForm() {
        String selectedMonth = (String) monthComboBox.getSelectedItem();
        String expenseName = expenseNameField.getText();
        String amountText = amountField.getText();
        Date selectedDate = (Date) dateSpinner.getValue();
        String email = getEmailFromToken(token);

        if (selectedMonth == null) {
            showError("Month selection is required");
            return;
        }
        if (expenseName.isEmpty()) {
            showError("Expense Name is required");
            return;
        }
        if (amountText.isEmpty()) {
            showError("Amount is required");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            showError("Invalid amount");
            return;
        }

        // Attach time
        LocalDate selectedLocalDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime currentTime = LocalTime.now();
        LocalDateTime fullDateTime = LocalDateTime.of(selectedLocalDate, currentTime);
        Date finalDateWithTime = Date.from(fullDateTime.atZone(ZoneId.systemDefault()).toInstant());

        ExpenseModel expense = new ExpenseModel(pocketName, selectedMonth, expenseName, amount, finalDateWithTime, email);
        expenseController.addExpense(expense);
        JOptionPane.showMessageDialog(null, "Expense added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddExpense("token", "DefaultPocketName"));
    }
}

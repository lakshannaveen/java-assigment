package ui;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import controllers.ExpenseController;
import models.ExpenseModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;
import java.time.YearMonth;

public class Expense extends JFrame {
    private JComboBox<String> monthComboBox;
    private JSpinner dateSpinner;
    private JTextField pocketNameField;
    private JTextField expenseNameField;
    private JTextField amountField;
    private ExpenseController expenseController;
    private String token;

    public Expense(String token) {
        this.token = token;
        setTitle("Add Expense");
        setSize(500, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        expenseController = new ExpenseController();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Back Button
        JButton backButton = new JButton("← Back");
        ExpenseStyle.styleBackButton(backButton);
        backButton.addActionListener(e -> {
            new StartPage(token);
            dispose();
        });
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBackground(Color.WHITE);
        backButtonPanel.add(backButton);
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 12, 16));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        pocketNameField = new JTextField();
        expenseNameField = new JTextField();
        amountField = new JTextField();
        ExpenseStyle.addPlaceholder(pocketNameField, "Enter pocket name");
        ExpenseStyle.addPlaceholder(expenseNameField, "Enter expense name");
        ExpenseStyle.addPlaceholder(amountField, "Enter amount");

        monthComboBox = new JComboBox<>();
        populateMonthComboBox();

        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy");
        dateSpinner.setEditor(dateEditor);
        setDateRange();

        formPanel.add(new JLabel("Pocket Name:"));
        formPanel.add(pocketNameField);
        formPanel.add(new JLabel("Select Month:"));
        formPanel.add(monthComboBox);
        formPanel.add(new JLabel("Expense Name (optional):"));
        formPanel.add(expenseNameField);
        formPanel.add(new JLabel("Amount (optional):"));
        formPanel.add(amountField);
        formPanel.add(new JLabel("Select Date (optional):"));
        formPanel.add(dateSpinner);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Submit
        JButton submitButton = new JButton("Submit Expense");
        ExpenseStyle.styleSubmitButton(submitButton);
        submitButton.addActionListener(e -> {
            clearPlaceholders();
            validateAndSubmitForm();
        });
        JPanel submitPanel = new JPanel();
        submitPanel.setBackground(Color.WHITE);
        submitPanel.add(submitButton);
        mainPanel.add(submitPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void populateMonthComboBox() {
        LocalDate now = LocalDate.now();
        YearMonth ym = YearMonth.of(now.getYear(), now.getMonth());
        monthComboBox.addItem(ym.getMonth().name() + " " + ym.getYear());
        monthComboBox.setSelectedIndex(0);
        monthComboBox.setEnabled(false);
    }

    private void setDateRange() {
        LocalDate now = LocalDate.now();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, now.getYear());
        cal.set(Calendar.MONTH, now.getMonthValue() - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date start = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date end = cal.getTime();
        ((SpinnerDateModel) dateSpinner.getModel()).setStart(start);
        ((SpinnerDateModel) dateSpinner.getModel()).setEnd(end);
        dateSpinner.setValue(start);
    }

    private void clearPlaceholders() {
        if (pocketNameField.getForeground() == Color.GRAY) pocketNameField.setText("");
        if (expenseNameField.getForeground() == Color.GRAY) expenseNameField.setText("");
        if (amountField.getForeground() == Color.GRAY) amountField.setText("");
    }

    private String getEmailFromToken(String token) {
        try {
            DecodedJWT decoded = JWT.decode(token);
            return decoded.getClaim("email").asString();
        } catch (Exception e) {
            return null;
        }
    }

    private void validateAndSubmitForm() {
        String pocket = pocketNameField.getText();
        String month = (String) monthComboBox.getSelectedItem();
        String name = expenseNameField.getText();
        String amountText = amountField.getText();
        Date date = (Date) dateSpinner.getValue();
        String email = getEmailFromToken(token);

        if (pocket.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pocket Name is required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount = 0;
        if (!amountText.isEmpty()) {
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Amount must be a number", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        ExpenseModel expense = new ExpenseModel(pocket, month, name, amount, date, email);
        expenseController.addExpense(expense);
        JOptionPane.showMessageDialog(this, "Expense added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Expense("your_token_here"));
    }
}

package ui;

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
    private JTextField expenseTypeField;
    private ExpenseController expenseController;

    public Expense() {
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
                new StartPage();
                dispose();
            }
        });

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.add(backButton);
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
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

        JLabel expenseTypeLabel = new JLabel("Expense Type (optional):");
        expenseTypeField = new JTextField();
        ExpenseStyle.addPlaceholder(expenseTypeField, "Enter expense type");
        formPanel.add(expenseTypeLabel);
        formPanel.add(expenseTypeField);

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
        if (expenseTypeField.getForeground() == Color.GRAY) {
            expenseTypeField.setText("");
            expenseTypeField.setForeground(Color.BLACK);
        }
    }

    private void validateAndSubmitForm() {
        String pocketName = pocketNameField.getText();
        String selectedMonth = (String) monthComboBox.getSelectedItem();
        String expenseName = expenseNameField.getText();
        String expenseType = expenseTypeField.getText();
        Date selectedDate = (Date) dateSpinner.getValue();

        if (pocketName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pocket Name is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedMonth == null) {
            JOptionPane.showMessageDialog(null, "Month selection is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ExpenseModel expense = new ExpenseModel(pocketName, selectedMonth, expenseName, expenseType, selectedDate);
        expenseController.addExpense(expense);

        JOptionPane.showMessageDialog(null, "Expense added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Expense();
            }
        });
    }
}

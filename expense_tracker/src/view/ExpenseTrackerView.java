package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import model.Transaction;

public class ExpenseTrackerView extends JFrame {

    private JTable transactionsTable;
    private DefaultTableModel model;
    private List<Transaction> displayedTransactions = new ArrayList<>();

    private JFormattedTextField amountField;
    private JTextField categoryField;

    private JTextField categoryFilterField;
    private JButton categoryFilterBtn;
    private JTextField amountFilterField;
    private JButton amountFilterBtn;

    private JButton clearFilterBtn;
    private JButton addTransactionBtn;
    private JButton removeTransactionBtn;
    private JButton undoBtn;

    public ExpenseTrackerView() {
        setTitle("Expense Tracker");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set up table model
        String[] columnHeaders = {"#", "Amount", "Category", "Timestamp"};
        model = new DefaultTableModel(columnHeaders, 0);
        transactionsTable = new JTable(model);
        transactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // --- Input Fields ---
        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JFormattedTextField(NumberFormat.getNumberInstance());
        amountField.setColumns(8);

        JLabel categoryLabel = new JLabel("Category:");
        categoryField = new JTextField(10);

        addTransactionBtn = new JButton("Add Transaction");
        removeTransactionBtn = new JButton("Remove Selected Transaction");
        undoBtn = new JButton("Undo Last Remove");
        undoBtn.setEnabled(false);  // disabled by default

        // --- Filter Section ---
        amountFilterField = new JTextField(7);
        amountFilterBtn = new JButton("Filter by Amount");

        categoryFilterField = new JTextField(10);
        categoryFilterBtn = new JButton("Filter by Category");

        clearFilterBtn = new JButton("Clear Filter");

        // --- Top Panel (Input) ---
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(amountLabel);
        inputPanel.add(amountField);
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryField);
        inputPanel.add(addTransactionBtn);
        inputPanel.add(removeTransactionBtn);
        inputPanel.add(undoBtn);

        // --- Bottom Panel (Filters) ---
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Amount:"));
        filterPanel.add(amountFilterField);
        filterPanel.add(amountFilterBtn);
        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(categoryFilterField);
        filterPanel.add(categoryFilterBtn);
        filterPanel.add(clearFilterBtn);

        // Add components to layout
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(transactionsTable), BorderLayout.CENTER);
        add(filterPanel, BorderLayout.SOUTH);

        // Final setup
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // --- Getters and Accessors ---

    public JButton getAddTransactionBtn() {
        return addTransactionBtn;
    }

    public JButton getRemoveTransactionBtn() {
        return removeTransactionBtn;
    }

    public JButton getUndoBtn() {
        return undoBtn;
    }

    public JTable getTransactionsTable() {
        return transactionsTable;
    }

    public DefaultTableModel getTableModel() {
        return model;
    }

    public double getAmountField() {
        try {
            return Double.parseDouble(amountField.getText().trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public String getCategoryField() {
        return categoryField.getText().trim();
    }

    public int getSelectedRowIndex() {
        return transactionsTable.getSelectedRow();
    }

    public List<Transaction> getDisplayedTransactions() {
        return displayedTransactions;
    }

    // --- Filters ---

    public String getCategoryFilterInput() {
        return categoryFilterField.getText().trim();
    }

    public double getAmountFilterInput() {
        try {
            return Double.parseDouble(amountFilterField.getText().trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public void addApplyCategoryFilterListener(ActionListener listener) {
        categoryFilterBtn.addActionListener(listener);
    }

    public void addApplyAmountFilterListener(ActionListener listener) {
        amountFilterBtn.addActionListener(listener);
    }

    public void addClearFilterListener(ActionListener listener) {
        clearFilterBtn.addActionListener(listener);
    }

    // --- Table Rendering ---

    public void refreshTable(List<Transaction> transactions) {
        model.setRowCount(0);
        displayedTransactions = transactions;

        int count = 0;
        double total = 0;

        for (Transaction t : transactions) {
            model.addRow(new Object[]{++count, t.getAmount(), t.getCategory(), t.getTimestamp()});
            total += t.getAmount();
        }

        model.addRow(new Object[]{"Total", null, null, total});
        transactionsTable.updateUI();
    }

    public void displayFilteredTransactions(List<Transaction> filteredTransactions) {
        refreshTable(filteredTransactions);
    }
}
        
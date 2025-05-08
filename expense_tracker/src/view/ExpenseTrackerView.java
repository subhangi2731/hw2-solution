package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columnHeaders = {"#", "Amount", "Category", "Timestamp"};
        model = new DefaultTableModel(columnHeaders, 0);
        transactionsTable = new JTable(model);
        transactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create input and action controls
        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JFormattedTextField(NumberFormat.getNumberInstance());
        amountField.setColumns(8);
        amountField.setToolTipText("Enter a numeric amount less than 1000");

        JLabel categoryLabel = new JLabel("Category:");
        categoryField = new JTextField(10);
        categoryField.setToolTipText("Enter a valid category like food, travel, bills, etc.");

        addTransactionBtn = new JButton("Add Transaction");
        addTransactionBtn.setToolTipText("Click to add a new transaction");

        removeTransactionBtn = new JButton("Remove Selected Transaction");
        removeTransactionBtn.setToolTipText("Select a row and click to remove the transaction");

        undoBtn = new JButton("Undo Last Remove");
        undoBtn.setToolTipText("Undo the last removed transaction");
        undoBtn.setEnabled(false);

        // Filter fields
        amountFilterField = new JTextField(7);
        amountFilterField.setToolTipText("Enter amount to filter");

        amountFilterBtn = new JButton("Filter by Amount");
        amountFilterBtn.setToolTipText("Apply amount filter");

        categoryFilterField = new JTextField(10);
        categoryFilterField.setToolTipText("Enter category to filter");

        categoryFilterBtn = new JButton("Filter by Category");
        categoryFilterBtn.setToolTipText("Apply category filter");

        clearFilterBtn = new JButton("Clear Filter");
        clearFilterBtn.setToolTipText("Reset all filters and display all transactions");

        // Top input panel using GridBagLayout for better spacing
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(amountLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(amountField, gbc);

        gbc.gridx = 2;
        inputPanel.add(categoryLabel, gbc);
        gbc.gridx = 3;
        inputPanel.add(categoryField, gbc);

        gbc.gridx = 4;
        inputPanel.add(addTransactionBtn, gbc);

        gbc.gridx = 5;
        inputPanel.add(removeTransactionBtn, gbc);

        gbc.gridx = 6;
        inputPanel.add(undoBtn, gbc);

        // Filter panel using FlowLayout
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Amount:"));
        filterPanel.add(amountFilterField);
        filterPanel.add(amountFilterBtn);
        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(categoryFilterField);
        filterPanel.add(categoryFilterBtn);
        filterPanel.add(clearFilterBtn);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(transactionsTable), BorderLayout.CENTER);
        add(filterPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public JButton getAddTransactionBtn() { return addTransactionBtn; }

    public JButton getRemoveTransactionBtn() { return removeTransactionBtn; }

    public JButton getUndoBtn() { return undoBtn; }

    public JButton getClearFilterButton() { return clearFilterBtn; }

    public JTable getTransactionsTable() { return transactionsTable; }

    public DefaultTableModel getTableModel() { return model; }

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

    public void clearFilterInputs() {
        categoryFilterField.setText("");
        amountFilterField.setText("");
    }

    public void highlightRows(List<Integer> rowIndexes) {
        transactionsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (rowIndexes != null && rowIndexes.contains(row)) {
                    c.setBackground(new Color(173, 255, 168));
                } else {
                    c.setBackground(table.getBackground());
                }
                return c;
            }
        });
        transactionsTable.repaint();
    }
}
package controller;

import model.ExpenseTrackerModel;
import model.Transaction;
import view.ExpenseTrackerView;

import java.util.Collections;
import java.util.List;

public class ExpenseTrackerController {

    private final ExpenseTrackerModel model;
    private final ExpenseTrackerView view;
    private TransactionFilter activeFilter; // holds the current filter (amount/category)

    public ExpenseTrackerController(ExpenseTrackerModel model, ExpenseTrackerView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Adds a transaction to the model and updates the view.
     */
    public boolean addTransaction(double amount, String category) {
        if (!InputValidation.isValidAmount(amount)) {
            System.err.println("[AddTransaction] Invalid amount: " + amount);
            return false;
        }

        if (!InputValidation.isValidCategory(category)) {
            System.err.println("[AddTransaction] Invalid category: " + category);
            return false;
        }

        Transaction transaction = new Transaction(amount, category);
        model.addTransaction(transaction);
        refresh();

        System.out.println("[AddTransaction] Added: " + amount + " | " + category);
        return true;
    }

    /**
     * Removes a transaction based on selected table index.
     */
    public boolean removeTransaction(int index) {
        List<Transaction> transactions = model.getTransactions();

        if (index < 0 || index >= transactions.size()) {
            System.err.println("[RemoveTransaction] Invalid index: " + index);
            return false;
        }

        Transaction toRemove = transactions.get(index);

        try {
            model.removeTransaction(toRemove);
            refresh();

            if (view.getUndoBtn() != null) {
                view.getUndoBtn().setEnabled(model.canUndo());
            }

            System.out.println("[RemoveTransaction] Removed: " +
                    toRemove.getAmount() + " | " +
                    toRemove.getCategory() + " | " +
                    toRemove.getTimestamp());

            return true;
        } catch (Exception e) {
            System.err.println("[RemoveTransaction] Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Applies a filter using the currently set strategy.
     */
    public boolean applyFilter() {
        if (activeFilter == null) {
            // No filter -> show full list
            refresh();
            return true;
        }

        List<Transaction> transactions = model.getTransactions();

        if (!activeFilter.inputValidation()) {
            System.err.println("[Filter] Invalid filter input.");
            return false;
        }

        List<Transaction> filtered = activeFilter.filter(transactions);
        view.displayFilteredTransactions(filtered);

        // Optional: highlight filtered rows if supported
        List<Integer> indexes = findFilteredRowIndexes(filtered, transactions);
        view.highlightRows(indexes);

        return true;
    }

    /**
     * Updates the currently active filter.
     */
    public void setFilter(TransactionFilter filter) {
        this.activeFilter = filter;
    }

    /**
     * Clears all filters and resets the UI view to display all transactions.
     */
    public void clearFilter() {
        this.activeFilter = null;
        view.clearFilterInputs();        // Reset input fields in the view
        view.highlightRows(null);        // Remove highlights if any
        refresh();                       // Refresh full table
        System.out.println("[Filter] All filters cleared.");
    }

    /**
     * Refreshes the table with the current unfiltered list.
     */
    public void refresh() {
        List<Transaction> transactions = model.getTransactions();
        view.refreshTable(transactions);
    }

    /**
     * Reverts the last removal.
     */
    public boolean undoRemove() {
        if (!model.canUndo()) {
            System.out.println("[Undo] Nothing to undo.");
            return false;
        }

        model.undoLastRemove();
        refresh();

        if (view.getUndoBtn() != null) {
            view.getUndoBtn().setEnabled(model.canUndo());
        }

        System.out.println("[Undo] Last removed transaction restored.");
        return true;
    }

    /**
     * Helper to map filtered transactions to their row indexes in the table.
     */
    private List<Integer> findFilteredRowIndexes(List<Transaction> filtered, List<Transaction> fullList) {
        List<Integer> indexes = new java.util.ArrayList<>();
        for (Transaction t : filtered) {
            int index = fullList.indexOf(t);
            if (index != -1) indexes.add(index);
        }
        return indexes;
    }
}
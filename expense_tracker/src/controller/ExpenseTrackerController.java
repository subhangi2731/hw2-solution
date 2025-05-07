package controller;

import model.ExpenseTrackerModel;
import model.Transaction;
import view.ExpenseTrackerView;

import java.util.List;

public class ExpenseTrackerController {

    private final ExpenseTrackerModel model;
    private final ExpenseTrackerView view;

    public ExpenseTrackerController(ExpenseTrackerModel model, ExpenseTrackerView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Adds a transaction to the model and updates the view.
     * Performs input validation before addition.
     *
     * @param amount   The amount for the transaction.
     * @param category The category for the transaction.
     * @return true if the transaction is added successfully, false otherwise.
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

        Transaction newTransaction = new Transaction(amount, category);
        model.addTransaction(newTransaction);
        refresh();

        System.out.println("[AddTransaction] Added: " + amount + " | " + category);
        return true;
    }

    /**
     * Removes a transaction by index from the view's table.
     * The removed transaction is passed to the model for deletion.
     * Also updates the Undo state in the view if supported.
     *
     * @param index Index of the selected row in the JTable.
     * @return true if successfully removed, false otherwise.
     */
    public boolean removeTransaction(int index) {
        List<Transaction> currentTransactions = model.getTransactions();

        if (index < 0 || index >= currentTransactions.size()) {
            System.err.println("[RemoveTransaction] Invalid row index: " + index);
            return false;
        }

        Transaction transactionToRemove = currentTransactions.get(index);

        try {
            model.removeTransaction(transactionToRemove);
            refresh();
            System.out.println("[RemoveTransaction] Removed: " +
                    transactionToRemove.getAmount() + ", " +
                    transactionToRemove.getCategory() + ", " +
                    transactionToRemove.getTimestamp());

            // If undo supported:
            if (view.getUndoBtn() != null) {
                view.getUndoBtn().setEnabled(model.canUndo());
            }

            return true;
        } catch (Exception e) {
            System.err.println("[RemoveTransaction] Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Refreshes the table in the view with the current list of transactions.
     */
    public void refresh() {
        view.refreshTable(model.getTransactions());
    }

    /**
     * Undoes the last removed transaction (if supported).
     *
     * @return true if undo was successful, false otherwise.
     */
    public boolean undoRemove() {
        if (!model.canUndo()) {
            System.out.println("[Undo] Nothing to undo.");
            return false;
        }

        model.undoLastRemove();
        refresh();

        // Disable undo button if there's nothing left to undo
        if (view.getUndoBtn() != null) {
            view.getUndoBtn().setEnabled(model.canUndo());
        }

        System.out.println("[Undo] Last removed transaction restored.");
        return true;
    }
}
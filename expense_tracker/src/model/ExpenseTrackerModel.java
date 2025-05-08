package model;

import java.util.ArrayList;
import java.util.List;

public class ExpenseTrackerModel {

    private final List<Transaction> transactions;
    private final List<Transaction> undoStack;

    public ExpenseTrackerModel() {
        this.transactions = new ArrayList<>();
        this.undoStack = new ArrayList<>();
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public void removeTransaction(Transaction t) {
        if (transactions.remove(t)) {
            undoStack.add(t);
        }
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public void undoLastRemove() {
        if (!undoStack.isEmpty()) {
            Transaction lastRemoved = undoStack.remove(undoStack.size() - 1);
            transactions.add(lastRemoved);
        }
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
}
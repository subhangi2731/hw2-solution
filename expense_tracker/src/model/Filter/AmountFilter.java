package controller;

import model.Transaction;
import java.util.List;
import java.util.ArrayList;

public class AmountFilter implements TransactionFilter {
    private final double targetAmount;

    public AmountFilter(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    @Override
    public boolean inputValidation() {
        return targetAmount > 0 && targetAmount < 1000;
    }

    @Override
    public List<Transaction> filter(List<Transaction> transactions) {
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAmount() == targetAmount) {
                filtered.add(t);
            }
        }
        return filtered;
    }
}

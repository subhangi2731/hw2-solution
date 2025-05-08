package controller;

import model.Transaction;
import java.util.List;
import java.util.ArrayList;

public class CategoryFilter implements TransactionFilter {
    private final String targetCategory;

    public CategoryFilter(String targetCategory) {
        this.targetCategory = targetCategory;
    }

    @Override
    public boolean inputValidation() {
        return targetCategory != null && !targetCategory.trim().isEmpty() && targetCategory.matches("[a-zA-Z]+");
    }

    @Override
    public List<Transaction> filter(List<Transaction> transactions) {
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getCategory().equalsIgnoreCase(targetCategory)) {
                filtered.add(t);
            }
        }
        return filtered;
    }
}

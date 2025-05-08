package controller;

import model.Transaction;
import java.util.List;

public interface TransactionFilter {
    List<Transaction> filter(List<Transaction> transactions);
    boolean inputValidation();
}

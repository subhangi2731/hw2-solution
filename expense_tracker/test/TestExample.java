// package test;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import controller.ExpenseTrackerController;
import controller.AmountFilter;
import controller.CategoryFilter;
import model.ExpenseTrackerModel;
import model.Transaction;
import view.ExpenseTrackerView;

public class TestExample {

  private ExpenseTrackerModel model;
  private ExpenseTrackerView view;
  private ExpenseTrackerController controller;

  public static final String CATEGORY_FOOD = "food";
  public static final String CATEGORY_ENTERTAINMENT = "entertainment";  

  @Before
  public void setup() {
    model = new ExpenseTrackerModel();
    view = new ExpenseTrackerView();
    controller = new ExpenseTrackerController(model, view);
  }

  public double getTotalCost() {
    double totalCost = 0.0;
    for (Transaction t : model.getTransactions()) {
      totalCost += t.getAmount();
    }
    return totalCost;
  }

  public void checkTransaction(double amount, String category, Transaction transaction) {
    assertEquals(amount, transaction.getAmount(), 0.01);
    assertEquals(category, transaction.getCategory());
    try {
      Date timestamp = Transaction.dateFormatter.parse(transaction.getTimestamp());
      assertNotNull(timestamp);
      assertTrue(Math.abs(new Date().getTime() - timestamp.getTime()) < 60000);
    } catch (ParseException e) {
      e.printStackTrace();
      assertNull("Timestamp parsing failed", null);
    }
  }

  @Test
  public void testAddTransaction() {
    assertEquals(0, model.getTransactions().size());
    assertTrue(controller.addTransaction(50.0, CATEGORY_FOOD));
    assertEquals(1, model.getTransactions().size());
    checkTransaction(50.0, CATEGORY_FOOD, model.getTransactions().get(0));
    assertEquals(50.0, getTotalCost(), 0.01);
  }

  @Test
  public void testRemoveTransaction() {
    assertEquals(0, model.getTransactions().size());
    Transaction t = new Transaction(50.0, CATEGORY_FOOD);
    model.addTransaction(t);
    assertEquals(1, model.getTransactions().size());
    model.removeTransaction(t);
    assertEquals(0, model.getTransactions().size());
    assertEquals(0.0, getTotalCost(), 0.01);
  }

  @Test
  public void testRemoveNonExistentTransaction() {
    Transaction t1 = new Transaction(50.0, CATEGORY_FOOD);
    Transaction t2 = new Transaction(30.0, CATEGORY_ENTERTAINMENT); // Never added

    model.addTransaction(t1);
    assertEquals(1, model.getTransactions().size());
    model.removeTransaction(t2); // attempt to remove nonexistent
    assertEquals(1, model.getTransactions().size()); // should not be removed
    assertTrue(model.getTransactions().contains(t1));
    assertFalse(model.getTransactions().contains(t2));
  }

  @Test
  public void testInvalidInputHandling() {
    assertFalse(controller.addTransaction(0.0, "Invalid"));
    assertFalse(controller.addTransaction(50.0, ""));
    assertEquals(0, model.getTransactions().size());
    assertEquals(0.0, getTotalCost(), 0.01);
  }

  @Test
  public void testFilterByAmount() {
    double[] amounts = {50.0, 30.0, 40.0};
    String[] categories = {CATEGORY_FOOD, CATEGORY_ENTERTAINMENT, CATEGORY_FOOD};
    for (int i = 0; i < 3; i++) controller.addTransaction(amounts[i], categories[i]);

    controller.setFilter(new AmountFilter(50.0));
    controller.applyFilter();
    List<Transaction> result = view.getDisplayedTransactions();
    assertEquals(1, result.size());
    assertEquals(50.0, result.get(0).getAmount(), 0.01);
  }

  @Test
  public void testFilterByCategory() {
    double[] amounts = {50.0, 30.0, 40.0};
    String[] categories = {CATEGORY_FOOD, CATEGORY_ENTERTAINMENT, CATEGORY_FOOD};
    for (int i = 0; i < 3; i++) controller.addTransaction(amounts[i], categories[i]);

    controller.setFilter(new CategoryFilter(CATEGORY_FOOD));
    controller.applyFilter();
    List<Transaction> result = view.getDisplayedTransactions();
    assertEquals(2, result.size());
    for (Transaction t : result) {
      assertEquals(CATEGORY_FOOD, t.getCategory());
    }
  }
}
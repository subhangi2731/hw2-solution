import javax.swing.JOptionPane;
import controller.ExpenseTrackerController;
import model.ExpenseTrackerModel;
import view.ExpenseTrackerView;
import controller.AmountFilter;
import controller.CategoryFilter;

public class ExpenseTrackerApp {

    public static void main(String[] args) {

        // Initialize MVC components
        ExpenseTrackerModel model = new ExpenseTrackerModel();
        ExpenseTrackerView view = new ExpenseTrackerView();
        ExpenseTrackerController controller = new ExpenseTrackerController(model, view);

        // Display the main UI
        view.setVisible(true);

        // --- ADD TRANSACTION ---
        view.getAddTransactionBtn().addActionListener(e -> {
            double amount = view.getAmountField();
            String category = view.getCategoryField();

            boolean added = controller.addTransaction(amount, category);
            if (!added) {
                JOptionPane.showMessageDialog(view,
                        "Invalid input. Please enter a non-zero amount < 1000 and a valid alphabetical category.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                view.toFront();
            }
        });

        // --- REMOVE TRANSACTION ---
        view.getRemoveTransactionBtn().addActionListener(e -> {
            int selectedRow = view.getSelectedRowIndex();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(view,
                        "Please select a transaction row to remove.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean removed = controller.removeTransaction(selectedRow);
            if (!removed) {
                JOptionPane.showMessageDialog(view,
                        "Could not remove the selected transaction. Please try again.",
                        "Remove Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- UNDO LAST REMOVAL ---
        view.getUndoBtn().addActionListener(e -> {
            boolean undone = controller.undoRemove();
            if (!undone) {
                JOptionPane.showMessageDialog(view,
                        "No recent removal to undo.",
                        "Undo Not Available",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // --- APPLY CATEGORY FILTER ---
        view.addApplyCategoryFilterListener(e -> {
            try {
                String input = view.getCategoryFilterInput();
                CategoryFilter categoryFilter = new CategoryFilter(input);
                controller.setFilter(categoryFilter);
                controller.applyFilter();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(view,
                        ex.getMessage(),
                        "Filter Error",
                        JOptionPane.WARNING_MESSAGE);
                view.toFront();
            }
        });

        // --- APPLY AMOUNT FILTER ---
        view.addApplyAmountFilterListener(e -> {
            try {
                double input = view.getAmountFilterInput();
                AmountFilter amountFilter = new AmountFilter(input);
                controller.setFilter(amountFilter);
                controller.applyFilter();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(view,
                        ex.getMessage(),
                        "Filter Error",
                        JOptionPane.WARNING_MESSAGE);
                view.toFront();
            }
        });

        // --- CLEAR FILTER ---
        view.addClearFilterListener(e -> {
            controller.setFilter(null);
            controller.applyFilter();
        });
    }
}
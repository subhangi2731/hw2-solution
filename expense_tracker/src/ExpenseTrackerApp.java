
import javax.swing.*;
import controller.ExpenseTrackerController;
import model.ExpenseTrackerModel;
import view.ExpenseTrackerView;
import controller.AmountFilter;
import controller.CategoryFilter;

public class ExpenseTrackerApp {

    public static void main(String[] args) {

        ExpenseTrackerModel model = new ExpenseTrackerModel();
        ExpenseTrackerView view = new ExpenseTrackerView();
        ExpenseTrackerController controller = new ExpenseTrackerController(model, view);

        view.setVisible(true);

        view.getAddTransactionBtn().addActionListener(e -> {
            double amount = view.getAmountField();
            String category = view.getCategoryField();

            boolean added = controller.addTransaction(amount, category);
            if (!added) {
                showDialog(view, "Invalid input. Please enter a valid amount and category.", "Input Error", JOptionPane.ERROR_MESSAGE);
                view.toFront();
            }
        });

        view.getRemoveTransactionBtn().addActionListener(e -> {
            int selectedRow = view.getSelectedRowIndex();
            if (selectedRow == -1) {
                showDialog(view, "Please select a transaction to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean removed = controller.removeTransaction(selectedRow);
            if (!removed) {
                showDialog(view, "Could not remove the selected transaction.", "Remove Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        view.getUndoBtn().addActionListener(e -> {
            boolean undone = controller.undoRemove();
            if (!undone) {
                showDialog(view, "No transaction available to undo.", "Undo Not Available", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        view.addApplyCategoryFilterListener(e -> {
            try {
                String category = view.getCategoryFilterInput();
                CategoryFilter filter = new CategoryFilter(category);
                controller.setFilter(filter);
                controller.applyFilter();
            } catch (IllegalArgumentException ex) {
                showDialog(view, ex.getMessage(), "Category Filter Error", JOptionPane.WARNING_MESSAGE);
                view.toFront();
            }
        });

        view.addApplyAmountFilterListener(e -> {
            try {
                double amount = view.getAmountFilterInput();
                AmountFilter filter = new AmountFilter(amount);
                controller.setFilter(filter);
                controller.applyFilter();
            } catch (IllegalArgumentException ex) {
                showDialog(view, ex.getMessage(), "Amount Filter Error", JOptionPane.WARNING_MESSAGE);
                view.toFront();
            }
        });

        view.addClearFilterListener(e -> {
            controller.clearFilter();
            showDialog(view, "All filters have been cleared.", "Filters Reset", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private static void showDialog(JFrame parent, String message, String title, int type) {
        JDialog dialog = new JDialog(parent, title, true);
        JOptionPane optionPane = new JOptionPane(message, type);
        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
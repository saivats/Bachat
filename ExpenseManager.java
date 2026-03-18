import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ExpenseManager class handles expense list operations.
 */
public class ExpenseManager {
    private final ArrayList<Expense> expenses;
    private final FileHandler fileHandler;

    /**
     * Creates an expense manager and loads saved expenses from file.
     *
     * @param fileHandler file handler for persistence
     */
    public ExpenseManager(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.expenses = new ArrayList<>(fileHandler.loadExpenses());
    }

    /**
     * Adds a new expense to the list and file.
     *
     * @param amount expense amount
     * @param category expense category
     * @param date expense date
     * @param note expense note
     * @return created expense
     */
    public Expense addExpense(double amount, String category, LocalDate date, String note) {
        int nextId = generateNextId();
        Expense expense = new Expense(nextId, amount, category, date, note);
        expenses.add(expense);
        fileHandler.saveExpenses(expenses);
        return expense;
    }

    /**
     * Edits an existing expense and updates the file.
     *
     * @param id expense ID
     * @param amount new amount
     * @param category new category
     * @param date new date
     * @param note new note
     * @return true if updated, otherwise false
     */
    public boolean editExpense(int id, double amount, String category, LocalDate date, String note) {
        Expense expense = getExpenseById(id);
        if (expense == null) {
            return false;
        }

        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setDate(date);
        expense.setNote(note);
        fileHandler.saveExpenses(expenses);
        return true;
    }

    /**
     * Deletes an expense by ID and updates the file.
     *
     * @param id expense ID
     * @return true if deleted, otherwise false
     */
    public boolean deleteExpense(int id) {
        Expense expense = getExpenseById(id);
        if (expense == null) {
            return false;
        }

        expenses.remove(expense);
        fileHandler.saveExpenses(expenses);
        return true;
    }

    /**
     * Finds an expense by ID.
     *
     * @param id expense ID
     * @return matching expense or null
     */
    public Expense getExpenseById(int id) {
        for (Expense expense : expenses) {
            if (expense.getId() == id) {
                return expense;
            }
        }
        return null;
    }

    /**
     * Returns all expenses.
     *
     * @return expense list
     */
    public List<Expense> getExpenses() {
        return new ArrayList<>(expenses);
    }

    /**
     * Calculates the total spending.
     *
     * @return sum of all expenses
     */
    public double getTotalSpending() {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    /**
     * Calculates category-wise spending totals.
     *
     * @return map of category and total amount
     */
    public Map<String, Double> getCategoryWiseBreakdown() {
        Map<String, Double> breakdown = new LinkedHashMap<>();
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            double current = breakdown.getOrDefault(category, 0.0);
            breakdown.put(category, current + expense.getAmount());
        }
        return breakdown;
    }

    /**
     * Generates the next available expense ID.
     *
     * @return next ID value
     */
    private int generateNextId() {
        int maxId = 0;
        for (Expense expense : expenses) {
            if (expense.getId() > maxId) {
                maxId = expense.getId();
            }
        }
        return maxId + 1;
    }
}

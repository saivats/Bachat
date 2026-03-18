/**
 * BudgetManager class manages the monthly budget and warning logic.
 */
public class BudgetManager {
    private double monthlyBudget;
    private final FileHandler fileHandler;

    /**
     * Creates a budget manager and loads the saved budget.
     *
     * @param fileHandler file handler for persistence
     */
    public BudgetManager(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.monthlyBudget = fileHandler.loadBudget();
    }

    /**
     * Sets the monthly budget and saves it to file.
     *
     * @param monthlyBudget budget amount
     */
    public void setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
        fileHandler.saveBudget(monthlyBudget);
    }

    /**
     * Returns the current monthly budget.
     *
     * @return monthly budget
     */
    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    /**
     * Returns a warning message based on current spending.
     *
     * @param totalSpending total expense amount
     * @return warning message or empty string
     */
    public String getBudgetWarning(double totalSpending) {
        if (monthlyBudget <= 0) {
            return "";
        }

        if (totalSpending > monthlyBudget) {
            return "Warning: Budget exceeded by " + String.format("%.2f", (totalSpending - monthlyBudget));
        }

        if (totalSpending >= monthlyBudget * 0.8) {
            return "Alert: You have used 80% or more of your monthly budget.";
        }

        return "";
    }
}

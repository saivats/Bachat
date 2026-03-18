import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main class for the Bachat Smart Expense Tracker application.
 * It provides a menu-driven CLI for managing student expenses.
 */
public class Main {

    /**
     * Entry point of the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileHandler fileHandler = new FileHandler();
        ExpenseManager expenseManager = new ExpenseManager(fileHandler);
        BudgetManager budgetManager = new BudgetManager(fileHandler);

        System.out.println("======================================");
        System.out.println("      Welcome to Bachat");
        System.out.println(" Smart Expense Tracker for Students");
        System.out.println("======================================");

        User user = initializeUser(scanner, fileHandler);
        System.out.println("Hello, " + user.getName() + " from " + user.getCity() + ".");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt(scanner, "Enter your choice: ");

            switch (choice) {
                case 1:
                    addExpense(scanner, expenseManager, budgetManager);
                    break;
                case 2:
                    editExpense(scanner, expenseManager);
                    break;
                case 3:
                    deleteExpense(scanner, expenseManager);
                    break;
                case 4:
                    viewAllExpenses(expenseManager);
                    break;
                case 5:
                    setMonthlyBudget(scanner, budgetManager);
                    break;
                case 6:
                    showTotalSpending(expenseManager);
                    break;
                case 7:
                    showCategoryBreakdown(expenseManager);
                    break;
                case 8:
                    showBudgetStatus(expenseManager, budgetManager);
                    break;
                case 9:
                    running = false;
                    System.out.println("Thank you for using Bachat.");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }

        scanner.close();
    }

    /**
     * Reads existing user information or asks the user to create it.
     *
     * @param scanner scanner for user input
     * @param fileHandler file handler for persistence
     * @return loaded or newly created user
     */
    private static User initializeUser(Scanner scanner, FileHandler fileHandler) {
        User user = fileHandler.loadUser();
        if (user != null) {
            return user;
        }

        System.out.println("Let's set up your profile.");
        String name = readNonEmptyString(scanner, "Enter your name: ");
        String city = readNonEmptyString(scanner, "Enter your city: ");
        user = new User(name, city);
        fileHandler.saveUser(user);
        System.out.println("Profile saved successfully.");
        return user;
    }

    /**
     * Displays the main menu.
     */
    private static void printMenu() {
        System.out.println();
        System.out.println("--------------- MENU ----------------");
        System.out.println("1. Add Expense");
        System.out.println("2. Edit Expense");
        System.out.println("3. Delete Expense");
        System.out.println("4. View All Expenses");
        System.out.println("5. Set Monthly Budget");
        System.out.println("6. Show Total Spending");
        System.out.println("7. Show Category-wise Breakdown");
        System.out.println("8. Show Budget Status");
        System.out.println("9. Exit");
        System.out.println("-------------------------------------");
    }

    /**
     * Adds a new expense after taking input from the user.
     *
     * @param scanner scanner for user input
     * @param expenseManager manager for expense operations
     * @param budgetManager manager for budget operations
     */
    private static void addExpense(Scanner scanner, ExpenseManager expenseManager, BudgetManager budgetManager) {
        double amount = readDouble(scanner, "Enter amount: ");
        String category = readNonEmptyString(scanner, "Enter category: ");
        LocalDate date = readDate(scanner, "Enter date (YYYY-MM-DD): ");
        String note = readNonEmptyString(scanner, "Enter note: ");

        Expense expense = expenseManager.addExpense(amount, category, date, note);
        System.out.println("Expense added successfully with ID: " + expense.getId());

        String warning = budgetManager.getBudgetWarning(expenseManager.getTotalSpending());
        if (!warning.isEmpty()) {
            System.out.println(warning);
        }
    }

    /**
     * Edits an existing expense by ID.
     *
     * @param scanner scanner for user input
     * @param expenseManager manager for expense operations
     */
    private static void editExpense(Scanner scanner, ExpenseManager expenseManager) {
        if (expenseManager.getExpenses().isEmpty()) {
            System.out.println("No expenses available to edit.");
            return;
        }

        viewAllExpenses(expenseManager);
        int id = readInt(scanner, "Enter expense ID to edit: ");
        Expense existing = expenseManager.getExpenseById(id);

        if (existing == null) {
            System.out.println("Expense ID not found.");
            return;
        }

        double amount = readDouble(scanner, "Enter new amount: ");
        String category = readNonEmptyString(scanner, "Enter new category: ");
        LocalDate date = readDate(scanner, "Enter new date (YYYY-MM-DD): ");
        String note = readNonEmptyString(scanner, "Enter new note: ");

        boolean updated = expenseManager.editExpense(id, amount, category, date, note);
        if (updated) {
            System.out.println("Expense updated successfully.");
        } else {
            System.out.println("Unable to update the expense.");
        }
    }

    /**
     * Deletes an existing expense by ID.
     *
     * @param scanner scanner for user input
     * @param expenseManager manager for expense operations
     */
    private static void deleteExpense(Scanner scanner, ExpenseManager expenseManager) {
        if (expenseManager.getExpenses().isEmpty()) {
            System.out.println("No expenses available to delete.");
            return;
        }

        viewAllExpenses(expenseManager);
        int id = readInt(scanner, "Enter expense ID to delete: ");

        boolean deleted = expenseManager.deleteExpense(id);
        if (deleted) {
            System.out.println("Expense deleted successfully.");
        } else {
            System.out.println("Expense ID not found.");
        }
    }

    /**
     * Displays all saved expenses.
     *
     * @param expenseManager manager for expense operations
     */
    private static void viewAllExpenses(ExpenseManager expenseManager) {
        List<Expense> expenses = expenseManager.getExpenses();
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        System.out.println();
        System.out.println("ID | Amount | Category | Date | Note");
        System.out.println("-----------------------------------------------");
        for (Expense expense : expenses) {
            System.out.println(expense);
        }
    }

    /**
     * Sets the monthly budget value.
     *
     * @param scanner scanner for user input
     * @param budgetManager manager for budget operations
     */
    private static void setMonthlyBudget(Scanner scanner, BudgetManager budgetManager) {
        double budget = readDouble(scanner, "Enter monthly budget: ");
        budgetManager.setMonthlyBudget(budget);
        System.out.println("Monthly budget saved successfully.");
    }

    /**
     * Shows the total spending amount.
     *
     * @param expenseManager manager for expense operations
     */
    private static void showTotalSpending(ExpenseManager expenseManager) {
        double total = expenseManager.getTotalSpending();
        System.out.printf("Total spending: %.2f%n", total);
    }

    /**
     * Shows category-wise spending totals.
     *
     * @param expenseManager manager for expense operations
     */
    private static void showCategoryBreakdown(ExpenseManager expenseManager) {
        Map<String, Double> breakdown = expenseManager.getCategoryWiseBreakdown();
        if (breakdown.isEmpty()) {
            System.out.println("No expenses available for breakdown.");
            return;
        }

        System.out.println("Category-wise Breakdown:");
        for (Map.Entry<String, Double> entry : breakdown.entrySet()) {
            System.out.printf("%s : %.2f%n", entry.getKey(), entry.getValue());
        }
    }

    /**
     * Shows the current budget status and warning if applicable.
     *
     * @param expenseManager manager for expense operations
     * @param budgetManager manager for budget operations
     */
    private static void showBudgetStatus(ExpenseManager expenseManager, BudgetManager budgetManager) {
        double budget = budgetManager.getMonthlyBudget();
        double total = expenseManager.getTotalSpending();

        if (budget <= 0) {
            System.out.println("Monthly budget is not set yet.");
            return;
        }

        System.out.printf("Monthly budget: %.2f%n", budget);
        System.out.printf("Current spending: %.2f%n", total);
        System.out.printf("Remaining budget: %.2f%n", (budget - total));

        String warning = budgetManager.getBudgetWarning(total);
        if (!warning.isEmpty()) {
            System.out.println(warning);
        } else {
            System.out.println("You are within your monthly budget.");
        }
    }

    /**
     * Reads a valid integer from the user.
     *
     * @param scanner scanner for user input
     * @param message prompt message
     * @return valid integer value
     */
    private static int readInt(Scanner scanner, String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException exception) {
                System.out.println("Invalid number. Please enter a whole number.");
            }
        }
    }

    /**
     * Reads a valid double value from the user.
     *
     * @param scanner scanner for user input
     * @param message prompt message
     * @return valid double value
     */
    private static double readDouble(Scanner scanner, String message) {
        while (true) {
            try {
                System.out.print(message);
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value < 0) {
                    System.out.println("Amount cannot be negative.");
                    continue;
                }
                return value;
            } catch (NumberFormatException exception) {
                System.out.println("Invalid amount. Please enter a numeric value.");
            }
        }
    }

    /**
     * Reads a non-empty string from the user.
     *
     * @param scanner scanner for user input
     * @param message prompt message
     * @return non-empty string
     */
    private static String readNonEmptyString(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    /**
     * Reads a valid date in YYYY-MM-DD format.
     *
     * @param scanner scanner for user input
     * @param message prompt message
     * @return valid local date
     */
    private static LocalDate readDate(Scanner scanner, String message) {
        while (true) {
            try {
                System.out.print(message);
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException exception) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }
}

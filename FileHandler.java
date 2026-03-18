import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * FileHandler class performs reading and writing of project data files.
 */
public class FileHandler {
    private static final String EXPENSE_FILE = "expenses.csv";
    private static final String BUDGET_FILE = "budget.txt";
    private static final String USER_FILE = "user.txt";

    /**
     * Saves all expenses to a CSV file.
     *
     * @param expenses list of expenses
     */
    public void saveExpenses(List<Expense> expenses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EXPENSE_FILE))) {
            for (Expense expense : expenses) {
                writer.write(expense.toCsv());
                writer.newLine();
            }
        } catch (IOException exception) {
            System.out.println("Error saving expenses: " + exception.getMessage());
        }
    }

    /**
     * Loads expenses from the CSV file.
     *
     * @return list of loaded expenses
     */
    public List<Expense> loadExpenses() {
        List<Expense> expenses = new ArrayList<>();
        File file = new File(EXPENSE_FILE);

        if (!file.exists()) {
            return expenses;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 5);
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    double amount = Double.parseDouble(parts[1]);
                    String category = parts[2];
                    LocalDate date = LocalDate.parse(parts[3]);
                    String note = parts[4];
                    expenses.add(new Expense(id, amount, category, date, note));
                }
            }
        } catch (IOException | NumberFormatException exception) {
            System.out.println("Error loading expenses: " + exception.getMessage());
        }

        return expenses;
    }

    /**
     * Saves the monthly budget to a text file.
     *
     * @param budget budget amount
     */
    public void saveBudget(double budget) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BUDGET_FILE))) {
            writer.write(String.valueOf(budget));
        } catch (IOException exception) {
            System.out.println("Error saving budget: " + exception.getMessage());
        }
    }

    /**
     * Loads the monthly budget from a text file.
     *
     * @return saved budget or 0 if not found
     */
    public double loadBudget() {
        File file = new File(BUDGET_FILE);
        if (!file.exists()) {
            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String value = reader.readLine();
            if (value != null && !value.trim().isEmpty()) {
                return Double.parseDouble(value.trim());
            }
        } catch (IOException | NumberFormatException exception) {
            System.out.println("Error loading budget: " + exception.getMessage());
        }

        return 0;
    }

    /**
     * Saves user details to a text file.
     *
     * @param user user profile
     */
    public void saveUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            writer.write(user.getName());
            writer.newLine();
            writer.write(user.getCity());
        } catch (IOException exception) {
            System.out.println("Error saving user profile: " + exception.getMessage());
        }
    }

    /**
     * Loads user details from a text file.
     *
     * @return user profile or null if not found
     */
    public User loadUser() {
        File file = new File(USER_FILE);
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String name = reader.readLine();
            String city = reader.readLine();
            if (name != null && city != null) {
                return new User(name, city);
            }
        } catch (IOException exception) {
            System.out.println("Error loading user profile: " + exception.getMessage());
        }

        return null;
    }
}

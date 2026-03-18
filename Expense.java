import java.time.LocalDate;

/**
 * Expense class represents one expense record in the system.
 */
public class Expense {
    private int id;
    private double amount;
    private String category;
    private LocalDate date;
    private String note;

    /**
     * Creates a new expense object.
     *
     * @param id unique expense ID
     * @param amount expense amount
     * @param category expense category
     * @param date expense date
     * @param note additional note
     */
    public Expense(int id, double amount, String category, LocalDate date, String note) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    /**
     * Returns expense ID.
     *
     * @return expense ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets expense ID.
     *
     * @param id expense ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns expense amount.
     *
     * @return expense amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets expense amount.
     *
     * @param amount expense amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Returns expense category.
     *
     * @return expense category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets expense category.
     *
     * @param category expense category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns expense date.
     *
     * @return expense date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets expense date.
     *
     * @param date expense date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns expense note.
     *
     * @return expense note
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets expense note.
     *
     * @param note expense note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Converts expense data to CSV format for file storage.
     *
     * @return CSV line
     */
    public String toCsv() {
        return id + "," + amount + "," + sanitize(category) + "," + date + "," + sanitize(note);
    }

    /**
     * Replaces commas in text fields to keep CSV simple.
     *
     * @param value text value
     * @return cleaned text value
     */
    private String sanitize(String value) {
        return value.replace(",", " ");
    }

    /**
     * Returns a formatted string for display in the console.
     *
     * @return formatted expense record
     */
    @Override
    public String toString() {
        return id + " | " + String.format("%.2f", amount) + " | " + category + " | " + date + " | " + note;
    }
}

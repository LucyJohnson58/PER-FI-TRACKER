import java.time.LocalDate;

public class Expense {

    private String category;
    private double amount;
    private LocalDate date;
    private String description;

    public Expense(String category, double amount, LocalDate date, String description) {
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDetails() {
        return "Expense: " + category +
                "\nAmount: MWK" + amount +
                "\nDate: " + date +
                "\nDescription: " + description;
    }

    public String getCategory() {
        return category.trim().toLowerCase();  // For filtering
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}

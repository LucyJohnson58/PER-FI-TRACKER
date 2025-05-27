import java.time.LocalDate;

public class Income {

    private String source;
    private double amount;
    private LocalDate date;
    private String description;

    public Income(String source, double amount, LocalDate date, String description) {
        this.source = source;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDetails() {
        return "Income Source: " + source +
                "\nAmount: MWK" + amount +
                "\nDate: " + date +
                "\nDescription: " + description;
    }

    public double getAmount() {
        return amount;
    }

    // ✅ Used for filtering
    public String getSource() {
        return source.trim().toLowerCase();
    }

    public String getDescription() {
        return description;
    }
}

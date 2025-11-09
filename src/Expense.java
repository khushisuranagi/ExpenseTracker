import javafx.beans.property.*;

public class Expense {
    private StringProperty description;
    private DoubleProperty amount;
    private StringProperty date;

    public Expense(String description, double amount, String date) {
        this.description = new SimpleStringProperty(description);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleStringProperty(date);
    }

    // TableView property getters
    public StringProperty descriptionProperty() { return description; }
    public DoubleProperty amountProperty() { return amount; }
    public StringProperty dateProperty() { return date; }

    // Regular getters for summaries
    public String getDescription() { return description.get(); }
    public double getAmount() { return amount.get(); }
    public String getDate() { return date.get(); }
}

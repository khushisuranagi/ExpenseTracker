import javafx.beans.property.*;

public class Expense {
    private StringProperty category;
    private StringProperty description;
    private DoubleProperty amount;
    private StringProperty date;

    public Expense(String category, String description,double amount, String date) {
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleStringProperty(date);
    }

    // TableView property getters
    public StringProperty categoryProperty(){ return category; }
    public StringProperty descriptionProperty() { return description; }
    public DoubleProperty amountProperty() { return amount; }
    public StringProperty dateProperty() { return date; }

    // Regular getters for summaries
    public String getCategory() { return category.get(); }
    public String getDescription() { return description.get(); }
    public double getAmount() { return amount.get(); }
    public String getDate() { return date.get(); }
}

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ExpenseTrackerUI {

    private VBox layout;
    private TableView<Expense> tableView;
    private ObservableList<Expense> expenses;

    private ListView<String> summaryView;
    private ObservableList<String> summaryItems;

    public ExpenseTrackerUI() {
        layout = new VBox(10);
        layout.setPadding(new Insets(15));

        Label title = new Label("Expense Tracker");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Category dropdown
        ComboBox<String> categoryDropdown = new ComboBox<>();
        categoryDropdown.setPromptText("Select Category");
        categoryDropdown.getItems().addAll("Food", "Travel", "Shopping", "Entertainment", "Other");

        //description input
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");
        // Amount input
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        // Add button
        Button addButton = new Button("Add Expense");

        // Initialize TableView
        tableView = new TableView<>();
        expenses = FXCollections.observableArrayList();
        tableView.setItems(expenses);

        TableColumn<Expense, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data -> data.getValue().categoryProperty());
        categoryCol.setPrefWidth(100);

        TableColumn<Expense, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(data -> data.getValue().descriptionProperty());
        descriptionCol.setPrefWidth(200);

        TableColumn<Expense, Double> amountCol = new TableColumn<>("Amount (₹)");
        amountCol.setCellValueFactory(data -> data.getValue().amountProperty().asObject());
        amountCol.setPrefWidth(100);

        TableColumn<Expense, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> data.getValue().dateProperty());
        dateCol.setPrefWidth(100);

        tableView.getColumns().addAll(categoryCol, descriptionCol,amountCol, dateCol);

        // Initialize summary ListView
        summaryView = new ListView<>();
        summaryItems = FXCollections.observableArrayList();
        summaryView.setItems(summaryItems);
        summaryView.setPrefHeight(120); // optional height for summary

        // Add button action
        addButton.setOnAction(e -> {
            String category = categoryDropdown.getValue();
            if (category == null || category.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a category!");
                alert.showAndWait();
                return;
            }

            String description = descriptionField.getText();
            if (description == null || description.trim().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a description!");
                alert.showAndWait();
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(amountField.getText());
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a valid amount!");
                alert.showAndWait();
                return;
            }

            String date = LocalDate.now().toString();

            // Save to database
            DatabaseHelper.addExpense(1, amount, description, category, date);

            // Add to TableView
            expenses.add(new Expense(category, description, amount, date));

            // Update summary
            updateSummary();

            // Clear inputs
            categoryDropdown.setValue(null);
            descriptionField.clear();
            amountField.clear();
        });

        // Add all components to layout
        layout.getChildren().addAll(title, categoryDropdown, descriptionField , amountField, addButton, tableView, summaryView);
    }

    public VBox getView() {
        return layout;
    }

    // Update summary ListView
    private void updateSummary() {
        summaryItems.clear();

        Map<String, Double> totals = new HashMap<>();
        for (Expense exp : expenses) {
            totals.put(exp.getCategory(), totals.getOrDefault(exp.getCategory(), 0.0) + exp.getAmount());
        }

        double overallTotal = 0;
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            summaryItems.add(entry.getKey() + ": ₹" + entry.getValue());
            overallTotal += entry.getValue();
        }

        summaryItems.add("Total Expenses: ₹" + overallTotal);
    }
}

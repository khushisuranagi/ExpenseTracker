import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

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

        // Description input
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description (e.g., Taxi to airport)");

        // Amount input
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        // Add button
        Button addButton = new Button("Add Expense");

        Button exportButton = new Button("Export to Excel (CSV)");
        exportButton.setOnAction(e -> {
            ExportToCSV.exportExpenses(expenses);
            new Alert(Alert.AlertType.INFORMATION, "Exported successfully to expenses.csv!").showAndWait();
        });

        Button deleteButton = new Button("Delete Selected");
        deleteButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> {
            Expense selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Please select an expense to delete!").showAndWait();
                return;
            }

            // Confirm deletion
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete this expense?");
            confirmAlert.setContentText(selected.getCategory() + " - ₹" + selected.getAmount());

            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                // Delete from database
                boolean deleted = DatabaseHelper.deleteExpense(selected.getId());

                if (deleted) {
                    // Remove from table
                    expenses.remove(selected);
                    updateSummary();
                    new Alert(Alert.AlertType.INFORMATION, "Expense deleted successfully!").showAndWait();
                }
            }
        });




        // Initialize TableView
        tableView = new TableView<>();
        expenses = FXCollections.observableArrayList();
        tableView.setItems(expenses);


        TableColumn<Expense, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        idCol.setPrefWidth(50);

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

        tableView.getColumns().addAll(idCol, descriptionCol, amountCol, dateCol);



        // Initialize summary ListView
        summaryView = new ListView<>();
        summaryItems = FXCollections.observableArrayList();
        summaryView.setItems(summaryItems);
        summaryView.setPrefHeight(120);

        loadExpensesFromDatabase();

        // Add button action
        addButton.setOnAction(e -> {
            String category = categoryDropdown.getValue();
            if (category == null || category.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please select a category!").showAndWait();
                return;
            }

            String description = descriptionField.getText();
            if (description == null || description.trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter a description!").showAndWait();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountField.getText());
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.WARNING, "Please enter a valid amount!").showAndWait();
                return;
            }

            // 👇 Properly formatted date
            String date = java.time.LocalDate.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd-MMM-yyyy"));

            // 👇 Add to database
            DatabaseHelper.addExpense(category, amount, description, date);

            // 👇 Add to the in-memory table
            //expenses.add(new Expense(category, description, amount, date));

            expenses.clear();
            loadExpensesFromDatabase();


            // 👇 Update summary + clear inputs
            updateSummary();
            categoryDropdown.setValue(null);
            descriptionField.clear();
            amountField.clear();
        });




        // Add all components to layout
        layout.getChildren().addAll(title, categoryDropdown, descriptionField, amountField, addButton, exportButton, deleteButton, tableView, summaryView);

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

    // Load expenses from database when UI starts
    private void loadExpensesFromDatabase() {
        List<Expense> loadedExpenses = DatabaseHelper.loadExpenses();
        expenses.addAll(loadedExpenses);
        updateSummary();
        System.out.println("📊 Loaded " + loadedExpenses.size() + " expenses into UI");
    }

}
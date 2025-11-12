import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginUI {

    private VBox layout;

    public LoginUI(Stage stage) {
        layout = new VBox(10);
        layout.setPadding(new Insets(15));

        Label title = new Label("Login");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Label message = new Label();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                message.setText("Please enter both username and password.");
                return;
            }

            boolean isValid = DatabaseHelper.checkLogin(username, password);

            if (isValid) {
                message.setText("Login successful!");
                ExpenseTrackerUI expenseUI = new ExpenseTrackerUI();
                stage.setScene(new Scene(expenseUI.getView(), 600, 400));
            } else {
                message.setText("Invalid login! Try again.");
            }
        });

        layout.getChildren().addAll(title, usernameField, passwordField, loginButton, message);
    }

    public VBox getView() {
        return layout;
    }
}

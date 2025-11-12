import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        DatabaseHelper.createUsersTable();
        DatabaseHelper.addDefaultUser();

        stage.setTitle("Login - Expense Tracker");
        stage.setScene(new Scene(new LoginUI(stage).getView(), 400, 250));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
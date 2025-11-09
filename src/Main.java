import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {   //JavaFX requires a class that extends Application and overrides the start(Stage stage) method. That start method is what opens the GUI window.
    @Override
    public void start(Stage stage) {
        stage.setTitle("Expense Tracker");
        stage.setScene(new Scene(new ExpenseTrackerUI().getView(), 600, 400));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

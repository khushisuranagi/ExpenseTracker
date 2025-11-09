import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:expenses.db";

    // connect to database
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    // add category
    public static void addCategory(String name) {
        String sql = "INSERT INTO categories(name) VALUES(?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Category added: " + name);
        } catch (SQLException e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }

    // add expense
    public static void addExpense(int categoryId, double amount, String description, String date) {
        String sql = "INSERT INTO expenses(category_id, amount, description, date) VALUES(?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, description);
            pstmt.setString(4, date);
            pstmt.executeUpdate();
            System.out.println("Expense added successfully");
        } catch (SQLException e) {
            System.out.println("Error adding expense: " + e.getMessage());
        }
    }

    // view all expenses
    public static void viewExpenses() {
        String sql = "SELECT e.id, c.name, e.amount, e.description, e.date " +
                "FROM expenses e LEFT JOIN categories c ON e.category_id = c.id";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("📋 All Expenses:");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getDouble("amount") + " | " +
                                rs.getString("description") + " | " +
                                rs.getString("date")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Error viewing expenses: " + e.getMessage());
        }
    }
}

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public static void addExpense(String category, double amount, String description, String date) {
        String sql = "INSERT INTO expenses(category, amount, description, date) VALUES(?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, description);
            pstmt.setString(4, date);
            pstmt.executeUpdate();

            System.out.println("Expense added successfully: " + category + ", " + amount + ", " + date);
        } catch (SQLException e) {
            System.out.println(" Error adding expense: " + e.getMessage());
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
            System.out.println(" Error viewing expenses: " + e.getMessage());
        }
    }


    public static void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "password TEXT)";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Users table ready");
        } catch (SQLException e) {
            System.out.println("Error creating users table: " + e.getMessage());
        }
    }


    public static void createExpensesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS expenses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "category TEXT, " +
                "description TEXT, " +
                "amount REAL, " +
                "date TEXT" +
                ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Expenses table created or already exists.");
        } catch (SQLException e) {
            System.out.println(" Error creating expenses table: " + e.getMessage());
        }
    }



    public static void addDefaultUser() {
        String sql = "INSERT OR IGNORE INTO users(username, password) VALUES('admin', '1234')";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Default user added");
        } catch (SQLException e) {
            System.out.println("Error adding default user: " + e.getMessage());
        }
    }

    // check if login details are correct
    public static boolean checkLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // true if found
        } catch (SQLException e) {
            System.out.println("Error checking login: " + e.getMessage());
            return false;
        }
    }


    public static List<Expense> loadExpenses() {
        List<Expense> expenseList = new ArrayList<>();
        String sql = "SELECT id, category, description, amount, date FROM expenses ORDER BY id DESC";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String category = rs.getString("category");
                String description = rs.getString("description");
                double amount = rs.getDouble("amount");
                String date = rs.getString("date");

                expenseList.add(new Expense(id, category, description, amount, date));
            }
            System.out.println(" Loaded " + expenseList.size() + " expenses from database");
        } catch (SQLException e) {
            System.out.println("Error loading expenses: " + e.getMessage());
        }

        return expenseList;
    }
    public static boolean deleteExpense(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Expense deleted successfully");
                return true;
            } else {
                System.out.println(" No expense found with that ID");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(" Error deleting expense: " + e.getMessage());
            return false;
        }
    }

}
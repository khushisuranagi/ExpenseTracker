import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportToCSV {

    public static void exportExpenses(List<Expense> expenses) {
        String fileName = "expenses.csv";

        try (FileWriter writer = new FileWriter(fileName)) {
            // 👇 Write header row
            writer.append("Category,Description,Amount (₹),Date\n");

            // 👇 Write each expense
            for (Expense exp : expenses) {
                writer.append(exp.getCategory() == null ? "" : exp.getCategory()).append(",");
                writer.append(exp.getDescription() == null ? "" : exp.getDescription().replace(",", " ")).append(",");
                writer.append(String.valueOf(exp.getAmount())).append(",");
                writer.append(exp.getDate() == null ? "" : exp.getDate()).append("\n");
            }

            writer.flush();
            System.out.println("✅ Export successful! File saved as: " + fileName);

            // 👇 Optional: Automatically open the file in Excel
            try {
                java.awt.Desktop.getDesktop().open(new java.io.File(fileName));
            } catch (IOException ex) {
                System.out.println("⚠️ Could not open file automatically: " + ex.getMessage());
            }

        } catch (IOException e) {
            System.out.println("❌ Error exporting data: " + e.getMessage());
        }
    }
}

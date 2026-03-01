import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportToCSV {

    public static void exportExpenses(List<Expense> expenses) {
        String fileName = "expenses.csv";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write('\ufeff');

            writer.append("ID,Category,Description,Amount,Date\n");

            double total = 0.0;

            for (Expense exp : expenses) {
                writer.append(String.valueOf(exp.getId())).append(",");
                writer.append(exp.getCategory() == null ? "" : escapeCSV(exp.getCategory())).append(",");
                writer.append(exp.getDescription() == null ? "" : escapeCSV(exp.getDescription())).append(",");
                writer.append(String.format("%.2f", exp.getAmount())).append(",");

                // Format date - wrap in quotes to preserve format
                String date = exp.getDate() == null ? "" : exp.getDate();
                writer.append("\"" + date + "\"").append("\n");

                // Add to total
                total += exp.getAmount();
            }


            writer.append("\n");
            writer.append("====,====,====,====,====\n"); // Separator line
            writer.append(",,,TOTAL EXPENSES:,₹" + String.format("%.2f", total) + "\n");
            writer.append("====,====,====,====,====\n"); // Separator line

            writer.flush();
            System.out.println(" Export successful! File saved as: " + fileName);
            System.out.println(" Total Expenses: ₹" + String.format("%.2f", total));

            try {
                java.awt.Desktop.getDesktop().open(new java.io.File(fileName));
            } catch (IOException ex) {
                System.out.println(" Could not open file automatically: " + ex.getMessage());
            }

        } catch (IOException e) {
            System.out.println(" Error exporting data: " + e.getMessage());
        }
    }

    // Helper method to escape CSV values properly
    private static String escapeCSV(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
import java.io.*;
import java.util.*;

public class ExpenseManager {
    private static final String FILE_NAME = "expenses.csv";

    public static void saveExpense(Expense expense) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(expense.toCSV() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Expense> getExpenses() {
        List<Expense> expenses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                expenses.add(new Expense(parts[0], parts[1], parts[2], Double.parseDouble(parts[3])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public static Map<String, Double> getMonthlySummary(String month) {
        Map<String, Double> summary = new HashMap<>();
        for (Expense e : getExpenses()) {
            if (e.getDate().startsWith(month)) {
                summary.put(e.getCategory(), summary.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
            }
        }
        return summary;
    }

    public static List<Expense> filterExpenses(String category, String startDate, String endDate) {
        List<Expense> filtered = new ArrayList<>();
        for (Expense e : getExpenses()) {
            if ((category.equals("All") || e.getCategory().equals(category)) &&
                    e.getDate().compareTo(startDate) >= 0 &&
                    e.getDate().compareTo(endDate) <= 0) {
                filtered.add(e);
            }
        }
        return filtered;
    }
}


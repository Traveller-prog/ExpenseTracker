import java.time.LocalDate;
import java.util.*;

class Expense {
    String category;
    double amount;
    LocalDate date;

    Expense(String category, double amount, LocalDate date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("📅 %s | 🗂️ %s | 💸 ₹%.2f", date, category, amount);
    }
}

public class ExpenseTracker {
    private static final List<Expense> expenses = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("🧾 Welcome to Your Enhanced Expense Tracker!");

        while (true) {
            System.out.println("\n🔘 Choose an option:");
            System.out.println("1️⃣ Add Expense");
            System.out.println("2️⃣ View All Expenses");
            System.out.println("3️⃣ View Category Summary");
            System.out.println("4️⃣ View Total Expenses");
            System.out.println("5️⃣ Exit");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addExpense();
                case "2" -> viewExpenses();
                case "3" -> categorySummary();
                case "4" -> showTotal();
                case "5" -> {
                    System.out.println("👋 Goodbye! ");
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    private static void addExpense() {
        System.out.print("Enter category (e.g., Food, Rent, Travel): ");
        String category = scanner.nextLine().trim();

        double amount = 0;
        while (true) {
            System.out.print("Enter amount (₹): ");
            try {
                amount = Double.parseDouble(scanner.nextLine().trim());
                if (amount <= 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid amount. Please enter a positive number.");
            }
        }

        LocalDate date = LocalDate.now();
        expenses.add(new Expense(category, amount, date));
        System.out.println("✅ Expense added successfully!");
    }

    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("📭 No expenses recorded yet.");
        } else {
            System.out.println("📋 All Expenses:");
            expenses.forEach(System.out::println);
        }
    }

    private static void categorySummary() {
        if (expenses.isEmpty()) {
            System.out.println("📭 No expenses to summarize.");
            return;
        }

        Map<String, Double> summary = new HashMap<>();
        for (Expense e : expenses) {
            summary.put(e.category, summary.getOrDefault(e.category, 0.0) + e.amount);
        }

        System.out.println("📊 Category-wise Summary:");
        summary.forEach((cat, amt) -> System.out.printf("🗂️ %s: ₹%.2f%n", cat, amt));
    }

    private static void showTotal() {
        double total = expenses.stream().mapToDouble(e -> e.amount).sum();
        System.out.printf("💰 Total Expenses: ₹%.2f%n", total);
    }
}
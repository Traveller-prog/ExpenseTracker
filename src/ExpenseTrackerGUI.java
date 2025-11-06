import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ExpenseTrackerGUI extends JFrame {
    private JTextField dateField, amountField, descriptionField, monthField;
    private JComboBox<String> categoryBox, filterCategory;
    private JTextField filterStartDate, filterEndDate;
    private JTextArea summaryArea, filterArea;

    public ExpenseTrackerGUI() {
        setTitle("Expense Tracker");
        setSize(700, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font inputFont = new Font("SansSerif", Font.PLAIN, 14);

        // === Input Panel ===
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Expense"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        dateField = new JTextField("2025-10-17");
        categoryBox = new JComboBox<>(new String[]{"Food", "Travel", "Utilities", "Entertainment", "Other"});
        descriptionField = new JTextField();
        amountField = new JTextField();
        JButton addButton = new JButton("Add Expense");

        String[] labels = {"Date (YYYY-MM-DD):", "Category:", "Description:", "Amount:"};
        JComponent[] fields = {dateField, categoryBox, descriptionField, amountField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            JLabel label = new JLabel(labels[i]);
            label.setFont(labelFont);
            inputPanel.add(label, gbc);
            gbc.gridx = 1;
            fields[i].setFont(inputFont);
            inputPanel.add(fields[i], gbc);
        }

        gbc.gridx = 1; gbc.gridy = labels.length;
        inputPanel.add(addButton, gbc);

        // === Summary Panel ===
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Monthly Summary"));

        JPanel summaryInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        monthField = new JTextField("2025-10", 10);
        JButton summaryButton = new JButton("Show Summary");
        summaryInputPanel.add(new JLabel("Month (YYYY-MM):"));
        summaryInputPanel.add(monthField);
        summaryInputPanel.add(summaryButton);

        summaryArea = new JTextArea(8, 40);
        summaryArea.setFont(inputFont);
        summaryArea.setEditable(false);
        JScrollPane summaryScroll = new JScrollPane(summaryArea);

        summaryPanel.add(summaryInputPanel, BorderLayout.NORTH);
        summaryPanel.add(summaryScroll, BorderLayout.CENTER);

        // === Filter Panel ===
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Expenses"));

        JPanel filterInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterCategory = new JComboBox<>(new String[]{"All", "Food", "Travel", "Utilities", "Entertainment", "Other"});
        filterStartDate = new JTextField("2025-10-01", 10);
        filterEndDate = new JTextField("2025-10-31", 10);
        JButton filterButton = new JButton("Apply Filter");

        filterInputPanel.add(new JLabel("Category:"));
        filterInputPanel.add(filterCategory);
        filterInputPanel.add(new JLabel("Start Date:"));
        filterInputPanel.add(filterStartDate);
        filterInputPanel.add(new JLabel("End Date:"));
        filterInputPanel.add(filterEndDate);
        filterInputPanel.add(filterButton);

        filterArea = new JTextArea(8, 40);
        filterArea.setFont(inputFont);
        filterArea.setEditable(false);
        JScrollPane filterScroll = new JScrollPane(filterArea);

        filterPanel.add(filterInputPanel, BorderLayout.NORTH);
        filterPanel.add(filterScroll, BorderLayout.CENTER);

        // === Add Panels to Frame ===
        add(inputPanel, BorderLayout.NORTH);
        add(summaryPanel, BorderLayout.CENTER);
        add(filterPanel, BorderLayout.SOUTH);

        // === Action Listeners ===
        addButton.addActionListener(e -> {
            try {
                String date = dateField.getText();
                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.");
                    return;
                }
                double amount = Double.parseDouble(amountField.getText());
                Expense exp = new Expense(
                        date,
                        categoryBox.getSelectedItem().toString(),
                        descriptionField.getText(),
                        amount
                );
                ExpenseManager.saveExpense(exp);
                JOptionPane.showMessageDialog(this, "Expense saved!");
                dateField.setText(""); descriptionField.setText(""); amountField.setText(""); descriptionField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        summaryButton.addActionListener(e -> {
            Map<String, Double> summary = ExpenseManager.getMonthlySummary(monthField.getText());
            summaryArea.setText("");
            for (String cat : summary.keySet()) {
                summaryArea.append(cat + ": ₹" + summary.get(cat) + "\n");
            }
        });

        filterButton.addActionListener(evt -> {
            List<Expense> filtered = ExpenseManager.filterExpenses(
                    filterCategory.getSelectedItem().toString(),
                    filterStartDate.getText(),
                    filterEndDate.getText()
            );

            filterArea.setText("");
            for (Expense exp : filtered) {
                filterArea.append(
                        exp.getDate() + " | " +
                                exp.getCategory() + " | ₹" +
                                exp.getAmount() + " | " +
                                exp.getDescription() + "\n"
                );
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new ExpenseTrackerGUI();
    }
}

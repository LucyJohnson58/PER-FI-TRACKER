import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    public static void addExpense(ArrayList<Expense> expenses) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("📝 Enter expense category: ");
        String category = scanner.nextLine();
        System.out.print("💵 Enter amount: MWK ");
        double amount = scanner.nextDouble(); scanner.nextLine();
        System.out.print("📄 Enter description: ");
        String description = scanner.nextLine();
        Expense e = new Expense(category, amount, LocalDate.now(), description);
        expenses.add(e);
        System.out.println("\n✅ Expense added successfully!");
    }

    public static void addIncome(ArrayList<Income> incomeList) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("📝 Enter income source: ");
        String source = scanner.nextLine();
        System.out.print("💵 Enter amount: MWK ");
        double amount = scanner.nextDouble(); scanner.nextLine();
        System.out.print("📄 Enter description: ");
        String description = scanner.nextLine();
        Income i = new Income(source, amount, LocalDate.now(), description);
        incomeList.add(i);
        System.out.println("\n✅ Income added successfully!");
    }

    public static void filterExpensesByCategory(ArrayList<Expense> expenses) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("🔍 Enter category to filter (e.g., 'Food', 'Rent'): ");
        String input = scanner.nextLine().trim().toLowerCase();
        boolean found = false;
        System.out.println("\n📁 FILTERED EXPENSES FOR CATEGORY: " + input);
        for (Expense e : expenses) {
            if (e.getCategory().toLowerCase().equals(input)) {
                System.out.println(e.getDetails());
                System.out.println("----------------------");
                found = true;
            }
        }
        if (!found) System.out.println("❌ No expenses found for category: " + input);
    }

    public static void filterIncomeBySource(ArrayList<Income> incomeList) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("🔍 Enter income source to filter (e.g., 'Salary', 'Bonus'): ");
        String input = scanner.nextLine().trim().toLowerCase();
        boolean found = false;
        System.out.println("\n📁 FILTERED INCOME FOR SOURCE: " + input);
        for (Income i : incomeList) {
            if (i.getSource().toLowerCase().equals(input)) {
                System.out.println(i.getDetails());
                System.out.println("----------------------");
                found = true;
            }
        }
        if (!found) System.out.println("❌ No income found for source: " + input);
    }

    public static void showTotalBalance(ArrayList<Expense> expenses, ArrayList<Income> incomeList) {
        double totalExpenses = 0;
        double totalIncome = 0;

        for (Expense e : expenses) totalExpenses += e.getAmount();
        for (Income i : incomeList) totalIncome += i.getAmount();

        double balance = totalIncome - totalExpenses;

        System.out.printf("\n💳 TOTAL INCOME  : MWK%.2f\n", totalIncome);
        System.out.printf("💸 TOTAL EXPENSES: MWK%.2f\n", totalExpenses);
        System.out.printf("💾 NET BALANCE   : MWK%.2f\n", balance);
    }

    public static void viewMonthlyReport(ArrayList<Expense> expenses, ArrayList<Income> incomeList) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        System.out.print("📅 Enter month to view report (format: yyyy-MM): ");
        String input = scanner.nextLine().trim();

        // 🔥 Validate input format (Prevents wrong month format)
        if (!input.matches("\\d{4}-\\d{2}")) {
            System.out.println("❌ Invalid format! Please enter month in 'yyyy-MM' format.");
            return;
        }

        double incomeTotal = 0;
        double expenseTotal = 0;

        for (Income income : incomeList) {
            if (income.getDate().format(formatter).equals(input)) {
                incomeTotal += income.getAmount();
            }
        }

        for (Expense expense : expenses) {
            if (expense.getDate().format(formatter).equals(input)) {
                expenseTotal += expense.getAmount();
            }
        }

        System.out.printf("\n📆 Report for %s\nIncome: MWK%.2f\nExpenses: MWK%.2f\nNet: MWK%.2f\n",
                input, incomeTotal, expenseTotal, incomeTotal - expenseTotal);
    }


    public static ArrayList<Income> incomeList = new ArrayList<>();

    public static ArrayList<Expense> loadExpensesFromFile(String filename) {
        ArrayList<Expense> loadedExpenses = new ArrayList<>();
        File file = new File(filename);

        // 🔥 Auto-create file if missing
        if (!file.exists()) {
            System.out.println("⚠️ File '" + filename + "' not found! Creating a new file...");
            try {
                file.createNewFile();
                return loadedExpenses;  // Return empty list since file is new
            } catch (IOException e) {
                System.out.println("❌ Error creating file: " + e.getMessage());
                return loadedExpenses;  // Prevent crash
            }
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty() || line.equals("----------------------")) continue;
                if (line.startsWith("Expense:")) {
                    try {
                        String category = line.replace("Expense:", "").trim();
                        double amount = Double.parseDouble(reader.nextLine().replace("Amount: MWK", "").trim());
                        LocalDate date = LocalDate.parse(reader.nextLine().replace("Date:", "").trim());
                        String description = reader.nextLine().replace("Description:", "").trim();
                        loadedExpenses.add(new Expense(category, amount, date, description));
                    } catch (Exception e) {
                        System.out.println("\n⚠️ Skipping malformed expense entry due to: " + e.getMessage());
                    }
                } else if (line.startsWith("Income Source:")) {
                    try {
                        String source = line.replace("Income Source:", "").trim();
                        double amount = Double.parseDouble(reader.nextLine().replace("Amount: MWK", "").trim());
                        LocalDate date = LocalDate.parse(reader.nextLine().replace("Date:", "").trim());
                        String description = reader.nextLine().replace("Description:", "").trim();
                        incomeList.add(new Income(source, amount, date, description));
                    } catch (Exception e) {
                        System.out.println("\n⚠️ Skipping malformed income entry due to: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("\n❌ Error loading expenses: " + e.getMessage());
        }

        return loadedExpenses;
    }

    public static void viewAllMonthlySummary(ArrayList<Expense> expenses, ArrayList<Income> incomeList) {
        Map<String, Double> incomeByMonth = new TreeMap<>();
        Map<String, Double> expenseByMonth = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (Income income : incomeList) {
            String month = income.getDate().format(formatter);
            incomeByMonth.put(month, incomeByMonth.getOrDefault(month, 0.0) + income.getAmount());
        }

        for (Expense expense : expenses) {
            String month = expense.getDate().format(formatter);
            expenseByMonth.put(month, expenseByMonth.getOrDefault(month, 0.0) + expense.getAmount());
        }

        Set<String> allMonths = new TreeSet<>();
        allMonths.addAll(incomeByMonth.keySet());
        allMonths.addAll(expenseByMonth.keySet());

        System.out.println("\n📆 MONTHLY SUMMARY:");
        for (String month : allMonths) {
            double totalIncome = incomeByMonth.getOrDefault(month, 0.0);
            double totalExpenses = expenseByMonth.getOrDefault(month, 0.0);
            double net = totalIncome - totalExpenses;

            System.out.printf("\n[%s]\nIncome: MWK%.2f\nExpenses: MWK%.2f\nNet: MWK%.2f\n",
                    month, totalIncome, totalExpenses, net);

            // 🔥 Bar Chart Visual
            int incomeBars = (int) (totalIncome / 1000);
            int expenseBars = (int) (totalExpenses / 1000);

            System.out.print("Income  : ");
            for (int i = 0; i < incomeBars; i++) System.out.print("▇");
            System.out.println();

            System.out.print("Expenses: ");
            for (int i = 0; i < expenseBars; i++) System.out.print("▇");
            System.out.println("\n------------------------");
        }
    }

    // Keep all other methods unchanged

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int attempts = 3;
        boolean authenticated = false;

        while (attempts > 0) {
            System.out.print("🔐 Enter password to access the Expense Tracker: ");
            String pass = scanner.nextLine().trim();

            if (pass.equals("admin123")) {
                authenticated = true;
                break;
            } else {
                attempts--;
                if (attempts > 0) {
                    System.out.println("❌ Incorrect password. Attempts left: " + attempts);
                }
            }
        }

        if (!authenticated) {
            System.out.println("\n🔒 Too many incorrect attempts. Exiting...");
            return;
        }

        System.out.println("\n✅ Debug: Starting Expense Tracker...");

        ArrayList<Expense> expenses = new ArrayList<>();
        expenses.addAll(loadExpensesFromFile("finance_data.txt"));

        boolean running = true;
        while (running) {
            System.out.println("\n📊 EXPENSE TRACKER MENU:");
            System.out.println("1️⃣ Add Expense");
            System.out.println("2️⃣ Add Income");
            System.out.println("3️⃣ View Expenses");
            System.out.println("4️⃣ View Income");
            System.out.println("5️⃣ Filter Expenses by Category");
            System.out.println("6️⃣ Save Data & Exit");
            System.out.println("7️⃣ Filter Income by Source");
            System.out.println("8️⃣ Show Total Balance");
            System.out.println("9️⃣ View Monthly Report");
            System.out.println("🔟 View All Monthly Summary");
            System.out.print("\n🔹 Select an option (1-10): ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("\n❌ Please enter a valid number!");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> addExpense(expenses);
                case 2 -> addIncome(incomeList);
                case 3 -> {
                    System.out.println("\n📂 ALL EXPENSES:");
                    for (Expense expense : expenses) {
                        System.out.println(expense.getDetails());
                        System.out.println("----------------------");
                    }
                }
                case 4 -> {
                    System.out.println("\n💵 ALL INCOME:");
                    for (Income income : incomeList) {
                        System.out.println(income.getDetails());
                        System.out.println("----------------------");
                    }
                }
                case 5 -> filterExpensesByCategory(expenses);
                case 6 -> running = false;
                case 7 -> filterIncomeBySource(incomeList);
                case 8 -> showTotalBalance(expenses, incomeList);
                case 9 -> viewMonthlyReport(expenses, incomeList);
                case 10 -> viewAllMonthlySummary(expenses, incomeList);
                default -> System.out.println("\n❌ Invalid choice! Please try again.");
            }
        }

        try (FileWriter writer = new FileWriter("finance_data.txt")) {
            writer.write("💰 EXPENSES 💰\n");
            for (Expense expense : expenses) {
                writer.write(expense.getDetails() + "\n");
                writer.write("----------------------\n");
            }

            writer.write("\n💵 INCOME 💵\n");
            for (Income income : incomeList) {
                writer.write(income.getDetails() + "\n");
                writer.write("----------------------\n");
            }

            System.out.println("\n✅ Data successfully saved to finance_data.txt");
        } catch (IOException e) {
            System.out.println("\n❌ Error saving data: " + e.getMessage());
        }

        System.out.println("\n👋 Exiting Expense Tracker. See you next time!");
    }
}

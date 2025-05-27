import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.ArrayList;

public class Dashboard extends Application {

    private final ArrayList<Expense> expenses = new ArrayList<>();
    private final ArrayList<Income> incomes = new ArrayList<>();
    private VBox contentArea = new VBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Sidebar sidebar = new Sidebar();
        BorderPane root = new BorderPane();
        contentArea.setPadding(new Insets(20));
        contentArea.setSpacing(10);

        root.setLeft(sidebar);
        root.setCenter(contentArea);

        sidebar.getStyleClass().add("sidebar");

        sidebar.btnAddExpense.setOnAction(e -> showExpenseDialog());
        sidebar.btnAddIncome.setOnAction(e -> showIncomeDialog());
        sidebar.btnViewSummary.setOnAction(e -> showSummary());
        sidebar.btnMonthlyReport.setOnAction(e -> showMonthlyBarChart());

        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setTitle("Personal Finance Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showExpenseDialog() {
        Dialog<Expense> dialog = new Dialog<>();
        dialog.setTitle("Add Expense");

        Label lblCategory = new Label("Category:");
        TextField txtCategory = new TextField();
        Label lblAmount = new Label("Amount:");
        TextField txtAmount = new TextField();
        Label lblDescription = new Label("Description:");
        TextField txtDescription = new TextField();

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.add(lblCategory, 0, 0);
        grid.add(txtCategory, 1, 0);
        grid.add(lblAmount, 0, 1);
        grid.add(txtAmount, 1, 1);
        grid.add(lblDescription, 0, 2);
        grid.add(txtDescription, 1, 2);

        dialog.getDialogPane().setContent(grid);
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String category = txtCategory.getText();
                    double amount = Double.parseDouble(txtAmount.getText());
                    String description = txtDescription.getText();
                    return new Expense(category, amount, LocalDate.now(), description);
                } catch (NumberFormatException ex) {
                    showError("Invalid amount entered.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(expense -> {
            expenses.add(expense);
            showConfirmation("Expense added successfully!");
        });
    }

    private void showIncomeDialog() {
        Dialog<Income> dialog = new Dialog<>();
        dialog.setTitle("Add Income");

        Label lblSource = new Label("Source:");
        TextField txtSource = new TextField();
        Label lblAmount = new Label("Amount:");
        TextField txtAmount = new TextField();
        Label lblDescription = new Label("Description:");
        TextField txtDescription = new TextField();

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.add(lblSource, 0, 0);
        grid.add(txtSource, 1, 0);
        grid.add(lblAmount, 0, 1);
        grid.add(txtAmount, 1, 1);
        grid.add(lblDescription, 0, 2);
        grid.add(txtDescription, 1, 2);

        dialog.getDialogPane().setContent(grid);
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String source = txtSource.getText();
                    double amount = Double.parseDouble(txtAmount.getText());
                    String description = txtDescription.getText();
                    return new Income(source, amount, LocalDate.now(), description);
                } catch (NumberFormatException ex) {
                    showError("Invalid amount entered.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(income -> {
            incomes.add(income);
            showConfirmation("Income added successfully!");
        });
    }

    private void showSummary() {
        contentArea.getChildren().clear();
        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double balance = totalIncome - totalExpense;

        Label title = new Label("Financial Summary");
        Label summaryLabel = new Label("Total Income: MWK " + totalIncome +
                "\nTotal Expense: MWK " + totalExpense +
                "\nNet Balance: MWK " + balance);

        title.getStyleClass().add("title-label");
        summaryLabel.getStyleClass().add("summary-label");

        contentArea.getChildren().addAll(title, summaryLabel);
    }

    private void showMonthlyBarChart() {
        contentArea.getChildren().clear();

        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        xAxis.setLabel("Type");
        yAxis.setLabel("Amount");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Income", totalIncome));
        series.getData().add(new XYChart.Data<>("Expenses", totalExpense));

        barChart.getData().add(series);
        barChart.setTitle("Monthly Report");

        contentArea.getChildren().add(barChart);
    }

    private void showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

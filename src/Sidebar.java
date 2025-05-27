import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {

    public Button btnAddExpense = new Button("Add Expense");
    public Button btnAddIncome = new Button("Add Income");
    public Button btnViewSummary = new Button("View Summary");
    public Button btnMonthlyReport = new Button("Monthly Report");

    public Sidebar() {
        setPadding(new Insets(20));
        setSpacing(15);

        btnAddExpense.setMaxWidth(Double.MAX_VALUE);
        btnAddIncome.setMaxWidth(Double.MAX_VALUE);
        btnViewSummary.setMaxWidth(Double.MAX_VALUE);
        btnMonthlyReport.setMaxWidth(Double.MAX_VALUE);

        getChildren().addAll(btnAddExpense, btnAddIncome, btnViewSummary, btnMonthlyReport);
    }
}

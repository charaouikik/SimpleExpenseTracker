package ExpenseTracker;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
public class LabelTotaler extends Label { //Extending label was the only way I found to make this work
    private TableView<Product> table;
    public LabelTotaler(TableView<Product> table) {
        super();//Calling label no-arg construct, good example of constructer chaining
        this.table=table;
        updateTotal();//Call update total method
        this.table.getItems().addListener((ListChangeListener<Product>) change -> updateTotal());
        //Whenever the list changes, so does the label, hence the listener
    }
    public void updateTotal() {
        double total=table.getItems().stream().mapToDouble(Product::getPrice).sum(); //Adds list items together after mapping them from thier products
        this.setText(String.format("Total Expenses: $%.2f", total));
        //Adds .2f formatting, calls the constructers and updates the label's shown value
    }



}

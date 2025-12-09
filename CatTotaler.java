package ExpenseTracker;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableView;
import java.util.HashMap;
import java.util.Map;
public class CatTotaler {
    private TableView<Product> table;
    private PieChart piechart;
    public Map<String,Double> map;
    public CatTotaler(TableView<Product> table) {
        this.table=table;
        this.piechart=new  PieChart();
        this.map=new HashMap<>();
        map.put("Food", 0.0);
        map.put("Clothes", 0.0);
        map.put("Electronics", 0.0);
        map.put("Bills", 0.0);
        map.put("Other", 0.0);
        //Hashmap made the most sence for the catagory total, easiest to handle
        updateTotal();
        table.getItems().addListener((ListChangeListener<Product>) change -> updateTotal());//Same logic from label totaler, use listener
    }
    //Reset piechart totals, then add em up again
    public void updateTotal() {
        map.replaceAll((String,Double)->0.0);
        for (Product p:table.getItems()) {
            String cat=p.getCategory();
            double price=p.getPrice();
            map.put(cat,map.get(cat)+price);
        }
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (String cat : map.keySet()) {
            double total = map.get(cat);
            if (total > 0) { // only show non-zero categories
                pieData.add(new PieChart.Data(cat + " $" + total, total));
            }
        }
        piechart.setData(pieData);
        piechart.setTitle("Expenses by Category");
    }
    public PieChart getPiechart() {
        return piechart;
    }
    public Map<String, Double> getExpenseperCat() {
        return map;
    }
}

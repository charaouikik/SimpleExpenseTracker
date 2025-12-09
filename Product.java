package ExpenseTracker;
import java.time.LocalDate;
// Class for Product Objects
public class Product {
    private String name;
    private double price;
    private String category;
    private LocalDate date;
    public Product(String name,double price,String category, LocalDate date) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.date=date;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public String getCategory() {
        return category;
    }
    public LocalDate getDate() {
        return date;
    }
    @Override
    public String toString() {
        return name+" $"+price;
    }
}

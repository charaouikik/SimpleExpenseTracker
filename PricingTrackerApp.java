package ExpenseTracker;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.logging.FileHandler;
import java.util.List;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
public class PricingTrackerApp extends Application {
    boolean nameflag=true;
    boolean priceflag=true;
    private FLHandler filehandler;
    public TableView<Product> table;
    public void start(Stage stage) {
        //Initilizae IO, tableview, and datepicker
        filehandler = new FLHandler("products.txt"); // handles file I/O
        table=new TableView<>();
        DatePicker datePicker = new DatePicker(LocalDate.now());
        //Tableview Configs
        TableColumn<Product, String> nameCol=new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product, Double> priceCol=new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Product, Integer> catCol=new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<Product, LocalDate> dateCol=new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        //Add Columns, Resize
        nameCol.setMinWidth(250);
        priceCol.setMinWidth(250);
        catCol.setMinWidth(250);
        dateCol.setMinWidth(250);
        table.getColumns().addAll(nameCol,priceCol,catCol,dateCol);
        //Load File
        table.getItems().addAll(filehandler.loadProducts());
        //TextFields
        TextField nameField=new TextField();
        nameField.setPromptText("Product name");
        TextField priceField=new TextField();
        priceField.setPromptText("Price");//Adds price and product Textfields to the GUI
        //Add catagory combobox
        ComboBox<String> catBox = new ComboBox<>();
        catBox.getItems().addAll("Food", "Clothes", "Electronics", "Bills", "Other");
        catBox.setPromptText("Select category");
        //Then, add label for total expenses, and call totaler method
        LabelTotaler totallabel=new LabelTotaler(table);
        //PieChart and Expense Totaler
        CatTotaler catrack=new CatTotaler(table);
        //Start adding buttons here
        Button addButton = new Button("Add");
        //Add prices/products functionality
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String priceText = priceField.getText();
            LocalDate date = datePicker.getValue();
            if (name.isEmpty() || priceText.isEmpty()||date==null) return; //Checks if thier empty cells
            try {
                double price = Double.parseDouble(priceText);//tru-catch for validation of input
                if (price<0) {
                    Warning("Price has to be a positive number.");
                    return;
                }
                //retrieve cat from combobox, if no catagory then return an eror
                String cat = catBox.getValue();
                if (cat==null) {
                    Warning("Select a category");
                    return;
                }
                //Add product and autosave
                Product p = new Product(name, price, cat, date);
                table.getItems().add(p);
                filehandler.saveProducts(table.getItems());//Add product to table and save
            }
            catch (NumberFormatException ex) {
                Warning("Price format error");
                return;
            }
            nameField.clear();
            priceField.clear();
            datePicker.setValue(LocalDate.now());
        });
        Button deleteButton = new Button("Delete");//Delete Button,
        deleteButton.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;//Select Item to get deleted
            table.getItems().remove(selected);
            filehandler.saveProducts(table.getItems());//Save the new file
        });
        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();//Same logic as prior
            if (selected == null) return;
            nameField.setText(selected.getName());
            priceField.setText(Double.toString(selected.getPrice()));//Setting Value to desired
            table.getItems().remove(selected);
        });
        Button sortPrice=new Button("Sort by Price");
        sortPrice.setOnAction(e -> {
            if (priceflag) {
                table.getItems().sort(Comparator.comparingDouble(Product::getPrice));
                sortPrice.setText("Sort Price ↓");
            }
            else {
                table.getItems().sort(Comparator.comparingDouble(Product::getPrice).reversed());
                sortPrice.setText("Sort Price ↑");
            }
            priceflag = !priceflag;//Use a boolean and flip each time, to get the whole acending/decending
        });
        Button sortName=new Button("Sort by Name");
        sortName.setOnAction(e -> {
                if (nameflag) {
                    table.getItems().sort(Comparator.comparing(Product::getName));
                    sortName.setText("Sort Name ↓");
                } else {
                    table.getItems().sort(Comparator.comparing(Product::getName).reversed());
                    sortName.setText("Sort Name ↑");
                }
                nameflag = !nameflag;
        });
        HBox sortRow=new HBox(10,sortName,sortPrice,catBox);
        HBox buttonRow = new HBox(10,addButton,editButton,deleteButton);
        VBox mainpane = new VBox(10,table,nameField,priceField,buttonRow,sortRow,totallabel,datePicker);
        VBox visualpane=new VBox(10,catrack.getPiechart());
        HBox layout=new HBox(10,mainpane,visualpane);
        //Infinite Vertical Space
        VBox.setVgrow(table, Priority.ALWAYS);
        Scene scene = new Scene(layout,1400,1000);
        stage.setScene(scene);
        stage.setTitle("Price Tracker");
        stage.show();
        //Autosave on close
        stage.setOnCloseRequest(e -> {filehandler.saveProducts(table.getItems());});
    }
    //Custom warning class for convenience purposing
    private void Warning(String alrt) {
        Alert alert = new Alert(Alert.AlertType.ERROR, alrt);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}


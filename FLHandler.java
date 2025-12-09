package ExpenseTracker;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
public class FLHandler {
    private File file;

    public FLHandler(String filename) { //Constructer to intialize file
        this.file = new File(filename);
    }

    public List<Product> loadProducts() {
        List<Product> loadproducts = new ArrayList<>();
        //The below reads off the file in two parts, product and price
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String parts[] = line.split(",");//Split apart both on the file by ','
                if (parts.length != 4) { //Checks to make sure the read lines are in exactly four
                    System.out.println("Invalid Line, Skipping.");
                    continue;
                }
                try {
                    //Parse input from file, one by one
                    String name = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    String cat = parts[2];
                    //Check for 'null' dates which cant be handled the same way other can-this was tricky lowk had to search it up
                    LocalDate date = null;
                    if (!parts[3].equalsIgnoreCase("null") && !parts[3].isEmpty()) {
                        try {
                            date = LocalDate.parse(parts[3]);
                        } catch (Exception e) {
                            System.out.println("Invalid date, using today: " + line);
                            date = LocalDate.now();
                        }
                    }
                    loadproducts.add(new Product(name, price, cat, date)); //Tries to parse, each product var sperate, try in order to avoid misreading and adding invalid
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price in line, skipping: " + line);
                }
            }
        }
        //Catches if there's no file
        catch (FileNotFoundException e) {         // If file missing or not there, we simply make a new one and write to it
            System.out.println("File not found, starting new file.");
        }
        return loadproducts;
    }

    public void saveProducts(List<Product> products) { //Simple file writer method
        try (PrintWriter pw = new PrintWriter(file)) {
            for (Product product : products) {
                //Write directly to file, use methods from Product to doso
                pw.println(product.getName() + "," + product.getPrice() + "," + product.getCategory() + "," + product.getDate());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

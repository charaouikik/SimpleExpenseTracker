package ExpenseTracker;
import java.io.*;
import java.util.*;
public class FLHandler {
    private File file;
    public FLHandler(String filename) { //Constructer to intialize file
        this.file=new File(filename);
    }
    public List<Product> loadProducts(){
        List<Product> loadproducts=new ArrayList<>();
        //The below reads off the file in two parts, product and price
        try (Scanner scanner=new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line=scanner.nextLine();
                String parts[]=line.split(",");//Split apart both on the file by ','
                if (parts.length!=2) { //Checks to make sure the read lines are in exactly two
                    System.out.println("Invalid Line, Skipping.");
                    continue;
                }
                try {
                    loadproducts.add(new Product(parts[0], Double.parseDouble(parts[1]))); //Tries to parse, try in order to avoid misreading and adding invalid
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
                pw.println(product.getName() + "," + product.getPrice());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

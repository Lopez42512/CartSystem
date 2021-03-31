import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class MainEntryPoint {
    public static Scanner reader = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        Item item2 = new Item();
        item2.setQuantity(10);
        System.out.println(item2.getQuantity());
        AppSystem app = new AppSystem();
        CartSystem cart = new CartSystem();

        Integer choice = 0;
        while (choice != 7) {
            menu();
            choice = reader.nextInt();
            switch (choice) {
            case 1:
                Item item = new Item();
                reader.nextLine();
                System.out.print("\nEnter the item name:\n");
                item.setItemName(reader.nextLine());
                System.out.print("\nEnter a description for the item:\n");
                item.setItemDesc(reader.nextLine());
                System.out.print("\nEnter the item's price:\n");
                item.setItemPrice(reader.nextDouble());
                System.out.print("\nEnter the quantity available in the System:\n");
                item.setAvailableQuantity(reader.nextInt());
                System.out.println(item.getItemName());
                if (app.add(item)) {
                    System.out.println("Item successfully added");
                } else {
                    System.out.println("Try Again");
                }
                break;
            case 2:
                app.display();
                System.out.println("Enter the name of the item");
                reader.nextLine();
                String item_name = reader.nextLine();
                Item item1 = app.getItemCollection().get(item_name);
                if (app.checkAvailability(item1))
                    if (cart.add(item1)) {
                        app.reduceAvailableQuantity(item_name);
                        System.out.println("Item successfully added");
                    } else {
                        System.out.println("Invalid or Unavailable Item, Please Try Again");
                    }
                ;
                break;
            case 3:
                cart.display();
                break;
            case 4:
                app.display();
                break;
            case 5:
                cart.display();
                System.out.println("Enter the name of the item");
                reader.nextLine();
                item_name = reader.nextLine();
                if (cart.remove(item_name) != null) {
                    System.out.println(item_name + " was removed from the cart");
                } else {
                    System.out.println("Invalid Item, Please Try Again");
                }
                break;
            case 6:
                app.display();
                System.out.println("Enter the name of the item");
                reader.nextLine();
                item_name = reader.nextLine();
                if (app.remove(item_name) != null) {

                    System.out.println(item_name + " was removed from the System");
                    if (cart.remove(item_name) != null) {
                        System.out.println(item_name + " was also removed from the cart");
                    }
                } else {
                    System.out.println("Invalid Item, Please Try Again");
                }

                break;
            case 7:
                System.out.println("\nByyyeee!!");
                break;
            }
        }
        reader.close();
    }

    public static void menu() {
        System.out.println("Choose an action:");
        System.out.println("1. Add item to System");
        System.out.println("2. Add item to Cart");
        System.out.println("3. Display Cart");
        System.out.println("4. Display System");
        System.out.println("5. Remove item from Cart");
        System.out.println("6. Remove item from System");
        System.out.println("7. Quit");
    }
}

abstract class TheSystem {
    HashMap<String, Item> itemCollection;

    TheSystem() {
        // Your code here
        itemCollection = new HashMap<String, Item>();
        if (getClass().getSimpleName().equals("AppSystem")) {
            try {
                File myObj = new File("sample.txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    String[] itemInfo = data.split("\\s ");
                    Item item = new Item();
                    item.setItemName(itemInfo[0]);
                    item.setItemDesc(itemInfo[1]);
                    item.setItemPrice(Double.parseDouble(itemInfo[2]));
                    item.setAvailableQuantity(Integer.parseInt(itemInfo[3]));
                    itemCollection.put(itemInfo[0], item);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    public HashMap<String, Item> getItemCollection() {
        // Your code here
        return itemCollection;
    }

    public Boolean checkAvailability(Item item) {
        // Your code here
        if (item.getQuantity() >= item.getAvailableQuantity()) {
            System.out.println("System is unable to add " + item.getItemName() + " to the card. System only has "
                    + item.getAvailableQuantity() + " " + item.getItemName() + "s.");
            return false;
        } else {
            return true;
        }
    }

    public Boolean add(Item item) {
        // Your code here
        if (item == null) {
            return false;
        } else if (itemCollection.get(item.getItemName()) != null && checkAvailability(item)) {

            int addOne = itemCollection.get(item.getItemName()).getQuantity();
            addOne++;
            itemCollection.get(item.getItemName()).setQuantity(addOne);
            if (item.getAvailableQuantity() == 0) {
                remove(item.getItemName());
            }
            return true;
        } else if (itemCollection.get(item.getItemName()) == null) {
            itemCollection.put(item.getItemName(), item);
            if (item.getAvailableQuantity() == 0) {
                remove(item.getItemName());
            }
            return true;
        } else {
            return false;
        }
    }

    public Item remove(String itemName) {
        // Your code here
        if (itemCollection.get(itemName) != null) {
            return itemCollection.remove(itemName);
        } else {
            return null;
        }
    }

    public abstract void display();

}

class AppSystem extends TheSystem {
    AppSystem() {
    }

    @Override
    public void display() {
        // Your code here
        System.out.println("AppSystem Inventory:");
        System.out.format("%-20s %-20s %-10s %-10s", "Name", "Description", "Price", "Available Quantity");
        System.out.println();
        for (String key : itemCollection.keySet()) {
            String name = itemCollection.get(key).getItemName() + " ";
            String description = itemCollection.get(key).getItemDesc() + " ";
            double price = itemCollection.get(key).getItemPrice();
            int quantity = itemCollection.get(key).getAvailableQuantity();
            System.out.format("%-20s %-20s %-10.2f %-10d", name, description, price, quantity);
            System.out.println();
        }
    }

    @Override // this overwrites the parents class add method
    public Boolean add(Item item) {
        // Your code here
        if (item == null) {
            return false;
        } else if (itemCollection.get(item.getItemName()) != null) {
            System.out.println(item.getItemName() + " is already in the App System");
            return false;
        } else {
            itemCollection.put(item.getItemName(), item);
            return true;
        }
    }

    public Item reduceAvailableQuantity(String item_name) {
        // Your code here
        if (itemCollection.get(item_name) != null) {
            int aq = itemCollection.get(item_name).getAvailableQuantity();
            aq--;
            if (aq == 0) {
                remove(item_name);
            } else {
                itemCollection.get(item_name).setAvailableQuantity(aq);
            }
            return itemCollection.get(item_name);

        } else {
            return null;
        }
    }

}

class Item {
    private String itemName;
    private String itemDesc;
    private Double itemPrice;
    private int quantity;
    private int availableQuantity;

    public Item() {
        // Your code here
        this.quantity = 1;
    }

    public Item(String itemName, String itemDesc, Double itemPrice, Integer availableQuantity) {
        // Your code here
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemPrice = itemPrice;
        this.availableQuantity = availableQuantity;
        this.quantity = 1;
    }

    public void setItemName(String setItemName) {
        this.itemName = setItemName;
    }

    public void setItemDesc(String setItemDesc) {
        this.itemDesc = setItemDesc;
    }

    public void setItemPrice(Double setItemPrice) {
        this.itemPrice = setItemPrice;
    }

    public void setAvailableQuantity(Integer setAvailableQuantity) {
        this.availableQuantity = setAvailableQuantity;
    }

    public void setQuantity(Integer setQuantity) {
        this.quantity = setQuantity;
    }

    public String getItemName() {
        return this.itemName;
    }

    public String getItemDesc() {
        return this.itemDesc;
    }

    public Double getItemPrice() {
        return this.itemPrice;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Integer getAvailableQuantity() {
        return this.availableQuantity;
    }
}

class CartSystem extends TheSystem {
    CartSystem() {
    }

    @Override
    public void display() {

        // Your code here
        final double taxes = .05;
        double preTaxTotal = 0.0;
        System.out.println("Cart:");
        System.out.format("%-20s %-20s %-10s %-10s %-10s%n", "Name", "Description", "Price", "Quantity", "Sub Total");
        for (String key : itemCollection.keySet()) {
            String name = itemCollection.get(key).getItemName() + " ";
            String description = itemCollection.get(key).getItemDesc() + " ";
            double price = itemCollection.get(key).getItemPrice();
            double total = itemCollection.get(key).getQuantity() * itemCollection.get(key).getItemPrice();
            int quantity = itemCollection.get(key).getQuantity();
            System.out.format("%-20s %-20s %-10.2f %-10d %-10.2f%n", name, description, price, quantity, total);
            System.out.println();
            preTaxTotal += total;
        }
        double cartTaxes = Math.floor(preTaxTotal * taxes * 100) / 100;
        double cartTotal = cartTaxes + preTaxTotal;
        System.out.format("%-20s %-20.2f%n%-20s %-20.2f%n%-20s %-20.2f%n", "Pre-tax Total", preTaxTotal, "Tax",
                cartTaxes, "Total", cartTotal);

    }
}
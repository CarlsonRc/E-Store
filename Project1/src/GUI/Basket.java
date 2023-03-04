package GUI;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Basket {
    private checkoutOptions checkoutOptions;
    private ArrayList<Inventory> inventory;
    private int maxArraySize = 0;
    Date date = Calendar.getInstance().getTime();
    SimpleDateFormat permutation = new SimpleDateFormat("yyMMddyyHHmm");
    public double subtotal = 0.00;

    private String [] itemIdArray;
    private String [] itemNameArray;
    private String [] itemInStockArray;
    private double [] itemPriceArray;
    private int [] itemQuantityArray;
    private int [] itemDiscountArray;
    private double [] itemSubtotalArray;

    final static double TAX_RATE = 0.060;
    final static double DISCOUNT_05 = 0.10;
    final static double DISCOUNT_10 = 0.15;
    final static double DISCOUNT_15 = 0.20;

    private String filename = "Transactions.txt";
    File file = new File(filename);

    // initiate memory for arrays to be filled
    public void createArrays() {
        maxArraySize = 10;
        if (checkoutOptions.itemCount == 0) {
            itemIdArray = new String[maxArraySize];
            itemNameArray = new String[maxArraySize];
            itemInStockArray = new String[maxArraySize];
            itemPriceArray = new double[maxArraySize];
            itemQuantityArray = new int[maxArraySize];
            itemDiscountArray = new int[maxArraySize];
            itemSubtotalArray = new double[maxArraySize];
        }
    }

    // fill arrays with items selected by user
    public void placeItemsInBasket(String itemId, String itemName, String itemInStock, double itemPrice, int itemQuantity , int itemDiscount, double itemSubtotal) {
        itemIdArray[checkoutOptions.itemCount] = itemId;
        itemNameArray[checkoutOptions.itemCount] = itemName;
        itemInStockArray[checkoutOptions.itemCount] = itemInStock;
        itemPriceArray[checkoutOptions.itemCount] = itemPrice;
        itemQuantityArray[checkoutOptions.itemCount] = itemQuantity;
        itemDiscountArray[checkoutOptions.itemCount] = itemDiscount;
        itemSubtotalArray[checkoutOptions.itemCount] = itemSubtotal;
    }

    // find the correct discount for given quantity
    public int discountPercent(int quantity) {
        if (quantity >= 1 && quantity <=4)
            return 0;
        if (quantity >=5 && quantity <=9)
            return 5;
        if (quantity >=10 && quantity <=14)
            return 10;
        if (quantity >=15)
            return 15;
        return 0;
    }

    // applying the quantity discount to the subtotal
    public double itemSubtotal(int quantity, double itemPrice) {

        double originalPrice = quantity * itemPrice;

        if (quantity >= 1 && quantity <=4)
            return originalPrice;
        if (quantity >=5 && quantity <=9)
            return originalPrice - (DISCOUNT_05 * (quantity * itemPrice));
        if (quantity >=10 && quantity <=14)
            return originalPrice - (DISCOUNT_10 * (quantity * itemPrice));
        if (quantity >=15)
            return originalPrice - (DISCOUNT_15 * (quantity * itemPrice));

        return 0.00;
    }

    // pull elements from arrays in basket class to be displayed
    public Object viewOrder() {
        StringBuilder order = new StringBuilder();
        for (int i = 0; i < checkoutOptions.itemCount; i++) {
            int itemNumber = i+1;
            order.append(itemNumber + ". " + itemIdArray[i] + " |" + itemNameArray[i] + " | $" + itemPriceArray[i] + " | " + discountPercent(itemQuantityArray[i]) + "% |" + " $" + itemSubtotalArray[i] + "\n");
            //order = viewOrder;
        }
        return order;
    }

    // create finalized order invoice for user
    public void finishOrder() {
        StringBuilder finish = new StringBuilder();


        double taxAmount = 0.00;
        taxAmount = subtotal * TAX_RATE;

        finish.append("Date: "+ date);
        finish.append(System.getProperty("line.separator"));// new line
        finish.append(System.getProperty("line.separator"));// new line
        finish.append("Number of line items: " + checkoutOptions.itemCount);
        finish.append(System.getProperty("line.separator"));// new line
        finish.append(System.getProperty("line.separator"));// new line
        finish.append("Item# / ID / Title / Price / Qty / Disc % / Subtotal:");
        finish.append(String.format(System.getProperty("line.separator")));// new line
        finish.append(System.getProperty("line.separator"));// new line
        finish.append(viewOrder());
        finish.append(System.getProperty("line.separator"));// new line
        finish.append(System.getProperty("line.separator"));// new line
        finish.append("Order subtotal:  $" + new DecimalFormat("#0.00").format(subtotal));
        finish.append(System.getProperty("line.separator"));// new line
        finish.append(System.getProperty("line.separator"));// new line
        finish.append("Tax rate:           6%");
        finish.append(System.getProperty("line.separator"));// new line
        finish.append(System.getProperty("line.separator"));// new line
        finish.append("Tax amount:     $" + new DecimalFormat("#0.00").format(taxAmount));
        finish.append(System.getProperty("line.separator"));// new line
        finish.append(System.getProperty("line.separator"));// new line
        finish.append("Order total:       $" + new DecimalFormat("#0.00").format(subtotal + taxAmount));
        finish.append(System.getProperty("line.separator"));// new line
        finish.append(System.getProperty("line.separator"));// new line
        finish.append("Thanks for shopping at HAYstack");

        JOptionPane.showMessageDialog(null, finish, "HAYstack - Final Invoice", JOptionPane.PLAIN_MESSAGE);
    }

    // print final order to transaction.txt with unique ID's per order
    public void printTransaction() throws IOException {

        // create transaction file if not already made
        if (file.exists() == false) {
            file.createNewFile();
        }

        PrintWriter writer = new PrintWriter(new FileWriter(filename, true));

        for (int i = 0; i < checkoutOptions.itemCount; i++) {
            writer.append(permutation.format(date));
            writer.append("| " + itemIdArray[i] + " |" + itemNameArray[i] + " | $" + itemPriceArray[i] + " | " + discountPercent(itemQuantityArray[i]) + "% |" + " $" + itemSubtotalArray[i]);
            writer.append(" | " + date + "\n");
        }
        writer.flush();
        writer.close();
    }

}

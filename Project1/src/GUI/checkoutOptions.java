package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class checkoutOptions extends JFrame {

    private ArrayList<Inventory>inventory;
    private Basket basket = new Basket();
    static int itemCount = 0;
    public double subtotal = 0.00;
    public double itemSubtotal = 0.00;

    // create buttons
    JButton b1;
    JButton b2;
    JButton b3;
    JButton b4;
    JButton b5;
    JButton b6;

    // create text fields
    JTextField idField;
    JTextField quantityField;
    JTextField detailsField;
    JTextField subtotalField;
    JTextField blankField; // Used to clear text field

    // create labels for fields
    JLabel idLabel = new JLabel("Enter item ID for item #"+ (itemCount+1)+ ":");
    JLabel quantityLabel = new JLabel("Enter quantity for item #"+ (itemCount+1)+ ":");
    JLabel detailsLabel = new JLabel("Details for item #"+ (itemCount+1)+ ":");
    JLabel subtotalLabel = new JLabel("Oder subtotal for "+ (itemCount+1) +" item(s):");

    public checkoutOptions(String title) throws FileNotFoundException {
        super(title);

        // creat container to hold JPanels
        Container c = getContentPane();

        // load inventory into arrays
        this.loadInventory();

        // create JPanel for fields and labels
        JPanel fieldsAndLabels = new JPanel(new GridBagLayout());

        // set constraints to organize GridBagLayout
        GridBagConstraints gc = new GridBagConstraints();



        // create text fields for user
        idField = new JTextField(10);// height of textFields
        idField.setColumns(40);// width of textFields
        quantityField = new JTextField(10);
        quantityField.setColumns(40);
        detailsField = new JTextField(10);
        detailsField.setColumns(40);
        subtotalField = new JTextField(10);
        subtotalField.setColumns(40);
        blankField = new JTextField(10);
        blankField.setColumns(40);



        //set the space allocated to cell
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        // set the location of first column
        gc.gridx = 0;
        gc.gridy = 0;
        fieldsAndLabels.add(idLabel, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        fieldsAndLabels.add(quantityLabel, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        fieldsAndLabels.add(detailsLabel, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        fieldsAndLabels.add(subtotalLabel, gc);

        // Second column
        gc.gridx = 1;
        gc.gridy = 0;
        fieldsAndLabels.add(idField, gc);

        gc.gridx = 1;
        gc.gridy = 1;
        fieldsAndLabels.add(quantityField, gc);

        gc.gridx = 1;
        gc.gridy = 2;
        fieldsAndLabels.add(detailsField, gc);

        gc.gridx = 1;
        gc.gridy = 3;
        fieldsAndLabels.add(subtotalField, gc);

        idField.setEnabled(true);
        quantityField.setEnabled(true);
        detailsField.setEditable(false);
        subtotalField.setEditable(false);

        // create buttons
        b1 = new JButton("Process Item #"+ (itemCount+1));
        b2 = new JButton("Confirm Item #"+ (itemCount+1));
        b3 = new JButton("View Order");
        b4 = new JButton("Finish Order");
        b5 = new JButton("New Order");
        b6 = new JButton("Exit");

        // add buttons to panel with a horizontal layout
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(b1);
        buttons.add(b2);
        buttons.add(b3);
        buttons.add(b4);
        buttons.add(b5);
        buttons.add(b6);
        b1.setEnabled(true);
        b2.setEnabled(false);
        b3.setEnabled(false);
        b4.setEnabled(false );
        b5.setEnabled(true);
        b6.setEnabled(true);

        c.add(fieldsAndLabels, BorderLayout.CENTER);
        c.add(buttons, BorderLayout.SOUTH);

        //add button behavior

        // Close on Exit action
        b6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // create new JFrame for new order
        b5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // close and load fresh JFrame & Arrays
                itemCount = 0;
                // need someway to clear arrays in basket
                checkoutOptions.super.dispose();
                App.main(null);
            }
        });

        // finalize order and print unique receipt for company
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                basket.finishOrder();
                try {
                    basket.printTransaction();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.exit(0);
            }
        });

        // view current items within basket arrays
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, basket.viewOrder(), "HAYstack - Current Basket Status", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // update labels , buttons & subtotal
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //increment item count
                itemCount++;

                // update labels
                idLabel.setText("Enter item ID for item #"+ (itemCount+1)+ ":");
                quantityLabel.setText("Enter quantity for item #"+ (itemCount+1)+ ":");
                detailsLabel.setText("Details for item #"+ (itemCount+1)+ ":");
                subtotalLabel.setText("Oder subtotal for "+ (itemCount+1) +" item(s):");

                // update buttons
                b1.setText("Process Item #"+ (itemCount+1));
                b2.setText("Confirm Item #"+ (itemCount+1));

                // add last items total to final total
                subtotal += itemSubtotal;
                basket.subtotal = subtotal;
                subtotalField.setText(String.valueOf(new DecimalFormat("#0.00").format(subtotal)));
                JOptionPane.showMessageDialog(null, "Item #"+ itemCount + " accepted. Added to your basket." ,"HAYstack - Item Confirmed", JOptionPane.PLAIN_MESSAGE);

                idField.setText("");
                quantityField.setText("");

                b1.setEnabled(true);
                b2.setEnabled(false);
                b3.setEnabled(true);
                b4.setEnabled(true);
            }
        });

        // create arrays, search for items in inventory, add items if matched to basket arrays, create err messages if user enters incorrect data
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // initialize arrays to store items
                basket.createArrays();

                // Grab item ID & quantity of items from user
                String itemID = idField.getText();// pass user input from text field to itemID variable
                int itemQuantity = Integer.parseInt(quantityField.getText());// pass user input from text field into itemQuantity variable

                int itemIndex = inventorySearch(itemID);

                // if item is valid add to basket, update buttons & details field
                if (itemIndex != -1) {
                    Inventory itemFound = inventory.get(Integer.parseInt(String.valueOf(itemIndex)));
                    itemSubtotal = basket.itemSubtotal(itemQuantity, itemFound.getItemPrice());


                    basket.placeItemsInBasket(itemFound.getItemId(),itemFound.getItemName(), itemFound.getInStock(), itemFound.getItemPrice(), itemQuantity, basket.discountPercent(itemQuantity), basket.itemSubtotal(itemQuantity, itemFound.getItemPrice()));
                    String itemDetails;
                    itemDetails = itemFound.getItemId()+ " |" + itemFound.getItemName() + " | $" + itemFound.getItemPrice() + " | " + basket.discountPercent(itemQuantity) + "% |" + " $" + basket.itemSubtotal(itemQuantity, itemFound.getItemPrice());
                    detailsField.setText(itemDetails);
                    b2.setEnabled(true);
                    b1.setEnabled(false);

                    // if item is not in stock send message to user, reset fields & buttons
                    if (itemFound.getInStock().equals(" false")) {
                        JOptionPane.showMessageDialog(null, "Sorry, this item is out of stock, please try another item", "HAYstack - ERROR", JOptionPane.ERROR_MESSAGE);
                        idField.setText("");
                        quantityField.setText("");
                        detailsField.setText("");
                        b1.setEnabled(true);
                        b2.setEnabled(false);
                    }
                    // if user enters a negative quantity send message to user, reset fields & buttons
                    if (itemQuantity < 1 && itemFound.getInStock().equals(" true")) {
                        JOptionPane.showMessageDialog(null, "Please enter positive numbers for number of items or item's", "HAYstack - ERROR", JOptionPane.ERROR_MESSAGE);
                        quantityField.setText("");
                        detailsField.setText("");
                        b1.setEnabled(true);
                        b2.setEnabled(false);
                    }
                }
                // if item requested is not found send message to user, reset fields
                else {
                    JOptionPane.showMessageDialog(null, "Item ID " + idField.getText() + " not found", "HAYstack - ERROR", JOptionPane.ERROR_MESSAGE);
                    idField.setText("");
                    quantityField.setText("");

                }
            }
        });

    }

    public int inventorySearch(String itemId) {
        for(int i = 0; i < this.inventory.size(); i++) {
            Inventory currentBook = inventory.get(i);
            if(currentBook.getItemId().equals(itemId))
                return i;
        }
        return -1;
    }

    public void loadInventory() throws FileNotFoundException {

        this.inventory = new ArrayList<Inventory>();

        File file = new File("inventory.txt");
        Scanner scanner = new Scanner(file);

        // while loop to split csv file
        while (scanner.hasNextLine()) {
            String item = scanner.nextLine();
            // strings to store separated data by comma
            String[] inventoryProperties = item.split(",");

            Inventory thisItem = new Inventory();

            thisItem.setItemId(inventoryProperties[0]);
            thisItem.setItemName(inventoryProperties[1]);
            thisItem.setInStock(inventoryProperties[2]);
            thisItem.setItemPrice(Double.parseDouble(inventoryProperties[3]));
            inventory.add(thisItem);
        }
        scanner.close();

    }
}


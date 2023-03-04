package GUI;

import javax.swing.*;
import java.io.FileNotFoundException;

/*  Name: Ryan Carlson
    Course: CNT4714 - Spring 2022
    Assignment title: project 1 - Event driven Enterprise Simulation
    Date: January 18, 2022
 */

public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = null;
                try {
                    frame = new checkoutOptions("HAYstack");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                frame.setSize(700, 200);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

            }
        });

    }

}

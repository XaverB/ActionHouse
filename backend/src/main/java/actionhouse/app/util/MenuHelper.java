package actionhouse.app.util;

import actionhouse.app.enums.CustomerMenu;
import actionhouse.app.enums.Menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MenuHelper {
    private static int readInteger(){
        int i = 0;
        boolean valid = false;
        do {
            System.out.print("Select menu: ");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String inputLine = reader.readLine();
                i = Integer.parseInt(inputLine);
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer.");
            } catch (IOException e) {
                System.out.println("☠️ Error reading input. Please try again. ☠️, " + e.getMessage());
            }
        } while (!valid);
        return i;
    }

    public static CustomerMenu readCustomerMenu() {
        printCustomerMenu();
        int i = readInteger();
        return CustomerMenu.values()[i];
    }

    /**
     * Prints the string s on the console and then reads an integer from the console.
     * If the input is not an integer, the method will print an error message and ask again.
     */
    public static Menu readMenu() {
        printMenu();
        int i = readInteger();
        return Menu.values()[i];
    }

    public static  void printMenu() {
        System.out.println("1. Customer");
        System.out.println("2. Article");
        System.out.println("3. Bids");
        System.out.println("4. Exit");
    }

    public static void printCustomerMenu() {
        System.out.println("1. Create Customer");
        System.out.println("2. Update Customer");
        System.out.println("3. Delete Customer");
        System.out.println("4. Show Customer");
        System.out.println("5. Show Customers");
        System.out.println("6. Exit");
    }

    public static String promptFor(BufferedReader in, String p) {
        System.out.print(p + "> ");
        System.out.flush();
        try {
            return in.readLine();
        } catch (Exception e) {
            return promptFor(in, p);
        }
    }
}

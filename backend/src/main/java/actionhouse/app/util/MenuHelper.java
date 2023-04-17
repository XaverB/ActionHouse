package actionhouse.app.util;

import actionhouse.app.enums.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MenuHelper {
    /**
     * Reads an integer from the console and returns the corresponding Menu.
     */
    public static CustomerMenu readCustomerMenu() {
        printCustomerMenu();
        int i = readInteger();
        return CustomerMenu.values()[i];
    }

    /**
     * Reads an integer from the console and returns the corresponding ArticleMenu.
     */
    public static ArticleMenu readArticleMenu() {
        printArticleMenu();
        int i = readInteger();
        return ArticleMenu.values()[i];
    }

    /**
     * Reads an integer from the console and returns the corresponding BidMenu.
     */
    public static BidMenu readBidMenu() {
        printBideMenu();
        int i = readInteger();
        return BidMenu.values()[i];
    }

    /**
     * Reads an integer from the console and returns the corresponding AnalyticsMenu.
     */
    public static AnalyticsMenu readAnalyticsMenu() {
        printAnalyticsMenu();
        int i = readInteger();
        return AnalyticsMenu.values()[i];
    }


    /**
     * Prints the menu on the console and then reads an integer from the console.
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
        System.out.println("4. Analytics");
        System.out.println("5. Exit");
    }

    public static void printCustomerMenu() {
        System.out.println("1. Create Customer");
        System.out.println("2. Update Customer");
        System.out.println("3. Delete Customer");
        System.out.println("4. Show Customer");
        System.out.println("5. Show Customers");
        System.out.println("6. Exit");
    }

    public static void printArticleMenu() {
        System.out.println("1. Create Article");
        System.out.println("2. Update Article");
        System.out.println("3. Delete Article");
        System.out.println("4. Show Article");
        System.out.println("5. Show Articles");
        System.out.println("6. Exit");
    }

    public static void printBideMenu() {
        System.out.println("1. Create Bid");
        System.out.println("2. Delete Bid");
        System.out.println("3. Exit");
    }

    public static void printAnalyticsMenu() {
        System.out.println("1. Find article by descirption");
        System.out.println("2. Get article price");
        System.out.println("3. Get top sellers");
        System.out.println("4. Get top articles");
        System.out.println("5. Exit");
    }

    /**
     * Reads a string from the console.
     */
    public static String promptFor(BufferedReader in, String p) {
        System.out.print(p + "> ");
        System.out.flush();
        try {
            return in.readLine();
        } catch (Exception e) {
            return promptFor(in, p);
        }
    }

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
}

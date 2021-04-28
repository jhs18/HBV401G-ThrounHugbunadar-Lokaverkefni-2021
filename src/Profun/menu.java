package Profun;
import java.util.*;

public class menu {
    public static void Menu() throws ClassNotFoundException {
        System.out.println("");
        System.out.println("---------------------MENU------------------------");
        System.out.println("");

        while (true) {
            Scanner myObj = new Scanner(System.in);
            System.out.print("Action(flight/customer): ");

            String inputMenu = myObj.nextLine();

            // Breytir öllu í lowercase því forritið er case sensitive
            inputMenu = inputMenu.toLowerCase();

            // MENU TRANSFER
            switch (inputMenu) {
                case "menu":
                    System.out.println("You are already in Menu");
                    myObj.close();
                    break;

                // SEARCH TRANSFER
                case "flight":
                    flightController.FlightController();
                    myObj.close();
                    break;

                case "quit":
                    myObj.close();
                    System.exit(0);

                    // CUSTOMER TRANSFER
                case "customer":
                    userController.Customer();
                    myObj.close();
                    break;
                default:
                    System.out.println("");
                    System.out.println("This location does not exist, for help type \"Help\" ");
                    System.out.println("");
                    break;
            }
        }
    }
}
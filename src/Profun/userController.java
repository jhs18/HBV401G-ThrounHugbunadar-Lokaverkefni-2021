package Profun;
import java.util.*;
import java.sql.*;

class userController {
    public static void Customer() throws ClassNotFoundException {
        System.out.println("");
        System.out.println("---------------------MENU -> CUSTOMER -------------------------");
        System.out.println("Here you can find information about your booking and account");
        System.out.println("To exit type \"quit\".");
        System.out.println("");
        customerStart();
    }

    public static void customerStart() throws ClassNotFoundException {
        Scanner myObj = new Scanner(System.in);
        System.out.print("Action(user/mybook/cancel): ");

        String inputCustomer = myObj.nextLine();
        inputCustomer = inputCustomer.toLowerCase();
        if(inputCustomer.equals("mybook")) {
            customerCheck();
        }
        else if(inputCustomer.equals("user")) {
            customerRun();
        }
        else if(inputCustomer.equals("quit")) {
            menu.Menu();
        }
        else if(inputCustomer.equals("cancel")) {
            System.out.println("You are on the cancellation page");
            Scanner myObj1 = new Scanner(System.in);
            System.out.print("Booking number: ");
            String cancelBookingNr = myObj1.nextLine();
            cancelBookingNr = cancelBookingNr.toLowerCase();

            System.out.print("Customer number: ");
            String cancelCustomerNr = myObj1.nextLine();
            cancelCustomerNr = cancelCustomerNr.toLowerCase();

            System.out.print("Are you sure? type \"yes\" to continue: ");
            String doubleCheck = myObj1.nextLine();
            doubleCheck = doubleCheck.toLowerCase();

            if(doubleCheck.equals("yes")) {
                customerCancel(cancelBookingNr,cancelCustomerNr);
                System.out.print("Flight cancelled.");
            }
            Customer();

        }
        else {
            System.out.println("Nothing matches " + inputCustomer + ", try again");
            customerStart();
        }
    }

    public static void customerRun() throws ClassNotFoundException {
        System.out.println("---------------------MENU -> CUSTOMER -> USER ------------------------");
        System.out.println("");
        Class.forName("org.sqlite.JDBC");
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + "src\\Profun\\verkefniDB.db");

            while(true) {
                Scanner myObj = new Scanner(System.in);
                System.out.print("Search(customerNr/name): ");

                String inputCustomer = myObj.nextLine();
                inputCustomer = inputCustomer.toLowerCase();

                if (inputCustomer.equals("quit")) {
                    Customer();
                    myObj.close();
                    break;
                }
                else {
                    statement = connection.prepareStatement("SELECT customerNR, fname, lname, nationality FROM Customers WHERE fname LIKE ? OR lname LIKE ? OR CustomerNr LIKE ?");
                    statement.setString(1, "%" + inputCustomer + "%");
                    statement.setString(2, "%" + inputCustomer + "%");
                    statement.setString(3, inputCustomer);
                    ResultSet r = statement.executeQuery();
                    System.out.println("Customer number, First name, Last name, Nationality");

                    // SKOÐAR HVORT SVARIÐ FRÁ GAGNASAFNINU SÉ TÓMT EÐA EKKI
                    if (!r.next()) {
                        System.out.println("No user mathces " + inputCustomer + ", try again");
                    }
                    else {
                        do {
                            String searchCustomerNR = r.getString("customerNR");
                            String searchFname = r.getString("fname");
                            String searchLname = r.getString("lname");
                            String searchNationality = r.getString("nationality");
                            System.out.println(searchCustomerNR + " " + searchFname + " " + searchLname + " " + searchNationality);
                        }
                        while (r.next());
                    }
                }
            }


            //ÞETTA VIRKAR, BANNAÐ AÐ BREYTA
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    // TEST BELOW SAFE TO DELETE
    public static void customerCheck() throws ClassNotFoundException {
        System.out.println("---------------------MENU -> CUSTOMER -> BOOKINGS ------------------------");
        System.out.println("");
        Class.forName("org.sqlite.JDBC");
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + "src\\Profun\\verkefniDB.db");

            while(true) {
                Scanner myObj = new Scanner(System.in);
                System.out.print("Search(customerNr): ");

                String inputCustomer = myObj.nextLine();
                inputCustomer = inputCustomer.toLowerCase();

                if (inputCustomer.equals("quit")) {
                    Customer();
                    myObj.close();
                    break;
                }
                else {
                    statement = connection.prepareStatement("SELECT bookingNr, customerNr, flightNr FROM BookedFlights WHERE customerNr like ?");
                    statement.setString(1, "%" + inputCustomer + "%");
                    ResultSet r = statement.executeQuery();
                    System.out.println("Booking number,  Customer number,  Flight number");

                    // SKOÐAR HVORT SVARIÐ FRÁ GAGNASAFNINU SÉ TÓMT EÐA EKKI
                    if (!r.next()) {
                        System.out.println("No booking matches " + inputCustomer + ", try again.");
                    }
                    else {
                        do {
                            String customerBookingNr = r.getString("bookingNr");
                            String customerCustomerNr = r.getString("customerNr");
                            String customerFlightNr = r.getString("flightNr");
                            System.out.println(customerBookingNr + " " + customerCustomerNr + " " + customerFlightNr);
                        }
                        while (r.next());
                    }
                }
            }

            //ÞETTA VIRKAR, BANNAÐ AÐ BREYTA
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    public static void customerCancel(String cancelBookingNr, String cancelCustomerNr) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + "src\\Profun\\verkefniDB.db");
            statement = connection.prepareStatement("DELETE FROM BookedFlights WHERE bookingNr like ? AND customerNr like ?;");
            statement.setString(1, cancelBookingNr);
            statement.setString(2, cancelCustomerNr);
            statement.executeUpdate();

            //ÞETTA VIRKAR, BANNAÐ AÐ BREYTA
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
}
package Profun;

import java.sql.*;
import java.util.Scanner;

public class flightController {

    public static void FlightController() throws ClassNotFoundException {
        System.out.println("");
        System.out.println("---------------------MENU -> FLIGHTS -------------------------");
        System.out.println("");
        System.out.println("To exit type \"quit\".");
        System.out.println("");
        flightControllerStart();
    }

    public static void flightControllerStart() throws ClassNotFoundException {
        Scanner myObj = new Scanner(System.in);
        System.out.print("Action(search/book): ");

        String inputFC = myObj.nextLine();
        inputFC = inputFC.toLowerCase();
        if(inputFC.equals("book")) {
            bookStart();
        }
        else if(inputFC.equals("search")) {
            searchStart();
        }
        else if(inputFC.equals("quit")) {
            menu.Menu();
        }
    }

    public static void searchStart() throws ClassNotFoundException {
        System.out.println("---------------------MENU -> FLIGHTS -> SEARCH ------------------");
        System.out.println("");
        Class.forName("org.sqlite.JDBC");
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + "src\\Profun\\verkefniDB.db");
            while(true) {
                Scanner myObj = new Scanner(System.in);
                System.out.print("Search(flightNr/destination/ArrivalAirport): ");

                String inputSearch = myObj.nextLine();
                inputSearch = inputSearch.toLowerCase();

                if (inputSearch.equals("quit")) {
                    FlightController();
                    myObj.close();
                    break;
                }
                else {
                    statement = connection.prepareStatement("SELECT flightNr, destination, departureDate, departureAirport, arrivalAirport, flightPrice FROM Flights WHERE flightNr LIKE ? OR destination LIKE ? OR arrivalAirport LIKE ?");
                    statement.setString(1, "%" + inputSearch + "%");
                    statement.setString(2, "%" + inputSearch + "%");
                    statement.setString(3, "%" + inputSearch + "%");
                    ResultSet r = statement.executeQuery();
                    System.out.println("Flight Number,  Destination,   Departure Date,  Departure Airport,   Arrival Airport,  Price");

                    // SKO??AR HVORT SVARI?? FR?? GAGNASAFNINU S?? T??MT E??A EKKI
                    if (!r.next()) {
                        System.out.println("No flights match your search " + inputSearch + ", please try again.");
                    }
                    else {
                        do {
                            String searchFlightNr = r.getString("flightNr");
                            String searchDestination = r.getString("destination");
                            String searchDepartureDate = r.getString("departureDate");
                            String searchDepartureAirport = r.getString("departureAirport");
                            String searchArrivalAirport = r.getString("arrivalAirport");
                            double searchFlightPrice = r.getDouble("flightPrice");
                            System.out.println(searchFlightNr + " " + searchDestination + " " + searchDepartureDate + " " + searchDepartureAirport + " " + searchArrivalAirport + " " + searchFlightPrice);
                        }
                        while (r.next());
                    }
                }
            }

            //??ETTA VIRKAR, BANNA?? A?? BREYTA
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

    public static void bookStart() throws ClassNotFoundException {
        System.out.println("---------------------MENU -> FLIGHTS -> BOOKING ------------------");
        System.out.println("");
        Scanner myObj = new Scanner(System.in);
        System.out.println("To quit type anything else than yes");
        System.out.print("To continue type \"yes\": ");

        String inputBook = myObj.nextLine();
        inputBook = inputBook.toLowerCase();

        if (inputBook.equals("yes")) {
            Scanner myObj2 = new Scanner(System.in);
            System.out.print("Customer Nr: ");

            String inputCustomerNr = myObj2.nextLine();
            inputCustomerNr = inputCustomerNr.toLowerCase();

            System.out.print("Flight number: ");
            String inputFlightNr = myObj2.nextLine();
            inputFlightNr = inputFlightNr.toLowerCase();

            // Kallar ?? falli?? correctInfo sem skilar true ef uppl??singarnar eru r??ttar
            // og false ef ????r eru rangar
            Boolean allRett = correctInfo(inputCustomerNr, inputFlightNr);

            if(allRett){
                System.out.println("");
                System.out.println("Info correct");
                System.out.println("");
                System.out.println("Are you sure you want to book this flight?");
                System.out.println("Customer number: " + inputCustomerNr);
                System.out.println("Flight number: " + inputFlightNr);
                Scanner myObj3 = new Scanner(System.in);
                System.out.print("To confirm type \"confirm\": ");
                String inputSecurity = myObj3.nextLine();
                inputSecurity = inputSecurity.toLowerCase();

                if(inputSecurity.equals("confirm")) {
                    // Vitum a?? allt var r??tt svo vi?? sendum ??a?? ??fram til a?? b??ka ??a??
                    bookRun(inputCustomerNr, inputFlightNr);
                    FlightController();
                    myObj.close();
                }
                else {
                    FlightController();
                    myObj.close();
                }
            }
            else {
                System.out.println("Info wrong");
                bookStart();
            }
        }
        else {
            FlightController();
            myObj.close();
        }
    }

    // SKO??AR HVORT UPPL??SINGARNAR S??U R??TTAR OG A?? ????R PASSI VI?? FLUG.
    public static Boolean correctInfo(String inputCustomerNr, String inputFlightNr) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection connection = null;
        PreparedStatement statement1 = null;
        PreparedStatement statement2 = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + "src\\Profun\\verkefniDB.db");
            String searchFlightNr = "";
            String searchCustomerNr = "";
            while(true) {
                statement1 = connection.prepareStatement("SELECT customerNr FROM Customers WHERE customerNr LIKE ?");
                statement1.setString(1, inputCustomerNr);
                ResultSet r1 = statement1.executeQuery();

                // SKO??AR HVORT ??A?? ER TIL CUSTOMER ME?? ??KVE??I?? N??MER
                while (r1.next()) {
                    searchCustomerNr = r1.getString("customerNr");
                }

                statement2 = connection.prepareStatement("SELECT flightNr FROM Flights WHERE flightNr LIKE ?");
                statement2.setString(1, inputFlightNr);
                ResultSet r2 = statement2.executeQuery();

                // SKO??AR HVORT ??A?? ER FLUG ME?? ??KVE??I?? N??MER.
                while (r2.next()) {
                    searchFlightNr = r2.getString("flightNr");
                }

                // SKO??AR HVORT ALLT PASSAR OG SKILAR TRUE/FALSE TIL BOOKSTART
                return searchCustomerNr.equals(inputCustomerNr) && searchFlightNr.equals(inputFlightNr);
            }

            //??ETTA VIRKAR, BANNA?? A?? BREYTA
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
        return false;
    }

    public static void bookRun(String inputCustomerNr, String inputFlightNr) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + "src\\Profun\\verkefniDB.db");
            statement = connection.prepareStatement("INSERT INTO BookedFlights(customerNr, flightNr)VALUES (?,?);");
            statement.setString(1, inputCustomerNr);
            statement.setString(2, inputFlightNr);
            statement.executeUpdate();

            //??ETTA VIRKAR, BANNA?? A?? BREYTA
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
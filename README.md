# HBV401G-Þróun Hugbúnaðar-Lokaverkefni-2021-Lokaverkefni-2021
Lokaverkefni í HBV401G Þróun hugbúnaðar.

HOW TO RUN THE 2F GROUP PROGRAM

To run the program run the java file: front.java
That will initiate the program.

You need to add the sqlite driver to run this program
You can add sqlite-jdbc-3.32.3.2.jar with these steps in intellij:
- Click File from the toolbar
- Select Project Structure option (CTRL + SHIFT + ALT + S on Windows/Linux, ⌘ + ; on Mac OS X)
- Select Modules at the left panel
- Select Dependencies tab
- Select + icon
- Select 1 JARs or directories option

If you want to run this from the command line you can also do this
as long as you have the sqlite-jdbc-3.32.3.2.jar file in your project.

javac front.java
java -cp .;sqlite-jdbc-3.32.3.2.jar front



THE FUNCTIONS OF THE PROGRAM
Front
- The driver file, contains the main function to run in Intellij.

Menu
- Stopping point between UserController and FlightController.
  is there for user experience.

FLIGHT CONTROLLER

FlightController()
- Start function inn FlightController

flightControllerStart()
- Takes input from the commandline about what the user wants to do in
  FlightController, f.x. search for flight, book a flight or quit and
  go back to main menu.

searchStart()
- Takes input from the commandline and returns all flights matching the
  input.

bookstart()
- Starts the booking process. Takes input from the commandline and sends them
  to correctInf() to verify all the information are correct.

Boolean correctInfo()
- Checks to see if the information from bookStart() are correct according to the
  database. If they are correct it sends the information to bookRun() which books
  the flight, if not then the user is sent back to flightControllerStart().

bookRun()
- Books a flight with the information from correctInfo()

USER CONTROLLER

Customer()
- Start function inn UserController

customerStart()
- Takes input from the commandline about what the user wants to do in
  UserController, f.x. check user info, check user bookings, cancel bookings
  and go back to the main menu.

customerRun()
- Takes input from the commandline and returns all information about a customer
  that matches the input.

customerCheck()
- Takes input from the commandline and shows all flights bookings reletated
  to a specifc customer.

customerCancel()
- Takes input from the commandline and cancels bookings.

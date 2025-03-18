// Importing necessary Java libraries
import java.util.InputMismatchException;
import java.util.Scanner;

public class PlaneManagement {
    // Constants for the number of rows and seats per row
    public static final int NUM_ROWS = 4;
    private static final int[] SEATS_PER_ROW = {12, 14, 14, 12};
    private static final int[][] seats = new int[NUM_ROWS][14]; // Assuming max seats per row is 14
    private static final Ticket[] tickets = new Ticket[NUM_ROWS * 14]; // Array to store tickets

    public static void main(String[] args) {
        System.out.println("\n\tWelcome to the Plane Management application");
        Scanner scanner = new Scanner(System.in);

        int option;
        do {
            displayMenu(); // Display the menu options
            System.out.print("Please select an option: ");
            option = scanner.nextInt();
            switch (option) {
                case 0:
                    System.out.println("\nThank you for using Plane Management Application");
                    break;
                case 1:
                    buy_seat(); // Call method to buy a seat
                    break;
                case 2:
                    cancel_seat(); // Call method to cancel a seat
                    break;
                case 3:
                    find_first_available(); // Call method find first available seat
                    break;
                case 4:
                    show_seating_plan(); // Call method to show seating plan
                    break;
                case 5:
                    print_tickets_info(); // Call method to print tickets information
                    break;
                case 6:
                    search_ticket(); // Call method to search ticket
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
        scanner.close();
    }
    private static void displayMenu() {  // Method to display the menu options
        System.out.println("\n*************************************************");
        System.out.println("*                  MENU OPTIONS                 *");
        System.out.println("*************************************************");
        System.out.println("     1) Buy a seat");
        System.out.println("     2) Cancel a seat");
        System.out.println("     3) Find first available seat");
        System.out.println("     4) Show seating plan");
        System.out.println("     5) Print tickets information and total sales");
        System.out.println("     6) Search ticket");
        System.out.println("     0) Exit");
        System.out.println("*************************************************");
    }
    private static void buy_seat() {  // Method to handle buying a seat
        Scanner scanner = new Scanner(System.in);
        do {
            char rowLetter;
            // Loop until a valid row letter is entered
            while (true) {
                System.out.print("\nEnter row letter (A, B, C, or D): ");
                String input = scanner.next().toUpperCase();
                if (input.length() == 1 && "ABCD".contains(input)) {
                    rowLetter = input.charAt(0);
                    break;
                } else {
                    System.out.println("Invalid row letter. Please enter a valid row letter.");
                }
            }

            int seatNumber;
            // Loop until a valid seat number is entered
            while (true) {
                // Prompt with appropriate message based on the selected row letter
                switch (rowLetter) {
                    case 'A':
                        System.out.print("Enter seat number (1-14 for row A): ");
                        break;
                    case 'B':
                        System.out.print("Enter seat number (1-12 for row B ): ");
                        break;
                    case 'C':
                        System.out.print("Enter seat number (1-12 for row C): ");
                        break;
                    case 'D':
                        System.out.print("Enter seat number (1-14 for row D): ");
                        break;
                }
                try {
                    seatNumber = scanner.nextInt();
                    // Check if the seat number is valid for the selected row
                    boolean isValidSeat = switch (rowLetter) {
                        case 'A', 'D' -> seatNumber >= 1 && seatNumber <= 14;
                        case 'B', 'C' -> seatNumber >= 1 && seatNumber <= 12;
                        default -> false;
                    };
                    if (!isValidSeat) {
                        System.out.println("Seat number out of range for row " + rowLetter + ". Please try again.");
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer for the seat number.");
                    scanner.next(); // Consume the invalid input
                }
            }

            int rowIndex = rowLetter - 'A';
            // Check if the seat is already sold
            if (seats[rowIndex][seatNumber - 1] == 1) {
                System.out.println("Seat " + rowLetter + seatNumber + " is already sold. Please choose another seat.");
                continue;
            }
            seats[rowIndex][seatNumber - 1] = 1;

            System.out.println("\n\tPerson Information");

            // Ask person information
            System.out.print("Enter person's name: ");
            String name = scanner.next();
            System.out.print("Enter person's surname: ");
            String surname = scanner.next();
            String email = "";  // Initialize email variable to an empty string
            boolean isValidEmail = false;  // Initialize isValidEmail variable to false
            while (!isValidEmail) {     // Start a while loop that will continue until isValidEmail becomes true
                System.out.print("Enter your email : ");
                email = scanner.next();

                if (email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {   // Use regular expression to check if email is valid
                    isValidEmail = true;   // Set isValidEmail to true if email is valid
                } else {
                    System.out.println("Invalid.Please enter valid email.");// print this error if the email is not valid
                }
            }

            // Calculate ticket price based on seat location
            int price = calculate_ticket_price(rowLetter, seatNumber);

            // Create a new Ticket object
            Person person = new Person(name, surname, email);
            Ticket ticket = new Ticket(rowLetter, seatNumber, price, person);

            // Add ticket to the array
            for (int i = 0; i < tickets.length; i++) {
                if (tickets[i] == null) {
                    tickets[i] = ticket;
                    break;
                }
            }
            System.out.println();
            ticket.save();

            System.out.println("Seat " + rowLetter + seatNumber + " has been successfully sold.");

            // Ask if the user wants to buy another ticket
            while (true) {
                System.out.print("\nDo you want to buy another ticket? (yes/no): ");
                String choice = scanner.next();
                if (choice.equalsIgnoreCase("yes")) {  // If the user wants to buy another ticket, break out of the loop and continue with the ticket buying process
                    break;
                } else if (choice.equalsIgnoreCase("no")) {  // If user doesn't want to buy another ticket return to  the menu
                    return;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");//print this error if user enter anything other than yes or no
                }
            }
        } while (true); // Loop indefinitely until the user decides to exit
    }
    // Method to handle canceling a seat reservation
    private static void cancel_seat() {
        Scanner scanner = new Scanner(System.in);

        // Check if there are any tickets to cancel
        boolean hasTicketsToCancel = false;
        for (Ticket ticket : tickets) {
            if (ticket != null) {
                hasTicketsToCancel = true;
                break;
            }
        }

        if (!hasTicketsToCancel) {
            System.out.println("No tickets to cancel!");
            return;
        }

        char rowLetter;
        // Loop until a valid row letter is entered
        while (true) {
            // Ask for row letter
            System.out.print("\nEnter row letter (A, B, C, or D): ");
            String input = scanner.next().toUpperCase();
            if (input.length() == 1 && "ABCD".contains(input)) {
                rowLetter = input.charAt(0);
                break;
            } else {
                System.out.println("Invalid row letter. Please enter a valid row letter.");
            }
        }
        int rowIndex = rowLetter - 'A';
        // Prompt with appropriate message based on the selected row letter
        switch (rowLetter) {
            case 'A':
                System.out.print("Enter seat number (1-14 for row A): ");
                break;
            case 'B':
                System.out.print("Enter seat number (1-12 for rows B): ");
                break;
            case 'C':
                System.out.print("Enter seat number (1-12 for rows C): ");
                break;
            case 'D':
                System.out.print("Enter seat number (1-14 for row D): ");
                break;
        }
        int seatNumber;
        // Loop until a valid seat number is entered
        while (true) {
            try {
                seatNumber = scanner.nextInt();

                // Check if seat number is valid for the selected row
                boolean isValidSeat = switch (rowLetter) {
                    case 'A', 'D' -> seatNumber >= 1 && seatNumber <= 14;
                    case 'B', 'C' -> seatNumber >= 1 && seatNumber <= 12;
                    default -> false;
                };

                if (!isValidSeat) {
                    System.out.println("Seat number out of range for row " + rowLetter + ". Please try again.");
                    System.out.print("Enter seat number: ");
                    continue;
                }

                // Check if seat is already available
                if (seats[rowIndex][seatNumber - 1] == 0) {
                    System.out.println("Seat " + rowLetter + seatNumber + " is already available.");
                    System.out.print("Enter seat number: ");
                    continue;
                }

                // Record seat as available
                seats[rowIndex][seatNumber - 1] = 0;

                // Remove ticket from array
                for (int i = 0; i < tickets.length; i++) {
                    if (tickets[i] != null && tickets[i].getRow() == rowLetter && tickets[i].getSeat() == seatNumber) {
                        tickets[i] = null;
                        break;
                    }
                }

                System.out.println("Seat " + rowLetter + seatNumber + " has been successfully canceled.");
                return;

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer for the seat number.");
                System.out.print("Enter seat number: ");
                scanner.next(); // Consume the invalid input
            }
        }
    }
    private static void find_first_available() {  // Method to find and display the first available seat
        // Search for the first available seat
        for (int i = 0; i < NUM_ROWS; i++) {   // Loop through each row
            for (int j = 0; j < SEATS_PER_ROW[i]; j++) {  // Loop through each seat in the row
                if (seats[i][j] == 0) {  // Check if the seat is available
                    char rowLetter = (char) ('A' + i);  // Convert the row index to row letter
                    System.out.println("First available seat: " + rowLetter + (j + 1));  // Display the available seat
                    return;  // Exit the method after finding the first available seat
                }
            }
        }
        System.out.println("No available seats found.");  // Display if no available seats are found
    }
    private static void show_seating_plan() {  // Method to display seating plan
        System.out.println("\n\tSeating Plan");
        System.out.println("--------------------");
        for (int i = 0; i < NUM_ROWS; i++) {
            char rowLetter = (char) ('A' + i); // Convert the row index to row letter
            System.out.print(" ");  // Print a space for formatting
            if (rowLetter == 'B' || rowLetter == 'C') { // Check if the current row is B or C
                System.out.print(""); // Print an empty string for formatting
            }
            int seatsInRow;
            if (i == 0 || i == NUM_ROWS - 1) {
                seatsInRow = 14; // For the first and last rows, set seatsInRow to 14
            } else {
                seatsInRow = 12; // For rows B and C, set seatsInRow to 12
            }
            for (int j = 0; j < seatsInRow; j++) {
                if (seats[i][j] == 1) {
                    System.out.print("X"); // Display 'X' for sold seats
                } else {
                    System.out.print("O"); // Display 'O' for available seats
                }
            }
            System.out.println(); // Move to the next row
        }
    }
    // Method to calculate the ticket price based on the row letter and seat number
    private static int calculate_ticket_price(char rowLetter, int seatNumber) {
        int price;  // Variable to store the calculated ticket price
        // Determine the ticket price based on the row letter and seat number
        if ((rowLetter == 'A' && seatNumber >= 1 && seatNumber <= 5) ||   // Check if seat is in the first set
                (rowLetter == 'B' && seatNumber >= 1 && seatNumber <= 5) ||
                (rowLetter == 'C' && seatNumber >= 1 && seatNumber <= 5) ||
                (rowLetter == 'D' && seatNumber >= 1 && seatNumber <= 5)) {
            price = 200; // Tickets in first set cost £200
        } else if ((rowLetter == 'A' && seatNumber >= 6 && seatNumber <= 9) || // Check if seat is in the second set
                (rowLetter == 'B' && seatNumber >= 6 && seatNumber <= 9) ||
                (rowLetter == 'C' && seatNumber >= 6 && seatNumber <= 9) ||
                (rowLetter == 'D' && seatNumber >= 6 && seatNumber <= 9)) {
            price = 150; // Tickets in second set cost £150
        } else if ((rowLetter == 'A' && seatNumber >= 10 && seatNumber <= 14) || // Check if seat is in the third set
                (rowLetter == 'B' && seatNumber >= 10 && seatNumber <= 12) ||
                (rowLetter == 'C' && seatNumber >= 10 && seatNumber <= 12) ||
                (rowLetter == 'D' && seatNumber >= 10 && seatNumber <= 14)) {
            price = 180; // Tickets in third set cost £180
        } else {
            System.out.println("Invalid seat. Unable to calculate ticket price."); // Print error message for invalid seats
            price = -1; // Return -1 for invalid seats
        }

        return price;  // Return the calculated ticket price
    }
    private static void print_tickets_info() {
        int totalSales = 0;
        boolean ticketsSold = false; // Flag to track if any tickets have been sold
        // Iterate through the tickets array
        for (Ticket ticket : tickets) {  // Check if the ticket is not null
            if (ticket != null) {  // Check if this is the first sold ticket
                if (!ticketsSold) {
                    ticketsSold = true;  // Update the flag to indicate that at least one ticket has been sold
                }
                ticket.printInfo();
                totalSales += ticket.getPrice();  // Add the ticket price to the total sales amount
            }
        }

        if (!ticketsSold) {
            System.out.println("No tickets have been sold.");  // Print message if no tickets have been sold
        } else {
            System.out.println("\nTotal Sales: £" + totalSales);  // Print total sales amount
        }
    }
    private static void search_ticket() {   // Method to search ticket
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Ask for row letter
            System.out.print("\nEnter row letter (A, B, C, or D): ");
            String rowInput = scanner.next().toUpperCase();
            if (rowInput.length() != 1 || !"ABCD".contains(rowInput)) {
                System.out.println("Invalid row letter. Please enter a valid row letter.");
                continue;
            }
            char rowLetter = rowInput.charAt(0);

            while (true) {
                // Display appropriate prompt for seat number based on row
                switch (rowLetter) {
                    case 'A':
                        System.out.print("Enter seat number (1-14 for row A): ");
                        break;
                    case 'B':
                        System.out.print("Enter seat number (1-12 for row B): ");
                    case 'C':
                        System.out.print("Enter seat number (1-12 for row C): ");
                        break;
                    case 'D':
                        System.out.print("Enter seat number (1-14 for row D): ");
                        break;
                }

                try {
                    // Get seat number input
                    int seatNumber = scanner.nextInt();

                    // Convert row letter to index (A -> 0, B -> 1, C -> 2, D -> 3)
                    int rowIndex = rowLetter - 'A';

                    // Check if seat number is valid
                    if (seatNumber < 1 || (rowLetter == 'A' || rowLetter == 'D' ? seatNumber > 14 : seatNumber > 12)) {
                        System.out.println("Invalid seat number. Please enter a seat number within the valid range.");
                        continue;
                    }

                    // Check if seat is sold and display ticket information
                    if (seats[rowIndex][seatNumber - 1] == 1) {
                        // Find the ticket associated with the seat
                        Ticket foundTicket = null;
                        for (Ticket ticket : tickets) {
                            if (ticket != null && ticket.getRow() == rowLetter && ticket.getSeat() == seatNumber) {
                                foundTicket = ticket;
                                break;
                            }
                        }

                        // Print ticket and person information if found
                        if (foundTicket != null) {
                            foundTicket.printInfo();
                        } else {
                            System.out.println("This seat is available.");
                        }
                    } else {
                        System.out.println("This seat is available.");
                    }
                    // Ask if the user wants to search another ticket
                    while (true) {
                        System.out.print("Do you want to search another ticket? (yes/no): ");
                        String choice = scanner.next();
                        if (choice.equalsIgnoreCase("yes")) {
                            search_ticket(); // Recursive call to search for another ticket
                            return;
                        } else if (choice.equalsIgnoreCase("no")) {
                            return;  // Return to the main menu
                        } else {
                            System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                        }
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer for the seat number.");
                    scanner.next(); // Consume the invalid input
                }
            }
        }
    }
}
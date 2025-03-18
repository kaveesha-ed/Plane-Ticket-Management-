import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Ticket {  // Class representing a Ticket with attributes row, seat, price, and person
    private char row;
    private int seat;
    private int price;
    private Person person;
    // Constructor to initialize a Ticket object with provided row, seat, price, and person
    public Ticket(char row, int seat, int price, Person person) {
        setRow(row);
        setSeat(seat);
        setPrice(price);
        setPerson(person);
    }
    public char getRow() {
        return row;
    }  // Getter method to retrieve the row of the ticket
    public void setRow(char row) {
        this.row = row;
    } // Setter method to set the row of the ticket
    public int getSeat() {
        return seat;
    } // Getter method to retrieve the seat number of the ticket
    public void setSeat(int seat) {
        this.seat = seat;
    }  // Setter method to set the seat number of the ticket
    public int getPrice() {
        return price;
    }   // Getter method to retrieve the price of the ticket
    public void setPrice(int price) {
        this.price = price;
    } // Setter method to set the price of the ticket
    public Person getPerson() {  // Getter method to retrieve the person associated with the ticket
        return person;
    }
    public void setPerson(Person person) {
        this.person = person;
    }  // Setter method to set the person associated with the ticket

    // Method to print ticket information
    public void printInfo() {
        System.out.println("\nTicket Information");
        System.out.println("-----------------------");
        System.out.println("Row: " + row);
        System.out.println("Seat: " + seat);
        System.out.println("Price: £" + price);
        System.out.println("Person Information");
        System.out.println("-----------------------");
        person.printInfo();
    }

    // Method to save ticket information to a file
    public void save() {
        String fileName = row + String.valueOf(seat) + ".txt";  // Generate the filename based on row and seat number
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Tickets Information");
            writer.println("Row: " + row);
            writer.println("Seat: " + seat);
            writer.println("Price: £" + price);
            writer.println("\nPerson Information");
            writer.println("Name: " + person.getName());
            writer.println("Surname: " + person.getSurname());
            writer.println("Email: " + person.getEmail());
            // Print confirmation message
            System.out.println("Ticket information has been successfully saved to file " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing ticket information to file: " + e.getMessage());
        }
    }
}

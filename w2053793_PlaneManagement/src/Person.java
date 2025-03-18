public class Person {   // Class representing a Person with attributes name, surname, and email
    private String name;
    private String surname;
    private String email;
    // Constructor to initialize a Person object with provided name, surname, and email
    public Person(String name, String surname, String email) {
        setName(name);
        setSurname(surname);
        setEmail(email);
    }

    // Getters and setters
    public String getName() {  // Getter method to retrieve the name of the person
        return name;
    }

    public void setName(String name) {  // Setter method to set the name of the person
        this.name = name;  // Set the name attribute of the person object
    }

    public String getSurname() {  // Getter method to retrieve the surname of the person
        return surname;
    }

    public void setSurname(String surname) {   // Setter method to set the surname of the person
        this.surname = surname;  // Set the surname attribute of the person object
    }

    public String getEmail() {  // Getter method to retrieve the email of the person
        return email;
    }

    public void setEmail(String email) {  // Setter method to set the email of the person
        this.email = email;  // Set the email attribute of the person object
    }
    // Method to print person information
    public void printInfo() {
        System.out.println("Name: " + name);
        System.out.println("Surname: " + surname);
        System.out.println("Email: " + email);
    }
}


import java.util.*;
import java.io.*;

public class Cyber_Library {
    public static void main(String[] args) throws IOException {
        Book bookInterface = new Book();
        Patron patronInterface = new Patron();
        Scanner console = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to Cyber Library");
            System.out.println(
                    "Would you like to \n 1.View Book \n 2.Patron Account \n 3.Return Book \n 4.Library Management \n 5.Exit Page \nEnter [1/2/3/4/5]");
            switch (console.nextLine()) {
                case "1":
                    bookInterface.viewBookInterfaces();
                    break;

                case "2":
                    patronInterface.patronAccountinterface();
                    break;

                case "3":
                    break;

                case "4":
                    break;

                case "5":
                    System.out.println("See You Again");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid Option, Please Try Again");
            }
        }
    }
}

class Book {
    public String title;
    public String author;
    public String genre;
    public String ISBN;
    public String availability;
    public String patron;

    private List<Book> books;

    public Book() {
        this.books = new ArrayList<>();
    }

    public Book(String title, String author, String genre, String ISBN, String availability, String patron) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.ISBN = ISBN;
        this.availability = availability;
        this.patron = patron;
    }

    static Scanner console = new Scanner(System.in);

    private void bookList() throws FileNotFoundException {
        FileReader bookinput = new FileReader("book.txt");
        Scanner book = new Scanner(bookinput);

        while (book.hasNextLine()) {
            String title = book.nextLine();
            String author = book.nextLine();
            String genre = book.nextLine();
            String ISBN = book.nextLine();
            String availability = book.nextLine();
            String patron = book.nextLine();

            books.add(new Book(title, author, genre, ISBN, availability, patron));
        }
    }

    public void saveBook(String book) throws IOException {
        PrintWriter bookWriter = new PrintWriter("book.txt");
        for (Book bookSave : books) {
            bookWriter.println(books.toString());
        }
    }

    public String toString() {
        return "Title : " + title + "\nBy : " + author + "\n" + genre + "\n" + ISBN + "\tStatus : " + availability
                + "\tBorrower : " + patron;
    }

    public void viewBookInterfaces() throws IOException {
        Patron patronsList = new Patron();
        List<Patron> patrons = patronsList.getPatrons();
        List<Book> newBookList = new ArrayList<>();
        String searchInput, borrowInput, patronName, patronID;

        System.out.println("Would You like to \n 1.Search for Book \n 2.View All Book \nEnter [1/2]");

        while (true) {
            switch (console.nextLine()) {
                case "1":
                    System.out.println("Enter Book Name / Book ISBN : ");
                    searchInput = console.nextLine();
                    for (Book bookSearch : books) {
                        if (bookSearch.title.equalsIgnoreCase(searchInput)
                                || bookSearch.ISBN.equalsIgnoreCase(searchInput)) {
                            System.out.println(bookSearch);
                            newBookList.add(bookSearch);
                        }
                    }

                    for (Book bookAvaibility : newBookList) {
                        if (bookAvaibility.availability.equals("AVAILABLE")) {
                            System.out.print("Would you like to borrow this book? [Y/N] : ");
                            borrowInput = console.next();

                            if (borrowInput.equalsIgnoreCase("Y") || borrowInput.equalsIgnoreCase("Yes")) {
                                System.out.print("Please enter your Patron Name : ");
                                patronName = console.next();
                                System.out.print("Please enter your Patron ID : ");
                                patronID = console.next();

                                while (true) {
                                    for (Patron checkPatron : patrons) {
                                        if (checkPatron.equals(patronName) && checkPatron.equals(patronID)) {
                                            bookAvaibility.availability = "Unavailable";
                                            System.out.println("Enjoy the Book");
                                            saveBook("book.txt");
                                            return;
                                        }
                                    }
                                    System.out.println("Invalid Patron Name or ID");
                                }
                            }
                        }
                    }
                    break;

                case "2":
                    for (Book viewAllBook : books) {
                        System.out.println(viewAllBook);
                    }
                    break;

                default:
                    System.out.println("Invalid Option, Please Try Again");
            }
        }
    }
}

class Patron {
    public String name;
    public String email;
    public int ID;
    public double fines;

    private List<Patron> patrons;

    public Patron() {
        this.patrons = new ArrayList<>();
    }

    public List<Patron> getPatrons() {
        return patrons;
    }

    public Patron(String name, String email, int ID, double fines) {
        this.name = name;
        this.email = email;
        this.ID = ID;
        this.fines = fines;
    }

    static Scanner console = new Scanner(System.in);

    public void patronList() throws FileNotFoundException {
        FileReader patronInput = new FileReader("patron.txt");
        Scanner patron = new Scanner(patronInput);

        while (patron.hasNextLine()) {
            String name = patron.nextLine();
            String email = patron.nextLine();
            int ID = Integer.parseInt(patron.nextLine());
            double fines = Double.parseDouble(patron.nextLine());

            patrons.add(new Patron(name, email, ID, fines));
        }
    }

    public void savePatron(Patron newPatron) throws IOException {
        PrintWriter patronWriter = new PrintWriter("patron.txt");
        for (Patron patronSave : patrons) {
            patronWriter.println(newPatron);
        }
    }

    public String toString() {
        return "Name : " + name + "\nID : " + ID + "\nE-mail : " + email + "\nFines : " + fines;
    }

    public void patronAccountinterface() throws IOException {
        Random IDGenerator = new Random();
        int newID = IDGenerator.nextInt(1000) + 1;
        String newName, newEmail, patronName;
        int patronID = 0;

        System.out
                .println("Would you like to \n 1.Register Patron Account \n 2.View Your Patron Account \nEnter [1/2]");

        while (true) {
            switch (console.nextLine()) {
                case "1":
                    System.out.print("Please enter your name : ");
                    newName = console.next();
                    System.out.print("Please enter your email : ");
                    newEmail = console.next();
                    while (true) {
                        for (Patron emailChecker : patrons) {
                            if (!emailChecker.email.equals(newEmail)) {
                                System.out.println("Email already exists");
                                System.out.print("Your Patron ID is " + newID);

                                Patron newPatron = new Patron(newName, newEmail, newID, 0.0);

                                savePatron(newPatron);
                            }
                        }
                        System.out.println("Email already exists, Please try again");
                    }
                case "2":
                    System.out.print("Please enter your Patron name : ");
                    patronName = console.next();
                    System.out.print("Please enter your Patron ID : ");
                    patronID = console.nextInt();

                    for (Patron accountVerify : patrons) {
                        if (accountVerify.name.equals(patronName) && accountVerify.ID == patronID) {
                            System.out.println("Name : " + accountVerify.name);
                            System.out.println("ID : " + accountVerify.ID);
                            System.out.println("E-mail : " + accountVerify.email);
                            System.out.println("Fines : " + accountVerify.fines);
                        }
                    }
                    break;

                default:
                    System.out.println("Invalid Option, Please Try Again");
            }
        }
    }
}

class Transaction {

}

class Management {

}
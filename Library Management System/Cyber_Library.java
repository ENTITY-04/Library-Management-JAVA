import java.util.*;
import java.io.*;

public class Cyber_Library {
    public static void main(String[] args) throws IOException {
        Book bookInterface = new Book();
        Patron patronInterface = new Patron();
        ReturnBook returnInterface = new ReturnBook();
        Management managementInterface = new Management();
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
                    returnInterface.returnBookInterface();
                    break;

                case "4":
                    managementInterface.managerInterface();
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

    public Book(String title, String author, String genre, String ISBN, String availability, String patron) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.ISBN = ISBN;
        this.availability = availability;
        this.patron = patron;
    }

    private List<Book> books;

    public Book() {
        this.books = new ArrayList<>();
    }

    public List<Book> getBooks() {
        return books;
    }

    static Scanner console = new Scanner(System.in);

    private void bookList() throws FileNotFoundException {
        FileReader bookInput = new FileReader("book.txt");
        Scanner book = new Scanner(bookInput);

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
        String search, borrow, patronName, patronID;

        System.out.println("Would You like to \n 1.Search For Book \n 2.View All Book \nEnter [1/2]");

        while (true) {
            switch (console.nextLine()) {
                case "1":
                    System.out.println("Enter Book Name / Book ISBN : ");
                    search = console.nextLine();
                    for (Book bookSearch : books) {
                        if (bookSearch.title.equalsIgnoreCase(search)
                                || bookSearch.ISBN.equalsIgnoreCase(search)) {
                            System.out.println(bookSearch);
                            newBookList.add(bookSearch);
                        }
                    }

                    for (Book bookAvailability : newBookList) {
                        if (!newBookList.isEmpty() && bookAvailability.availability.equals("AVAILABLE")) {
                            System.out.print("Would you like to borrow this book? [Y/N] : ");
                            borrow = console.next();

                            if (borrow.equalsIgnoreCase("Y") || borrow.equalsIgnoreCase("Yes")) {
                                System.out.print("Please enter your Patron Name : ");
                                patronName = console.next();
                                System.out.print("Please enter your Patron ID : ");
                                patronID = console.next();

                                while (true) {
                                    for (Patron patronVerify : patrons) {
                                        if (patronVerify.equals(patronName) && patronVerify.equals(patronID)) {
                                            bookAvailability.availability = "Unavailable";
                                            bookAvailability.patron = patronName;
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

    public Patron(String name, String email, int ID) {
        this.name = name;
        this.email = email;
        this.ID = ID;
    }

    private List<Patron> patrons;

    public Patron() {
        this.patrons = new ArrayList<>();
    }

    public List<Patron> getPatrons() {
        return patrons;
    }

    static Scanner console = new Scanner(System.in);

    public void patronList() throws FileNotFoundException {
        FileReader patronInput = new FileReader("patron.txt");
        Scanner patron = new Scanner(patronInput);

        while (patron.hasNextLine()) {
            String name = patron.nextLine();
            String email = patron.nextLine();
            int ID = Integer.parseInt(patron.nextLine());

            patrons.add(new Patron(name, email, ID));
        }
    }

    public void savePatron(Patron newPatron) throws IOException {
        PrintWriter patronWriter = new PrintWriter("patron.txt");
        for (Patron patronSave : patrons) {
            patronWriter.println(newPatron);
        }
    }

    public String toString() {
        return "Name : " + name + "\nID : " + ID + "\nE-mail : " + email;
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
                                System.out.print("Your Patron ID is " + newID);
                                Patron newPatron = new Patron(newName, newEmail, newID);
                                System.out.print("Patron Account created Successfully");
                                savePatron(newPatron);
                                return;
                            }
                        }
                        System.out.println("Email already exists, Please try again");
                    }
                case "2":
                    System.out.print("Please enter your Patron name : ");
                    patronName = console.next();
                    System.out.print("Please enter your Patron ID : ");
                    patronID = console.nextInt();

                    for (Patron patronVerify : patrons) {
                        if (patronVerify.name.equals(patronName) && patronVerify.ID == patronID) {
                            System.out.println("Name : " + patronVerify.name);
                            System.out.println("ID : " + patronVerify.ID);
                            System.out.println("E-mail : " + patronVerify.email);
                        }
                    }
                    break;

                default:
                    System.out.println("Invalid Option, Please Try Again");
            }
        }
    }
}

class ReturnBook {

    public ReturnBook() {
    }

    static Scanner console = new Scanner(System.in);

    public void returnBookInterface() throws IOException {
        Book bookInterface = new Book();
        Patron patronsList = new Patron();
        List<Patron> patrons = patronsList.getPatrons();

        Book booksList = new Book();
        List<Book> books = booksList.getBooks();

        List<Book> newBookList = new ArrayList<>();

        String patronName, confirmation;
        int patronID = 0, bookCounter = 0, returnBookNum = 0;

        System.out.print("Please enter your Patron name : ");
        patronName = console.next();
        System.out.print("Please enter your Patron ID : ");
        patronID = console.nextInt();

        for (Patron patronVerify : patrons) {
            if (patronVerify.name.equals(patronName) && patronVerify.ID == patronID) {
                for (Book borrowedBook : books) {
                    if (borrowedBook.patron.equals(patronName)) {
                        System.out.println("Borrowed Book : \n" + bookCounter + ". " + borrowedBook);
                        newBookList.add(borrowedBook);
                        bookCounter++;
                    }
                }
            }
        }
        while (true) {
            System.out.print("Do you want to return the book [Y/N] : ");
            confirmation = console.next();

            if (confirmation.equalsIgnoreCase("Y") || confirmation.equalsIgnoreCase("Y")) {
                while (true) {
                    System.out.print("Enter the number of book you would like to return : ");
                    returnBookNum = console.nextInt();

                    if (returnBookNum >= 1 && returnBookNum <= newBookList.size()) {
                        Book returnedBook = newBookList.get(returnBookNum - 1);
                        returnedBook.availability = "AVAILABLE";
                        returnedBook.patron = "NONE";
                        bookInterface.saveBook("book.txt");
                        System.out.println("Book returned successfully");
                        return;
                    } else {
                        System.out.print("Invalid Book");
                    }
                }
            } else if (confirmation.equalsIgnoreCase("N") || confirmation.equalsIgnoreCase("No")) {
                System.out.print("Enjoy the book");
                return;
            } else {
                System.out.print("Invalid Option, Please Try Again");
            }
        }
    }
}

class Management {

    public Management() {
    }

    static Scanner console = new Scanner(System.in);

    public void managerInterface() throws IOException {
        Book bookInterface = new Book();
        Patron patronInterface = new Patron();

        Patron patronsList = new Patron();
        List<Patron> patrons = patronsList.getPatrons();

        Book booksList = new Book();
        List<Book> books = booksList.getBooks();

        List<Book> newBookList2 = new ArrayList<>();

        String password;
        String newBookTitle, newBookAuthor, newBookGenre, newBookISBN, newBookAvailability, newBookPatron;
        String newPatronName, newPatronEmail;
        int List = 0, Add = 0, Remove = 0, bookCounter = 1, removeBookNum = 0, removePatronID = 0;

        Random IDGenerator = new Random();
        int newPatronID = IDGenerator.nextInt(1000) + 1;

        System.out.print("Please Enter Password : ");
        password = console.nextLine();

        if (password.equals("Manager")) {
            System.out.println(
                    " 1. Show List of Book / Patron \n 2. Add Book / Patron \n 3. Remove Book / Patron \nEnter [1/2/3]");

            switch (console.nextLine()) {
                case "1":
                    while (true) {
                        System.out.print("1. List of Book / 2. List of Patron [1/2] : ");
                        List = console.nextInt();

                        if (List == 1) {
                            for (Book bookList : books) {
                                System.out.println(bookList);
                            }
                            return;
                        } else if (List == 2) {
                            for (Patron patronList : patrons) {
                                System.out.println(patronList);
                            }
                            return;
                        } else {
                            System.out.println("Invalid Option, Please Try Again");
                        }
                    }
                case "2":
                    while (true) {
                        System.out.print("1. Add Book / 2. Add Patron [1/2] : ");
                        Add = console.nextInt();

                        if (Add == 1) {
                            System.out.print("Enter Book Title : ");
                            newBookTitle = console.next();
                            System.out.print("Enter Book Author : ");
                            newBookAuthor = console.next();
                            System.out.print("Enter Book Genre : ");
                            newBookGenre = console.next();
                            System.out.print("Enter Book ISBN : ");
                            newBookISBN = console.next();
                            System.out.print("Enter Book Availability : ");
                            newBookAvailability = console.next();
                            System.out.print("Enter Book Patron : ");
                            newBookPatron = console.next();

                            System.out.println("Book Added Successfully");
                            Book newBook = new Book(newBookTitle, newBookAuthor, newBookGenre, newBookISBN,
                                    newBookAvailability, newBookPatron);
                            bookInterface.saveBook("book.txt");
                            return;
                        } else if (Add == 2) {
                            System.out.print("Please enter your name : ");
                            newPatronName = console.next();
                            System.out.print("Please enter your email : ");
                            newPatronEmail = console.next();
                            while (true) {
                                for (Patron emailChecker : patrons) {
                                    if (!emailChecker.email.equals(newPatronEmail)) {
                                        System.out.print("Your Patron ID is " + newPatronID);
                                        Patron newPatron = new Patron(newPatronName, newPatronEmail, newPatronID);
                                        System.out.print("Patron Account created Successfully");
                                        patronInterface.savePatron(newPatron);
                                        return;
                                    }
                                }
                                System.out.println("Email already exists, Please try again");
                            }
                        } else {
                            System.out.println("Invalid Option, Please Try Again");
                        }
                    }
                case "3":
                    while (true) {
                        System.out.print("1. Remove Book / 2. Remove Patron [1/2] : ");
                        Remove = console.nextInt();

                        if (Remove == 1) {
                            for (Book bookList : books) {
                                System.out.println(bookCounter + ". " + bookList);
                                newBookList2.add(bookList);
                                bookCounter++;
                            }

                            while (true) {
                                System.out.print("Enter the number of book you would like to remove : ");
                                removeBookNum = console.nextInt();

                                if (removeBookNum >= 1 && removeBookNum <= newBookList2.size()) {
                                    Book removeBook = newBookList2.get(removeBookNum - 1);
                                    bookInterface.saveBook("book.txt");
                                    System.out.println("Book Remove Successfully");
                                    return;
                                } else {
                                    System.out.print("Invalid Book");
                                }
                            }
                        } else if (Remove == 2) {
                            for (Patron patronList : patrons) {
                                System.out.println(patronList);
                            }

                            while (true) {
                                System.out.print("Enter Patron ID you would like to remove : ");
                                removePatronID = console.nextInt();

                                for (Patron patronList : patrons) {
                                    if (patronsList.ID == removePatronID) {
                                        Patron removedPatron = patrons.remove(removePatronID);
                                        System.out.print("Patron Account removed Successfully");
                                        patronInterface.savePatron(removedPatron);
                                        return;
                                    }
                                }
                                System.out.println("Invalid Patron");
                            }
                        } else {
                            System.out.println("Invalid Option, Please Try Again");
                        }
                    }
                default:
                    System.out.println("Invalid Option, Please Try Again");
            }
        } else {
            System.out.println("Password Incorrect, Access Denied");
        }
    }
}
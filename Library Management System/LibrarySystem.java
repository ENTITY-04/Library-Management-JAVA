import java.util.*;
import java.io.*;

public class LibrarySystem {
    public static void main(String[] args) throws IOException {
        Book UI_Book = new Book();
        Patron UI_Patron = new Patron();
        Management UI_Management = new Management();
        Scanner console = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome to Cyber Library");
            System.out.println(
                    "\nWould you like to \n 1.View Book \n 2.View Patron Account \n 3.Management \n 4.Exit Page \nEnter [1/2/3/4]");
            switch (console.nextLine()) {
                case "1":
                    UI_Book.pageBook();
                    break;

                case "2":
                    UI_Patron.pagePatron();
                    break;

                case "3":
                    UI_Management.pageManagement();
                    break;

                case "4":
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
    public String status;
    public String patron;

    public Book(String title, String author, String genre, String ISBN, String status, String patron) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.ISBN = ISBN;
        this.status = status;
        this.patron = patron;
    }

    private static List<Book> books;

    public Book() {
        this.books = new ArrayList<>();
        try {
            bookList("book.txt");
        } catch (FileNotFoundException e) {
            System.err.println("\nNo Book File Loaded : " + e.getMessage());
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    static Scanner console = new Scanner(System.in);

    public void bookList(String bookFile) throws FileNotFoundException {
        FileReader bookInput = new FileReader(bookFile);
        Scanner book = new Scanner(bookInput);

        while (book.hasNextLine()) {
            String title = book.nextLine();
            if (title.trim().isEmpty())
                continue;
            String author = book.nextLine();
            String genre = book.nextLine();
            String ISBN = book.nextLine();
            String status = book.nextLine();
            String patron = book.nextLine();

            books.add(new Book(title, author, genre, ISBN, status, patron));
        }
        book.close();
    }

    public void addBook(Book newBook) throws IOException {
        try (PrintWriter bookWriter = new PrintWriter(new FileWriter("book.txt", true))) {
            bookWriter.println(newBook.toString());
        } catch (IOException e) {
            System.err.println("\nBook Save Unsuccessful : " + e.getMessage());
        }
    }

    public void saveBook(String bookFile, List<Book> books) throws IOException {
        try (PrintWriter bookWriter = new PrintWriter(new FileWriter(bookFile))) {
            for (Book bookSave : books) {
                bookWriter.println(bookSave.toString());
            }
        } catch (IOException e) {
            System.err.println("\nBook Save Unsuccessful : " + e.getMessage());
        }
    }

    public String toString() {
        return title + "\n" + author + "\n" + genre + "\n" + ISBN + "\t" + status + "\t" + patron;
    }

    public void pageBook() throws IOException {
        while (true) {
            System.out.println(
                    "\n 1.Borrowing Book \n 2.Returning Book \n 3.View All Book \n 4.Return to Main Menu \nEnter [1/2/3/4]");
            switch (console.nextLine()) {
                case "1":
                    bookBorrow();
                    break;

                case "2":
                    bookReturn();
                    break;

                case "3":
                    bookView();
                    break;

                case "4":
                    return;

                default:
                    System.out.println("Invalid Option, Please Try Again");
            }
        }
    }

    public void bookBorrow() throws IOException {
        Patron patronsList = new Patron();
        List<Patron> patrons = patronsList.getPatrons();

        List<Book> bookBorrowList = new ArrayList<>();

        String patronName, patronID, confirmBorrow, bookInfo;

        System.out.println("\nEnter Book Name / Book ISBN");
        bookInfo = console.nextLine();

        for (Book bookSearch : books) {
            if (bookSearch.title.equalsIgnoreCase(bookInfo)
                    || bookSearch.ISBN.equalsIgnoreCase(bookInfo)) {
                System.out.println("\nTitle : " + bookSearch.title + "\nBy Author : " + bookSearch.author);
                System.out.println("\nGenre : " + bookSearch.genre);
                System.out.println("\nISBN : " + bookSearch.ISBN + "\tStatus : " + bookSearch.status);

                bookBorrowList.add(bookSearch);
            }
        }

        if (!bookBorrowList.isEmpty()) {
            for (Book bookStatus : bookBorrowList) {
                if (bookStatus.status.equals("AVAILABLE")) {
                    System.out.print("\nWould you like to borrow this book? [Y/N] : ");
                    confirmBorrow = console.nextLine();

                    if (confirmBorrow.equalsIgnoreCase("Y") || confirmBorrow.equalsIgnoreCase("Yes")) {
                        System.out.print("\nPlease enter your Patron Name : ");
                        patronName = console.nextLine();
                        System.out.print("Please enter your Patron ID : ");
                        patronID = console.nextLine();

                        while (true) {
                            for (Patron patronVerify : patrons) {
                                if (patronVerify.name.equals(patronName)
                                        && patronVerify.ID.equals(patronID)) {
                                    bookStatus.status = "UNAVAILABLE";
                                    bookStatus.patron = patronID;
                                    saveBook("book.txt", books);
                                    System.out.println("Enjoy the Book");
                                    return;
                                }
                            }
                            System.out.println("\nInvalid Patron Name or ID");
                            break;
                        }
                    } else {
                        return;
                    }
                }
            }
        } else {
            System.out.println("Unable to find Book");
        }
    }

    public void bookReturn() throws IOException {
        Patron patronsList = new Patron();
        List<Patron> patrons = patronsList.getPatrons();

        List<Book> bookReturnList = new ArrayList<>();

        String patronName, patronID, confirmReturn;
        int counter = 1, returnBookNum = 0;
        boolean verify = false;

        while (true) {
            System.out.print("Please enter your Patron name : ");
            patronName = console.nextLine();
            System.out.print("Please enter your Patron ID : ");
            patronID = console.nextLine();

            for (Patron patronVerify : patrons) {
                if (patronVerify.name.equals(patronName) && patronVerify.ID.equals(patronID)) {
                    for (Book bookBorrowed : books) {
                        if (bookBorrowed.patron.equals(patronID)) {
                            System.out.println("Borrowed Book : \n" + counter + ". ");
                            System.out.println(
                                    "\nTitle : " + bookBorrowed.title + "\nBy Author : " + bookBorrowed.author);
                            System.out.println("\nGenre : " + bookBorrowed.genre);
                            System.out.println("\nISBN : " + bookBorrowed.ISBN + "\tStatus : " + bookBorrowed.status
                                    + "\tBorrower : " + bookBorrowed.patron);
                            bookReturnList.add(bookBorrowed);
                            counter++;
                        }
                    }

                    if (!bookReturnList.isEmpty()) {
                        System.out.print("\nDo you want to return the book [Y/N] : ");
                        confirmReturn = console.nextLine();

                        if (confirmReturn.equalsIgnoreCase("Y") || confirmReturn.equalsIgnoreCase("Yes")) {
                            while (true) {
                                System.out.print("Enter the number of the book you would like to return : ");
                                returnBookNum = Integer.parseInt(console.nextLine());

                                if (returnBookNum >= 1 && returnBookNum <= bookReturnList.size()) {
                                    Book returnedBook = bookReturnList.get(returnBookNum - 1);
                                    returnedBook.status = "AVAILABLE";
                                    returnedBook.patron = "NONE";
                                    saveBook("book.txt", books);
                                    System.out.println("Book Returned successfully");
                                    return;
                                }
                                System.out.print("Invalid Book, Please Try Again");
                            }
                        } else {
                            return;
                        }
                    } else {
                        System.out.println("No Borrowed Book Found");
                        return;
                    }
                }
            }
            System.out.println("Invalid Patron Name or ID, Please Try Again");
        }
    }

    public void bookView() {
        for (Book bookView : books) {
            System.out.println("\nTitle : " + bookView.title + "\nBy Author : " + bookView.author);
            System.out.println("\nGenre : " + bookView.genre);
            System.out.println("\nISBN : " + bookView.ISBN + "\tStatus : " + bookView.status);
        }
        return;
    }
}

class Patron {
    public String name;
    public String email;
    public String ID;

    public Patron(String name, String email, String newID) {
        this.name = name;
        this.ID = newID;
        this.email = email;
    }

    private List<Patron> patrons;

    public Patron() {
        this.patrons = new ArrayList<>();
        try {
            patronList("patron.txt");
        } catch (FileNotFoundException e) {
            System.err.println("\nNo Patron File Loaded : " + e.getMessage());
        }
    }

    public List<Patron> getPatrons() {
        return patrons;
    }

    static Scanner console = new Scanner(System.in);

    public void patronList(String patronFile) throws FileNotFoundException {
        FileReader patronInput = new FileReader("patron.txt");
        Scanner patron = new Scanner(patronInput);

        while (patron.hasNextLine()) {
            String name = patron.nextLine();
            if (name.trim().isEmpty())
                continue;
            String ID = patron.nextLine();
            String email = patron.nextLine();

            patrons.add(new Patron(name, email, ID));
        }
        patron.close();
    }

    public void addPatron(Patron newPatron) throws IOException {
        try (PrintWriter patronWriter = new PrintWriter(new FileWriter("patron.txt", true))) {
            patronWriter.println(newPatron.toString());
        } catch (IOException e) {
            System.err.println("\nPatron Save Unsuccessful : " + e.getMessage());
        }
    }

    public void savePatron(String patronFile) throws IOException {
        try (PrintWriter patronWriter = new PrintWriter(new FileWriter(patronFile))) {
            for (Patron patronSave : patrons) {
                patronWriter.println(patronFile.toString());
            }
        } catch (IOException e) {
            System.err.println("\nPatron Save Unsuccessful : " + e.getMessage());
        }
    }

    public String toString() {
        return name + "\n" + ID + "\n" + email;
    }

    public void pagePatron() throws IOException {
        while (true) {
            System.out
                    .println("\n 1.Register Patron \n 2.View Patron Account \n 3.Return to Main Menu \nEnter [1/2/3]");
            switch (console.nextLine()) {
                case "1":
                    patronRegister();
                    break;

                case "2":
                    patronAccount();
                    break;

                case "3":
                    return;

                default:
                    System.out.println("Invalid Option, Please Try Again");
            }
        }
    }

    public void patronRegister() throws IOException {
        Random IDGenerator = new Random();
        int IDLimit = IDGenerator.nextInt(1, 10000);
        String newID = String.format("%05d", IDLimit);
        String newName, newEmail;

        System.out.print("Please enter your name: ");
        newName = console.nextLine();

        while (true) {
            System.out.print("Please enter your email: ");
            newEmail = console.nextLine();

            for (Patron emailChecker : patrons) {
                if (!emailChecker.email.equals(newEmail)) {
                    System.out.println("Your Patron ID is " + newID);
                    Patron newPatron = new Patron(newName, newID, newEmail);
                    patrons.add(newPatron);
                    addPatron(newPatron);
                    System.out.println("Patron Account created Successfully");
                    return;
                }
            }
            System.out.println("\nEmail Already Exists, Please Try Again\n");
        }
    }

    public void patronAccount() {
        String patronName, patronID;
        while (true) {
            System.out.print("Please enter your Patron name : ");
            patronName = console.nextLine();
            System.out.print("Please enter your Patron ID : ");
            patronID = console.nextLine();

            for (Patron patronVerify : patrons) {
                if (patronVerify.name.equals(patronName) && patronVerify.ID.equals(patronID)) {
                    System.out.println("\nName : " + patronVerify.name);
                    System.out.println("\nID : " + patronVerify.ID);
                    System.out.println("\nE-mail : " + patronVerify.email);
                    return;
                }
            }
            System.out.println("Invalid Patron Name or ID, Please Try Again");
        }
    }
}

class Management {

    public Management() {
    }

    static Scanner console = new Scanner(System.in);

    public void pageManagement() throws IOException {
        while (true) {
            System.out.println(
                    "\n 1.Search Book / Patron \n 2.View All Book / Patron \n 3.Add Book / Patron \n 4.Remove Book / Patron \n 5.Return to Main Menu \nEnter [1/2/3/4/5]");
            switch (console.nextLine()) {
                case "1":
                    searchData();
                    break;

                case "2":
                    viewAll();
                    break;

                case "3":
                    addData();
                    break;

                case "4":
                    removeData();
                    break;

                case "5":
                    return;

                default:
                    System.out.println("Invalid Option, Please Try Again");
            }
        }
    }

    public void searchData() {
        Book booksList = new Book();
        List<Book> books = booksList.getBooks();

        Patron patronsList = new Patron();
        List<Patron> patrons = patronsList.getPatrons();

        String searchInput;
        String bookTitle, bookAuthor, bookISBN;
        String patronName, patronID, patronEmail;

        while (true) {
            System.out.println("\n1.Search For Book \n2.Search For Patron \n Enter [1/2]");
            searchInput = console.nextLine();

            if (searchInput.equals("1")) {
                System.out.println("Enter Book Title : ");
                bookTitle = console.nextLine();

                System.out.println("Enter Book Author : ");
                bookAuthor = console.nextLine();

                System.out.println("Enter Book ISBN : ");
                bookISBN = console.nextLine();

                for (Book bookSearch : books) {
                    if ((bookTitle == null || bookSearch.title.toLowerCase().contains(bookTitle.toLowerCase())
                            || bookTitle.isEmpty())
                            && (bookAuthor == null || bookSearch.author.toLowerCase().contains(bookAuthor.toLowerCase())
                                    || bookAuthor.isEmpty())
                            && (bookISBN == null || bookSearch.ISBN.toLowerCase().contains(bookISBN.toLowerCase())
                                    || bookISBN.isEmpty())) {
                        System.out.println("\nTitle : " + bookSearch.title + "\nBy Author : " + bookSearch.author);
                        System.out.println("\nGenre : " + bookSearch.genre);
                        System.out.println("\nISBN : " + bookSearch.ISBN + "\tStatus : " + bookSearch.status
                                + "\tPatron : " + bookSearch.patron);
                        return;
                    }
                }
            } else if (searchInput.equals("2")) {
                System.out.print("Enter Patron name / None : ");
                patronName = console.nextLine();

                System.out.print("Enter Patron ID / None : ");
                patronID = console.nextLine();

                System.out.print("Enter Patron E-mail / None : ");
                patronEmail = console.nextLine();

                for (Patron patronSearch : patrons) {
                    if ((patronName == null || patronSearch.name.toLowerCase().contains(patronName.toLowerCase())
                            || patronName.isEmpty())
                            && (patronID == null || patronSearch.ID.contains(patronID) || patronID.isEmpty())
                            && (patronEmail == null
                                    || patronSearch.email.toLowerCase().contains(patronEmail.toLowerCase())
                                    || patronEmail.isEmpty())) {
                        System.out.println("\nName : " + patronSearch.name);
                        System.out.println("\nID : " + patronSearch.ID);
                        System.out.println("\nE-mail : " + patronSearch.email);
                        return;
                    }
                }
            }
            System.out.println("Invalid Option, Please Try Again");
        }
    }

    public void viewAll() {
        Book booksList = new Book();
        List<Book> books = booksList.getBooks();

        Patron patronsList = new Patron();
        List<Patron> patrons = patronsList.getPatrons();

        String viewInput;

        while (true) {
            System.out.println("\n1.View All Book \n2.View All Patron \n Enter [1/2]");
            viewInput = console.nextLine();

            if (viewInput.equals("1")) {
                for (Book viewBooks : books) {
                    System.out.println("\nTitle : " + viewBooks.title + "\nBy Author : " + viewBooks.author);
                    System.out.println("\nGenre : " + viewBooks.genre);
                    System.out.println("\nISBN : " + viewBooks.ISBN + "\tStatus : " + viewBooks.status + "\tPatron : "
                            + viewBooks.patron);
                }
                return;
            } else if (viewInput.equals("2")) {
                for (Patron viewPatrons : patrons) {
                    System.out.println("\nName : " + viewPatrons.name);
                    System.out.println("\nID : " + viewPatrons.ID);
                    System.out.println("\nE-mail : " + viewPatrons.email);
                }
                return;
            }
            System.out.println("Invalid Option, Please Try Again");
        }
    }

    public void addData() throws IOException {
        Book UI_Book = new Book();
        Book booksList = new Book();
        List<Book> books = booksList.getBooks();

        Patron UI_Patron = new Patron();

        String addInput;

        String newTitle, newAuthor, newGenre, newISBN, newStatus = "AVAILABLE", newPatron = "NONE";

        while (true) {
            System.out.println("\n1.Add Book \n2.Add Patron \n Enter [1/2]");
            addInput = console.nextLine();

            if (addInput.equals("1")) {
                System.out.print("Enter Book Author : ");
                newAuthor = console.next();
                System.out.print("Enter Book Genre : ");
                newGenre = console.next();
                while (true) {
                    System.out.print("Enter Book Title : ");
                    newTitle = console.next();
                    System.out.print("Enter Book ISBN : ");
                    newISBN = console.next();

                    for (Book bookChecker : books) {
                        if (!bookChecker.title.equalsIgnoreCase(newTitle)
                                && !bookChecker.ISBN.equalsIgnoreCase(newISBN)) {
                            Book newBook = new Book(newTitle, newAuthor, newGenre, newISBN, newStatus, newPatron);
                            books.add(newBook);
                            UI_Book.addBook(newBook);
                            System.out.println("Book Added Successfully");
                            return;
                        }
                    }
                    System.out.println("\nSimilar Book Title or Same Book ISBN Already Exists, Please Try Again\n");
                }
            } else if (addInput.equals("2")) {
                UI_Patron.patronRegister();
                return;
            }
            System.out.println("Invalid Option, Please Try Again");
        }
    }

    public void removeData() throws IOException {
        Book UI_Book = new Book();
        Book booksList = new Book();
        List<Book> books = booksList.getBooks();

        Patron UI_Patron = new Patron();
        Patron patronsList = new Patron();
        List<Patron> patrons = patronsList.getPatrons();

        List<Book> removeBookList = new ArrayList<>();

        String removeInput, removePatronID;
        int removeBookISBN = 0;

        while (true) {
            System.out.println("\n1.Remove Book \n2.Remove Patron \n Enter [1/2]");
            removeInput = console.nextLine();

            if (removeInput.equals("1")) {
                for (Book removeBookChecker : books) {
                    System.out.println(
                            "\nTitle : " + removeBookChecker.title + "\nBy Author : " + removeBookChecker.author);
                    System.out.println("\nGenre : " + removeBookChecker.genre);
                    System.out.println("\nISBN : " + removeBookChecker.ISBN + "\tStatus : " + removeBookChecker.status
                            + "\tPatron : " + removeBookChecker.patron);
                    removeBookList.add(removeBookChecker);
                }

                while (true) {
                    System.out.print("Enter the Book ISBN you would like to remove : ");
                    removeBookISBN = Integer.parseInt(console.nextLine());

                    if (removeBookISBN >= 1 && removeBookISBN <= removeBookList.size()) {
                        Book removeBook = removeBookList.remove(removeBookISBN - 1);
                        UI_Book.saveBook("book.txt", books);
                        System.out.println("Book Remove Successfully");
                        return;
                    }
                    System.out.print("Invalid Book ISBN");
                }
            } else if (removeInput.equals("2")) {
                for (Patron removePatronChecker : patrons) {
                    System.out.println("\nName : " + removePatronChecker.name);
                    System.out.println("\nID : " + removePatronChecker.ID);
                    System.out.println("\nE-mail : " + removePatronChecker.email);
                }

                while (true) {
                    System.out.print("Enter Patron ID you would like to remove : ");
                    removePatronID = console.nextLine();

                    Iterator<Patron> patronIterator = patrons.iterator();
                    while (patronIterator.hasNext()) {
                        Patron patronList = patronIterator.next();
                        if (patronList.ID.equals(removePatronID)) {
                            patronIterator.remove();
                            UI_Patron.savePatron("patron.txt");
                            System.out.println("Patron Account removed Successfully");
                            return;
                        }
                    }
                    System.out.println("Invalid Patron");
                }
            }
            System.out.println("Invalid Option, Please Try Again");
        }
    }
}
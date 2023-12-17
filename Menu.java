import java.util.*;
import java.io.*;

public class Menu {
  static Scanner console = new Scanner(System.in);

  public static void main(String[] args) {
    Booking Sys = new Booking();
    String Email, desiredClass;
    boolean Window, Aisle, Table;
    double Price = 0;

    while (true) {
      System.out.println("\n- - Seat Booking System - -\n");
      System.out.println("- - Main Menu - -\n");
      System.out.println("1 - Reserve Seat");
      System.out.println("2 - Cancel Seat");
      System.out.println("3 - View Seat Reservation");
      System.out.println("Q - Quit");
      System.out.print("Pick: ");
      switch (console.next().toUpperCase()) {
        case "1":
          System.out.print("Enter Your Email: ");
          Email = console.next();

          while (true) {
            System.out.print("Do Your Prefer Standard Class [STD] or First Class [1ST]: ");
            desiredClass = console.next().toUpperCase();
            if (desiredClass.equals("STD") || desiredClass.equals("1ST")) {
              break;
            } else {
              System.out.println("Invalid Input");
            }
          }
          System.out.print("Do Your Prefer Seat By Window [Y|N]: ");
          Window = inputPrompt("");

          System.out.print("Do Your Prefer Seat By Aisle [Y|N]: ");
          Aisle = inputPrompt("");

          System.out.print("Do Your Prefer Seat With Table [Y|N]: ");
          Table = inputPrompt("");

          while (true) {
            System.out.print("Enter Your Maximum Budget: RM ");
            try {
              Price = Double.parseDouble(console.next());
              break;
            } catch (NumberFormatException e) {
              System.out.println("Invalid Input");
            }
          }
          Sys.reserveSeat(Email, desiredClass, Window, Aisle, Table, Price);
          break;

        case "2":
          Sys.cancelSeat();
          break;

        case "3":
          Sys.viewSeat();
          break;

        case "Q":
          System.out.println("\nShutting Down, GoodBye");
          System.exit(0);
          break;

        default:
          System.out.println("\nInvalid Input\n");
      }
    }
  }

  private static boolean inputPrompt(String prompt) {
    while (true) {
      String input = console.next().toUpperCase();
      if (input.equals("Y") || input.equals("YES")) {
        return true;
      } else if (input.equals("N") || input.equals("NO")) {
        return false;
      } else {
        System.out.println("Invalid Input");
      }
    }
  }
}

class Booking {
  static Scanner console = new Scanner(System.in);
  private static List<Seat> seats;

  public Booking() {
    this.seats = new ArrayList<>();
    try {
      loadSeats("seats.txt");
    } catch (FileNotFoundException e) {
      System.out.println("System Failure, No File Loaded");
    }
  }

  private void loadSeats(String file) throws FileNotFoundException {
    FileReader input = new FileReader("seats.txt");
    Scanner read = new Scanner(input);

    while (read.hasNext()) {
      String seatNum = read.next();
      String seatClass = read.next();
      boolean isWindow = Boolean.parseBoolean(read.next());
      boolean isAisle = Boolean.parseBoolean(read.next());
      boolean isTable = Boolean.parseBoolean(read.next());
      double seatPrice = Double.parseDouble(read.next());
      String email = read.next();

      seats.add(new Seat(seatNum, seatClass, isWindow, isAisle, isTable, seatPrice, email));
    }
    read.close();
  }

  private void Save(String file) {
    try (PrintWriter save = new PrintWriter(new FileWriter(file))) {
      for (Seat seat : seats) {
        save.println(seat.toString());
      }
    } catch (IOException e) {
      System.out.println("System Failure, File Save Unsuccessful");
    }
  }

  public void reserveSeat(String email, String desiredClass, boolean window, boolean aisle, boolean table,
      double price) {
    String desireSeat;
    List<Seat> seatFound = new ArrayList<>();

    System.out.println("\n" + Seat.header());
    for (Seat R : seats) {
      if (R.seatClass.equals(desiredClass) && R.isWindow == window && R.isAisle == aisle && R.isTable == table
          && R.seatPrice <= price && R.email.equals("free")) {
        System.out.println(R);
        seatFound.add(R);
      }
    }
    if (seatFound.isEmpty()) {
      for (Seat R : seats) {
        if (R.seatClass.equals(desiredClass) && R.email.equals("free")) {
          System.out.println(R);
          seatFound.add(R);
        }
      }
      System.out.println("\nNo Match Seat Found, Above Are The Available Seat");
    }
    while (!seatFound.isEmpty()) {
      System.out.print("\nEnter a Seat Number to Reserve Seat : ");
      desireSeat = console.next().toUpperCase();
      for (Seat P : seatFound) {
        if (P.seatNum.equals(desireSeat) && P.email.equals("free")) {
          P.email = email;
          System.out.println("Seat Reserved Successfully");
          Save("seats.txt");
          return;
        }
      }
      System.out.println("Invalid Seat Number");
    }
  }

  public void cancelSeat() {
    String inputEmail, num;
    boolean Found = false;

    System.out.print("Enter Your Email : ");
    inputEmail = console.next();

    System.out.println("\n" + Seat.header());
    for (Seat C : seats) {
      if (C.email.equals(inputEmail)) {
        System.out.println(C);
        Found = true;
      }
    }
    if (!Found) {
      System.out.println("\nNo Seat Found With This Email");
      return;
    }

    while (Found) {
      System.out.print("\nEnter Your Seat Number : ");
      num = console.next().toUpperCase();
      for (Seat C : seats) {
        if (C.seatNum.equals(num) && C.email.equals(inputEmail)) {
          C.email = "free";
          System.out.println("Seat Cancel Successfully");
          Save("seats.txt");
          return;
        }
      }
      System.out.println("Invalid Seat Number");
    }
  }

  public void viewSeat() {
    String viewOption, viewEmail;
    boolean emailView = false;

    while (true) {
      System.out.print("View Personal Seat Reservation or All Seat Reservation [P|ALL]: ");
      viewOption = console.next();

      if (viewOption.equalsIgnoreCase("All")) {
        System.out.println("\n" + Seat.header());
        for (Seat V : seats) {
          System.out.println(V);
        }
        return;
      } else if (viewOption.equalsIgnoreCase("P")) {
        System.out.print("Enter Your Email : ");
        viewEmail = console.next();

        while (!viewEmail.isEmpty()) {
          System.out.println("\n" + Seat.header());
          for (Seat C : seats) {
            if (C.email.equals(viewEmail)) {
              System.out.println(C);
              emailView = true;
            }
          }
          if (!emailView) {
            System.out.println("\nNo Seat Found With This Email");
          }
          return;
        }
      } else {
        System.out.println("Invalid Input");
      }
    }
  }
}

class Seat {
  public String seatNum;
  public String seatClass;
  public boolean isWindow;
  public boolean isAisle;
  public boolean isTable;
  public double seatPrice;
  public String email;

  public Seat(String seatNum, String seatClass, boolean isWindow, boolean isAisle, boolean isTable, double seatPrice,
      String email) {
    this.seatNum = seatNum;
    this.seatClass = seatClass;
    this.isWindow = isWindow;
    this.isAisle = isAisle;
    this.isTable = isTable;
    this.seatPrice = seatPrice;
    this.email = email;
  }

  public static String header() {
    return "SEAT\t" + "CLASS\t" + " WINDOW\t" + " AISLE\t" + " TABLE\t" + " PRICE\t" + " EMAIL\t";
  }

  public String toString() {
    return seatNum + "\t " + seatClass + "\t " + isWindow + "\t " + isAisle + "\t " + isTable + "\t " + seatPrice
        + "\t " + email;
  }
}
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Receipt_V1 {

  public static void main(String[] args) {
    Scanner console = new Scanner(System.in);

    // Variable for Date, Time and Tax Number
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm:ss");
    String newDateTime = now.format(formatter);
    Random num = new Random();
    int taxNum = num.nextInt(9000);

    double totalPrice = 0.0, subTotal = 0.0, tax = 0.0, finalTotal = 0.0;

    double cash = 0.0;
    String payPlatform, paymentMethod;

    // Variable to keep items name, quantities, and prices
    String item;
    int qty = 0;
    double price = 0.0;
    List<String> items = new ArrayList<>();
    List<Integer> qtys = new ArrayList<>();
    List<Double> prices = new ArrayList<>();

    System.out.println("WELCOME");
    System.out.println(
        "Please enter your items details below. Type [OK] once finish");

    while (true) {
      System.out.print("Item Name: ");
      item = console.nextLine();
      items.add(item);

      // This section content function to stop the loop and proceed to checkout
      if (item.equalsIgnoreCase("OK") || item.equalsIgnoreCase("Okay")) {
        break;
      }

      // While and Try-Catch are used to keep the loop until a valid input is receive
      System.out.print("Item Quantity: ");
      while (true) {
        try {
          qty = Integer.parseInt(console.nextLine());
          if (qty >= 1) {
            break;
          } else {
            System.out.println("Please enter a valid item quantity");
          }
        } catch (NumberFormatException q) {
          System.out.println("Please enter a valid item quantity");
          System.out.print("Item Quantity: ");
        }
      }
      qtys.add(qty);

      System.out.print("Item Price: ");
      while (true) {
        try {
          price = Double.parseDouble(console.nextLine());
          if (price >= 0.00) {
            break;
          } else {
            System.out.println("Please enter a valid item price");
          }
        } catch (NumberFormatException p) {
          System.out.println("Please enter a valid item price");
          System.out.print("Item Price: ");
        }
      }
      prices.add(price);
    }

    // This section is for payment method input
    while (true) {
      System.out.print("Please Specify Payment Method [CASH / CASHLESS]: ");
      paymentMethod = console.nextLine();

      if (paymentMethod.equalsIgnoreCase("Cash") ||
          paymentMethod.equalsIgnoreCase("Cashs")) {
        break;
      } else if (paymentMethod.equalsIgnoreCase("Cashless")) {
        break;
      } else {
        System.out.println("Please Re-enter Payment Method");
      }
    }

    // This section is for receipt layout
    System.out.println();
    System.out.println("  Cyberjaya Mall SDN BHD (00127)");
    System.out.println(" No 7, Desa Maju, 57100 Cyberjaya");
    System.out.println();
    // taxNum is used for random tax number
    System.out.println("        Tax Invoice " + taxNum);
    System.out.println("-----------------------------------");

    /*
     * why use .size? Because its a array list not normal array
     * why have -1? Because to not print the OK for checkout loop
     */
    for (int index = 0; index < items.size() - 1; index++) {
      totalPrice = prices.get(index) * qtys.get(index);

      // %-12s is for the padding % for printf, -12 for size of padding, s for string
      // in printf
      System.out.printf("%-12s *%-2d RM%-2.2f ~ RM%-2.2f%n", items.get(index), qtys.get(index), prices.get(index),
          totalPrice);
      subTotal += totalPrice;
    }

    System.out.println("-----------------------------------");
    System.out.printf("%-27s RM%.2f%n", "Subtotal:", subTotal);
    System.out.printf("%-27s RM%.2f%n", "Tax 6%:", (tax = (subTotal * 0.06)));
    System.out.printf("%-27s RM%.2f%n", "Total:", (finalTotal = (subTotal + tax)));
    System.out.println();

    while (true) {
      if (paymentMethod.equalsIgnoreCase("Cash") ||
          paymentMethod.equalsIgnoreCase("Cashs")) {
        System.out.printf("%-27s RM", "CASH:");
        cash = console.nextDouble();

        if (cash >= finalTotal) {
          System.out.printf("%-27s RM%.2f%n", "CHANGE:", (cash - finalTotal));
          break;
        } else {
          System.out.println("Insufficient CASH");
        }
      }
      if (paymentMethod.equalsIgnoreCase("Cashless")) {
        System.out.printf("%-28s", "Payment Platform: ");
        payPlatform = console.nextLine();
        System.out.printf("%-27s RM%.2f%n", payPlatform, finalTotal);
        break;
      }
    }

    System.out.println("-----------------------------------");
    /*
     * Why not using the padding method for printing the date and time?
     * Idk, I tried but does not work
     */
    System.out.println("        " + newDateTime);
    System.out.println();
    System.out.println(
        "            Thank You\n  For Shopping At Cyberjaya Mall\n        Please Come Again");
    console.close();
  }
}
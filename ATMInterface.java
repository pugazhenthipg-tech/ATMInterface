import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

/**
 * Task 3 - ATM Interface
 *
 * A small in-memory ATM. The demo PIN is 1234, three wrong attempts lock the
 * card, and successful deposits and withdrawals are recorded in a history.
 */
public class ATMInterface {

    static final String PIN = "1234";
    static final int MAX_PIN_ATTEMPTS = 3;
    static final double MAX_TRANSACTION = 50000.00;
    static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static Scanner scanner = new Scanner(System.in);
    static double balance = 5000.00;
    static ArrayList<String> history = new ArrayList<String>();

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--demo")) {
            runDemo();
        } else {
            runATM();
        }
        scanner.close();
    }

    static void runATM() {
        System.out.println("===== Welcome to SAM ATM =====");

        int attempts = 0;
        boolean loggedIn = false;

        while (attempts < MAX_PIN_ATTEMPTS) {
            String line = readLine("Enter your 4-digit PIN: ");
            if (line == null) {
                System.out.println("Input ended. Goodbye!");
                return;
            }

            if (line.trim().equals(PIN)) {
                loggedIn = true;
                break;
            }

            attempts++;
            if (attempts < MAX_PIN_ATTEMPTS) {
                System.out.println("Wrong PIN. Attempts left: "
                        + (MAX_PIN_ATTEMPTS - attempts) + ".");
            } else {
                System.out.println("Too many wrong attempts. Card blocked.");
            }
        }

        if (!loggedIn) {
            return;
        }

        System.out.println("Login successful.");
        boolean running = true;

        while (running) {
            printMenu();
            String line = readLine("Choose an option: ");
            if (line == null) {
                System.out.println("Input ended. Goodbye!");
                return;
            }

            int choice;
            try {
                choice = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number from 1 to 5.");
                continue;
            }

            switch (choice) {
                case 1:
                    if (!depositFromInput()) {
                        return;
                    }
                    break;
                case 2:
                    if (!withdrawFromInput()) {
                        return;
                    }
                    break;
                case 3:
                    System.out.println("Current balance: " + money(balance));
                    break;
                case 4:
                    printHistory();
                    break;
                case 5:
                    running = false;
                    System.out.println("Thank you for using SAM ATM. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Choose 1 to 5.");
            }
        }
    }

    static boolean depositFromInput() {
        Double amount = readAmount("Enter amount to deposit: ");
        if (amount == null) {
            System.out.println("Input ended. Goodbye!");
            return false;
        }

        depositMoney(amount.doubleValue());
        return true;
    }

    static boolean withdrawFromInput() {
        Double amount = readAmount("Enter amount to withdraw: ");
        if (amount == null) {
            System.out.println("Input ended. Goodbye!");
            return false;
        }

        withdrawMoney(amount.doubleValue());
        return true;
    }

    static void depositMoney(double amount) {
        if (!validTransactionAmount(amount)) {
            return;
        }

        balance = round2(balance + amount);
        record("DEPOSIT", amount);
        System.out.println("Deposited " + money(amount)
                + ". New balance: " + money(balance) + ".");
    }

    static void withdrawMoney(double amount) {
        if (!validTransactionAmount(amount)) {
            return;
        }

        if (amount > balance) {
            System.out.println("Insufficient balance. Available: "
                    + money(balance) + ".");
            return;
        }

        balance = round2(balance - amount);
        record("WITHDRAWAL", amount);
        System.out.println("Withdrew " + money(amount)
                + ". Remaining balance: " + money(balance) + ".");
    }

    static boolean validTransactionAmount(double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            System.out.println("Please enter a valid amount.");
            return false;
        }
        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return false;
        }
        if (amount > MAX_TRANSACTION) {
            System.out.println("Amount cannot be more than "
                    + money(MAX_TRANSACTION) + " per transaction.");
            return false;
        }
        if (!hasTwoDecimals(amount)) {
            System.out.println("Amount can have at most two decimal places.");
            return false;
        }
        return true;
    }

    static boolean hasTwoDecimals(double amount) {
        double cents = amount * 100.0;
        return Math.abs(cents - Math.round(cents)) < 0.000001;
    }

    static double round2(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }

    static String money(double amount) {
        return String.format(Locale.US, "Rs. %,.2f", amount);
    }

    static void record(String type, double amount) {
        String entry = "#" + (history.size() + 1)
                + "  " + TIME_FORMAT.format(new Date())
                + "  " + type
                + "  " + money(amount)
                + "  Balance: " + money(balance);
        history.add(entry);
    }

    static void printHistory() {
        System.out.println();
        System.out.println("----- Transaction history -----");
        if (history.size() == 0) {
            System.out.println("No transactions yet.");
            return;
        }

        for (int i = 0; i < history.size(); i++) {
            System.out.println(history.get(i));
        }
    }

    static void printMenu() {
        System.out.println();
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Check balance");
        System.out.println("4. Transaction history");
        System.out.println("5. Exit");
    }

    /**
     * Reads an amount.
     *
     * @return the amount, or null when input ends
     */
    static Double readAmount(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (line == null) {
                return null;
            }

            try {
                return Double.valueOf(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount, for example 250.50.");
            }
        }
    }

    static String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine();
    }

    static void runDemo() {
        System.out.println("=== ATM Interface (demo) ===");

        balance = 5000.00;
        history.clear();

        System.out.println("Demo PIN is 1234; opening balance is "
                + money(balance) + ".");
        System.out.println("Login with wrong PIN 0000: rejected");
        System.out.println("Login with correct PIN 1234: accepted");

        depositMoney(1500.50);
        withdrawMoney(2000.00);
        withdrawMoney(45000.00);

        System.out.println("Balance: " + money(balance));
        printHistory();
    }
}

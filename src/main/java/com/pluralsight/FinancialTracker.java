package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) throws IOException {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("""
                    ┌─────────────────────────────────────┐
                    │                                     │
                    │      welcome to transaction app     │
                    │                                     │
                    └─────────────────────────────────────┘""");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) {
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>,<time>,<vendor>,<type>,<amount>
        // For example: 2023-04-29,13:45:00,Amazon,PAYMENT,29.99
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                String date = (parts[0]);
                LocalDate realDate = LocalDate.parse(date);
                String time = parts[1];
                LocalTime realTime = LocalTime.parse(time);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);
                Transaction transaction = new Transaction(realDate, realTime, description, vendor, amount);
                transactions.add(transaction);
                System.out.println(transaction);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void addDeposit(Scanner scanner) throws IOException {
        // This method should prompt the user to enter the date, time, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Deposit` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv",true));
            System.out.println("Please add the date of the deposit (Example: 2023-03-14): ");
            String input = scanner.nextLine();
            LocalDate date = LocalDate.parse(input, DATE_FORMATTER);

            System.out.println("Please add  the time of the deposit (Example: 18:24:45): ");
            input = scanner.nextLine();
            LocalTime time = LocalTime.parse(input, TIME_FORMATTER);

            System.out.println("Please add description");
            String description = scanner.nextLine();

            System.out.println("Please enter the name of the vendor: ");
            String vendor = scanner.nextLine();

            System.out.println("Please enter the amount of the deposit: ");
            double depositAmount = scanner.nextDouble();
            scanner.nextLine();
            if (depositAmount < 0) {
                System.out.println("Deposit can't be a negative number. Deposit can only be processed starting from $1");
                depositAmount = 1;
            }
            Transaction payment = new Transaction(date, time, description, vendor, depositAmount * -1);
            transactions.add(payment);
            bufferedWriter.write(date + "|" + time + "|" + description + "|" + vendor + "|" + depositAmount + "\n");
            bufferedWriter.close();
        } catch (DateTimeParseException e) {
            System.out.println("Error: Could not parse date/time!");
        } catch (IOException e) {
            System.out.println("Error: Could not instantiate writer!");
        } catch (Exception e) {
            System.out.println("Error: please try again!");
        }
    }

    private static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Payment` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));
            System.out.println("Please add the date of the payment in this format: (yyyy-mm-dd) ");
            String input = scanner.nextLine();
            LocalDate date = LocalDate.parse(input, DATE_FORMATTER);

            System.out.println("Please add the time of the payment in this format: (hh:mm:ss)");
            input = scanner.nextLine();
            LocalTime time = LocalTime.parse(input, TIME_FORMATTER);

            System.out.println("Please add description");
            String description = scanner.nextLine();

            System.out.println("Please enter the name of the vendor: ");
            String vendor = scanner.nextLine();

            System.out.println("Please enter amount of payment");
            double paymentAmount = scanner.nextDouble();
            scanner.nextLine();
            if (paymentAmount < 0) {
                System.out.println("Error: payment can't be a positive number. Payment can only be processed starting from $1");
                paymentAmount = 1;
            }
            Transaction payment = new Transaction(date, time, description, vendor, +paymentAmount < 0 ? paymentAmount : paymentAmount * -1);
            transactions.add(payment);
            bufferedWriter.write(date + "|" + time + "|" + description + "|" + vendor + "|" + (paymentAmount < 0 ? paymentAmount : paymentAmount) * -1 + "\n");
            bufferedWriter.close();

        } catch (DateTimeParseException e) {
            System.out.println("Error could not parse date/time!");
        } catch (IOException e) {
            System.out.println("Error: Unspecified issue with adding deposit! Check formatting if inputs!");
        } catch (Exception e) {
            System.out.println("Error: Unspecified issue with adding deposit! Check formatting of inputs!");
        }

    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("""
                                  ┌─────────────────────────────────────┐
                                  │                                     │
                                  │      welcome to the ledger menu     │
                                  │                                     │
                                  └─────────────────────────────────────┘""");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, type, and amount.
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.
        /*ArrayList<Transaction> depositList = new ArrayList<>();
        for (Transaction transaction : transactions) {

            if (transaction.getAmount() > 0) {
                depositList.add(transaction);
            }
        }
        if (depositList.isEmpty()) {
            System.out.println("Error: no deposits found!");
        } else {
            for (Transaction transaction : depositList) {
                System.out.println(transaction);
            }
        }*/
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.println(transaction);
            }
        }
    }


    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.
        /*ArrayList<Transaction> paymentList = new ArrayList<>();
        for (Transaction transaction : transactions) {

            if (transaction.getDescription().toLowerCase().contains("payment")) {
                System.out.println(transaction);
            } else {
                System.out.println("Error: no payments found! ");*/
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction);
            }
        }

    }


    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate thisMonth = LocalDate.now();
                    System.out.println("Displaying all transactions for this month:... " + thisMonth.getMonth());
                    filterTransactionsByDate(thisMonth.withDayOfMonth(1), thisMonth);
                    break;
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate lastMonth = LocalDate.now().minusMonths(1);
                    System.out.println("Displaying all transactions for the previous month:... " + lastMonth.getMonth());
                    filterTransactionsByDate(lastMonth.withDayOfMonth(1), lastMonth.withDayOfMonth(lastMonth.lengthOfMonth()));
                    break;
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate thisYear = LocalDate.now();
                    System.out.println("Displaying all transactions for the current year:... " + thisYear.getYear());
                    filterTransactionsByDate(thisYear.withDayOfYear(1), thisYear);
                    break;
                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate lastYear = LocalDate.now().minusYears(1);
                    System.out.println("Displaying all transactions for last year:... " + lastYear.getYear());
                    filterTransactionsByDate(lastYear.withMonth(1).withDayOfMonth(31), lastYear.withMonth(1).withDayOfMonth(31));
                    break;
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, vendor, and amount for each transaction.
                    System.out.println("PLease type the name of the vendor you would like to check for: ");
                    String vendorName = scanner.nextLine();
                    filterTransactionsByVendor(vendorName);
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
        ArrayList<Transaction> transactionsByDate = new ArrayList<>();
        for (Transaction transaction : transactions) {
            LocalDate dateToCheck = transaction.getDate();
            if (dateToCheck.isAfter(startDate.minusDays(1)) && dateToCheck.isBefore(endDate.plusDays(1))) {
                transactionsByDate.add(transaction);
            }
        }
        if (transactionsByDate.isEmpty()) {
            System.out.println("Error: No transaction found with given date range!");

            for (Transaction transaction : transactionsByDate) {
                System.out.println(transaction);
            }
        }
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
        ArrayList<Transaction> transactionByVendor = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                transactionByVendor.add(transaction);
            }
        }
        if (transactionByVendor.isEmpty()) {
            System.out.println("Error: There's no transactions with the given vendor provided! ");

            for (Transaction transaction : transactionByVendor) {
                System.out.println(transaction);
            }
        }
    }
}
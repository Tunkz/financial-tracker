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
            // Used some ASCII to make the intro look somehow fancy
            System.out.println(colors.ANSI_GREEN + """
                    ┌─────────────────────────────────────┐
                    │                                     │
                    │      welcome to transaction app     │
                    │                                     │
                    └─────────────────────────────────────┘""" + colors.ANSI_RESET);
            //Select an option that can lead you to either one of the available resources
            System.out.println(colors.ANSI_GREEN + "Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger" + colors.ANSI_RESET);
            System.out.println(colors.ANSI_RED + "X) Exit" + colors.ANSI_RESET);

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
        String line;
        try {
            // Used the Buffer reader and saved it as br so i can read files with the given file name
            // There's a private static final inside the financial Tracker class that let's me add the "FILE_NAME" everywhere i want
            // Breaking the "|" with the split. method
            // Parts is the name of the array
            // Saved it in the Array transactions with the .add method

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
                try {
                    // added the Thread.sleep method so when code is being processed it takes time and it just doesn't bump in
                    Thread.sleep(1);
                    System.out.println((transaction.getAmount() > 0 ? colors.ANSI_GREEN : colors.ANSI_RED) + transaction + colors.ANSI_RESET);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Here i created a BufferWriter so i can get the user information for the deposit
    // Getting user input so all the information is saved in the new variable i created called payment
    // making sure the number is negative with the * -1 because when you're making a deposit it takes it from your account meaning is a substraction
    // Finally saving the payment variable to the 'transactions" arraylist with the .add
    private static void addDeposit(Scanner scanner) throws IOException {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));
            System.out.println(colors.ANSI_GREEN + "Please add the date of the deposit (Example: 2023-03-14): " + colors.ANSI_RESET);
            String input = scanner.nextLine();
            LocalDate date = LocalDate.parse(input, DATE_FORMATTER);

            System.out.println(colors.ANSI_GREEN + "Please add  the time of the deposit (Example: 18:24:45): " + colors.ANSI_RESET);
            input = scanner.nextLine();
            LocalTime time = LocalTime.parse(input, TIME_FORMATTER);

            System.out.println(colors.ANSI_GREEN + "Please add description" + colors.ANSI_RESET);
            String description = scanner.nextLine();

            System.out.println(colors.ANSI_GREEN + "Please enter the name of the vendor: " + colors.ANSI_RESET);
            String vendor = scanner.nextLine();

            System.out.println(colors.ANSI_GREEN + "Please enter the amount of the deposit: " + colors.ANSI_RESET);
            double depositAmount = scanner.nextDouble();
            scanner.nextLine();
            if (depositAmount < 0) {
                System.out.println(colors.ANSI_RED + "Deposit can't be a negative number. Deposit can only be processed starting from $1" + colors.ANSI_RESET);
                depositAmount = 1;
            }
            Transaction payment = new Transaction(date, time, description, vendor, depositAmount * -1);
            transactions.add(payment);
            bufferedWriter.write(date + "|" + time + "|" + description + "|" + vendor + "|" + depositAmount + "\n");
            bufferedWriter.close();
        } catch (DateTimeParseException e) {
            System.out.println(colors.ANSI_RED + "Error: Could not parse date/time!" + colors.ANSI_RESET);
        } catch (IOException e) {
            System.out.println(colors.ANSI_RED + "Error: Could not instantiate writer!" + colors.ANSI_RESET);
        } catch (Exception e) {
            System.out.println(colors.ANSI_RED + "Error: please try again!" + colors.ANSI_RESET);
        }
    }

    // This does the same thing as addDeposit. Just different words, but the same output and the process is the same.
    private static void addPayment(Scanner scanner) {

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));
            System.out.println(colors.ANSI_GREEN + "Please add the date of the payment in this format: (yyyy-mm-dd) " + colors.ANSI_RESET);
            String input = scanner.nextLine();
            LocalDate date = LocalDate.parse(input, DATE_FORMATTER);

            System.out.println(colors.ANSI_GREEN + "Please add the time of the payment in this format: (hh:mm:ss)" + colors.ANSI_RESET);
            input = scanner.nextLine();
            LocalTime time = LocalTime.parse(input, TIME_FORMATTER);

            System.out.println(colors.ANSI_GREEN + "Please add description" + colors.ANSI_RESET);
            String description = scanner.nextLine();

            System.out.println(colors.ANSI_GREEN + "Please enter the name of the vendor: " + colors.ANSI_RESET);
            String vendor = scanner.nextLine();

            System.out.println(colors.ANSI_GREEN + "Please enter amount of payment" + colors.ANSI_RESET);
            double paymentAmount = scanner.nextDouble();
            scanner.nextLine();
            if (paymentAmount < 0) {
                System.out.println(colors.ANSI_RED + "Error: payment can't be a positive number. Payment can only be processed starting from $1" + colors.ANSI_RESET);
                paymentAmount = 1;
            }
            Transaction payment = new Transaction(date, time, description, vendor, +paymentAmount < 0 ? paymentAmount : paymentAmount * -1);
            transactions.add(payment);
            bufferedWriter.write(date + "|" + time + "|" + description + "|" + vendor + "|" + (paymentAmount < 0 ? paymentAmount : paymentAmount) * -1 + "\n");
            bufferedWriter.close();
            //Found an error
        } catch (DateTimeParseException e) {
            System.out.println(colors.ANSI_RED + "Error could not parse date/time!" + colors.ANSI_RESET);
        } catch (IOException e) {
            System.out.println(colors.ANSI_RED + "Error: Unspecified issue with adding deposit! Check formatting if inputs!" + colors.ANSI_RESET);
        } catch (Exception e) {
            System.out.println(colors.ANSI_RED + "Error: Unspecified issue with adding deposit! Check formatting of inputs!" + colors.ANSI_RESET);
        }

    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println(colors.ANSI_GREEN + "Ledger");
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
            System.out.println("H) Home" + colors.ANSI_RESET);

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
                    break;
                default:
                    System.out.println(colors.ANSI_RED + "Invalid option " + colors.ANSI_RESET);
                    break;
            }
        }
    }

    // Displayed  the Ledger and added the method to the switch
    private static void displayLedger() {

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    //Displayed deposits to the method then added it to the switch
    private static void displayDeposits() {

        for (Transaction transaction : transactions) {
            System.out.println((transaction.getAmount() > 0 ? colors.ANSI_GREEN : colors.ANSI_RED) + transaction + colors.ANSI_RESET);
        }
    }


    // This time i just displayed the payments that are < than 0 ( all numbers will be -)
    private static void displayPayments() {

        for (Transaction transaction : transactions) {
            System.out.println((transaction.getAmount() < 0 ? colors.ANSI_GREEN : colors.ANSI_RED) + transaction + colors.ANSI_RESET);
        }
    }


    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println(colors.ANSI_GREEN + "Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back" + colors.ANSI_RESET);

            String input = scanner.nextLine().trim();
            // Added the "filter" method to make my options broad and not narrow
            switch (input) {
                case "1":

                    LocalDate thisMonth = LocalDate.now();
                    System.out.println(colors.ANSI_GREEN + "Displaying all transactions for this month:... " + colors.ANSI_RESET + thisMonth.getMonth());
                    filterTransactionsByDate(thisMonth.withDayOfMonth(1), thisMonth);
                    break;
                case "2":

                    LocalDate lastMonth = LocalDate.now().minusMonths(1);
                    System.out.println(colors.ANSI_GREEN + "Displaying all transactions for the previous month:... " + colors.ANSI_RESET + lastMonth.getMonth());
                    filterTransactionsByDate(lastMonth.withDayOfMonth(1), lastMonth.withDayOfMonth(lastMonth.lengthOfMonth()));
                    break;
                case "3":

                    LocalDate thisYear = LocalDate.now();
                    System.out.println(colors.ANSI_GREEN + "Displaying all transactions for the current year:... " + colors.ANSI_RESET + thisYear.getYear());
                    filterTransactionsByDate(thisYear.withDayOfYear(1), thisYear);
                    break;
                case "4":

                    LocalDate lastYear = LocalDate.now().minusYears(1);
                    System.out.println(colors.ANSI_GREEN + "Displaying all transactions for last year:... " + colors.ANSI_RESET + lastYear.getYear());
                    filterTransactionsByDate(lastYear.withMonth(1).withDayOfMonth(1), lastYear.withMonth(12).withDayOfMonth(31));
                    // found an error look into it
                    break;
                case "5":

                    System.out.println(colors.ANSI_GREEN + "PLease type the name of the vendor you would like to check for: " + colors.ANSI_RESET);
                    String vendorName = scanner.nextLine();
                    filterTransactionsByVendor(vendorName);
                case "0":
                    running = false;
                default:
                    System.out.println(colors.ANSI_RED + "Invalid option" + colors.ANSI_RESET);
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        ArrayList<Transaction> transactionsByDate = new ArrayList<>();
        for (Transaction transaction : transactions) {
            LocalDate dateToCheck = transaction.getDate();
            // This returns the transactions between start date and end date
            if (dateToCheck.isAfter(startDate.minusDays(1)) && dateToCheck.isBefore(endDate.plusDays(1))) {
                transactionsByDate.add(transaction);
            }
        }
        if (transactionsByDate.isEmpty()) {
            System.out.println(colors.ANSI_RED + "Error: No transaction found!" + colors.ANSI_RESET);
            return;

        }
        for (Transaction transaction : transactionsByDate) {
            System.out.println(transaction);
        }
    }

    private static void filterTransactionsByVendor(String vendor) {
        ArrayList<Transaction> transactionByVendor = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                transactionByVendor.add(transaction);
            }
        }
        if (transactionByVendor.isEmpty()) {
            System.out.println(colors.ANSI_RED + "Error: No transactions recorded! " + colors.ANSI_RESET);
            return;

        }
        for (Transaction transaction : transactionByVendor) {
            System.out.println(transaction);
        }
    }
}
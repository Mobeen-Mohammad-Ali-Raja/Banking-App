import model.*;
import net.sqlitetutorial.DataHandling;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class CLIMenu {
    static Scanner reader = new Scanner(System.in);
    static boolean running = true;
    static Customer currentCustomer;
    static LocalDate today = LocalDate.now();

    public static void main() {
        Logger.log("Program started");
        IO.println("== Acme Teller System ==");

        while (running) {
            showMainMenu();
        }

        IO.println("\nThank you for using Acme Teller System. Goodbye!");
        Logger.log("Program terminated");
    }

    private static void showMainMenu() {
        IO.println("""
                === Main Menu ===
                1. Find Customer
                2. Sign Up Customer
                3. Switch Customer
                4. Run End-of-Day Processing (Trigger Standing Order/Direct Debit checks)
                5. Help
                0. Exit
                """);
        IO.print("Select option: ");

        try {
            byte choice = reader.nextByte();
            reader.nextLine();

            switch (choice) {
                case 1:
                    Logger.log("1. Find Customer");
                    findCustomer();
                    break;
                case 2:
                    Logger.log("2. Sign Up Customer");
                    signUpCustomer();
                    break;
                case 3:
                    Logger.log("3. Switch Customer");
                    switchCustomer();
                    break;
                case 4:
                    Logger.log("4. Run End-of-Day Processing");
                DataHandling.processScheduledPayments();
                break;
            case 5:
                Logger.log("5. Help");
                    help("main menu");
                    break;
                case 0:
                    Logger.log("0. Exit");
                    running = false;
                    break;
                default:
                    Logger.log("Invalid option selected");
                    System.out.println("Invalid option, Try again!\n");
            }
        } catch (Exception e) {
            Logger.log("Error in main menu: " + e.getMessage());
            IO.println("Invalid input. Please enter a number.\n");
            reader.nextLine();
        }
    }

    // User can find the customer by their national ID
    private static void findCustomer() {
        IO.println("\n=== Find Customer ===");
        IO.print("Enter CustomerID: ");

        String customerId = reader.nextLine();
        Logger.log("CustomerID inserted: " + customerId);
        Customer customer = CustomerAuthentication.findCustomerById(customerId);
        currentCustomer = customer;

        if (customer != null) {
            Logger.log("Customer Found");
            IO.println("Customer Found\n");
            customerPortal();
        } else {
            Logger.log("Customer Not Found");
            IO.println("Customer Not Found\n");
            IO.println("""
                    1. Try Again
                    0. Back to Main Menu
                    """);
            IO.print("Select an option: ");

            try {
                byte choice = reader.nextByte();
                reader.nextLine();

                if (choice == 1) {
                    Logger.log("1. Try Again");
                    findCustomer();
                }
                if (choice == 0) {
                    Logger.log("0. Back to Main Menu");
                    showMainMenu();
                } else {
                    Logger.log("Invalid option selected");
                    IO.println("Invalid option, Try again!\n");
                }
            } catch (Exception e) {
                Logger.log("Error in findCustomer: " + e.getMessage());
                IO.println("Invalid input.\n");
                reader.nextLine();
            }
        }
    }

    //    Allows users to create a new customer account
    private static void signUpCustomer() {
        IO.println("\n=== Sign Up Customer ===");
        IO.println("To create a new customer account, please provide the following information.");
        IO.println("""
                1. Continue with Sign Up
                2. Help
                0. Back to Main Menu
                """);
        IO.print("Select an option: ");

        byte option = reader.nextByte();
        reader.nextLine();

        try {
            switch (option) {
                case 1:
                    Logger.log("1. Continue with Sign Up");
                    IO.print("Creating new customer...");
                    createCustomer();
                    break;
                case 2:
                    Logger.log("2. Help");
                    help("sign up");
                    signUpCustomer();
                    break;
                case 0:
                    Logger.log("Back to Main Menu");
                    break;
                default:
                    Logger.log("Invalid option selected");
                    IO.println("Invalid option, Try again\n");
                    signUpCustomer();
            }
        } catch (Exception e) {
            Logger.log("Error in signUpCustomer: " + e.getMessage());
            IO.println("Invalid input.\n");
            reader.nextLine();
            signUpCustomer();
        }
    }

    private static void createCustomer() {
        IO.println("\n=== Create New Customer===");
        IO.println("Please enter the following information:\n");

        // Request customer's full name with validation
        String name = "";
        boolean validName = false;
        while (!validName) {
            IO.print("Full Name:\t");
            name = reader.nextLine().trim();
            Logger.log("Name inserted: " + name);

            if (name.isEmpty()) {
                IO.println("Error: Name cannot be empty.");
            } else if (name.length() < 2) {
                IO.println("Error: Name must be at least 2 characters long.");
            } else if (name.length() > 60) {
                IO.println("Error: Name cannot exceed 60 characters.");
            } else if (!name.matches("^(?!\\s)[a-zA-Z\\s'-]{1,60}$")) {
                IO.println("Error: Name cannot include numbers or special characters");
            } else {
                validName = true;
            }
        }

        // Request customer's National ID with validation
        String nationalID = "";
        boolean validNationalId = false;
        while (!validNationalId) {
            IO.print("National ID Number:\t");
            nationalID = reader.nextLine().trim().toUpperCase();
            Logger.log("National ID number inserted: " + nationalID);

            if (nationalID.isEmpty()) {
                IO.println("Error: National ID cannot be empty.");
            } else if (!CustomerAuthentication.validNationalIdChecker(nationalID)) {
                IO.println("Error: Invalid National ID format. Expected format: 2 letters, 6 numbers, 1 letter (e.g, AB123456C)");
            } else {
                // Checking if national ID already exists in database
                Customer existingCustomer = CustomerAuthentication.findCustomerByNationalId(nationalID);
                if (existingCustomer != null) {
                    IO.println("Error: This National ID is already registered.");
                } else {
                    validNationalId = true;
                }
            }
        }

        // Request customer's Photo ID type (passport or driving license) and number with validation
        IO.println("\n--- Photo Identification ---");
        byte photoIdType = 0;
        boolean validPhotoIdType = false;
        String photoIdTypeStr = "";

        while (!validPhotoIdType) {
            IO.println("""
                    1. Passport
                    2. Driving License
                    3. Help
                    0. Cancel Operation
                    """);
            IO.print("Select an option:\t");

            try {
                String option = reader.nextLine().trim();
                if (option.isEmpty()) {
                    Logger.log("Invalid option selected");
                    IO.println("Please enter an option.");
                    continue;
                }
                photoIdType = Byte.parseByte(option);
                Logger.log("photoIdType inserted: " + photoIdType);

                if (photoIdType == 0) {
                    Logger.log("0. Cancel Operation");
                    IO.println("\nOperation cancelled.");
                    signUpCustomer();
                    return;
                } else if (photoIdType == 3) {
                    Logger.log("3. Help");
                    help("photo id");
                    continue;
                } else if (photoIdType == 1) {
                    Logger.log("Photo id type: Passport");
                    photoIdTypeStr = "Passport";
                    validPhotoIdType = true;
                } else if (photoIdType == 2) {
                    Logger.log("Photo id type: Driving License");
                    photoIdTypeStr = "Driving License";
                    validPhotoIdType = true;
                } else {
                    Logger.log("Invalid option selected");
                    IO.print("Please enter a number between 0 and 3: ");
                }
            } catch (NumberFormatException e) {
                Logger.log("Error message: " + e.getMessage());
                IO.print("Please enter a valid number (0-3): ");
            }
        }

        // Request photo ID number with validation
        String photoIdNumber = "";
        boolean validPhotoNumber = false;

        while (!validPhotoNumber) {
            IO.print("Photo ID Number:\t");
            photoIdNumber = reader.nextLine().trim().toUpperCase();
            Logger.log("Photo ID Number inserted: " + photoIdNumber);

            if (photoIdNumber.trim().isEmpty()) {
                Logger.log("Photo ID Number inserted: empty/null");
                IO.println("Error: Photo ID number cannot be empty.");
            } else if (photoIdTypeStr.equals("Passport") && !CustomerAuthentication.validPassportChecker(photoIdNumber)) {
                IO.println("Error: Invalid Passport number. Expected format: 9 digits (e.g, 123456789)");
            } else if (photoIdTypeStr.equals("Driving License") && !CustomerAuthentication.validDrivingLicenceChecker(photoIdNumber)) {
                IO.println("Error: Invalid Driving License number. Expected format: 16 alphanumeric characters (e.g, MORGA7531162M9IJ");
            } else {
                validPhotoNumber = true;
            }
        }


        // Request customer's address (utility bill, council tax letter) with validation
        IO.println("\n--- Address Information ---");
        byte addressDocumentType = 0;
        boolean validAddressDocumentType = false;
        String addressDocumentTypeStr = "";

        while (!validAddressDocumentType) {
            IO.println("""
                    Please select the address verification document provided:
                    1. Utility Bill (gas, electricity, water)
                    2. Council Tax Letter
                    3. Help
                    0. Cancel Operation
                    """);
            IO.print("Select an option:\t");


            try {
                String option = reader.nextLine();

                if (option.trim().isEmpty()) {
                    Logger.log("Invalid option selected");
                    IO.print("Please enter an option (0-3):\t");
                    continue;
                }
                addressDocumentType = Byte.parseByte(option);

                if (addressDocumentType == 0) {
                    Logger.log("0. Cancel Operation");
                    IO.println("\nOperation cancelled.");
                    signUpCustomer();
                    return;
                } else if (addressDocumentType == 3) {
                    Logger.log("3. Help");
                    help("address verification");
                    continue;
                } else if (addressDocumentType == 1) {
                    Logger.log("Utility Bill type selected");
                    addressDocumentTypeStr = "Utility Bill";
                    validAddressDocumentType = true;
                } else if (addressDocumentType == 2) {
                    Logger.log("Council Tax Letter type selected");
                    addressDocumentTypeStr = "Council Tax Letter";
                    validAddressDocumentType = true;
                } else {
                    Logger.log("Invalid option selected");
                    IO.print("Please enter a number between 0 and 3:");
                }
            } catch (NumberFormatException e) {
                Logger.log("Error message: " + e.getMessage());
                IO.print("Please enter a valid number (0-3):\t");
            }
        }


        // Request for document's ID number or Reference number with validation
        IO.println("\n--- " + addressDocumentTypeStr + " Details ---");
        String addressId = "";
        boolean validRefNumber = false;

        while (!validRefNumber) {
            IO.print(addressDocumentTypeStr + " Reference Number:\t");
            addressId = reader.nextLine().trim();
            Logger.log("Address ID inserted: " + addressId);

            if (addressId.isEmpty()) {
                Logger.log("Address ID inserted: empty/null");
                IO.println("Error: " + addressDocumentTypeStr + " reference number cannot be empty.");
            } else if (addressId.length() < 3) {
                IO.println("Error: Reference number must be at least 3 characters long.");
            } else if (addressId.length() > 30) {
                IO.println("Error: Reference number cannot exceed 30 characters.");
            } else {
                validRefNumber = true;
            }
        }

        // Confirm full details for sign up
        IO.println("\n=== Confirm Customer Details ===");
        IO.println("Name: " + name);
        IO.println("National ID: " + nationalID);
        IO.println("Photo ID Type: " + photoIdTypeStr);
        IO.println("Photo ID: " + photoIdNumber);
        IO.println("Address Document Type: " + addressDocumentTypeStr);
        IO.println("Address Reference Number: " + addressId);

        boolean validConfirm = false;

        while (!validConfirm) {
            IO.println("\n1. Confirm and Create Customer");
            IO.println("2. Edit Details");
            IO.println("3. Help");
            IO.println("0. Cancel Operation");
            IO.print("Select an option:\t");

            try {
                String option = reader.nextLine().trim();
                if (option.isEmpty()) {
                    Logger.log("Option selected: empty/null");
                    IO.print("Please enter an option (0-3)");
                    continue;
                }

                byte confirmOption = Byte.parseByte(option);

                if (confirmOption == 0) {
                    Logger.log("0. Cancel Operation");
                    IO.println("\nOperation cancelled");
                    signUpCustomer();
                    return;
                } else if (confirmOption == 3) {
                    Logger.log("3. Help");
                    help("customer confirmation");
                    // Display confirmation again
                    IO.println("\n=== Confirm Customer Details ===");
                    IO.println("Name: " + name);
                    IO.println("National ID: " + nationalID);
                    IO.println("Photo ID: " + photoIdNumber);
                    IO.println("Address: " + addressDocumentTypeStr);
                    IO.println("Address Reference Number: " + addressId);
                    IO.println("\n1. Confirm and Create Customer");
                    IO.println("2. Edit Details");
                    IO.println("3. Help");
                    IO.println("0. Cancel Operation");
                    IO.print("Select an option:\t");
                    continue;
                } else if (confirmOption == 2) {
                    Logger.log("1. Edit Details");
                    IO.println("\nEditing customer details...\n");
                    createCustomer();
                    return;
                } else if (confirmOption == 1) {
                    Logger.log("1. Confirm and Create Customer");
                    validConfirm = true;
                } else {
                    Logger.log("Invalid option selected");
                    IO.print("Please enter a number between 0 and 3: ");
                }
            } catch (NumberFormatException e) {
                Logger.log("Error message: " + e.getMessage());
                IO.println("Please enter a valid number (0-3): ");
            }
        }

        // Create customer
        try {
            Logger.log("1. Confirm and Create Customer");

            DataHandling.insertCustomer(name, nationalID, photoIdNumber, addressId);
            Logger.log("Data inserted into database: Name: " + name + ", National ID: " + nationalID + ", Photo ID: " + photoIdNumber + ", Adress: " + addressId);

            IO.println("\nCustomer created successfully!");
            IO.println("Customer details have been saved to the database.\n");

            // Find and set new customer as current customer
            Customer newCustomer = CustomerAuthentication.findCustomerByNationalId(nationalID);

            if (newCustomer != null) {
                currentCustomer = newCustomer;
                boolean validAccountChoice = false;

                while (!validAccountChoice) {
                    // Open an account immediately or customer portal
                    IO.println("Would you like to open an account for this customer?");
                    IO.println("""
                            1. Yes, open an account now
                            2. No, return to customer portal
                            3. Help
                            0. Cancel and exit
                            """);
                    IO.print("Select an option:\t");

                    try {
                        String option = reader.nextLine().trim();
                        if (option.trim().isEmpty()) {
                            Logger.log("Option selected: empty/null");
                            IO.print("Please enter an option (0-3):\t");
                            continue;
                        }
                        byte accountChoice = Byte.parseByte(option);

                        if (accountChoice == 0) {
                            Logger.log("0. Cancel and exit");
                            IO.println("\nReturning to main menu.");
                            return;
                        } else if (accountChoice == 3) {
                            Logger.log("3. Help");
                            help("open account after signup");
                            continue;
                        } else if (accountChoice == 1) {
                            Logger.log("1. Yes open an account now");
                            IO.println("\nOpening account for new customer...");
                            openAccount();
                            validAccountChoice = true;
                        } else if (accountChoice == 2) {
                            Logger.log("2. No, return to customer portal");
                            customerPortal();
                            validAccountChoice = true;
                        } else {
                            Logger.log("Invalid option selected");
                            IO.print("Please enter a number between 0 and 3: ");
                        }
                    } catch (NumberFormatException e) {
                        Logger.log("Error message: " + e.getMessage());
                        IO.print("Please enter a valid number (0-3): ");
                    }
                }
            } else {
                IO.println("Error: Could not retrieve the newly created customer.");
            }
        } catch (Exception e) {
            Logger.log("Error message: " + e.getMessage());
            IO.println("\n Error creating customer: " + e.getMessage());
            boolean validErrorOption = false;

            while (!validErrorOption) {
                IO.println("Please try again or contact support.\n");
                IO.println("""
                        1. Try creating customer again
                        0. Return to main menu
                        """);
                IO.print("Select an option:\t");

                try {
                    String option = reader.nextLine().trim();
                    if (option.isEmpty()) {
                        Logger.log("Invalid option selected");
                        IO.print("Please enter an option (0-3): ");
                        continue;
                    }
                    byte errorChoice = Byte.parseByte(option);
                    Logger.log("Error choice: " + errorChoice);

                    if (errorChoice == 0) {
                        Logger.log("0. Return to main menu");
                        IO.println("\nReturning to Main Menu");
                        return;
                    } else if (errorChoice == 1) {
                        IO.println("\nCreating new customer...");
                        createCustomer();
                        validErrorOption = true;
                    } else {
                        Logger.log("Invalid option selected");
                        IO.println("Please enter a number between 0 and 1");
                    }
                } catch (NumberFormatException ex) {
                    Logger.log("Error message: " + ex.getMessage());
                    IO.println("Please enter a valid number (0-1): ");
                }
            }
        }
    }

    // Allows the user to switch customers
    private static void switchCustomer() {
        IO.println("\n=== Switch Customer ===");
        IO.println("This option allows you to switch to another customer session.");
        IO.println("You will need to find an existing customer to switch their session");
        IO.println("""
                1. Find Customer to Switch To
                2. Help
                0. Back to Main Menu
                """);
        IO.print("Select an option: ");

        byte option = reader.nextByte();
        reader.nextLine();

        try {
            switch (option) {
                case 1:
                    Logger.log("1. Find Customer to Switch To");
                    IO.println("Switching to another customer session...");
                    findCustomer();
                    break;
                case 2:
                    Logger.log("2. Help");
                    help("switch customer");
                    switchCustomer();
                    break;
                case 0:
                    Logger.log("0. Back to Main Menu");
                    break;
                default:
                    Logger.log("Invalid option selected");
                    IO.println("Invalid option, Try again!\n");
                    switchCustomer();
            }
        } catch (Exception e) {
            Logger.log("Error in switchCustomer: " + e.getMessage());
            IO.println("Invalid input.\n");
            reader.nextLine();
            switchCustomer();
        }
    }

    // Using context for different cases of help to be shown
    private static void help(String context) {
        IO.println("\n=== Help ===");

        switch (context) {
            case "main menu":
                Logger.log("Main Menu Help Screen");
                IO.println("""
                        --- Main Menu Help ---
                        1. Find Customer - Search for an existing customer by Customer ID
                        2. Sign Up Customer - Register a new customer in the system
                        3. Switch Customer - Change to another customer's session
                        4. Run End-of-Day Processing - Manually trigger all scheduled payments (Standing Orders/Direct Debits)
                        5. Help - Display this help information
                        0. Exit - Close the Acme Teller System
                        """);
                break;
            case "customer portal":
                Logger.log("Customer Portal Help Screen");
                IO.println("""
                        --- Customer Portal Help ---
                        1. View Accounts - See all accounts associated with this customer
                        2. Open Account - Create a new account for this customer
                        3. Switch Customer - Change to another customer's session
                        4. Help - Display this help information
                        0. Back to Main Menu - Return to the main menu
                        """);
                break;
            case "list customer accounts":
                Logger.log("List Customer Accounts Help Screen");
                IO.println("""
                        --- Account List Help ---
                        This screen displays all accounts for the current customer.
                        Each account shows: Account Number, Type, Sort Code, and Balance.
                        
                        Options:
                        1. Select Account - Choose an account to perform operations on
                        2. Help - Display this help information
                        0. Back - Return to Customer Portal
                        """);
                break;
            case "select accounts":
                Logger.log("Select Account Help Screen");
                IO.println("""
                        --- Account Operations Help ---
                        1. Deposit - Add money to the selected account
                        2. Withdraw - Remove money from the selected account
                        3. View Transactions - See transaction history for this account
                        4. Help - Display this help information
                        0. Back to Accounts List - Return to account selection
                        """);
                break;
            case "open account":
                Logger.log("Open Account Help Screen");
                IO.println("""
                        --- Open Account Help ---
                        This process guides you through creating a new account.
                        
                        Account Types:
                        1. Personal - Standard personal banking account
                        2. ISA - Tax-free Individual Savings Account
                        3. Business - Account for business transactions
                        4. Help - Display this help information
                        0. Back - Return to Customer Portal
                        """);
                break;
            case "create account":
                Logger.log("Create Account Help Screen");
                IO.println("""
                        --- Create Account Help ---
                        1. Set Initial Balance - Specify starting balance for the new account
                        2. Help - Display this help information
                        0. Cancel - Abort account creation
                        """);
                break;
            case "sign up":
                Logger.log("Sign Up Help Screen");
                IO.println("""
                        --- Sign Up Customer Help ---
                        This process registers a new customer in the system.
                        You will need to provide personal information and identification.
                        After registration, you can immediately open accounts for the customer.
                        """);
                break;
            case "switch customer":
                Logger.log("Switch Customer Help Screen");
                IO.println("""
                        --- Switch Customer Help ---
                        This allows you to switch between different customer sessions.
                        You need to find an existing customer first before switching to their session.
                        Useful for serving multiple customers in sequence.
                        """);
                break;
            case "photo id":
                Logger.log("Photo ID Help Screen");
                IO.println("""
                        --- Photo ID Help ---
                        This process verifies the customer's identity using a photo ID.
                        
                        1. Passport - International travel document with photo
                        2. Driving License - Government issued driving permit with photo
                        3. Help - Display this help information
                        0. Cancel Operation - Return to previous menu
                        """);
                break;
            case "address verification":
                Logger.log("Address Verification Help Screen");
                IO.println("""
                        --- Address Verification Help ---
                        This process verifies the customer's address through documentation
                        
                        Acceptable documents:
                        1. Utility Bill - Recent bill for gas, electricty or water services
                           (must be less than 3 months old and show customer's name and address)
                        2. Council Tax Letter - Official council tax statement or bill
                           (must be for the current tax year and show customer's name and address)
                        3. Help - Display this help information
                        0. Cancel Operation - Return to previous menu
                        """);
                break;
            case "customer confirmation":
                Logger.log("Customer Confirmation Help Screen");
                IO.println("""
                        --- Confirm Customer Help ---
                        Please review all customer details carefully before submission.
                        
                        1. Confirm and Create Customer - Save customer to database
                        2. Edit Details - Make changes to customer information
                        3. Help - Display this help information
                        0. Cancel Operation - Abort customer creation
                        """);
                break;
            case "open account after signup":
                Logger.log("Open Account After Signup Help Screen");
                IO.println("""
                        --- Open Account After Signup Help ---
                        Options after successful customer creation:
                        
                        1. Yes, open an account now - Proceed to account creation for this customer
                        2. No, return to customer portal - Go to the customer management menu
                        3. Help - Display this help information
                        0. Cancel and exit - Return to main menu
                        """);
                break;
            case "account personal":
                Logger.log("Personal Account Help Screen");
                IO.println("""
                        --- Personal Account Help ---
                        Standard banking account for daily use.
                        
                        Rules & Fees:
                        - Minimum Balance: £1.00 (Account cannot be empty).
                        - Overdraft: Up to £500.00 available.
                        - Fees: None.
                        
                        Operations:
                        1. Deposit: Add funds.
                        2. Withdraw: Remove funds (subject to balance/overdraft).
                        3. View Transactions: See history.
                        4. Set Up Direct Debit: Schedule a payment to a recipient.
                        5. Set Up Standing Order: Schedule regular payments (e.g. Monthly).
                        6. View Scheduled Payments: List all active Direct Debits & Standing Orders.
                        """);
                break;

            case "account isa":
                Logger.log("ISA Account Help Screen");
                IO.println("""
                        --- ISA Account Help ---
                        Tax-free savings account with interest benefits.
                        
                        Rules & Fees:
                        - Limit: Only ONE ISA allowed per customer.
                        - Interest: 2.75% Annual Interest Rate.
                        - Overdraft: Not available for ISAs.
                        
                        Operations:
                        4. Apply Annual Interest: Calculates 2.75% of current balance
                           and adds it to the account (Can only be done once per day).
                        """);
                break;

            case "account business":
                Logger.log("Business Account Help Screen");
                IO.println("""
                        --- Business Account Help ---
                        Account for registered Sole Traders, Ltd Companies, and Partnerships.
                        
                        Rules & Fees:
                        - Annual Fee: £120.00 (Deducted automatically).
                        - Overdraft: Up to £1000.00 available.
                        - Cheque Book: Available upon request.
                        
                        Operations:
                        4. Issue Cheque Book: Registers a cheque book for this account.
                           (System prevents duplicate issuance).
                        """);
                break;
            default:
                Logger.log("General Help Screen");
                IO.println("""
                        --- General Help ---
                        Welcome to Acme Teller System!
                        Use the number keys to navigate through menus.
                        Press 0 or Back options to return to previous menus.
                        Press Help in any menu for context-specific help
                        """);
        }
        IO.print("\nPress Enter to continue...");
        reader.nextLine();
    }

    private static void customerPortal() {
        IO.println(currentCustomer.formattedView());
        IO.println("1. View Accounts");
        IO.println("2. Open Account");
        IO.println("3. Switch Customer");
        IO.println("4. Help");
        IO.println("0. Back to Main Menu\n");
        IO.print("Select option: ");

        try {
            byte choice = reader.nextByte();
            reader.nextLine();

            switch (choice) {
                case 1:
                    Logger.log("1. View Accounts");
                    listCustomerAccounts();
                    break;
                case 2:
                    Logger.log("2. Open Account");
                    openAccount();
                    break;
                case 3:
                    Logger.log("3. Switch Customer");
                    switchCustomer();
                    break;
                case 4:
                    Logger.log("4. Help");
                    help("customer portal");
                    customerPortal();
                    break;
                case 0:
                    Logger.log("0. Back to Main Menu");
                    break;
                default:
                    Logger.log("Invalid option selected");
                    IO.println("Invalid option, Try again!\n");
                    customerPortal();
            }
        } catch (Exception e) {
            Logger.log("Error in customerPortal: " + e.getMessage());
            IO.println("Invalid input.\n");
            reader.nextLine();
            customerPortal();
        }
    }

    private static void listCustomerAccounts() {
        System.out.printf("=== %s (%s) Accounts === %n", currentCustomer.getName(), currentCustomer.getCustomerId());
        Account.listCustomerAccounts(currentCustomer.getCustomerId());

        IO.println("1. Select Account");
        IO.println("2. Help");
        IO.println("0. Back\n");

        IO.print("Select option: ");

        try {
            byte choice = reader.nextByte();
            reader.nextLine();

            switch (choice) {
                case 1:
                    Logger.log("1. Select Account");
                    IO.print("Choose an account: ");
                    byte accountSelection = reader.nextByte();
                    reader.nextLine();
                    selectAccount(accountSelection);
                    break;
                case 2:
                    Logger.log("2. Help");
                    help("list customer accounts");
                    listCustomerAccounts();
                    break;
                case 0:
                    Logger.log("0. Back");
                    customerPortal();
                    break;
                default:
                    Logger.log("Invalid option selected");
                    IO.println("Invalid option, Try again!\n");
                    listCustomerAccounts();
            }
        } catch (Exception e) {
            Logger.log("Error in listCustomerAccounts: " + e.getMessage());
            IO.println("Invalid input.\n");
            reader.nextLine();
            listCustomerAccounts();
        }
    }

    private static void selectAccount(byte accountSelection) {
        int accountId = Account.getAccountIdBySelection(currentCustomer.getCustomerId(), accountSelection);

        if (accountId == -1) {
            Logger.log("Error: User selected invalid account index: " + accountSelection);
            IO.println("Invalid account selection.");
            listCustomerAccounts();
            return;
        }

        String accountType = Transaction.getAccountType(accountId);
        boolean isBusiness = accountType.toUpperCase().contains("BUSINESS");
        boolean isISA = accountType.equalsIgnoreCase("ISA");
        boolean isPersonal = accountType.equalsIgnoreCase("PERSONAL"); // Explicit check
        boolean inAccount = true;

        Logger.log("User entered Account Menu for Account ID: " + accountId + " (" + accountType + ")");

        while (inAccount) {
            System.out.printf("=== %s (%s) Accounts === %n", currentCustomer.getName(), currentCustomer.getCustomerId());
            Transaction.displayAccountDetails(accountId);

            IO.println("1. Deposit");
            IO.println("2. Withdraw");
            IO.println("3. View Transactions");

            if (isISA) {
                IO.println("4. Apply Annual Interest");
                IO.println("5. Help");
            } else if (isBusiness) {
                IO.println("4. Issue Cheque Book");
                IO.println("5. Help");
            } else if (isPersonal) {
                // Options to set up direct debit, standing order and view scheduled payments
                IO.println("4. Set Up Direct Debit");
                IO.println("5. Set Up Standing Order");
                IO.println("6. View Scheduled Payments");
                IO.println("7. Help");
            } else {
                IO.println("4. Help");
            }

            IO.println("0. Back to Accounts List\n");
            IO.print("Select an option: ");

            try {
                byte choice = 0;
                try {
                    choice = reader.nextByte();
                    reader.nextLine();
                } catch (Exception e) {
                    IO.println("Incorrect input, select a valid option");
                    reader.nextLine();
                    continue;
            }

                switch (choice) {
                    case 1:
                        Logger.log("User Selected:1. Deposit");
                        deposit(accountSelection);
                        break;
                    case 2:
                        Logger.log("User Selected:2. Withdraw");
                        withdraw(accountSelection);
                        break;
                    case 3:
                        Logger.log("User Selected:3. View Transactions");
                        viewTransactions(accountId);
                        break;
                    case 4:
                        if (isISA) {
                        Logger.log("User Selected: Apply ISA Interest");
                            DataHandling.applyISAInterest(accountId);
                        } else if (isBusiness) {
                        Logger.log("User Selected: Issue Cheque Book");
                            DataHandling.issueChequeBook(accountId);
                        } else if (isPersonal) {
                        // Direct Debit Setup
                        Logger.log("User Selected: Setup Direct Debit");
                        IO.print("Enter Recipient Name: ");
                        String recipient = reader.nextLine();
                        IO.print("Enter Amount: £");
                        try {
                            double amount = reader.nextDouble();
                            reader.nextLine();
                            IO.print("Enter Start Date (dd/mm/yyyy): ");
                            String dateInput = reader.nextLine();
                            LocalDate startDate = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            if (!startDate.isAfter(today)) {
                                IO.println("Start date must be after today.");
                            } else {
                                DataHandling.setupDirectDebit(accountId, recipient, amount, dateInput);
                            }
                        } catch(Exception e) {
                            reader.nextLine();
                            IO.println("Invalid input.");
                        }
                    }
                    break;

                    case 5:
                        if (isPersonal) {
                            Logger.log("User Selected: Setup Standing Order");
                            IO.println("\n--- Set Up Standing Order ---");
                            IO.println("(Enter '0' at any step to cancel)");

                            // Recipient
                            IO.print("Enter Recipient Name: ");
                            String recipient = reader.nextLine();

                            // Amount
                            double amount = 0;
                            boolean validAmount = false;
                            boolean cancelled = false;

                            while (!validAmount) {
                                IO.print("Enter Amount: £");
                                String amountInput = reader.nextLine().trim();

                                if (amountInput.equals("0")) {
                                    IO.println("Operation cancelled.");
                                    cancelled = true;
                                    break;
                                }

                                try {
                                    amount = reader.nextDouble();
                                    reader.nextLine(); // ClearS buffer
                                    if (amount > 0) {
                                        validAmount = true;
                                    } else {
                                        IO.println("Error: Amount must be positive.");
                                    }
                                } catch (Exception e) {
                                    reader.nextLine(); // Clear invalid input
                                    IO.println("Error: Please enter a valid number.");
                                }
                            }
                            if (cancelled) continue;

                            // Frequency Validation
                            String freq = "";
                            boolean validFreq = false;
                            while (!validFreq) {
                                IO.print("Enter Frequency (Daily, Weekly, Monthly, Yearly): ");
                                String input = reader.nextLine().trim();

                                // Check against the allowed list
                                if (input.equalsIgnoreCase("Daily") ||
                                        input.equalsIgnoreCase("Weekly") ||
                                        input.equalsIgnoreCase("Monthly") ||
                                        input.equalsIgnoreCase("Yearly")) {
                                    freq = input;
                                    validFreq = true;
                                } else {
                                    IO.println("Error: Invalid frequency. You must enter Daily, Weekly, Monthly, or Yearly.");
                                }
                            }
                            if (cancelled) continue;

                            // Date entry
                            IO.print("Enter Start Date (dd/mm/yyyy): ");
                            String dateInput = reader.nextLine();

                            // Sends data to Database
                            DataHandling.setupStandingOrder(accountId, recipient, amount, freq, dateInput);
                        }
                        if (isISA) {
                            help("account isa");
                        } else if (isBusiness) {
                            help("account business");
                        }
                    break;

                    // View personal scheduled payments
                    case 6:
                        if (isPersonal) {
                            Logger.log("User Selected: View Scheduled Payments");
                            DataHandling.viewScheduledPayments(accountId);
                        } else {
                            IO.println("Invalid option.");
                        }
                    break;

                    // Personal accounts
                    case 7:
                        if (isPersonal) {
                            help("account personal");
                        } else {
                            IO.println("Invalid option.");
                        }
                    break;
                    case 0:
                        Logger.log("User Selected: 0. Back");
                        inAccount = false;
                        listCustomerAccounts();
                    break;
                    default:
                        Logger.log("Invalid Option Selected in Account Menu");
                        IO.println("Invalid option, Try again!\n");
                }
            } catch (Exception e) {
                Logger.log("Error in selectAccount: " + e.getMessage());
                IO.println("Invalid input.\n");
                reader.nextLine();
            }
        }
    }

    private static void deposit(byte accountSelection) {
        IO.println("\n=== Deposit ===");

        int accountId = Account.getAccountIdBySelection(currentCustomer.getCustomerId(), accountSelection);

        if (currentCustomer == null) {
            IO.println("Error: No customer selected.");
            return;
        }

        IO.println("Enter the amount you wish to deposit into the account");
        IO.print("Enter deposit amount: £");

        try {
            double amount = reader.nextDouble();
            reader.nextLine();

            if (amount <= 0) {
                Logger.log("Invalid deposit amount");
                IO.println("Deposit amount must be greater than zero!\n");
                return;
            }


            IO.println("Depositing: £" + amount);
            Logger.log("Depositing: £" + amount);
            DataHandling.deposit(accountId, amount);

        } catch (Exception e) {
            Logger.log("Error in deposit: " + e.getMessage());
            IO.println("Invalid amount entered. Please enter a numeric value.\n");
            reader.nextLine();
        }
    }

    private static void withdraw(byte accountSelection) {
        IO.println("\n=== Withdraw ===");

        int accountId = Account.getAccountIdBySelection(currentCustomer.getCustomerId(), accountSelection);

        if (currentCustomer == null) {
            Logger.log("Customer object is null");
            IO.println("Error: No customer selected.");
        }

        IO.println("Enter the amount you wish to withdraw from the account.");
        IO.print("Enter withdrawal amount: £");

        try {
            double amount = reader.nextDouble();
            reader.nextLine();

            if (amount <= 0) {
                Logger.log("Invalid withdrawal amount");
                IO.println("Withdrawal amount must be greater than zero!\n");
                return;
            }

            IO.println("Withdrawing: £" + amount);
            Logger.log("Data inserted into database : Amount: " + amount + " using Account ID: " + accountId);
            DataHandling.withdraw(accountId, amount);

        } catch (Exception e) {
            Logger.log("Error message: " + e.getMessage());
            IO.println("Invalid amount entered. Please enter a numeric value.\n");
            reader.nextLine();
        }
    }

    private static void viewTransactions(int accountSelection) {
        IO.println("\n=== View Transactions ===");
        Transaction.listTransactionHistory(accountSelection);
        IO.println("0. Back to Customer Portal\n");
        IO.print("Select an option: ");

        try {
            byte choice = reader.nextByte();
            reader.nextLine();
            IO.println(choice);

            if (choice == 0) {
                Logger.log("0. Back to Customer Portal");
                customerPortal();
            } else {
                Logger.log("Incorrect option, returning to customer portal");
                IO.println("Incorrect option, returning to customer portal\n");
            }
        } catch (Exception e) {
            Logger.log("Error in viewTransactions: " + e.getMessage());
            IO.println("Invalid input, enter a valid option");
            reader.nextLine();
            viewTransactions(accountSelection);
        }
    }

    private static void openAccount() {
        IO.println("""
                \n=== Open Account ===
                === Select Account Type ===
                1. Personal
                2. ISA
                3. Business
                4. Help
                0. Back to Customer Portal
                """);
        IO.print("Select an option: ");

        try {
            byte choice = reader.nextByte();
            reader.nextLine();

            String accountType = "";
            boolean validChoice = true;

            switch (choice) {
                case 1:
                    Logger.log("1. Personal");
                    accountType = "Personal";
                    break;
                case 2:
                    Logger.log("2. ISA");
                    accountType = "ISA";
                    break;
                case 3:
                    Logger.log("3. Business");
                    accountType = "Business";
                    break;
                case 4:
                    Logger.log("4. Help");
                    help("open account");
                    openAccount();
                    break;
                case 0:
                    Logger.log("0. Back to Customer Portal");
                    customerPortal();
                    break;
                default:
                    Logger.log("Invalid option selected");
                    IO.println("Invalid option, Try again!\n");
                    openAccount();
                    return;
            }

            if (!accountType.isEmpty()) {
                createAccount(accountType);
            }
        } catch (Exception e) {
            Logger.log("Error in openAccount: " + e.getMessage());
            IO.println("Invalid input.\n");
            reader.nextLine();
            openAccount();
        }
    }

    private static void createAccount(String accountType) {
        IO.println("\n=== Create " + accountType.toUpperCase() + " Account ===");

        // 1. ISA Check
        if (accountType.equalsIgnoreCase("ISA")) {
            if (DataHandling.customerHasISA(currentCustomer.getCustomerId())) {
                Logger.log("Blocked: Customer " + currentCustomer.getCustomerId() + " attempted second ISA.");
                IO.println("Error: Customer already has an ISA Account. Limit is one per customer.");
                IO.print("\nPress Enter to return...");
                reader.nextLine();
                customerPortal();
                return;
            }
        }

        // 2. Business Check
        if (accountType.equalsIgnoreCase("Business")) {
            if (DataHandling.customerHasBusiness(currentCustomer.getCustomerId())) {
                Logger.log("Blocked: Customer " + currentCustomer.getCustomerId() + " attempted second Business Account.");
                IO.println("Error: Customer already has a Business Account. Limit is one per customer.");
                IO.print("\nPress Enter to return...");
                reader.nextLine();
                customerPortal();
                return;
            }
        }

        // 3. Business Type Selection
        if (accountType.equalsIgnoreCase("Business")) {
            boolean validBusinessType = false;
            while (!validBusinessType) {
                IO.println("\nPlease select Business Type:");
                IO.println("1. Sole Trader");
                IO.println("2. Ltd");
                IO.println("0. Cancel");
                IO.print("Selection: ");
                // TODO: Fix try catch
                byte typeChoice = 0;
                try {
                    typeChoice = reader.nextByte();
                    reader.nextLine();
                } catch (Exception e) {
                    reader.nextLine();
                    Logger.log("Invalid Input during Business Type Selection");
                    IO.println("Invalid input. Please enter a number.");
                    continue;
                }

                if (typeChoice == 0) {
                    Logger.log("User Cancelled Business Account Creation");
                    IO.println("Operation cancelled.");
                    return;
                }

                if (typeChoice == 1) {
                    accountType = "Business (Sole Trader)";
                    validBusinessType = true;
                } else if (typeChoice == 2) {
                    accountType = "Business (Ltd)";
                    validBusinessType = true;
                } else {
                    Logger.log("Invalid Business Type Selection: " + typeChoice);
                    IO.println("Error: Invalid selection. Eligible types: Sole Trader and Ltd.");
                }
            }
        }

        // 4. Balance Selection with Logging
        double balance = 0;
        boolean validBalance = false;

        while (!validBalance) {
            IO.println("\n--- Account Setup ---");
            IO.println("1. Set Initial Balance");
            IO.println("2. Help");
            IO.println("0. Cancel");
            IO.print("Select an option: ");

            byte choice = 0;
            try {
                choice = reader.nextByte();
                reader.nextLine();
            } catch (Exception e) {
                reader.nextLine();
                IO.println("\nError: Incorrect type, enter a valid option (0-2)");
                continue;
            }

            if (choice == 0) {
                Logger.log("User Cancelled Account Creation");
                IO.println("Operation cancelled.");
                openAccount();
                return;
            } else if (choice == 2) {
                help("create account");
                continue;
            } else if (choice == 1) {
                IO.print("Enter initial deposit amount: £");
                try {
                    balance = reader.nextDouble();
                    reader.nextLine();
                } catch (Exception e) {
                    Logger.log("Input Error: Invalid non-numeric balance entered");
                    IO.println("Invalid amount entered. Please try again.");
                    reader.nextLine();
                    continue;
                }

                // --- NEW LOGGING FOR VALIDATION FAILURES ---
                if (balance < 0) {
                    Logger.log("Validation Fail: User entered negative balance: " + balance);
                    IO.println("Error: Opening balance cannot be negative.");
                    continue;
                }

                if (accountType.equalsIgnoreCase("Personal") && balance < 1.00) {
                    Logger.log("Validation Fail: Personal Account balance < £1.00");
                    IO.println("Error: Personal Accounts require a minimum opening balance of £1.00.");
                    continue;
                }

                validBalance = true;
            } else {
                IO.println("Invalid option (out of bounds), enter a valid option (0-2)");
            }
        }

        // 5. Confirmation
        IO.println("\nCreating " + accountType + " account with opening balance: £" + balance);
        IO.println("1. Confirm");
        IO.println("0. Cancel");
        IO.print("Select: ");

        byte confirm = 0;
        try {
            confirm = reader.nextByte();
            reader.nextLine();
        } catch (Exception e) { reader.nextLine(); }

        if (confirm == 1) {
            Logger.log("User Confirmed Creation of " + accountType + " Account.");
            DataHandling.createAccount(currentCustomer.getCustomerId(), accountType, balance);

            IO.println(accountType + " Account Created Successfully!");
            IO.println("=== Back to Customer Portal ===");
            customerPortal();
        } else {
            Logger.log("User Cancelled Confirmation.");
            IO.println("Account creation cancelled.");
            customerPortal();
        }
    }

}
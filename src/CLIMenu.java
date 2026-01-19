import model.*;
import net.sqlitetutorial.DataHandling;

import java.util.Scanner;

public class CLIMenu {
    static Scanner reader = new Scanner(System.in);
    static boolean running = true;
    static Customer currentCustomer;

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
                4. Help
                0. Exit
                """);
        IO.print("Select option: ");

        // TODO: try catch
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
                Logger.log("4. Help");
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
    }

    // User can find the customer by their national ID
    private static void findCustomer() {
        IO.println("\n=== Find Customer ===");
        IO.print("Enter CustomerID: ");

        String customerId = reader.nextLine();
        Logger.log("CustomerID inserted: " + customerId);
        Customer customer = CustomerAuthentication.findCustomerById(customerId);
        currentCustomer = customer;

        // TODO: Customer search logic
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

            byte choice = reader.nextByte();
            reader.nextLine();

            if (choice == 1) {
                Logger.log("1. Try Again");
                findCustomer();
            }
            if (choice == 0) {
                Logger.log("0. Back to Main Menu");
                showMainMenu();
            }else {
                Logger.log("Invalid option selected");
                IO.println("Invalid option, Try again!\n");
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

        switch (option) {
            case 1:
                Logger.log("1. Continue with Sign Up");
                // TODO: Logic for creating customer
                IO.print("Creating new customer...");
//                IO.println("Customer created successfully!\n");
//                customerPortal();
                createCustomer();
                break;
            case 2:
                Logger.log("2. Help");
                help("sign up");
                signUpCustomer();
                break;
            case 0:
                Logger.log("Back to Main Menu");
                showMainMenu();
                break;
            default:
                Logger.log("Invalid option selected");
                IO.println("Invalid option, Try again\n");
                signUpCustomer();
        }
    }

    private static void createCustomer() {
        IO.println("\n=== Create New Customer===");
        IO.println("Please enter the following information:\n");

        // Request customer's full name
        IO.print("Full Name:\t");
        String name = reader.nextLine();
        Logger.log("Name inserted: " + name);

        // TODO: Validation for full name

        // Request customer's National ID
        IO.print("National ID Number:\t");
        String nationalID = reader.nextLine();
        Logger.log("National ID number inserted: " + nationalID);

        // TODO: Validation for national id

        // Request customer's Photo ID type (passport or driving license) and number
        IO.println("\n--- Photo Identification ---");
        IO.println("""
                1. Passport
                2. Driving License
                3. Help
                0. Cancel Operation
                """);
        IO.print("Select an option:\t");

        byte photoIdType = 0;
        boolean validPhotoIdType = false;

        while (!validPhotoIdType) {
            try {
                String option = reader.nextLine();
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
                    IO.println("\n--- Photo Identification ---");
                    IO.println("""
                            1. Passport
                            2. Driving License
                            3. Help
                            0. Cancel Operation
                            """);
                    IO.print("Select an option:\t");
                } else if (photoIdType >= 1 && photoIdType <= 2) {
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

        String photoIdTypeStr = "";
        switch (photoIdType) {
            case 1:
                Logger.log("Photo id type: Passport");
                photoIdTypeStr = "Passport";
                break;
            case 2:
                Logger.log("Photo id type: Driving License");
                photoIdTypeStr = "Driving License";
                break;
        }

        IO.print("Photo ID Number: ");
        String photoIdNumber = reader.nextLine();
        Logger.log("Photo ID Number inserted: " + photoIdNumber);

        while (photoIdNumber.trim().isEmpty()) {
            Logger.log("Photo ID Number inserted: empty/null");
            IO.println("Photo ID number cannot be empty.");
            IO.print("Photo ID Number:\t");
            photoIdNumber = reader.nextLine();
        }


        // Request customer's address (utility bill, council tax letter)
        IO.println("\n--- Address Information ---");
        IO.println("""
                Please select the address verification document provided:
                1. Utility Bill (gas, electricity, water)
                2. Council Tax Letter
                3. Help
                0. Cancel Operation
                """);
        IO.print("Select an option:\t");

        byte addressDocumentType = 0;
        boolean validAddressDocumentType = false;

        while (!validAddressDocumentType) {
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
                    IO.println("\n--- Address Information ---");
                    IO.println("""
                            Please select the address verification document provided:
                            1. Utility Bill (gas, electricity, water)
                            2. Council Tax Letter
                            3. Help
                            0. Cancel Operation
                            """);
                    IO.print("Select an option:\t");
                    continue;
                } else if (addressDocumentType >= 1 && addressDocumentType <= 2) {
                    Logger.log("Correct option type selected");
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

        String addressDocumentTypeStr = "";
        switch (addressDocumentType) {
            case 1:
                Logger.log("Utility Bill type selected");
                addressDocumentTypeStr = "Utility Bill";
                break;
            case 2:
                Logger.log("Council Tax Letter type selected");
                addressDocumentTypeStr = "Council Tax Letter";
                break;
        }

        // Request for document's ID number or Reference number
        IO.println("\n--- " + addressDocumentTypeStr + " Details ---");

        IO.print(addressDocumentTypeStr + " Reference Number:\t");
        String addressId = reader.nextLine();
        Logger.log("Address ID inserted: " + addressId);

        while (addressId.trim().isEmpty()) {
            Logger.log("Address ID inserted: empty/null");
            IO.println(addressDocumentTypeStr + " reference number cannot be empty.");
            IO.print(addressDocumentTypeStr + " Reference Number:\t");
            addressId = reader.nextLine();
        }

        // Confirm full details for sign up
        IO.println("\n=== Confirm Customer Details ===");
        IO.println("Name: " + name);
        IO.println("National ID: " + nationalID);
        IO.println("Photo ID Type: " + photoIdTypeStr);
        IO.println("Photo ID: " + photoIdNumber);
        IO.println("Address Document Type: " + addressDocumentTypeStr);
        IO.println("Address Reference Number: " + addressId);
        IO.println("\n1. Confirm and Create Customer");
        IO.println("2. Edit Details");
        IO.println("3. Help");
        IO.println("0. Cancel Operation");
        IO.print("Select an option:\t");

        byte confirmOption = 0;
        boolean validConfirm = false;

        while (!validConfirm) {
            try {
                String option = reader.nextLine();

                if (option.trim().isEmpty()) {
                    Logger.log("Option selected: empty/null");
                    IO.print("Please enter an option (0-3)");
                    continue;
                }

                confirmOption = Byte.parseByte(option);

                if (confirmOption == 0) {
                    Logger.log("0. Cancel Operation");
                    IO.println("\nOperation cancelled");
                    signUpCustomer();
                    return;
                } else if (confirmOption == 3) {
                    Logger.log("3. Help");
                    help("customer confirmation");
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
                } else if (confirmOption >= 1 && confirmOption <= 2) {
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

        switch (confirmOption) {
            case 1: // Confirm and insert customer into database
                try {
                    Logger.log("1. Confirm and Create Customer");

                    DataHandling.insertCustomer(name, nationalID, photoIdNumber, addressId);
                    Logger.log("Data inserted into database: Name: " + name + ", National ID: " + nationalID + ", Photo ID: " + photoIdNumber + ", Adress: " + addressId);
//                    Customer customer = CustomerAuthentication.findCustomerByNationalId(nationalID);

                    IO.println("\nCustomer created successfully!");
                    IO.println("Customer details have been saved to the database.\n");


                    // Open an account immediately or customer portal
                    IO.println("Would you like to open an account for this customer?");
                    IO.println("""
                            1. Yes, open an account now
                            2. No, return to customer portal
                            3. Help
                            0. Cancel and exit
                            """);
                    IO.print("Select an option:\t");

                    byte accountChoice = 0;
                    boolean validAccountChoice = false;

                    while (!validAccountChoice) {
                        try {
                            String option = reader.nextLine();

                            if (option.trim().isEmpty()) {
                                Logger.log("Option selected: empty/null");
                                IO.print("Please enter an option (0-3): ");
                                continue;
                            }

                            accountChoice = Byte.parseByte(option);

                            if (accountChoice == 0) {
                                Logger.log("0. Cancel and exit");
                                IO.println("\nReturning to main menu.");
                                showMainMenu();
                            } else if (accountChoice == 3) {
                                Logger.log("3. Help");
                                help("open account after signup");
                                IO.println("\nWould you like to open an account for this customer?");
                                IO.println("""
                                        1. Yes, open an account now
                                        2. No, return to customer portal
                                        3. Help
                                        0. Cancel and exit
                                        """);
                                IO.print("Select an option:\t");
                                continue;
                            } else if (accountChoice >= 1 && accountChoice <= 2) {
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

                    if (accountChoice == 1) {
                        Logger.log("1. Yes, open an account now");
                        IO.println("\nOpening account for new customer...");
                        openAccount();
                    } else {
                        Logger.log("2. No, return to customer portal");
                        customerPortal();
                    }
                } catch (Exception e) {
                    Logger.log("Error message: " + e.getMessage());
                    IO.println("\n Error creating customer: " + e.getMessage());
                    IO.println("Please try again or contact support.\n");

                    IO.println("""
                            1. Try creating customer again
                            0. Return to main menu
                            """);

                    byte errorChoice = 0;
                    boolean validErrorOption = false;

                    while (!validErrorOption) {
                        try {
                            String option = reader.nextLine();

                            if (option.trim().isEmpty()) {
                                Logger.log("Invalid option selected");
                                IO.print("Please enter an option (0-3): ");
                                continue;
                            }

                            errorChoice = Byte.parseByte(option);

                            if (errorChoice == 0) {
                                Logger.log("0. Return to main menu");
                                IO.println("\nReturning to Main Menu");
                                showMainMenu();
                            } else if (errorChoice == 1) {
                                validErrorOption = true;
                            } else {
                                Logger.log("Invalid option selected");
                                IO.println("Please enter a number between 0 and 1");
                            }
                        } catch (NumberFormatException _) {
                            Logger.log("Error message: " + e.getMessage());
                            IO.println("Please enter a valid number (0-1): ");
                        }
                    }

                    Logger.log("1. Try creating customer again");
                    IO.println("\nCreating new customer...");
                    createCustomer();
                }
                break;
            case 2: // Edit details by starting over
                Logger.log("2. Edit Details");
                IO.println("\nEditing customer details...\n");
                createCustomer();
                break;
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
                showMainMenu();
                break;
            default:
                Logger.log("Invalid option selected");
                IO.println("Invalid option, Try again!\n");
                switchCustomer();
        }

        IO.println("Customer session started\n");

        // Go back to find customer
        findCustomer();
    }

    // Using context for different cases of help to be shown
    private static void help(String context) {
        // TODO: Add the text for each help option
        IO.println("\n=== Help ===");

        switch (context) {
            case "main menu":
                Logger.log("Main Menu Help Screen");
                IO.println("""
                        --- Main Menu Help ---
                        1. Find Customer - Search for an existing customer by Customer ID
                        2. Sign Up Customer - Register a new customer in the system
                        3. Switch Customer - Change to another customer's session
                        4. Help - Display this help information
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
                Logger.log("Create Acccount Help Screen");
                IO.println("""
                        --- Create Account Help ---
                        1. Set Initial Balance - Specify starting balance for the new account
                        2. Enable Overdraft - Allow account to go below zero (subject to approval)
                        3. Complete Account Creation - Finish setup and create the account
                        4. Help - Display this help information
                        0. Cancel - Abort account creation
                        """);
                break;
            case "sign up": // To be properly implemented
                Logger.log("Sign Up Help Screen");
                IO.println("""
                        --- Sign Up Customer Help ---
                        This process registers a new customer in the system.
                        You will need to provide personal information and identification.
                        After registration, you can immediately open accounts for the customer.
                        """);
                break;
            case "switch customer": // To be properly implemented
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
        boolean inPortal = true;
        // Printf to include the customer name variable
//        IO.println("=== John Smith | CUST001 ===");
        IO.println(currentCustomer.formattedView());
        IO.println("1. View Accounts");
        IO.println("2. Open Account");
        IO.println("3. Switch Customer");
        IO.println("4. Help");
        IO.println("0. Back to Main Menu");


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
                inPortal = false;
                switchCustomer();
                break;
            case 4:
                Logger.log("4. Help");
                help("customer portal");
                break;
            case 0:
                Logger.log("0. Back to Main Menu");
                inPortal = false;
                IO.println();
                break;
            default:
                Logger.log("Invalid option selected");
                IO.println("Invalid option, Try again!\n");
        }
    }

    private static void listCustomerAccounts() {
        // TODO: Logic for retrieving customer accounts



        System.out.printf("=== %s (%s) Accounts === %n", currentCustomer.getName(), currentCustomer.getCustomerId());
        Account.listCustomerAccounts(currentCustomer.getCustomerId());

        IO.println("1. Select Account");
        IO.println("2. Help");
        IO.println("0. Back");

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
                break;
            case 0:
                Logger.log("0. Back");
                customerPortal();
                break;
            default:
                Logger.log("Invalid option selected");
                IO.println("Invalid option, Try again!\n");
        }
    }

    private static void selectAccount(byte accountSelection) {
        int accountId = Account.getAccountIdBySelection(currentCustomer.getCustomerId(), accountSelection);

        if (accountId == -1) {
            Logger.log("Invalid account selected");
            IO.println("Invalid account selection.");
            listCustomerAccounts();
            return;
        }

        boolean inAccount = true;

        while (inAccount) {
            System.out.printf("=== %s (%s) Accounts === %n", currentCustomer.getName(), currentCustomer.getCustomerId());
            Transaction.displayAccountDetails(accountId);


            IO.println("1. Deposit");
            IO.println("2. Withdraw");
            IO.println("3. View Transactions");
            IO.println("4. Help");
            IO.println("0. Back to Accounts List");
            IO.print("Select an option: ");

            byte choice = reader.nextByte();
            reader.nextLine();

            switch (choice) {
                case 1:
                    Logger.log("1. Deposit");
                    deposit(accountSelection);
                    break;
                case 2:
                    Logger.log("2. Withdraw");
                    withdraw(accountSelection);
                    break;
                case 3:
                    Logger.log("3. View Transactions");
                    viewTransactions(accountId);
                    break;
                case 4:
                    Logger.log("4. Help");
                    help("select accounts");
                    break;
                case 0:
                    Logger.log("0. Back to Accounts List");
                    inAccount = false;
                    listCustomerAccounts();
                    break;
                default:
                    Logger.log("Invalid option selected");
                    IO.println("Invalid option, Try again!\n");
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
            Logger.log("Despositing: £" + amount);
            DataHandling.deposit(accountId, amount);

        } catch (Exception e) {
            Logger.log("Error message: " + e.getMessage());
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
        IO.println("0. Back to Customer Portal");
        IO.print("Select an option: ");

        byte choice = reader.nextByte();
        reader.nextLine();


        if (choice == 0) {
            Logger.log("0. Back to Customer Portal");
            customerPortal();
        }
        else{
            Logger.log("Showing transactions details");
            IO.println("Transaction details shown\n");
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
    }

    private static void createAccount(String accountType) {
        IO.println("\n=== Create " + accountType.toUpperCase() + " Account ===");

        // 1. ISA RULE: Only one per customer
        if (accountType.equalsIgnoreCase("ISA")) {
            if (DataHandling.customerHasISA(currentCustomer.getCustomerId())) {
                Logger.log("Error: Customer has existing ISA Account");
                IO.println("Error: Customer already has an ISA Account. Limit is one per customer.");
                IO.print("\nPress Enter to return...");
                reader.nextLine();
                customerPortal();
                return;
            }
        }

        // 2. BUSINESS RULE: Validate Business Type
        if (accountType.equalsIgnoreCase("Business")) {
            IO.println("Please select Business Type:");
            IO.println("""
                    1. Sole Trader
                    2. Ltd
                    3. Partnership
                    4. Charity (Not Eligible)
                    5. Public Sector (Not Eligible)
                    """);

            IO.print("Selection: ");
            byte typeChoice = 0;
            try {
                typeChoice = reader.nextByte();
                reader.nextLine();
            } catch (Exception e) {
                reader.nextLine();
            }

            // Check eligibility
            boolean isEligible = switch (typeChoice) {
                case 1, 2, 3 -> true;
                default -> false;
            };

            if (!isEligible) {
                Logger.log("Error: business type is not eligible for an account");
                IO.println("Error: This business type is not eligible for a business account.");
                IO.println("Eligible types: Sole Trader, Ltd, Partnership.");
                IO.print("\nPress Enter to return...");
                reader.nextLine();
                return;
            }
        }

        // 3. Set Initial Balance
        IO.println("""
                1. Set Initial Balance
                2. Help
                0. Cancel
                """);
        IO.print("Select an option: ");

        byte choice = reader.nextByte();
        reader.nextLine();

        if (choice == 0) {
            Logger.log("0. Cancel");
            IO.println("Operation cancelled.");
            return;
        } else if (choice == 2) {
            Logger.log("2. Help");
            help("create account");
            createAccount(accountType); // Recursively call to restart
            return;
        }

        IO.print("Enter initial deposit amount: £");
        double balance = 0;
        try {
            balance = reader.nextDouble();
            reader.nextLine();
        } catch (Exception e) {
            Logger.log("Error message: " + e.getMessage());
            IO.println("Invalid amount entered.");
            reader.nextLine();
            return;
        }

        // 4. PERSONAL RULE: Minimum £1 needed to opening the account
        if (accountType.equalsIgnoreCase("Personal") && balance < 1.00) {
            Logger.log("Error: Personal account balance is less than £1.00");
            IO.println("Error: Personal Accounts require a minimum opening balance of £1.00.");
            return;
        }

        // 5. Account creation confirmations
        IO.println("Creating " + accountType + " account with opening balance: £" + balance);
        IO.println("1. Confirm");
        IO.println("0. Cancel");
        IO.print("Select: ");

        byte confirm = reader.nextByte();
        reader.nextLine();

        if (confirm == 1) {
            Logger.log("1. Confirm");
            // Call the DataHandling method to insert into DB
            DataHandling.createAccount(currentCustomer.getCustomerId(), accountType, balance);
            IO.println(accountType + " Account Created Successfully!");

            IO.println("=== Back to Customer Portal ===");
            customerPortal();
        } else {
            Logger.log("0. Cancel");
            IO.println("Account creation cancelled.");
        }
    }

}
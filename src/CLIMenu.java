import java.util.Scanner;

public class CLIMenu {
    static Scanner reader = new Scanner(System.in);
    static boolean running = true;

    public static void main() {
        IO.println("== Acme Teller System ==");

        while (running) {
            showMainMenu();
        }

        IO.println("\nThank you for using Acme Teller System. Goodbye!");
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
                findCustomer();
                break;
            case 2:
                signUpCustomer();
                break;
            case 3:
                switchCustomer();
                break;
            case 4:
                help("main menu");
                break;
            case 0:
                running = false;
                break;
            default:
                System.out.println("Invalid option, Try again!\n");
        }
    }

    private static void findCustomer() {
        IO.println("\n=== Find Customer ===");
        IO.print("Enter CustomerID: ");

        String customerId = reader.nextLine();

        // TODO: Customer search logic
        if (customerId.length() > 0) {
            IO.println("Customer Found\n");
            customerPortal();
        } else {
            IO.println("Customer Not Found\n");
            IO.println("""
                    1. Try Again
                    0. Back to Main Menu
                    """);
            IO.print("Select an option: ");

            byte choice = reader.nextByte();
            reader.nextLine();

            if (choice == 1) {
                findCustomer();
            }
            if (choice == 0)
                showMainMenu();
            else {
                IO.println("Invalid option, Try again!\n");
            }
        }
    }

    private static void signUpCustomer() {
        IO.println("\n=== Sign Up Customer ===");
        IO.print("Creating new customer...");
        // TODO: Add prompts for customer details
        // TODO: Logic for creating customer
        IO.println("Customer created\n");

        customerPortal();
    }

    private static void switchCustomer() {
        IO.println("\n=== Switch Customer ===");
        IO.println("Switching to another customer session...");
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
                IO.println("""
                        --- Sign Up Customer Help ---
                        This process registers a new customer in the system.
                        You will need to provide personal information and identification.
                        After registration, you can immediately open accounts for the customer.
                        """);
                break;
            case "switch customer": // To be properly implemented
                IO.println("""
                        --- Switch Customer Help ---
                        This allows you to switch between different customer sessions.
                        You need to find an existing customer first before switching to their session.
                        Useful for serving multiple customers in sequence.
                        """);
                break;
            default:
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
        IO.println("=== John Smith | CUST001 ===");
        IO.println("1. View Accounts");
        IO.println("2. Open Account");
        IO.println("3. Switch Customer");
        IO.println("4. Help");
        IO.println("0. Back to Main Menu");

        byte choice = reader.nextByte();
        reader.nextLine();

        switch (choice) {
            case 1:
                listCustomerAccounts();
                break;
            case 2:
                openAccount();
                break;
            case 3:
                inPortal = false;
                switchCustomer();
                break;
            case 4:
                help("customer portal");
                break;
            case 0:
                inPortal = false;
                IO.println();
                break;
            default:
                IO.println("Invalid option, Try again!\n");
        }
    }

    private static void listCustomerAccounts() {
        // TODO: Logic for retrieving customer accounts
        IO.println("=== John Smith(CUST001) Accounts");
        IO.println("""
                No | Type       | Account Number | Sort Code | Balance
                1  | Personal   | 12345678       | 60-60-60  | £500.00
                2  | ISA        | 87654321       | 60-60-70  | £1000.00
                """);
        IO.println("1. Select Account");
        IO.println("2. Help");
        IO.println("0. Back");
        
        byte choice = reader.nextByte();
        reader.nextLine();
        
        switch (choice) {
            case 1:
                IO.print("Choose an account: ");
                byte accountSelection = reader.nextByte();
                reader.nextLine();
                selectAccount(accountSelection);
                break;
            case 2:
                help("list customer accounts");
                break;
            case 0:
                customerPortal();
                break;
            default:
                IO.println("Invalid option, Try again!\n");
        }
    }

    private static void selectAccount(byte accountSelection) {
        boolean inAccount = true;
        
        while (inAccount) {
            IO.println("\n=== John Smith(CUST001) Account Operations ===");
            IO.println("Account #" + (accountSelection == 1 ? "1 (Personal)" : "2 (ISA)"));
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
                    deposit();
                    break;
                case 2:
                    withdraw();
                    break;
                case 3:
                    viewTransactions();
                    break;
                case 4:
                    help("select accounts");
                    break;
                case 0: 
                    inAccount = false;
                    listCustomerAccounts();
                    break;
                default:
                    IO.println("Invalid option, Try again!\n");
            }
        }
    }

    private static void deposit() {
        IO.println("\n=== Deposit ===");
        IO.print("Enter deposit amount: £");
        double amount = reader.nextDouble();
        reader.nextLine();
        IO.println("Depositing: £" + amount);
        IO.println("Deposit completed!\n");
    }

    private static void withdraw() {
        IO.println("\n=== Withdraw ===");
        IO.print("Enter withdrawal amount: £");
        double amount = reader.nextDouble();
        reader.nextLine();
        IO.println("Withdrawing: £" + amount);
        IO.println("Withdrawal completed\n");
    }

    private static void viewTransactions() {
        IO.println("\n=== View Transactions ===");
        IO.println("1. Deposit - £100.00 - Jan 1, 2026");
        IO.println("2. Withdrawal - £70.00 - Jan 3, 2026");
        IO.println("3. Deposit - £250.00 - Jan 4, 2026");
        IO.println("0. Back to Customer Portal");
        IO.print("Select an option: ");

        byte choice = reader.nextByte();
        reader.nextLine();

        if (choice == 0)
            customerPortal();
        else
            IO.println("Transaction details shown\n");
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
                accountType = "Personal";
                break;
            case 2:
                accountType = "ISA";
                break;
            case 3: 
                accountType = "Business";
                break;
            case 4: 
                help("open account");
                openAccount();
                break;
            case 0:
                customerPortal();
                break;
            default: 
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
        IO.println("""
                1. Set Initial Balance
                2. Enable Overdraft
                3. Complete Account Creation
                4. Help
                0. Cancel Operation
                """);
        IO.print("Select an option: ");

        byte createChoice = reader.nextByte();
        reader.nextLine();

        switch (createChoice) {
            case 1: // TODO: Implement try catch
                IO.print("Enter initial balance: £");
                double balance = reader.nextDouble();
                reader.nextLine();
                IO.println("Initial balance set to: £" + balance);
                createAccount(accountType);
                break;
            case 2: // Overdraft will change depending on account
                IO.println("Overdraft Enabled!");
                createAccount(accountType);
                break;
            case 3:
                // TODO: Creating account logic added
                IO.println("Creating account...");
                IO.println(accountType + " Account Created!\n");
                IO.println("=== Back to Customer Portal ===");
                customerPortal();
                break;
            case 4:
                help("create account");
                createAccount(accountType);
                break;
            case 0:
                IO.println("Account creation cancelled\n");
                openAccount();
                break;
            default:
                IO.println("Invalid option, Try again!\n");
                createAccount(accountType);
        }
    }

}
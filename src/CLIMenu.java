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
                5. Back to Customer Portal
                """);
        IO.print("Select an option: ");
        
        byte choice = reader.nextByte();
        reader.nextLine();
        
        String accountType = "";
        
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
                break;
            case 5:
                customerPortal();
                break;
            default: 
                IO.println("Invalid option, Try again!\n");
        }
        
        IO.println("\n=== Create " + accountType.toUpperCase() + " Account ===");
        IO.println("""
                1. Set Initial Balance
                2. Enable Overdraft
                3. Complete Account Creation
                0. Cancel Operation
                """);
        IO.print("Select an option: ");
        
        byte createChoice = reader.nextByte();
        reader.nextLine();
        
        if (createChoice == 0) {
            IO.println("Account creation cancelled\n");
            customerPortal();
        }
        
        // TODO: Creating account logic added
        IO.println("Creating account...");
        
        if (createChoice == 1) {
            IO.print("Enter initial balance: £");
            double balance = reader.nextDouble();
            reader.nextLine();
            IO.println("Initial balance set to: £" + balance);
        }
        // Overdraft will change depending on account
        if (createChoice == 2) 
            IO.println("Overdraft Enabled!");
        
        IO.println(accountType + " Account Created!\n");
        IO.println("=== Back to Customer Portal ===");
        customerPortal();
    }
}
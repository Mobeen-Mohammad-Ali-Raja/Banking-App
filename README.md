# Acme Bank Teller System

## Overview
This is a Java-based command-line interface (CLI) banking system developed for Acme Bank. The application allows bank tellers to authenticate customers, manage accounts (Personal, ISA, Business), and perform financial transactions securely and efficiently.

## Project Contributors
**Group Members:**

| Name |
| :--- | 
| **Daniel** | 
| **Mobeen** | 
| **Praveenan** | 
| **Shashank** | 

## Key Features
* **Customer Authentication:** Secure login using unique customer identifiers.
* **Account Management:** Support for Personal, ISA, and Business accounts with specific rules.
* **Automated Identification:** System-generated unique 8 digit account numbers and correct Sort Code allocation.
* **Transaction Processing:** Deposits, withdrawals, and transfers with validation.
* **Persistence:** Data is saved and loaded to ensure continuity between sessions.
* **Logging:** Automated logging of all transactions and system events.
* **Help System:** Context-sensitive help available throughout the application

## Account Types & Rules
### Personal Account
* Sort Code: 60-60-60
* Minimum opening balance: £1.00
* Supports overdraft facility (up to £500)
* Multiple accounts per customer allowed

### ISA Account
* Sort Code: 60-60-70
* Annual interest rate: 2.75%
* Only one ISA account per customer
* Tax-free savings account

### Business Account
* Sort Code: 60-70-70
* Annual fee: £120 (automatically applied)
* Eligible business types: Sole Trader, Ltd, Partnership
* Cheque book issuance capability
* Overdraft facility (up to £1000)
* Only one business account per customer

## Project Structure

## Installation & Setup

### Prerequisites
* Java JDK 11 or higher
* SQLite 

### Running the Application
1. Clone the repository
2. Navigate to the project directory
3. Compile all Java files
4. Run the application

## Database Schema
The system uses three main SQLite tables:
* `customers`: Stores customer identification information
* `accounts`: Stores account details including type, balance and sort codes
* `transactions`: Logs all financial transactions

## Logging
All system events are logged to `logs.txt` with timestamps, including:
* Customer authentication attempts
* Account creation and modifications
* Financial transactions
* System errors and warnings

## Future Enhancements


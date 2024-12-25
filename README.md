# JavaFX ATM Application

A secure ATM (Automated Teller Machine) application built with JavaFX and SQL Server, featuring account management, transactions, and overdraft facilities.

## Features

- Secure login with account number and PIN
- Cash withdrawals with overdraft support up to -500
- Cash deposits
- Balance inquiries
- Automatic session management
- Persistent storage using SQL Server
- Debug logging system

## Technical Stack

- Java
- JavaFX
- SQL Server
- JDBC

## Prerequisites

- JDK 8 or higher
- SQL Server Express
- SQL Server JDBC Driver
- JavaFX SDK

## Database Setup

1. Install SQL Server Express
2. Create a database named `ATMDB`
3. Create the `BankAccounts` table:
```sql
CREATE TABLE BankAccounts (
    AccountID INT PRIMARY KEY,
    Password INT NOT NULL,
    Balance DECIMAL(10,2) NOT NULL
)
```

## Configuration

Update the database connection string in `BankAccount.java`:
```java
private static final String CONNECTION_URL = 
    "jdbc:sqlserver://V\\SQLEXPRESS;databaseName=ATMDB;integratedSecurity=true;trustServerCertificate=true;";
```

## Project Structure

- `Main.java` - Application entry point and MVC initialization
- `Model.java` - Business logic and state management
- `View.java` - JavaFX GUI implementation
- `Controller.java` - User input handling
- `Bank.java` - Banking operations manager
- `BankAccount.java` - Account data and database operations
- `Debug.java` - Debugging utilities
- `application.css` - UI styling

## Security Features

- Session-based authentication
- Automatic logout on completion
- Secure password handling
- Database-backed transaction persistence

## Running the Application

1. Compile the project:
```bash
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls *.java
```

2. Run the application:
```bash
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls Main
```

## Usage Limits

- Overdraft limit: -500 units
- Requires valid account number and PIN
- Single session per account

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

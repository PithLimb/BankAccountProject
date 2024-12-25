package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Bank {
    private BankAccount account = null; // Currently logged-in account

    // Constructor
    public Bank() {
        Debug.trace("Bank::<constructor>");
        // No need to initialize in-memory accounts
    }

    // Login method
    public boolean login(int newAccNumber, int newAccPasswd) {
        Debug.trace("Bank::login: accNumber = " + newAccNumber);
        logout(); // Logout any previous account

        // Fetch account from the database
        account = BankAccount.fetchAccount(newAccNumber, newAccPasswd);

        if (account != null) {
            Debug.trace("Bank::login: Login successful for AccountID " + newAccNumber);
            return true;
        } else {
            Debug.trace("Bank::login: Login failed for AccountID " + newAccNumber);
            account = null;
            return false;
        }
    }

    // Logout method
    public void logout() {
        if (loggedIn()) {
            Debug.trace("Bank::logout: logging out, accNumber = " + account.getAccNumber());
            account = null;
        }
    }

    // Check if logged in
    public boolean loggedIn() {
        return account != null;
    }

    // Deposit money
    public boolean deposit(double amount) {
        if (loggedIn()) {
            return account.deposit(amount);
        } else {
            return false;
        }
    }

    // Withdraw money
    public boolean withdraw(double amount) {
        if (loggedIn()) {
            return account.withdraw(amount);
        } else {
            return false;
        }
    }

    // Get balance
    public double getBalance() {
        if (loggedIn()) {
            return account.getBalance();
        } else {
            return -1; // Indicator of an error
        }
    }
}

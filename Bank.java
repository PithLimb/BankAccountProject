package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Bank class - simple implementation of a bank, with a list of bank accounts and
// a current account that we are logged in to.

// This class contains one method ('login') which you need to complete first
// to make the basic ATM work.

// If you choose the ATM for your project, you should make other modifications to 
// the system yourself.
public class Bank {
    // Instance variables containing the bank information
    private BankAccount account = null; // Currently logged-in account ('null' if no one is logged in)

    // Constructor method - this provides a couple of example bank accounts to work with
    public Bank() {
        Debug.trace("Bank::<constructor>");
        // No need to initialize in-memory accounts
    }

    // Check whether the current saved account and password correspond to 
    // an actual bank account, and if so login to it (by setting 'account' to it)
    // and return true. Otherwise, reset the account to null and return false
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

    // Reset the bank to a 'logged out' state
    public void logout() {
        if (loggedIn()) {
            Debug.trace("Bank::logout: logging out, accNumber = " + account.getAccNumber());
            account = null;
        }
    }

    // Test whether the bank is logged in to an account or not
    public boolean loggedIn() {
        return account != null;
    }

    // Try to deposit money into the account (by calling the deposit method on the 
    // BankAccount object)
    public boolean deposit(double amount) {
        if (loggedIn()) {
            return account.deposit(amount);
        } else {
            return false;
        }
    }

    // Try to withdraw money into the account (by calling the withdraw method on the 
    // BankAccount object)
    public boolean withdraw(double amount) {
        if (loggedIn()) {
            return account.withdraw(amount);
        } else {
            return false;
        }
    }

    // Get the account balance (by calling the balance method on the 
    // BankAccount object)
    public double getBalance() {
        if (loggedIn()) {
            return account.getBalance();
        } else {
            return -1; // Use -1 as an indicator of an error
        }
    }
}

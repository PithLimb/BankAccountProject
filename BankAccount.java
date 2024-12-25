package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankAccount {
    private int accNumber;
    private int accPasswd;
    private double balance;

    // Database connection details
    // Use double backslashes in Java strings
    private static final String CONNECTION_URL = 
	        "jdbc:sqlserver://V\\SQLEXPRESS;databaseName=ATMDB;integratedSecurity=true;trustServerCertificate=true;";

    // Static block to load the JDBC driver
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Debug.trace("JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            Debug.trace("JDBC Driver not found: " + e.getMessage());
        }
    }

    // Constructor
    public BankAccount(int a, int p, double b) {
        this.accNumber = a;
        this.accPasswd = p;
        this.balance = b;
    }

    // Getters
    public int getAccNumber() {
        return accNumber;
    }

    public int getAccPasswd() {
        return accPasswd;
    }

    public double getBalance() {
        return balance;
    }

    // Withdraw money from the account
    public boolean withdraw(double amount) {
        Debug.trace("BankAccount::withdraw: amount = " + amount);

        if (amount > 0 && (balance - amount) >= -500) { // Allow overdraft up to -500
            balance -= amount;
            updateBalanceInDB();
            return true;
        }
        return false;
    }

    // Deposit money into the account
    public boolean deposit(double amount) {
        Debug.trace("BankAccount::deposit: amount = " + amount);

        if (amount > 0) {
            balance += amount;
            updateBalanceInDB();
            return true;
        }
        return false;
    }

    // Update balance in the database
    private void updateBalanceInDB() {
        String updateSQL = "UPDATE BankAccounts SET Balance = ? WHERE AccountID = ?";

        try (Connection con = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement ps = con.prepareStatement(updateSQL)) {

            ps.setDouble(1, balance);
            ps.setInt(2, accNumber);
            ps.executeUpdate();

            Debug.trace("BankAccount::updateBalanceInDB: Account " + accNumber + " new balance " + balance);

        } catch (SQLException e) {
            Debug.trace("BankAccount::updateBalanceInDB: SQLException: " + e.getMessage());
        }
    }

    // Static method to fetch a BankAccount from the database
    public static BankAccount fetchAccount(int accNumber, int accPasswd) {
        String selectSQL = "SELECT * FROM BankAccounts WHERE AccountID = ? AND Password = ?";

        try (Connection con = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement ps = con.prepareStatement(selectSQL)) {

            ps.setInt(1, accNumber);
            ps.setInt(2, accPasswd);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double bal = rs.getDouble("Balance");
                Debug.trace("BankAccount::fetchAccount: Found AccountID " + accNumber + " with balance " + bal);
                return new BankAccount(accNumber, accPasswd, bal);
            }

        } catch (SQLException e) {
            Debug.trace("BankAccount::fetchAccount: SQLException: " + e.getMessage());
        }

        Debug.trace("BankAccount::fetchAccount: Account not found.");
        return null;
    }
}

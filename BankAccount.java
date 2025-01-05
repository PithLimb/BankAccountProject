package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// BankAccount class
// This class has instance variables for the account number, password and balance, and methods
// to withdraw, deposit, check balance etc.
// This class contains methods which you need to complete to make the basic ATM work.
// Tutors can help you get this part working in lab sessions. 
// If you choose the ATM for your project, you should make other modifications to 
// the system yourself.
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

   // Return the current balance in the account
   public double getBalance() {
       return balance;
   }

   // Withdraw money from the account. Return true if successful, or 
   // false if the amount is negative, or less than the amount in the account
    public boolean withdraw( double amount ) 
    { 
        Debug.trace( "BankAccount::withdraw: amount =" + amount ); 

        if (amount > 0 - overdraft && amount <= balance + overdraft){
            balance -= amount;
            return true;
        }
        return false;
    }

   // Deposit the amount of money into the account. Return true if successful,
   // or false if the amount is negative
    public boolean deposit( double amount )
    { 
        Debug.trace( "LocalBank::deposit: amount = " + amount ); 
        if (amount > 0){
            balance += amount;
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

       // Return to overdraft limit for the account
    public int getOverdraft(){
        Debug.trace("LocalBank::getOverdraft");

        return overdraft;
    }

    public boolean transfer(BankAccount targetAccount, int amount) {
        if (this.withdraw(amount)) {  // Withdraw from the current account
            targetAccount.deposit(amount);  // Deposit into the target account
            return true;
        }
        return false;  // Transfer failed if withdraw failed
    }
}

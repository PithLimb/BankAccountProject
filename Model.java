package application;

// The model represents all the actual content and functionality of the app
// For the ATM, it keeps track of the information shown in the display
// (the title and two message boxes), and the interaction with the bank, executes
// commands provided by the controller and tells the view to update when
// something changes
public class Model {
    // the ATM model is always in one of three states - waiting for an account number, 
    // waiting for a password, or logged in and processing account requests. 
    // We use string values to represent each state:
    // (the word 'final' tells Java we won't ever change the values of these variables)
    final String ACCOUNT_NO = "account_no";
    final String PASSWORD = "password";
    final String LOGGED_IN = "logged_in";

    // variables representing the ATM model
    String state = ACCOUNT_NO;      // Current state
    int  number = 0;                // current number displayed in GUI (as a number, not a string)
    Bank  bank = null;              // The ATM talks to a bank, represented by the Bank object
    int accNumber = -1;             // Account number typed in
    int accPasswd = -1;             // Password typed in
    // These three are what are shown on the View display
    String title = "Bank ATM";      // The contents of the title message
    String display1 = null;         // The contents of the Message 1 box (a single line)
    String display2 = null;         // The contents of the Message 2 box (may be multiple lines)

    // The other parts of the model-view-controller setup
    public View view;
    public Controller controller;

    // Model constructor - we pass it a Bank object representing the bank we want to talk to
    public Model(Bank b) {
        Debug.trace("Model::<constructor>");          
        bank = b;
    }

    // Initialising the ATM (or resetting after an error or logout)
    // set state to ACCOUNT_NO, number to zero, and display message 
    // provided as argument and standard instruction message
    public void initialise(String message) {
        setState(ACCOUNT_NO);
        number = 0;
        display1 = message; 
        display2 =  "Enter your account number\n" +
                    "Followed by \"Ent\"";
    }

    // use this method to change state - mainly so we print a debugging message whenever 
    // the state changes
    public void setState(String newState) {
        if (!state.equals(newState)) {
            String oldState = state;
            state = newState;
            Debug.trace("Model::setState: changed state from " + oldState + " to " + newState);
        }
    }

    // process a number key (the key is specified by the label argument)
    public void processNumber(String label) {
        char c = label.charAt(0);
        number = number * 10 + (c - '0'); // Build number 
        display1 = "" + number;
        display();  // update the GUI
    }

    // process the Clear button - reset the number (and number display string)
    public void processClear() {
        number = 0;
        display1 = "";
        display();  // update the GUI
    }

    // process the Enter button
    // this is the most complex operation - the Enter key causes the ATM to change state
    // from account number, to password, to logged_in and back to account number
    // (when you log out)
    public void processEnter() {
        switch (state) {
            case ACCOUNT_NO:
                // we were waiting for a complete account number - save the number we have
                // reset the tyed in number to 0 and change to the state where we are expecting 
                // a password
                accNumber = number;
                number = 0;
                setState(PASSWORD);
                display1 = "";
                display2 = "Now enter your password\n" +
                           "Followed by \"Ent\"";
                break;
            case PASSWORD:
                // we were waiting for a password - save the number we have as the password
                // and then contact the bank with accNumber and accPasswd to try and login to
                // an account
                accPasswd = number;
                number = 0;
                display1 = "";
                if (bank.login(accNumber, accPasswd)) {
                    setState(LOGGED_IN);
                    display2 = "Accepted\n" +
                               "Now enter the transaction you require";
                } else {
                    initialise("Unknown account/password");
                }
                break;
            case LOGGED_IN:     
            default: 
                // do nothing in any other state (ie logged in)
        }  
        display();  // update the GUI
    }

    // Withdraw button - check we are logged in and if so try and withdraw some money from
    // the bank (number is the amount showing in the interface display)
    public void processWithdraw() {
        if (state.equals(LOGGED_IN)) {            
            if (bank.withdraw(number)) {
                display2 = "Withdrawn: " + number;
            } else {
                display2 = "Insufficient funds or overdraft limit reached";
            }
            number = 0;
            display1 = "";           
        } else {
            initialise("You are not logged in");
        }
        display();  // update the GUI
    }

    // Deposit button - check we are logged in and if so try and deposit some money into
    // the bank (number is the amount showing in the interface display)
    public void processDeposit() {
        if (state.equals(LOGGED_IN)) {
            if (bank.deposit(number)) {
                display2 = "Deposited: " + number;
            } else {
                display2 = "Deposit failed";
            }
            display1 = "";
            number = 0;
        } else {
            initialise("You are not logged in");
        }
        display();  // update the GUI
    }

    // Balance button - check we are logged in and if so access the current balance
    // and display it
    public void processBalance() {
        if (state.equals(LOGGED_IN)) {
            double balance = bank.getBalance();
            display2 = "Your balance is: " + balance;
            number = 0;
            display1 = "";
        } else {
            initialise("You are not logged in");
        }
        display();  // update the GUI
    }

    // Finish button - check we are logged in and if so log out
    public void processFinish() {
        if (state.equals(LOGGED_IN)) {
            setState(ACCOUNT_NO);
            number = 0;
            display2 = "Welcome: Enter your account number";
            bank.logout();
        } else {
            initialise("You are not logged in");
        }
        display();  // update the GUI
    }

    // Any other key results in an error message and a reset of the GUI
    public void processUnknownKey(String action) {
        Debug.trace("Model::processUnknownKey: unknown button \"" + action + "\", re-initialising");
        initialise("Invalid command");
        display();
    }

    // This is where the Model talks to the View, by calling the View's update method
    // The view will call back to the model to get new information to display on the screen
    public void display() {
        Debug.trace("Model::display");
        view.update();
    }

        // Transfer fucntion for sending money to other accounts
    public void processTransfer() {
        switch (state) {
            case LOGGED_IN:
                // First step: Ask for the target account number
                setState(TRANSFER_ACCOUNT);
                display1 = ""; // Clear the display for input
                display2 = "Enter the target account number, then press Trn";
                break;

            case TRANSFER_ACCOUNT:
                // Second step: Save the target account number and ask for the amount
                targetAccountNumber = number; // Capture the input as the account number
                number = 0; // Reset the number for the next input
                display1 = "";
                display2 = "Enter the transfer amount, then press Trn";
                setState(TRANSFER_AMOUNT);
                break;

            case TRANSFER_AMOUNT:
                // Final step: Perform the transfer
                int transferAmount = number; // Capture the amount
                number = 0; // Reset for future operations

                // Perform the transfer
                if (bank.transfer(targetAccountNumber, transferAmount)) {
                    display2 = "Transferred " + transferAmount + " to account " + targetAccountNumber;
                } else {
                    display2 = "Transfer failed. Check the account number or balance.";
                }

                // Reset the state to logged in
                setState(LOGGED_IN);
                break;

            default:
                initialise("Invalid transfer operation");
        }
        display(); // Update the GUI
    }
}

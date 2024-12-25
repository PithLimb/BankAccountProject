package application;

import application.Controller;

import application.View;

// CI701 Coursework
// atm project Main class
// The code here creates the ATM GUI interface and model functionality, but the methods
// in the BankAccount class which actually do the banking functions are incomplete.
// The assessment project is to complete BankAccount and add further functionality as discussed in lectures.
import javafx.application.Application;
import javafx.stage.Stage;

// CI701 Coursework
// ATM project Main class
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage window) {
        // Set up debugging and print initial debugging messages
        Debug.set(true);
        Debug.trace("ATM starting");
        Debug.trace("Main::start");

        // Create a Bank object for this ATM
        Bank b = new Bank();
        // Removed in-memory account addition
        // Accounts are managed in the database

        // Create the Model, View, and Controller objects
        Model model = new Model(b); // The model needs the Bank object to 'talk to' the bank
        View view = new View();
        Controller controller = new Controller();

        // Link them together so they can talk to each other
        model.view = view;
        model.controller = controller;

        controller.model = model;
        controller.view = view;

        view.model = model;
        view.controller = controller;

        // Start up the GUI (view), and then tell the model to initialize and display itself
        view.start(window);
        model.initialise("Welcome to the ATM");
        model.display();

        // Application is now running
        Debug.trace("ATM running");
    }
}

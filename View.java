package application;
//The View class creates and manages the GUI for the application.
//It doesn't know anything about the ATM itself, it just displays
//the current state of the Model, (title, output1 and output2), 
//and handles user input from the buttonsand handles user input

//We import lots of JavaFX libraries (we may not use them all, but it
//saves us having to thinkabout them if we add new code)
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

class View {
    int H = 420;         // Height of window pixels 
    int W = 500;         // Width  of window pixels 

    // UI Components
    Label      title;         // Title area
    TextField  message;       // Message area (for numbers)
    TextArea   reply;         // Reply area (for instructions/results)
    ScrollPane scrollPane;    // Scrollbars for TextArea  
    GridPane   grid;          // Main layout grid
    TilePane   buttonPane;    // Tiled pane for buttons

    // MVC components
    public Model model;
    public Controller controller;

    // Constructor
    public View() {
        Debug.trace("View::<constructor>");
    }

    // Start GUI
    public void start(Stage window) {
        Debug.trace("View::start");

        // Initialize layout
        grid = new GridPane();
        grid.setId("Layout");           // Assign CSS ID
        buttonPane = new TilePane();
        buttonPane.setId("Buttons");    // Assign CSS ID

        // Initialize controls
        title  = new Label();           // Title label
        grid.add(title, 0, 0);          // Add to top

        message  = new TextField();     // Message field
        message.setEditable(false);     // Read-only
        grid.add(message, 0, 1);        // Add to second row

        reply  = new TextArea();        // Reply area
        reply.setEditable(false);       // Read-only
        scrollPane  = new ScrollPane(); // Scroll pane
        scrollPane.setContent(reply);   // Embed TextArea
        grid.add(scrollPane, 0, 2);     // Add to third row

        // Define button labels
        String labels[][] = {
            {"7",    "8",  "9",  "",  "Dep",  ""},
            {"4",    "5",  "6",  "",  "W/D",  ""},
            {"1",    "2",  "3",  "",  "Bal",  "Fin"},
            {"CLR",  "0",  "",   "",  "",     "Ent"} 
        };

        // Create buttons
        for (String[] row : labels) {
            for (String label : row) {
                if (label.length() >= 1) {
                    Button b = new Button(label);        
                    b.setOnAction(this::buttonClicked); // Set action handler
                    buttonPane.getChildren().add(b);    // Add to TilePane
                } else {
                    // Add spacer for empty buttons
                    buttonPane.getChildren().add(new Text()); 
                }
            }
        }
        grid.add(buttonPane, 0, 3); // Add buttons to fourth row

        // Create and set scene
        Scene scene = new Scene(grid, W, H);   
        scene.getStylesheets().add("atm.css"); // Apply CSS
        window.setScene(scene);
        window.show();
    }

    // Button click handler
    public void buttonClicked(ActionEvent event) {
        Button b = (Button) event.getSource();
        if (controller != null) {          
            String label = b.getText();   // Get button label
            Debug.trace("View::buttonClicked: label = " + label);
            controller.process(label);    // Pass to controller
        }
    }

    // Update display based on model
    public void update() {        
        if (model != null) {
            Debug.trace("View::update");
            String message1 = model.title;        // Get title
            title.setText(message1);              // Set title label
            String message2 = model.display1;     // Get message1
            message.setText(message2);            // Set message field
            String message3 = model.display2;     // Get message2
            reply.setText(message3);              // Set reply area
        }
    }
}

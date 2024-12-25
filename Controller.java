package application;
//The ATM controller is quite simple - the process method is passed
//the label on the button that was pressed, and it calls different
//methods in the model depending on what was pressed.
public class Controller {
    public Model model;
    public View view;

    // Constructor
    public Controller() {
        Debug.trace("Controller::<constructor>");
    }

    // Process user actions
    public void process(String action) {
        Debug.trace("Controller::process: action = " + action);
        switch (action) {
            case "1": case "2": case "3": case "4": case "5":
            case "6": case "7": case "8": case "9": case "0": 
                model.processNumber(action);
                break;
            case "CLR":
                model.processClear();
                break;
            case "Ent":
                model.processEnter();
                break;
            case "W/D":
                model.processWithdraw();
                break; 
            case "Dep":
                model.processDeposit();
                break;
            case "Bal":
                model.processBalance();
                break; 
            case "Fin":
                model.processFinish();
                break;
            default:
                model.processUnknownKey(action);
                break;
        }    
    }
}
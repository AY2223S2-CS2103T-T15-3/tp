package vimification.taskui;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import vimification.logic.Logic;
import vimification.logic.commands.CommandException;
import vimification.logic.commands.CommandResult;
import vimification.logic.parser.ParserException;

/**
 *
 */
public class CommandInput extends UiPart<TextField> {

    private static final String FXML = "CommandInput.fxml";
    private MainScreen parent;
    private Logic logic;

    public CommandInput(MainScreen parent, Logic logic) {
        super(FXML);
        this.parent = parent;
        this.logic = logic;
    }

    /**
     * Listener for handling all keyboard events to Vimification.
     *
     * @param event
     */
    @FXML
    private void handleKeyPressed(KeyEvent event) {

        boolean isEnterEvent = event.getCode().equals(KeyCode.ENTER);
        boolean isEscEvent = event.getCode().equals(KeyCode.ESCAPE);

        if (isEscEvent) {
            returnFocusToParent();
            System.out.println("You escaped");
        }

        if (isEnterEvent) {
            String commandString = this.getRoot().getText();
            executeCommand(commandString);
            returnFocusToParent();
        }

    }

    // TODO: REMOVE THIS AFTER TESTING
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String cleanCommandString(String commandString) {
        boolean isCommandHasColon = commandString.startsWith(":");
        if (!isCommandHasColon) {
            System.out.println("[Your command] " + commandString + " is invalid");
        }

        String strippedCommandString = commandString.substring(1).strip();
        return strippedCommandString;
    }

    private void executeCommand(String input) {

        String commandString = cleanCommandString(input);
        System.out.println("Your command is " + input);

        processUiCommand(commandString);

        try {
            CommandResult result = logic.execute(commandString);
            parent.initializeTaskListPanel();
            System.out.println(result.getFeedbackToUser());
        } catch (CommandException e) {
            System.out.println("[Your command] " + input + " is invalid");
        } catch (ParserException e) {
            System.out.println("[Your command] " + input + " can't be parsed");
        }
    }

    private void processUiCommand(String commandString) {
        checkIsExitCommand(commandString);

        // TODO : TEMPORARY, REMOVE THIS IN THE FUTURE AFTER ABSTRACTING INTO GUI COMMANDS
        if (isNumeric(commandString)) {
            parent.getTaskListPanel().scrollToTaskIndex(Integer.parseInt(commandString));
            return;
        }
    }

    private void checkIsExitCommand(String result) {
        boolean isExit = result.equals("wq!");
        if (isExit) {
            Platform.exit();
        }
    }

    private void returnFocusToParent() {
        parent.getRoot().requestFocus();
        this.getRoot().setVisible(false);
    }


    @FXML
    public void initialize() {
        this.getRoot().setFocusTraversable(true); // Important
        this.getRoot().setVisible(false);
    }

}

package vimification.ui;

import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import vimification.internal.Logic;
import vimification.internal.command.CommandException;
import vimification.internal.command.CommandResult;
import vimification.internal.parser.ParserException;

/**
 *
 */
public class CommandInput extends UiPart<HBox> {

    private static final String FXML = "CommandInput.fxml";
    private MainScreen mainScreen;
    private Logic logic;

    @FXML
    private TextField inputField;

    public CommandInput(MainScreen mainScreen, Logic logic) {
        super(FXML);
        this.mainScreen = mainScreen;
        this.logic = logic;
        inputField.prefWidthProperty().bind(this.getRoot().widthProperty());
        inputField.prefHeightProperty().bind(this.getRoot().heightProperty());
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

        if (isEscEvent || isTextFieldEmpty()) {
            mainScreen.clearBottomComponent();
            returnFocusToParent();
        }

        if (isEnterEvent) {
            String commandString = inputField.getText();
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

        boolean isUiCommand = processUiCommand(commandString);

        if (isUiCommand) {
            return;
        }

        CommandResult result = logic.execute(commandString);
        mainScreen.initializeTaskTabPanel();
        mainScreen.loadCommandResultComponent(result);

        // TODO: Should only clear if the task has been deleted.
        if (result.getFeedbackToUser().contains("Deleted Task:")) {
            mainScreen.clearRightComponent();
        }
    }

    private boolean processUiCommand(String commandString) {
        // TODO : TEMPORARY, REMOVE THIS IN THE FUTURE AFTER ABSTRACTING INTO GUI COMMANDS
        if (isNumeric(commandString)) {
            mainScreen.getTaskTabPanel().scrollToTaskIndex(Integer.parseInt(commandString));
            return true;
        }
        return false;
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
        inputField.setText(":");
        inputField.positionCaret(1);
        inputField.requestFocus();
    }

    private void returnFocusToParent() {
        mainScreen.getRoot().requestFocus();
    }

    @FXML
    private void initialize() {
        this.getRoot().setFocusTraversable(true); // Important
        inputField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean wasFocused,
                    Boolean isCommandInputFocused) {
                // bottomComponent may be displaying CommandResult.
                boolean isCommandInputLingering =
                        mainScreen.bottomComponent.getChildren().contains(getRoot());
                if (!isCommandInputFocused && isCommandInputLingering) {
                    mainScreen.clearBottomComponent();
                }
            }
        });
    }

    private boolean isTextFieldEmpty() {
        return inputField.getText().equals("");
    }
}

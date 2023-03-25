package vimification.internal;

import javafx.collections.ObservableList;
import vimification.internal.commands.CommandException;
import vimification.internal.commands.CommandResult;
import vimification.internal.parser.ParserException;
import vimification.model.task.Task;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     *
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParserException;

    // /**
    // * Returns the AddressBook.
    // *
    // * @see vimification.model.Model#getTaskList()
    // */
    // ReadOnlyTaskPlanner getTaskList();

    // /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<Task> getFilteredTaskList();

    // /**
    // * Returns the user prefs' address book file path.
    // */
    // Path getTaskListFilePath();

    // /**
    // * Returns the user prefs' GUI settings.
    // */
    // GuiSettings getGuiSettings();

    // /**
    // * Set the user prefs' GUI settings.
    // */
    // void setGuiSettings(GuiSettings guiSettings);
}

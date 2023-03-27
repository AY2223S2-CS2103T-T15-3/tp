package vimification.internal.command.logic;

import static java.util.Objects.requireNonNull;

import vimification.commons.core.Index;
import vimification.internal.command.CommandException;
import vimification.internal.command.CommandResult;
import vimification.model.LogicTaskList;
import vimification.model.task.Task;

/**
 * Deletes a task identified using it's displayed index from the address book.
 */
public class DeleteCommand extends UndoableLogicCommand {

    public static final String COMMAND_WORD = "d";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the task identified by the index number used in the displayed task list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String SUCCESS_MESSAGE_FORMAT = "Task %1$s deleted";
    public static final String UNDO_MESSAGE =
            "The command has been undone. The deleted task has been added back.";

    // targetIndex is ZERO-BASED
    private final Index targetIndex;
    private Task deletedTask;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.deletedTask = null;
    }

    @Override
    public CommandResult execute(LogicTaskList taskList)
            throws IndexOutOfBoundsException, CommandException {
        requireNonNull(taskList);
        deletedTask = taskList.remove(targetIndex.getZeroBased());
        return new CommandResult(String.format(SUCCESS_MESSAGE_FORMAT, deletedTask));
    }

    @Override
    public CommandResult undo(LogicTaskList taskList) throws CommandException {
        requireNonNull(taskList);
        taskList.add(targetIndex.getZeroBased(), deletedTask);
        return new CommandResult(String.format(UNDO_MESSAGE, deletedTask));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && targetIndex.equals(((DeleteCommand) other).targetIndex));
    }
}

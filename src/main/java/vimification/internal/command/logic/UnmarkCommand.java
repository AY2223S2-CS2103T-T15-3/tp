package vimification.internal.command.logic;

import vimification.commons.core.Index;
import vimification.internal.command.CommandException;
import vimification.internal.command.CommandResult;
import vimification.model.LogicTaskList;

import static java.util.Objects.requireNonNull;

public class UnmarkCommand extends UndoableLogicCommand {
    public static final String COMMAND_WORD = "unmark";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": unmark the task identified by the index number used in the displayed task list as not completed.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";
    public static final String SUCCESS_MESSAGE_FORMAT = "Task %1$s marked as not completed";
    public static final String UNDO_MESSAGE = "The command has been undone. The task is now completed.";

    public final Index targetIndex;

    public UnmarkCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(LogicTaskList taskList) throws IndexOutOfBoundsException, CommandException {
        requireNonNull(taskList);
        int zero_based_index = targetIndex.getZeroBased();
        taskList.unmark(zero_based_index);
        return new CommandResult(String.format(SUCCESS_MESSAGE_FORMAT, targetIndex.getOneBased()));
    }

    @Override
    public CommandResult undo(LogicTaskList taskList) throws CommandException {
        requireNonNull(taskList);
        int zero_based_index = targetIndex.getZeroBased();
        taskList.mark(zero_based_index);
        return new CommandResult(UNDO_MESSAGE);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UnmarkCommand // instanceof handles nulls
                && targetIndex.equals(((UnmarkCommand) other).targetIndex));
    }
}
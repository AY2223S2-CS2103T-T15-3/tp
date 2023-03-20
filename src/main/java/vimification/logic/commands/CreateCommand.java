package vimification.logic.commands;

import static java.util.Objects.requireNonNull;

import vimification.model.LogicTaskList;
import vimification.model.task.Task;

/**
 * Creates a new task and adds it to the task planner.
 */
public class CreateCommand extends UndoableLogicCommand {

    public static final String COMMAND_WORD = "create";
    public static final String SUCCESS_MESSAGE_FORMAT = "New task created: %1$s";
    public static final String UNDO_MESSAGE =
            "The command has been undoed. The new task has been deleted.";

    private final Task newTask;

    /**
     * Creates an CreateCommand to add the specified {@code Task}
     */
    public CreateCommand(Task task) {
        requireNonNull(task);
        newTask = task;
    }

    // Pass a TaskList instead
    @Override
    public CommandResult execute(LogicTaskList taskList) throws CommandException {
        requireNonNull(taskList);
        taskList.add(newTask);
        return new CommandResult(String.format(SUCCESS_MESSAGE_FORMAT, newTask));
    }

    @Override
    public CommandResult undo(LogicTaskList taskList) throws CommandException {
        requireNonNull(taskList);
        taskList.pop();
        return new CommandResult(UNDO_MESSAGE);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CreateCommand // instanceof handles nulls
                        && newTask.equals(((CreateCommand) other).newTask));
    }
}

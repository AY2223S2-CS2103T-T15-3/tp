package vimification.internal.command.logic;

import java.util.Objects;

import vimification.common.core.Index;
import vimification.internal.command.CommandResult;
import vimification.model.CommandStack;
import vimification.model.LogicTaskList;
import vimification.model.task.Task;

public class InsertCommand extends UndoableLogicCommand {

    public static final String COMMAND_WORD = "i";
    public static final String SUCCESS_MESSAGE_FORMAT =
            "New field(s) have been inserted into task %s.";
    public static final String UNDO_MESSAGE =
            "The command has been undone. Inserted field(s) have been discarded.";

    private final Index targetIndex;
    private final InsertRequest request;
    private Task oldTask = null;

    public InsertCommand(Index targetIndex, InsertRequest request) {
        this.targetIndex = targetIndex;
        this.request = request;
    }

    @Override
    public CommandResult execute(LogicTaskList taskList, CommandStack commandStack) {
        int index = targetIndex.getZeroBased();
        Task oldTask = taskList.get(index);
        Task newTask = oldTask.clone();
        this.oldTask = oldTask;
        if (request.getInsertedDeadline() != null) {
            newTask.setDeadline(request.getInsertedDeadline());
        }
        request.getInsertedLabels().forEach(newTask::addLabel);
        taskList.set(index, newTask);
        commandStack.push(this);
        return new CommandResult(String.format(SUCCESS_MESSAGE_FORMAT, oldTask));
    }

    @Override
    public CommandResult undo(LogicTaskList taskList) {
        taskList.set(targetIndex.getZeroBased(), oldTask);
        return new CommandResult(UNDO_MESSAGE);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof InsertCommand)) {
            return false;
        }
        InsertCommand otherCommand = (InsertCommand) other;
        return Objects.equals(targetIndex, otherCommand.targetIndex)
                && Objects.equals(request, otherCommand.request)
                && Objects.equals(oldTask, otherCommand.oldTask);
    }
}

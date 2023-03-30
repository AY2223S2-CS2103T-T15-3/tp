package vimification.storage;

import java.nio.file.Files;
import java.util.List;
// import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

// import vimification.commons.core.LogsCenter;
import vimification.commons.exceptions.IllegalValueException;
import vimification.commons.util.FileUtil;
import vimification.model.LogicTaskList;
import vimification.model.oldcode.Deadline;
import vimification.model.task.Task;
import vimification.model.oldcode.Todo;
import vimification.storage.oldcode.JsonAdaptedDeadline;
import vimification.storage.oldcode.JsonAdaptedTodo;

/**
 * An Immutable TaskPlanner that is serializable to JSON format.
 */
@JsonRootName(value = "logicTaskList")
public class JsonAdaptedLogicTaskList {

    // private static final Logger LOGGER = LogsCenter.getLogger(JsonAdaptedLogicTaskList.class);

    private final List<JsonAdaptedTask> tasks;

    @JsonCreator
    public JsonAdaptedLogicTaskList(@JsonProperty("tasks") List<JsonAdaptedTask> tasks) {
        this.tasks = tasks == null ? List.of() : tasks;
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created
     *        {@code JsonSerializableAddressBook}.
     */
    public JsonAdaptedLogicTaskList(LogicTaskList source) {
        tasks = source.stream().map(JsonAdaptedTask::new).collect(Collectors.toList());
    }


    /**
     * Converts this address book into the model's {@code TaskPlanner} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public LogicTaskList toModelType() throws IllegalValueException {
        LogicTaskList taskList = new LogicTaskList();
        for (JsonAdaptedTask jsonAdaptedTask : tasks) {
            Task task = jsonAdaptedTask.toModelType();
            taskList.addTask(task);
        }
        return taskList;
    }

    @Override
    public String toString() {
        return "JsonAdpatedLogicTaskList: [" + tasks + "]";
    }
}

package vimification.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import vimification.commons.exceptions.IllegalValueException;
import vimification.model.task.Priority;
import vimification.model.task.Status;
import vimification.model.task.Task;

/**
 * Jackson-friendly version of {@link Task}.
 */
public class JsonAdaptedTask {

    private final String title;
    private final LocalDateTime deadline;
    private final Status status;
    private final Priority priority;
    private final List<String> labels;

    @JsonCreator
    public JsonAdaptedTask(
            @JsonProperty("title") String title,
            @JsonProperty("deadline") LocalDateTime deadline,
            @JsonProperty("status") Status status,
            @JsonProperty("priority") Priority priority,
            @JsonProperty("labels") List<String> labels) {
        this.title = title;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
        this.labels = labels;
    }


    /**
     * Converts a given {@code Task} into this class for Jackson use.
     */
    public JsonAdaptedTask(Task task) {
        title = task.getTitle();
        deadline = task.getDeadline();
        status = task.getStatus();
        priority = task.getPriority();
        labels = new ArrayList<>(task.getLabels());
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted
     *         person.
     */
    public Task toModelType() throws IllegalValueException {
        Task task = new Task(title, deadline, status, priority);
        labels.forEach(task::addLabel);
        return task;
    }
}

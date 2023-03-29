package vimification.model.task;

import static java.util.Objects.requireNonNull;
import static vimification.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Task {

    private String title;
    private LocalDateTime deadline;
    private Status status;
    private Priority priority;
    private Set<String> labels;

    /**
     * Every field must be present and not null.
     */
    public Task(String title, LocalDateTime deadline, Status status, Priority priority) {
        requireAllNonNull(title, status, priority);
        this.title = title;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
        this.labels = new HashSet<>();
    }

    public Task(String title) {            //new todo
        this(title, null, Status.NOT_DONE, Priority.UNKNOWN);
    }

    public Task(String title, Status status) {        //new todo with status
        this(title, null, status, Priority.UNKNOWN);
    }

    public Task(String title, Priority priority) {        //new todo with priority
        // used when creating new tasks
        this(title, null, Status.NOT_DONE, priority);
    }

    public Task(String title, Status status, Priority priority) {        //new todo with status and priority
        // used when creating new tasks
        this(title, null, status, priority);
    }
    public Task(String title, LocalDateTime deadline) {            //new deadline
        this(title, deadline, Status.NOT_DONE, Priority.UNKNOWN);
    }

    public Task(String title, LocalDateTime deadline, Status status) {        //new deadline with status
        this(title, deadline, status, Priority.UNKNOWN);
    }

    public Task(String title, LocalDateTime deadline, Priority priority) {        //new deadline with priority
        // used when creating new tasks
        this(title, deadline, Status.NOT_DONE, priority);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        requireNonNull(title);
        this.title = title;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        requireNonNull(deadline);
        this.deadline = deadline;
    }

    public void deleteDeadline() {
        this.deadline = null;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        requireNonNull(status);
        this.status = status;
    }

    public void setStatus(int level) {
        this.status = Status.fromInt(level);
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        requireNonNull(priority);
        this.priority = priority;
    }

    public void setPriority(int level) {
        this.priority = Priority.fromInt(level);
    }

    public boolean containsKeyword(String keyword) {
        return title.contains(keyword);
    }

    public void addLabel(String label) {
        requireNonNull(label);
        label = label.toLowerCase();
        if (!labels.add(label)) {
            throw new IllegalArgumentException("Tag already exists");
        }
    }

    public void deleteLabel(String label) {
        requireNonNull(label);
        label = label.toLowerCase();
        if (!labels.remove(label)) {
            throw new IllegalArgumentException("Tag does not exist");
        }
    }

    public boolean containsLabel(String label) {
        return labels.contains(label.toLowerCase());
    }

    public boolean isSamePriority(Priority priority) {
        return this.priority.equals(priority);
    }

    public boolean isSamePriority(int level) {
        return isSamePriority(Priority.fromInt(level));
    }

    public boolean isDateBefore(LocalDateTime date) {
        return deadline != null && (deadline.isBefore(date) || deadline.isEqual(date));
    }

    public boolean isDateAfter(LocalDateTime date) {
        return deadline != null && (deadline.isAfter(date) || deadline.isEqual(date));
    }

    public Task clone() {
        Task clonedTask = new Task(title, deadline, status, priority);
        clonedTask.labels.addAll(labels);
        return clonedTask;
    }

    public boolean isSameTask(Task otherTask) {
        if (otherTask == this) {
            return true;
        }
        return otherTask.title.equals(title)
                && otherTask.deadline.equals(deadline)
                && otherTask.status.equals(status)
                && otherTask.priority.equals(priority)
                && otherTask.labels.equals(labels);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Task)) {
            return false;
        }
        Task otherTask = (Task) other;
        return otherTask.title.equals(title) && otherTask.status.equals(status);
    }

    @Override
    public String toString() {
        return String.format("Task [title: %s;"
                + " deadline: %s; status: %s;"
                + " priority: %s; labels: %s]",
                title, deadline, status, priority, labels);
    }
}

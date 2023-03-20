package vimification.taskui;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import vimification.model.task.Task;
import javafx.fxml.FXML;

/**
 * Panel containing task details, such as description, the type of the task, etc.
 */
public class TaskDetailPanel extends UiPart<VBox> {
    private static final String FXML = "TaskDetailPanel.fxml";

    private Task task;

    @FXML
    private ListView<Task> taskListView;
    @FXML
    private Label descriptionText;
    @FXML
    private Label typeText;

    /**
     * Creates a {@code TaskDetailPanel}.
     */
    public TaskDetailPanel(Task task) {
        super(FXML);
        this.task = task;
        setup();
    }

    public void setup() {
        descriptionText.setText(task.getDescription());
        typeText.setText("Todo");
    }

}

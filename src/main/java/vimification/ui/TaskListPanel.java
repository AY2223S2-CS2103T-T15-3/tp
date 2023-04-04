package vimification.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.function.Predicate;

import javafx.event.Event;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import vimification.model.UiTaskList;
import vimification.model.task.Task;



/**
 * Panel containing the list of tasks.
 */
public class TaskListPanel extends UiPart<VBox> {
    private static final String FXML = "TaskListPanel.fxml";

    private UiTaskList taskList;

    public UiTaskList getTaskList() {
        return taskList;
    }

    private MainScreen mainScreen;

    @FXML
    private ListView<Task> taskListView;

    /**
     * Creates a {@code TaskListPanel} with the given {@code ObservableList}.
     */
    public TaskListPanel(UiTaskList taskList) {
        super(FXML);
        this.taskList = taskList;
        taskListView.setItems(taskList.getInternalList());
        taskListView.setCellFactory(listView -> new TaskListViewCell());
    }

    public void setMainScreen(MainScreen parent) {
        this.mainScreen = parent;
    }

    public void requestFocus() {
        taskListView.requestFocus();
    }

    @FXML
    private void initialize() {
        // TODO: Implement Visual Mode
        // taskListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.getRoot().setFocusTraversable(true);
        taskListView.setFocusTraversable(true);
        Platform.runLater(() -> {
            // Hackish way of requesting focus
            // after every listItem has been
            // loaded
            taskListView.requestFocus();
            taskListView.getSelectionModel().selectFirst();
        });
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
        case K:
            System.out.println("You've moved up");
            replaceKeyEvent(event, KeyCode.UP);
            break;

        case J:
            System.out.println("You've moved down");
            replaceKeyEvent(event, KeyCode.DOWN);
            break;

        default:
            System.out.println("You've pressed: " + event.getText());
            return; // Do nothing if user presses any other key.
        }
        rerender();
    }

    /**
     * To replace an old Keyevent with a new key event
     */
    public void replaceKeyEvent(KeyEvent event, KeyCode keyCode) {
        event.consume();
        KeyEvent newEvent = new KeyEvent(event.getSource(), event.getTarget(), event.getEventType(),
                event.getCharacter(), event.getText(), keyCode, event.isShiftDown(),
                event.isControlDown(), event.isAltDown(), event.isMetaDown());
        Event.fireEvent(event.getTarget(), newEvent);
    }

    /**
     * Rerender the right component of the main screen.
     */
    public void rerender() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        TaskDetailPanel detailTask = new TaskDetailPanel(selectedTask);
        mainScreen.loadRightComponent(detailTask);
    }

    public void searchForTask(Predicate<? super Task> predicate) {
        taskList.setPredicate(predicate);
    }
}

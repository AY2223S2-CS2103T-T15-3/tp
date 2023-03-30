package vimification.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import vimification.model.task.Task;



/**
 * Panel containing the list of tasks.
 */
public class TaskListPanel extends UiPart<VBox> {
    private static final String FXML = "TaskListPanel.fxml";

    private MainScreen mainScreen;

    @FXML
    private ListView<Task> taskListView;

    /**
     * Creates a {@code TaskListPanel} with the given {@code ObservableList}.
     */
    public TaskListPanel(ObservableList<Task> taskList) {
        super(FXML);
        taskListView.setItems(taskList);
        taskListView.setCellFactory(listView -> new TaskListViewCell());
    }

    public void setMainScreen(MainScreen parent) {
        this.mainScreen = parent;
    }

    public void requestFocus() {
        taskListView.requestFocus();
    }

    /**
     * Scroll to the kth index on the TaskListPanel.
     *
     * @param displayedIndex
     */
    public void scrollToTaskIndex(int displayedIndex) {
        taskListView.getFocusModel().focus(displayedIndex - 1);
        taskListView.getSelectionModel().select(displayedIndex - 1);
        taskListView.scrollTo(displayedIndex - 1);
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
        switch (event.getText()) {
        case "l":
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            TaskDetailPanel detailTask = new TaskDetailPanel(selectedTask);
            mainScreen.loadRightComponent(detailTask);
            break;
        case "j":
            System.out.println("You've moved down");
            navigateToNextCell();
            break;
        case "k":
            System.out.println("You've moved up");
            navigateToPrevCell();
            break;
        default:
            System.out.println("You've pressed: " + event.getText());
            break;
        }
    }


    private void navigateToNextCell() {
        int currIndex = taskListView.getFocusModel().getFocusedIndex();
        int lastIndex = taskListView.getItems().size();

        // We can only navigate to the next cell if we are not the last cell.
        boolean isCurrLastCell = currIndex == lastIndex;
        if (!isCurrLastCell) {
            taskListView.getFocusModel().focusNext();
            taskListView.getSelectionModel().select(currIndex + 1);
            taskListView.scrollTo(currIndex + 1);
        }
    }

    private void navigateToPrevCell() {
        int currIndex = taskListView.getFocusModel().getFocusedIndex();

        // We can only navigate to the previous cell if we are not the first cell.
        boolean isCurrFirstCell = currIndex == 0;
        if (!isCurrFirstCell) {
            taskListView.getFocusModel().focusPrevious();
            taskListView.getSelectionModel().select(currIndex - 1);
            taskListView.scrollTo(currIndex - 1);
        }
    }
}

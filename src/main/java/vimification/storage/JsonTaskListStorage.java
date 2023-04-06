package vimification.storage;

import java.io.IOException;
import java.nio.file.Path;

import vimification.common.exceptions.DataConversionException;
import vimification.common.util.JsonUtil;
import vimification.model.TaskList;

public class JsonTaskListStorage implements TaskListStorage {

    private Path filePath;

    public JsonTaskListStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getTaskListFilePath() {
        return filePath;
    }

    @Override
    public TaskList readTaskList() throws DataConversionException, IOException {
        return JsonUtil
                .readJsonFile(filePath, JsonAdaptedTaskList.class)
                .toModelType();
    }

    @Override
    public void saveTaskList(TaskList taskList) throws IOException {
        JsonUtil.saveJsonFile(new JsonAdaptedTaskList(taskList), filePath);
    }
}

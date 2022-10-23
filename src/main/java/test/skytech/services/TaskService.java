package test.skytech.services;

import test.skytech.entitys.Task;

public interface TaskService {

    Task getTaskById(long taskId);

    void addTask(long taskId, Task task);

    Task removeTask(long taskId);
}

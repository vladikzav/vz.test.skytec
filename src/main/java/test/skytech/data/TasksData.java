package test.skytech.data;

import test.skytech.entitys.Task;
import test.skytech.services.TaskService;
import java.util.concurrent.*;

public class TasksData implements TaskService {

    private volatile ConcurrentSkipListMap<Long, Task> tasks;


    public TasksData(){
        tasks = new ConcurrentSkipListMap<>();
    }

    public boolean taskExist(long taskId){
        return tasks.containsKey(taskId);
    }

    @Override
    public Task getTaskById(long taskId) {
        return tasks.get(taskId);
    }

    @Override
    public synchronized void addTask(long taskId, Task task) {
        tasks.put(taskId, task);
    }

    @Override
    public synchronized Task removeTask(long taskId) {
        return tasks.remove(taskId);
    }

    public synchronized ConcurrentSkipListMap<Long, Task> getTasks() {
        return tasks;
    }
}

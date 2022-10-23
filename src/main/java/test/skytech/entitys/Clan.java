package test.skytech.entitys;

import test.skytech.data.TasksData;

import java.util.concurrent.*;

public class Clan {

    private long id;     // id клана
    private String name; // имя клана
    private volatile int gold;    // текущее количество золота в казне клана

    private final TasksData tasks;

    public Clan(long id, String name)
    {
        this.id = id;
        this.name = name;
        tasks = new TasksData();
        gold = 0;
    }

    public void addTask(long taskId, Task task){
        tasks.addTask(taskId, task);
    }

    public void removeTask(long taskId){
        tasks.removeTask(taskId);
    }

    public Task getTaskById(long taskId){
        return tasks.getTaskById(taskId);
    }

    public ConcurrentSkipListMap<Long, Task> getTasks() {
        return tasks.getTasks();
    }

    public boolean taskExist(long taskId){
        return tasks.taskExist(taskId);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public synchronized void addGold(int gold) {
        this.gold += gold;
    }

    public synchronized void takeGold(int gold) {
        this.gold += gold;
    }

    public int getGold(){
        return gold;
    }


    @Override
    public String toString() {
        return "Clan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gold=" + gold +
                ", tasks=" + tasks +
                '}';
    }
}

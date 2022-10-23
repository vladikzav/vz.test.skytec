package test.skytech.controllers;

import test.skytech.Main;
import test.skytech.data.ClansData;
import test.skytech.data.TasksData;
import test.skytech.entitys.Clan;
import test.skytech.entitys.Task;
import test.skytech.services.GoldTransactionService;

public class TaskController implements GoldTransactionService {

    private final ClansData clans;
    private final TasksData tasks;

    public TaskController(ClansData clans, TasksData tasks) {
        this.clans = clans;
        this.tasks = tasks;
    }

    private boolean checkTask(long clanId, long taskId){
        Clan clan = clans.getClanById(clanId);
        return clan.taskExist(taskId);
    }

    public synchronized void completeTask(long clanId, long taskId) {
        // ...
        if(checkTask(clanId,taskId)){
            Task task = tasks.getTaskById(taskId);
            if(task.isDone())
            {
                Clan clan = clans.getClanById(clanId);
                clan.addGold(task.getGold());
                sendTransactionMessage(taskId, task.getGold(), clanId);
                clan.removeTask(taskId);
                // clan.[gold] += gold;
                // как-то сохранить изменения
            }
        }
    }

    @Override
    public String sendTransactionMessage(long from, int amount, long to) {
        String message = new java.util.Date() + ": Task " + from + " complete by clan: " + to +". " + amount + " gold received!";
        Main.sendMessage(message);
        return message;
    }

    @Override
    public String sendTransactionMessage(long from, int amount, long to, String memo) {
        String message = new java.util.Date() + ": Task " + from + " complete by clan: " + to +". "
                + amount + "gold received! Additional Info: " + memo;
        Main.sendMessage(message);
        return message;
    }

    @Override
    public String sendTransactionMessage(long from, long to) {
        String message = new java.util.Date() + ": Task " + from + " complete by clan: " + to;
        Main.sendMessage(message);
        return message;
    }

    @Override
    public String sendTransactionMessage(long from, long to, String memo) {
        String message = new java.util.Date() + ": Task " + from + " complete by clan: " + to +". Additional Info: " + memo;
        Main.sendMessage(message);
        return message;
    }

    @Override
    public String sendTransactionMessage(long from) {
        return null;
    }

    @Override
    public synchronized String sendTransactionMessage(String message) {
        message = new java.util.Date()+ ": " + message;
        Main.sendMessage(message);
        return message;
    }

}

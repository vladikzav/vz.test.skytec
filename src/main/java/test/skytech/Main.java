package test.skytech;


import test.skytech.controllers.ClanGoldOperationsController;
import test.skytech.controllers.TaskController;
import test.skytech.data.ClansData;
import test.skytech.data.TasksData;
import test.skytech.data.UsersData;
import test.skytech.entitys.Clan;
import test.skytech.entitys.Task;
import test.skytech.entitys.User;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.*;

public class Main {

    public volatile static ClanGoldOperationsController clanGoldOperationsController;
    public volatile static TaskController taskController;
    public volatile static TasksData tasksData;
    public volatile static UsersData usersData;
    public volatile static ClansData clansData;

    public static volatile Vector<String> transactions;

    public static volatile Vector<String> threads;
    public static volatile ConcurrentSkipListMap<Long, Task> taskQueue;

    public static void main(String[] args) {
        tasksData = new TasksData();
        usersData = new UsersData();
        clansData = new ClansData();
        transactions = new Vector<>();
        threads = new Vector<>();
        taskQueue = new ConcurrentSkipListMap<>();

        for (int i = 0; i < 100; i++) {
            tasksData.addTask(i, new Task((i), 10*i));
            System.out.println(tasksData.getTaskById(i).toString());
            usersData.addUser(i, new User(i, "userName"+i));
            System.out.println(usersData.getUserById(i).toString());
            clansData.addClan(i, new Clan(i, "clanName"+i));
            System.out.println(clansData.getClanById(i).toString());
        }
        for (int i = 0; i < 100; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, 100);
            usersData.getUserById(i).addGold(i*10);
            System.out.println(usersData.getUserById(i).toString());
            clansData.getClanById(i).addTask(randomNum, tasksData.getTaskById(randomNum));
            System.out.println(clansData.getClanById(i) + " Added task: " + clansData.getClanById(i).getTaskById(randomNum).toString());
        }

        clanGoldOperationsController = new ClanGoldOperationsController(clansData, usersData);
        taskController = new TaskController(clansData, tasksData);

        for (int i = 0; i < 30; i++) {
            new TaskMakerThread("TaskMakerThread"+i);
            new TaskPayerThread("TaskPayerThread"+i);
            new UserTransactionThread("UserTransactionThread"+i);
            new TaskGiverThread("TaskGiverThread"+i);
            new GoldGiverThread("GoldGiverThread"+i);
        }

        new LastThread("LastOne");

    }

    public static synchronized void threadsIsDone(String string){
        threads.add(string);
    }

    public static synchronized void sendMessage(String string){
        System.out.println(string);
        transactions.add(string);
    }

    public static void addDoneTaskToQueue(long clanId, long taskId) throws InterruptedException {
        synchronized (clansData.getClanById(clanId).getTasks()) {
            clansData.getClanById(clanId).getTasks().wait(50);
            if (!clansData.getClanById(clanId).getTasks().isEmpty()) {
                clansData.getClanById(clanId).getTaskById(taskId).setDone();
                taskQueue.put(clanId, clansData.getClanById(clanId).getTaskById(taskId));
            }
            clansData.getClanById(clanId).getTasks().notify();
        }
    }

    public static synchronized void removeDoneTaskFromQueue(){
        if(!Main.taskQueue.isEmpty()) {
            taskController.completeTask(taskQueue.firstEntry().getKey(), taskQueue.firstEntry().getValue().getId());
            taskQueue.remove(taskQueue.firstEntry().getKey());
        }
    }

    public static void addRandomTaskToClan(long clanId){
        int randomNum = ThreadLocalRandom.current().nextInt(0, 100);
        clansData.getClanById(clanId).addTask(randomNum, tasksData.getTaskById(randomNum));
    }

    public static void addGoldToUser(long userId, int gold) {
        sendMessage(new java.util.Date() + ": User: " + userId + " was blessed by the gods! They gave him " + gold + " gold!");
        usersData.getUserById(userId).addGold(gold);
    }
}

class TaskMakerThread implements Runnable
{
    Thread thread;
    String name;
    TaskMakerThread(String name) {
        this.name = name;
        thread = new Thread(this, name);
        System.out.println("Создан дополнительный поток " +
                thread);
        thread.start();
    }
    @Override
    public void run() {
        try {
            int i = 0;
            while (i<100){
                int randomClanId = ThreadLocalRandom.current().nextInt(0, 100);
                if(!Main.clansData.getClanById(randomClanId).getTasks().isEmpty()){
                Main.addDoneTaskToQueue(randomClanId, Main.clansData.getClanById(randomClanId).getTasks().firstKey());
                System.out.println("\tдополнительный поток " + name + ". Итерация номер: " + i);
                Thread.sleep(500);
                }else {
                    i--;
                }

                i++;
            }
            Main.threadsIsDone(name +" : Is Done!");
        } catch (InterruptedException e) {
            System.out.println(
                    "\tдополнительный поток прерван");
        }
        System.out.println(
                "\tдополнительный поток "+ name +" завершён");
    }
}

class TaskPayerThread implements Runnable
{
    Thread thread;
    String name;
    TaskPayerThread(String name) {
        this.name = name;
        thread = new Thread(this, name);
        System.out.println("Создан дополнительный поток " +
                thread);
        thread.start();
    }
    @Override
    public void run() {
        try {
            int i = 0;
            while (i<100){
                System.out.println("\tдополнительный поток " + name + ". Итерация номер: " + i);
                Main.removeDoneTaskFromQueue();
                Thread.sleep(500);
                i++;
            }
            Main.threadsIsDone(name +" : Is Done!");
        } catch (InterruptedException e) {
            System.out.println(
                    "\tдополнительный поток прерван");
        }
        System.out.println(
                "\tдополнительный поток "+ name +" завершён");
    }
}

class UserTransactionThread implements Runnable
{
    Thread thread;
    String name;
    UserTransactionThread(String name) {
        this.name = name;
        thread = new Thread(this, name);
        System.out.println("Создан дополнительный поток " +
                thread);
        thread.start();
    }
    @Override
    public void run() {
        try {
            int i = 0;
            while (i<100){
                int randomUserId = ThreadLocalRandom.current().nextInt(0, 100);
                int randomClanId = ThreadLocalRandom.current().nextInt(0, 100);
                int randomGold = ThreadLocalRandom.current().nextInt(0, 200);
                System.out.println("\tдополнительный поток " + name + ". Итерация номер: " + i);
                Main.clanGoldOperationsController.addGoldToClan(randomUserId, randomClanId, randomGold);
                Thread.sleep(500);
                i++;
            }
            Main.threadsIsDone(name +" : Is Done!");
            System.out.println(
                    "\tдополнительный поток "+ name +" завершён");
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

class GoldGiverThread implements Runnable
{
    Thread thread;
    String name;
    GoldGiverThread(String name) {
        this.name = name;
        thread = new Thread(this, name);
        System.out.println("Создан дополнительный поток " +
                thread);
        thread.start();
    }
    @Override
    public void run() {
        try {
            int i =0;
            while (i<100){
                int randomNum = ThreadLocalRandom.current().nextInt(0, 100);
                int randomAmount = ThreadLocalRandom.current().nextInt(100, 1000);
                Main.addGoldToUser(randomNum, randomAmount);
                System.out.println("\tдополнительный поток " + name + "итерация номер: " + i);
                Thread.sleep(500);
                i++;
            }
            Main.threadsIsDone(name +" : Is Done!");
        } catch (InterruptedException e) {
            System.out.println(
                    "\tдополнительный поток прерван");
        }
        System.out.println(
                "\tдополнительный поток "+ name +" завершён");
    }
}

class TaskGiverThread implements Runnable
{
    Thread thread;
    String name;
    TaskGiverThread(String name) {
        this.name = name;
        thread = new Thread(this, name);
        System.out.println("Создан дополнительный поток " +
                thread);
        thread.start();
    }
    @Override
    public void run() {
        try {
            int i =0;
            while (i<100){
                int randomNum = ThreadLocalRandom.current().nextInt(0, 100);
                Main.addRandomTaskToClan(randomNum);
                System.out.println("\tдополнительный поток " + name + "итерация номер: " + i);
                Thread.sleep(500);
                i++;
            }
            Main.threadsIsDone(name +" : Is Done!");
        } catch (InterruptedException e) {
            System.out.println(
                    "\tдополнительный поток прерван");
        }
        System.out.println(
                "\tдополнительный поток "+ name +" завершён");
    }
}

class LastThread implements Runnable
{
    Thread thread;
    String name;
    LastThread(String name) {
        this.name = name;
        thread = new Thread(this, name);
        System.out.println("Создан дополнительный поток " +
                thread);
        thread.start();
    }
    @Override
    public void run() {
        try {
            int i = 0;
            while (i<100){
                Thread.sleep(600);
                System.out.println("Monitoring thread is active. Iteration "+i+"/100");
                i++;
            }
            for (int j = 0; j < Main.transactions.size(); j++) {
                System.out.println(Main.transactions.get(j));
            }
            System.out.println(Main.threads.size() + " threads is done successfully");
        } catch (InterruptedException e) {
            System.out.println(
                    "\tдополнительный поток прерван");
        }
        System.out.println(
                "\tдополнительный поток "+ name +" завершён");
    }
}
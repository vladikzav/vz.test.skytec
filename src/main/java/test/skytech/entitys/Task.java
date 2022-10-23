package test.skytech.entitys;

public class Task {

    private final long id;

    private final int gold;

    private volatile boolean done;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", gold=" + gold +
                ", done=" + done +
                '}';
    }

    public Task(long id, int gold){
        done = false;
        this.id = id;
        this.gold = gold;
    }

    public synchronized void setDone(){
        done = true;
    }

    public boolean isDone(){
        return done;
    }

    public long getId() {
        return id;
    }

    public int getGold() {
        return gold;
    }
}

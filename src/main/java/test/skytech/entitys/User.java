package test.skytech.entitys;

public class User {

    private long id;

    private String name;
    private volatile int gold;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gold=" + gold +
                '}';
    }

    public User(long id, String name){
        this.id = id;
        this.name = name;
        gold = 0;
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

    public boolean checkGoldAmount(int gold) {
        return this.gold >= gold;
    }

    public int getGold(){
        return gold;
    }
}

package test.skytech.data;

import test.skytech.entitys.User;
import test.skytech.services.UserService;
import java.util.concurrent.ConcurrentSkipListMap;

public class UsersData implements UserService {

    private volatile ConcurrentSkipListMap<Long,User> users;


    public UsersData(){
        users = new ConcurrentSkipListMap<>();
    }
    @Override
    public User getUserById(long userId) {
        return users.get(userId);
    }

    @Override
    public synchronized void addUser(long userId, User user) {
        users.put(userId, user);
    }


}

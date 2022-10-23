package test.skytech.services;

import test.skytech.entitys.User;

public interface UserService {

    User getUserById(long userId);

    void addUser(long userId, User user);
}

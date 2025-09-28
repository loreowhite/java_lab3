package com.example.servlet;

import java.util.HashMap;
import java.util.Map;

public class UserStorage {
    private static final Map<String, User> users = new HashMap<>();

    public static void save(User user) {
        users.put(user.getLogin(), user);
    }

    public static User find(String login) {
        return users.get(login);
    }

    public static boolean checkCredentials(String login, String password) {
        User u = users.get(login);
        return u != null && u.getPassword().equals(password);
    }
}
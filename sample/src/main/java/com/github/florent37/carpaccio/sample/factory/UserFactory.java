package com.github.florent37.carpaccio.sample.factory;

import com.github.florent37.carpaccio.sample.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 27/07/15.
 */
public class UserFactory {

    protected static List<Object> generateUserList() {
        List<Object> users = new ArrayList<>();
        for (int i = 0; i < 20; ++i) {
            users.add(new User("User" + i, "http://lorempixel.com/" + generateRandom() + "/" + generateRandom() + "/"));
        }
        return users;
    }

    public static int generateRandom() {
        return (int) (400 + (Math.random() * 100));
    }
}

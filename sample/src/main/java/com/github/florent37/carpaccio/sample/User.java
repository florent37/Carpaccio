package com.github.florent37.carpaccio.sample;

/**
 * Created by florentchampigny on 23/07/15.
 */
public class User {

    String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

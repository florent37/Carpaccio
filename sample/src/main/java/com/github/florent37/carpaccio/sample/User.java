package com.github.florent37.carpaccio.sample;

/**
 * Created by florentchampigny on 23/07/15.
 */
public class User {

    String name;
    String image;

    public User(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return name;
    }
}

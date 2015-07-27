package com.github.florent37.carpaccio.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.florent37.carpaccio.Carpaccio;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);
//
        //Carpaccio carpaccio = (Carpaccio) findViewById(R.id.carpaccio);
        //carpaccio.mapObject("user1",new User("florent","http://i.imgur.com/DvpvklR.png"));
        //carpaccio.mapObject("user2",new User("champigny",null));
        //carpaccio.mapObject("user3",new User("FROM android:text",null));

        setContentView(R.layout.activity_main_recyclerview_mapping);

        Carpaccio carpaccio = (Carpaccio) findViewById(R.id.carpaccio);
        carpaccio.mapList("user",generateUserList());

    }

    protected List<Object> generateUserList(){
        List<Object> users = new ArrayList<>();
        for(int i=0;i<20;++i){
            users.add(new User("User"+i,"http://lorempixel.com/40"+i+"/40"+i+"/"));
        }
        return users;
    }

}

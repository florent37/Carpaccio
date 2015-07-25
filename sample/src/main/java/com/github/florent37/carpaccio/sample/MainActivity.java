package com.github.florent37.carpaccio.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.florent37.carpaccio.Carpaccio;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scroll);

        //Carpaccio carpaccio = (Carpaccio) findViewById(R.id.carpaccio);
        //carpaccio.mapObject("user1",new User("florent"));
        //carpaccio.mapObject("user2",new User("champigny"));
    }


}

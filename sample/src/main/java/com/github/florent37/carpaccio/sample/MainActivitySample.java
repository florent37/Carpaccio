package com.github.florent37.carpaccio.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.florent37.carpaccio.Carpaccio;
import com.github.florent37.carpaccio.sample.model.User;

import butterknife.Bind;
import butterknife.ButterKnife;
public class MainActivitySample extends AppCompatActivity {

    @Bind(R.id.carpaccio) Carpaccio carpaccio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sample);

        ButterKnife.bind(this);

        carpaccio.mapObject("user1", new User("florent", "http://i.imgur.com/DvpvklR.png"));
        carpaccio.mapObject("user2", new User("champigny", null));
        carpaccio.mapObject("user3", new User("FROM android:text", null));
    }


}

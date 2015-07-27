package com.github.florent37.carpaccio.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.florent37.carpaccio.Carpaccio;
import com.github.florent37.carpaccio.sample.factory.UserFactory;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivityRecycler extends AppCompatActivity {

    @Bind(R.id.carpaccio) Carpaccio carpaccio;
    protected List<Object> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_recyclerview_mapping);
        ButterKnife.bind(this);

        this.users = UserFactory.generateUserList();

        carpaccio.mapList("user", this.users);
    }

}

package com.github.florent37.carpaccio.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.carpaccio.Carpaccio;
import com.github.florent37.carpaccio.controllers.adapter.Holder;
import com.github.florent37.carpaccio.controllers.adapter.OnItemClickListener;
import com.github.florent37.carpaccio.controllers.adapter.OnItemSwipedListener;
import com.github.florent37.carpaccio.controllers.adapter.RecyclerViewCallbackAdapter;
import com.github.florent37.carpaccio.sample.factory.UserFactory;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivityRecycler extends AppCompatActivity {

    @Bind(R.id.carpaccio)
    Carpaccio carpaccio;
    protected List<Object> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_recyclerview_mapping);
        ButterKnife.bind(this);

        this.users = UserFactory.generateUserList();

        carpaccio.mapList("user", this.users);
        carpaccio.mapObject("userHeader", UserFactory.generateUser());
        carpaccio.onItemClick("user", new OnItemClickListener() {
            @Override
            public boolean isClickable(Object item, int position, Holder holder) {
                return false;
            }

            @Override
            public void onItemClick(Object item, int position, Holder holder) {
                Toast.makeText(getApplicationContext(), "position " + position, Toast.LENGTH_SHORT).show();
            }

        });
        carpaccio.onItemSwiped("user",new OnItemSwipedListener(){

            @Override
            public boolean canSwipe(int position, Object object) {
                return position > 3;
            }

            @Override
            public boolean onItemSwiped(Object item, int position, Holder holder, RecyclerView.Adapter adapter) {
                return position%2 == 0;
            }
        });
    }

}

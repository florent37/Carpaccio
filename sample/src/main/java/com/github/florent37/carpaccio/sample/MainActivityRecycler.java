package com.github.florent37.carpaccio.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.carpaccio.Carpaccio;
import com.github.florent37.carpaccio.controllers.adapter.Holder;
import com.github.florent37.carpaccio.controllers.adapter.OnItemClickListener;
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
        carpaccio.onItemClick("user", new OnItemClickListener() {
            @Override
            public void onItemClick(Object item, int position, View view) {
                Toast.makeText(getApplicationContext(), "position " + position, Toast.LENGTH_SHORT).show();
            }
        });
        carpaccio.getAdapter("user").setRecyclerViewCallback(new RecyclerViewCallbackAdapter<Object>() {

            @Override
            public Holder<Object> onCreateViewHolder(View view, int viewType) {
                return new Holder<Object>(view){
                    @Override
                    public void onBind(Object object) {
                        super.onBind(object);
                    }
                };
            }
        });
    }

}

package com.github.florent37.carpaccio.sample.androidversion;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.carpaccio.sample.R;
import com.squareup.picasso.Picasso;

/**
 * Created by florentchampigny on 22/07/15.
 */
public class AndroidVersionViewHolder extends RecyclerView.ViewHolder{
    TextView textView;
    ImageView imageView;

    public AndroidVersionViewHolder(View itemView) {
        super(itemView);
        this.textView = (TextView) itemView.findViewById(R.id.text);
        this.imageView = (ImageView) itemView.findViewById(R.id.image);
    }

    public void bind(AndroidVersion androidVersion) {
        this.textView.setText(androidVersion.getTitre());
        Picasso.with(imageView.getContext()).load(androidVersion.getUrl()).into(imageView);
    }
}

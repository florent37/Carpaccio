package com.github.florent37.carpacciocontrollers;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.materialimageloading.MaterialImageLoading;
import com.squareup.picasso.Callback;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class ImageViewController {

    private static final String TAG = "ImageViewController";

    class ImageViewToAnimateMaterial {
        ImageView imageView;
        int duration;

        public ImageViewToAnimateMaterial(ImageView imageView, int duration) {
            this.imageView = imageView;
            this.duration = duration;
        }
    }

    List<ImageViewToAnimateMaterial> imageViewToAnimateMaterial = new ArrayList<>();

    public void url(final ImageView imageView, String url) {
        Picasso.with(imageView.getContext()).load(url).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                onImageLoadedFromUrl(imageView);
            }

            @Override
            public void onError() {

            }
        });
    }

    //can be overrided
    protected void onImageLoadedFromUrl(ImageView imageView) {
        startWaitingAnimateMaterialImageView(imageView);
    }

    protected void startWaitingAnimateMaterialImageView(ImageView imageView) {
        ImageViewToAnimateMaterial toRemove = null;
        for (ImageViewToAnimateMaterial toAnimateMaterial : imageViewToAnimateMaterial) {
            if (toAnimateMaterial.imageView.equals(imageView)) {
                toRemove = toAnimateMaterial;
                startAnimateMaterialImageView(toAnimateMaterial.imageView, toAnimateMaterial.duration);
            }
        }
        if (toRemove != null)
            imageViewToAnimateMaterial.remove(toRemove);
    }


    protected void startAnimateMaterialImageView(ImageView imageView, int duration) {
        try {
            MaterialImageLoading.animate(imageView).setDuration(duration).start();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void animateMaterial(ImageView imageView, String duration) {
        Integer dur = CommonViewController.stringToInt(duration);
        if (dur != null) {
            if (imageView.getDrawable() == null) {
                imageViewToAnimateMaterial.add(new ImageViewToAnimateMaterial(imageView, dur));
            } else {
                startAnimateMaterialImageView(imageView, dur);
            }
        }
    }

    public void kenburns(ImageView imageView) {
        ReplaceViewController replaceViewController = new ReplaceViewController();
        KenBurnsView kenBurnsView = replaceViewController.replace(imageView,"com.flaviofaria.kenburnsview.KenBurnsView");
    }

}

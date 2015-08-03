package com.github.florent37.carpaccio.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.florent37.carpaccio.Carpaccio;
import com.github.florent37.carpaccio.controllers.helper.FastBlurHelper;
import com.github.florent37.carpaccio.controllers.helper.GrayScaleHelper;
import com.github.florent37.carpaccio.controllers.transformation.BlurTransformation;
import com.github.florent37.carpaccio.controllers.transformation.GrayScaleTransformation;
import com.github.florent37.materialimageloading.MaterialImageLoading;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class ImageViewController {

    private static final String TAG = "ImageViewController";
    public static ConcurrentHashMap<String, Bitmap> PREVIEW_BITMAPS = new ConcurrentHashMap<>();

    public boolean ENABLE_PREVIEW = false;

    class ImageViewToAnimateMaterial {
        ImageView imageView;
        int duration;

        public ImageViewToAnimateMaterial(ImageView imageView, int duration) {
            this.imageView = imageView;
            this.duration = duration;
        }
    }


    List<ImageViewToAnimateMaterial> imageViewToAnimateMaterial = new ArrayList<>();
    HashMap<View, List<Transformation>> transformations = new HashMap<>();

    protected void addTransformation(View view, Transformation transformation) {
        List<Transformation> list = transformations.get(view);
        if (list == null) {
            list = new ArrayList<>();
            transformations.put(view, list);
        }
        list.add(transformation);
    }

    public void enablePreview(ImageView imageView) {
        ENABLE_PREVIEW = true;
    }

    public void previewUrl(final ImageView imageView, String url) {
        if (Carpaccio.IN_EDIT_MODE && ENABLE_PREVIEW)
            url(imageView, url);
    }

    public void circle(ImageView imageView) {
        new CommonViewController().replaceViewithTagToRemove(imageView, "de.hdodenhof.circleimageview.CircleImageView", "circle()");
    }

    public void setUrl(final ImageView imageView, String url) {
        url(imageView,url);
    }

    public void url(final ImageView imageView, String url) {
        if (Carpaccio.IN_EDIT_MODE && ENABLE_PREVIEW) {
            Bitmap bitmap = PREVIEW_BITMAPS.get(url);
            try {
                if (bitmap == null) {
                    URL src = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) src.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(input);

                    if (bitmap != null)
                        PREVIEW_BITMAPS.put(url, bitmap);
                }
            } catch (Exception e) {
                if (imageView.getDrawable() == null)
                    imageView.setImageDrawable(new ColorDrawable(Color.parseColor("#3E3E3E")));
            }

            if (bitmap != null)
                imageView.setImageBitmap(bitmap);
        } else {
            RequestCreator requestCreator = Picasso.with(imageView.getContext()).load(url);
            if (transformations.get(imageView) != null) {
                requestCreator = requestCreator.transform(transformations.get(imageView));
                transformations.remove(imageView);
            }

            if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP)
                requestCreator = requestCreator.centerCrop().fit();

            if (imageView.getScaleType() == ImageView.ScaleType.CENTER_INSIDE)
                requestCreator = requestCreator.centerInside().fit();

            requestCreator.into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    onImageLoadedFromUrl(imageView);
                }

                @Override
                public void onError() {

                }
            });
        }
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

    public void animateMaterial(ImageView imageView, int duration) {
        if (!imageView.isInEditMode()) {
            if (imageView.getDrawable() == null) {
                willAnimateMaterial(imageView, duration);
            } else {
                startAnimateMaterialImageView(imageView, duration);
            }
        }
    }


    public void willAnimateMaterial(ImageView imageView, int duration) {
        imageViewToAnimateMaterial.add(new ImageViewToAnimateMaterial(imageView, duration));
    }

    public void willBlur(ImageView imageView, int radius) {
        addTransformation(imageView, new BlurTransformation(radius));
    }

    public void willGrayScale(ImageView imageView) {
        addTransformation(imageView, new GrayScaleTransformation());
    }

    public void kenburns(ImageView imageView) {
        new CommonViewController().replaceViewithTagToRemove(imageView, "com.flaviofaria.kenburnsview.KenBurnsView", "kenburns()");
    }

    public void blur(final ImageView imageView, final int radiusString) {
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap newBitmap = FastBlurHelper.getBitmapBlurFromView(imageView, radiusString);
                    imageView.setImageBitmap(newBitmap);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }, 10);

    }

    public void greyScale(final ImageView imageView) {
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap newBitmap = GrayScaleHelper.getBitmapGreyScaleFromView(imageView);
                    imageView.setImageBitmap(newBitmap);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }, 10);

    }
}

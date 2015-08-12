# Carpaccio

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Carpaccio-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/2211)
[![Join the chat at https://gitter.im/florent37/Carpaccio](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/florent37/Carpaccio?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Developed to facilitate integration on Android ( Designers can thanks me :D )

![logo](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/carpaccio_small.png)

**Data Mapping & Smarter Views framework for android**

With Carpaccio, your views became smarter, instead of calling functions on views, now your views can call functions !
You no longer need to extend a view to set a custom behavior

Carpaccio also come with a beautiful mapping engine !

#Usage

```xml
<com.github.florent37.carpaccio.Carpaccio
        android:id="@+id/carpaccio"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:register="
            com.github.florent37.carpaccio.controllers.ImageViewController;
            com.github.florent37.carpaccio.controllers.TextViewController
        ">

        <ImageView
               android:layout_width="match_parent"
               android:layout_height="150dp"
               android:scaleType="centerCrop"
               android:tag="url(http://i.imgur.com/DSjXNox.jpg)" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textSize="20sp"
            android:tag="
                font(Roboto-Thin.ttf);
                setText($user.name);"/>

</com.github.florent37.carpaccio.Carpaccio>
```

![url](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/sample_crop.png)

#Download

Add into your **build.gradle**

[![Download](https://api.bintray.com/packages/florent37/maven/Carpaccio/images/download.svg)](https://bintray.com/florent37/maven/Carpaccio/_latestVersion)

```groovy
compile ('com.github.florent37:carpaccio:1.0.0@aar'){
    transitive=true
}
```

------------

#DataBinding

```xml
<TextView
       android:tag="
            setText($user)
       "/>

<ImageView
       android:tag="
           url($user.getImageUrl())
       " />

<ImageView
       android:tag="
            url($user.imageUrl)
       "/>

<TextView
       android:text="$user.name"/>
```

In your activity / fragment :

```java
Carpaccio carpaccio = (Carpaccio)findViewById("R.id.carpaccio");
carpaccio.mapObject("user",new User("florent", "www."));
```

![set_text](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/set_text.png)

------------

##RecyclerView Mapping

You dreamed it, Carpaccio did it ! You can now bind a List with a RecyclerView !
**WORKS WITH ANDROID STUDIO PREVIEW !!!**, don't hesitate to refresh your preview
![url](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/refresh.png)

![recycler](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/recycler_small.png)
![recycler_preview](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/recycler_preview_small.png)


R.layout.activity_main_recyclerview_mapping
```xml
<com.github.florent37.carpaccio.Carpaccio
        android:id="@+id/carpaccio"
        app:register="
            com.github.florent37.carpaccio.controllers.RecyclerViewController;
            com.github.florent37.carpaccio.controllers.ImageViewController;
            com.github.florent37.carpaccio.controllers.TextViewController;
        ">

        <android.support.v7.widget.RecyclerView
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"

                    android:tag="
                        adapter(user,R.layout.cell_user)
                    "
                    />

</com.github.florent37.carpaccio.Carpaccio>
```

R.layout.cell_user
```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="100dp"
    android:gravity="center_vertical"
    android:orientation="horizontal">

        <ImageView
            android:layout_width="100dp"
            android:layout_height="match_parent"
            android:layout_marginRight="20dp"
            android:tag="
                enablePreview();
                previewUrl(http://lorempixel.com/400/400/);
                url($user.image);
                " />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:tag="
                setText($user.name);
                setFont(Roboto-Black.ttf);"
            />

</LinearLayout>
```

Finally, in your activiy/fragment you just have to indicate the List to map !

```java
setContentView(R.layout.activity_main_recyclerview_mapping);
Carpaccio carpaccio = (Carpaccio)findViewById("R.id.carpaccio");
carpaccio.mapList("user", this.users);
```

------------

#ViewControllers

Carpaccio provide some awesome ViewControllers, you can use them directly into your project
[Read the Wiki to have a list of all provided viewControllers](https://github.com/florent37/Carpaccio/wiki)

------------

TextViewController can set a custom font (from assets/fonts/) to a TextView and provide text binding

**WORKS WITH ANDROID STUDIO PREVIEW !!!**

![font](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/custom_ttf_small.png)

```xml
<com.github.florent37.carpaccio.Carpaccio
        app:register="
            com.github.florent37.carpaccio.controllers.TextViewController;
        ">

        <TextView
             android:tag="
                 font(Pacifico.ttf);
                 android:text="$user.getName()"
             "/>
</com.github.florent37.carpaccio.Carpaccio>
```

------------

##ImageViewController

```xml
<com.github.florent37.carpaccio.Carpaccio
        app:register="
            com.github.florent37.carpaccio.controllers.ImageViewController;
        ">

        <ImageView
             android:tag="
                 url(http://i.imgur.com/DvpvklR.png)
             " />
</com.github.florent37.carpaccio.Carpaccio>
```

![url](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/url_small.png)

**WORKS WITH ANDROID STUDIO PREVIEW !!!**, don't hesitate to refresh your preview
![url](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/refresh.png)

Preview an url image

```xml
<ImageView
     android:tag="
        enablePreview();
        url(http://i.imgur.com/DvpvklR.png);
     " />
```

![url](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/preview_image_url_small.png)

And some awesome customisations


![circle](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/circle_small_2.png)
![blur](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/blur_small.png)
![greyscale](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/greyscale_small.png)

```xml
<ImageView
      android:tag="

           circle();
           blur(); OR willBlur();
           greyScale(); OR willGreyScale();

           url(http://i.imgur.com/DvpvklR.png);
      " />
```

[![Video](http://share.gifyoutube.com/mGz9OZ.gif)](https://youtu.be/A0eyvpNh5wM)
[![Video](http://share.gifyoutube.com/vpMYjp.gif)](https://youtu.be/4b84gswKGkA)


```xml
<ImageView
      android:tag="
           animateMaterial(6000);
           kenburns();
           url(http://i.imgur.com/DvpvklR.png);
      " />
```

#Community

Looking for contributors, feel free to fork !

Tell me if you're using my library in your application, I'll share it in this README

#Dependencies

* [Picasso][picasso] (from Square)
* [KenBurnsView][kenburnsview] (from flavioarfaria)
* [Android-Observablescrollview][android-observablescrollview] (from ksoichiro)

#Credits

Author: Florent Champigny
www.florentchampigny.com/

<a href="https://plus.google.com/+florentchampigny">
  <img alt="Follow me on Google+"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/gplus.png" />
</a>
<a href="https://twitter.com/florent_champ">
  <img alt="Follow me on Twitter"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/twitter.png" />
</a>
<a href="https://www.linkedin.com/profile/view?id=297860624">
  <img alt="Follow me on LinkedIn"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/linkedin.png" />
</a>


License
--------

    Copyright 2015 florent37, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[picasso]: https://github.com/square/picasso
[kenburnsview]: https://github.com/flavioarfaria/KenBurnsView
[android-observablescrollview]: https://github.com/ksoichiro/Android-ObservableScrollView

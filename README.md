# Carpaccio

Make your view smarter !

#Usage

```xml
<com.github.florent37.carpaccio.Carpaccio
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:register="
            com.github.florent37.carpacciocontrollers.ImageViewController;
            com.github.florent37.carpacciocontrollers.TextViewController
        ">

        <ImageView
             android:layout_width="match_parent"
             android:layout_height="150dp"
             android:tag="
                 url(http://i.imgur.com/DvpvklR.png)
             " />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Hi, how are you ?"
            android:textSize="20sp"
            android:tag="
                font(Roboto-Light.ttf)
            "/>

</com.github.florent37.carpaccio.Carpaccio>
```

#Download

Add into your **build.gradle**

```groovy
compile ('com.github.florent37:carpaccio:1.0.0){
    transitive=true
}
```

#ViewControllers

Carpaccio provide some ViewControllers, you can import them directly into your project

Add into your **build.gradle**

```groovy
compile ('com.github.florent37:carpacciocontrollers:1.0.0){
    transitive=true
}
```

##ImageViewController

ImageViewController can directly set an image source from an url

```xml
<com.github.florent37.carpaccio.Carpaccio
        app:register="
            com.github.florent37.carpacciocontrollers.ImageViewController;
        ">

        <ImageView
             android:tag="
                 url(http://i.imgur.com/DvpvklR.png)
             " />
</com.github.florent37.carpaccio.Carpaccio>
```

##TextViewController

TextViewController can set a custom font (from assets/fonts/) to a TextView

```xml
<com.github.florent37.carpaccio.Carpaccio
        app:register="
            com.github.florent37.carpacciocontrollers.TextViewController;
        ">

        <TextView
             android:tag="
                 font(Roboto-Light.ttf)
             "/>
</com.github.florent37.carpaccio.Carpaccio>
```

##ParallaxViewController

ParallaxViewController can add a paralax effect on ObservableScrollView childs

```xml
<com.github.florent37.carpaccio.Carpaccio
        app:register="
            com.github.florent37.carpacciocontrollers.ParallaxViewController;
        ">

        <com.github.ksoichiro.android.observablescrollview.ObservableScrollView
                    android:tag="registerParallax()">

              <View
                    android:tag="
                        parallaxY(0.5);
                    " />

              <View
                    android:tag="
                        parallaxY(1.5);
                    " />

        </com.github.ksoichiro.android.observablescrollview.ObservableScrollView

</com.github.florent37.carpaccio.Carpaccio>
```

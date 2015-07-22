# Carpaccio

Make your view smarter !

Usage
--------

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

Download
--------

Add into your **build.gradle**

```groovy
compile ('com.github.florent37:carpaccio:1.0.0){
    transitive=true
}
```

If you want to use the carpaccio default view controllers :

* ImageViewController : url(imageUrl)
* TextViewController : font(fontName)
* ParallaxViewController : registerParallax() & parallax(float)

Add into your **build.gradle**

```groovy
compile ('com.github.florent37:carpacciocontrollers:1.0.0){
    transitive=true
}
```
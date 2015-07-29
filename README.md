# Carpaccio

![logo](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/carpaccio.png)

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
            com.github.florent37.carpacciocontrollers.ImageViewController;
            com.github.florent37.carpacciocontrollers.TextViewController
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

```groovy
compile ('com.github.florent37:carpaccio:1.0.0'){
    transitive=true
}
compile ('com.github.florent37:carpacciocontrollers:1.0.0'){
    transitive=true
}
```

#DataBinding

```xml
<TextView
       android:tag="
            setText($user)
       "/>
```
( Which will call user.toString )

In your activity / fragment :

```java
Carpaccio carpaccio = (Carpaccio)findViewById("R.id.carpaccio");
carpaccio.mapObject("user",new User("florent"));
```

![set_text](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/set_text.png)

You can also specify a method (must return a String)

```xml
<TextView
       android:tag="
            setText($user.getName())
       "/>
```

And simplify the method name

```xml
<TextView
       android:tag="
            setText($user.name)
       "/>
```

##RecyclerView Mapping

You dreamed it, Carpaccio did it ! You can now bind a List with a RecyclerView !

![recycler](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/recycler_small.png)

R.layout.activity_main_recyclerview_mapping
```xml
<com.github.florent37.carpaccio.Carpaccio
        android:id="@+id/carpaccio"
        app:register="
            com.github.florent37.carpacciocontrollers.CommonViewController;
            com.github.florent37.carpacciocontrollers.ImageViewController;
            com.github.florent37.carpacciocontrollers.TextViewController;
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
        android:tag="url($user.image);"
        android:layout_marginRight="20dp" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:tag="setText($user.name)" />

</LinearLayout>
```

Finally, in your activiy/fragment you just have to indicate the List to map !

```java
setContentView(R.layout.activity_main_recyclerview_mapping);
Carpaccio carpaccio = (Carpaccio)findViewById("R.id.carpaccio");
carpaccio.mapList("user", this.users);
```

[![Video](http://share.gifyoutube.com/yAp6Lw.gif)](https://youtu.be/alE3Pewmulo)

------------

#Customize

You can add you own functions to Carpaccio, simply create a custom ViewControllers,
for example **MyViewController**

```java
public class MyViewController{

    public void myFunction(View view, String argument1){
        //your usage
    }

}
```

Then you can use it into your layout

```xml
<com.github.florent37.carpaccio.Carpaccio
        app:register="
            com.mypackage.MyViewController
        ">

        <View
            android:tag="
               myFunction(theValueOfMyArgument)
            "/>

</com.github.florent37.carpaccio.Carpaccio>
```

------------

#ViewControllers

Carpaccio provide some awesome ViewControllers, you can import them directly into your project

Add into your **build.gradle**

```groovy
compile ('com.github.florent37:carpacciocontrollers:1.0.0'){
    transitive=true
}
```

------------

##TextViewController

TextViewController can set a custom font (from assets/fonts/) to a TextView

**WORKS WITH ANDROID STUDIO PREVIEW !!!**

![font](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/custom_font_small.png)


Usage : **font(fontName)**

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

And provides a data binding setText

Usage : **setText($variable)** or **setText($variable.function())**

```xml
<TextView
     android:tag="
         setText($user)
     "/>
```

Or directly on the android:text

Usage : **android:text="$variable"**

```xml
<TextView
     android:text="$user"/>
```

------------

##ImageViewController

###Url

ImageViewController can directly set an image source from an url

Usage : **url(imageUrl)**

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

![url](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/url_small.png)

Usage : **kenburns()**

```xml
<ImageView
      android:tag="
           kenburns();
           url(http://i.imgur.com/DvpvklR.png);
      " />
```

[![Video](http://share.gifyoutube.com/vpMYjp.gif)](https://youtu.be/4b84gswKGkA)

###Blur
You can blur an ImageView

Usage :
* **willBlur()** if use url(www...)
* **blur()** else

```xml
<ImageView
      android:tag="
           willBlur();
           url(http://i.imgur.com/DvpvklR.png);
      " />

<ImageView
      android:src="@drawable/my_image"
      android:tag="
           blur()
      " />
```

![blur](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/blur_small.png)

###GreyScale

Usage :
* **willGreyScale()** if use url(www...)
* **greyScale()** else

```xml
<ImageView
      android:tag="
           willGreyScale();
           url(http://i.imgur.com/DvpvklR.png);
      " />

<ImageView
      android:src="@drawable/my_image"
      android:tag="
           greyScale()
      " />
```

![greyscale](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/greyscale_small.png)

###AnimateMaterial

Display your image with a material animation
Usage : **animateMaterial(duration)**

```xml
<ImageView
      android:tag="
           animateMaterial(6000);
           url(http://i.imgur.com/DvpvklR.png);
      " />
```

[![Video](http://share.gifyoutube.com/mGz9OZ.gif)](https://youtu.be/A0eyvpNh5wM)

------------

##ParallaxViewController

ParallaxViewController can add a parallax effect on ScrollView childs

Usage :

* ScrollView : **registerParallax()**
* ScrollView childs : **parallaxY(float)**

```xml
<com.github.florent37.carpaccio.Carpaccio
        app:register="
            com.github.florent37.carpacciocontrollers.ParallaxViewController;
        ">

        <ScrollView
                    android:tag="registerParallax()">

              <View
                    android:tag="
                        parallaxY(0.5);
                    " />

              <View
                    android:tag="
                        parallaxY(1.5);
                    " />

        </ScrollView

</com.github.florent37.carpaccio.Carpaccio>
```

[![Video](http://share.gifyoutube.com/mLOpk7.gif)](https://youtu.be/DB_aHUGNwLQ)

------------

##CommonViewController

Bind a RecyclerView (see DataBinding)

Usage :

* **adapter(listMappedName,cellLayoutName)**

```xml
<android.support.v7.widget.RecyclerView
     android:layout_width="match_parent"
     android:layout_height="match_parent"

     android:tag="
         adapter(user,R.layout.cell_user)
     "
     />
```

Open an activity onClick


* **clickStartActivity(activityName) **

```xml
<Button
      android:tag="clickStartActivity(.MainActivitySample)"
      />

<Button
      android:tag="clickStartActivity(com.github.florent37.carpaccio.sample.MainActivitySample)"
      />

<Button
      android:tag="clickStartActivity($activity1)"
      />
```

Replace by another View class

```xml
<ScrollView
      android:layout_width="match_parent"
      android:layout_height="match_parent"

      android:tag="
          replace(com.github.ksoichiro.android.observablescrollview.ObservableScrollView);
      "
      />
```


* **margin(top,right,bottom,left)** & **padding(top,right,bottom,left)**

```xml
<View
      android:layout_width="match_parent"
      android:layout_height="match_parent"

      android:tag="
          margin(0,10,0,0);
          padding(5,0,5,0);
      "
      />
```

------------

##AnimationViewController

Easings :
* easeIn : accelerate
* easeOut : descelerate
* easeInOut : accelerate then descelerate

Functions :
* **animateAphaIn(duration,easing)**
* **animateScaleUp(duration,easing)**
* **animateEnterY(translationY,duration,easing)**
* **animateEnterX(translationX,duration,easing)**
* **animateEnter(translationX,translationY,duration,easing)**



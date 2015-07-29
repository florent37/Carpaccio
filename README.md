# Carpaccio

Make your view smarter !

Your view evolved and now can call functions

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
             android:tag="
                 url(http://i.imgur.com/DvpvklR.png)
             " />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Hi, how are you ?"
            android:textSize="20sp"
            android:tag="
                font(Roboto-Light.ttf);
                setText($user.getName())
            "/>

</com.github.florent37.carpaccio.Carpaccio>
```

#Download

Add into your **build.gradle**

```groovy
compile ('com.github.florent37:carpaccio:1.0.0'){
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
carpaccio.mapObject("user1",new User("florent"));
```

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

##ListView / RecyclerView

R.layout.activity_main_recyclerview_mapping
```xml
<android.support.v7.widget.RecyclerView
            android:layout_width="match_parent"
            android:layout_height="match_parent"

            android:tag="
                adapter(user,R.layout.cell_user)
            "
            />
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

```java
setContentView(R.layout.activity_main_recyclerview_mapping);
Carpaccio carpaccio = (Carpaccio)findViewById("R.id.carpaccio");
carpaccio.mapList("user", this.users);
```

**ADD IMAGE HERE !!!!**

#Customize

Create a custom ViewControllers, for example **MyViewController**

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


#ViewControllers

Carpaccio provide some ViewControllers, you can import them directly into your project

Add into your **build.gradle**

```groovy
compile ('com.github.florent37:carpacciocontrollers:1.0.0'){
    transitive=true
}
```


##ImageViewController

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

You can blur an ImageView

Usage :
* **willBlur()** if use url(www...)
* **blur()** else

```xml
<ImageView
      android:tag="`
           willBlur();
           url(http://i.imgur.com/DvpvklR.png);
      " />

<ImageView
      android:src="@drawable/my_image"
      android:tag="`
           blur()
      " />
```

![blur](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/blur_small.png)

You can greyScale an ImageView

Usage :
* **willGreyScale()** if use url(www...)
* **greyScale()** else

```xml
<ImageView
      android:tag="`
           willGreyScale();
           url(http://i.imgur.com/DvpvklR.png);
      " />

<ImageView
      android:src="@drawable/my_image"
      android:tag="`
           greyScale()
      " />
```

![greyscale](https://raw.githubusercontent.com/florent37/Carpaccio/master/screenshot/greyscale_small.png)

And display your image with a material animation
Usage : **animateMaterial(duration)**

```xml
<ImageView
      android:tag="`
           animateMaterial(6000);
           url(http://i.imgur.com/DvpvklR.png);
      " />
```

[![Video](http://share.gifyoutube.com/mGz9OZ.gif)](https://youtu.be/A0eyvpNh5wM)

##TextViewController

TextViewController can set a custom font (from assets/fonts/) to a TextView

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
<com.github.florent37.carpaccio.Carpaccio
        app:register="
            com.github.florent37.carpacciocontrollers.TextViewController;
        ">

        <TextView
             android:tag="
                 setText($user)
             "/>
</com.github.florent37.carpaccio.Carpaccio>
```

##ParallaxViewController

ParallaxViewController can add a parallax effect on ObservableScrollView childs

Usage :

* ObservableScrollView : **registerParallax()**
* ObservableScrollView childs : **parallaxY(float)**

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

##CommonViewController

Bind a list/recyclerview (see DataBinding)

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

Usage :
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

Usage :

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

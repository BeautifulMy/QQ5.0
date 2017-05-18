package com.myname.slidingmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView image = (ImageView) findViewById(R.id.main_iv);
        SlidingMenu sliding = (SlidingMenu) findViewById(R.id.sliding);
        sliding.setOnSlidingmenuListener(new SlidingMenu.onSlisingMenuListener() {
            @Override
            public void rotate(float fraction) {
                image.setRotation(360*fraction);
            }
        });
    }
}

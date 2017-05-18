package com.myname.parallax;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParallaxListview listView = (ParallaxListview) findViewById(R.id.listview);
        View view = View.inflate(this, R.layout.header, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        listView.addHeaderView(view);
        listView.setAdapter(new ArrayAdapter<String >(this,android.R.layout.simple_list_item_1,Constant.NAMES));
        listView.setImageView(imageView);
        listView.setOverScrollMode(AbsListView.OVER_SCROLL_NEVER);
    }
}

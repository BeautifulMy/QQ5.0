package com.myname.qq50;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView menuListView = (ListView) findViewById(R.id.menu_listview);
        final ListView mainListView = (ListView) findViewById(R.id.main_listview);
        final ImageView imageview= (ImageView) findViewById(R.id.iv_head);
        MySlidingMenu slidingMenu = (MySlidingMenu) findViewById(R.id.sliding);
        mainListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,Constant.NAMES));
        menuListView.setAdapter(new ArrayAdapter<String >(this,android.R.layout.simple_list_item_1,Constant.sCheeseStrings){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.WHITE);
                return textView;
            }
        });
        slidingMenu.setonSlidingMenuListener(new MySlidingMenu.onSlidingMenuListen() {
            @Override
            public void setRotate(float fraction) {
                imageview.setRotation(360*fraction);
            }

            @Override
            public void setIsOpen(boolean isOpen) {
                if (isOpen) {
                    Toast.makeText(MainActivity.this,"开",Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(MainActivity.this,"关",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

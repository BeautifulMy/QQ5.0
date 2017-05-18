package com.myname.quickindex;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {
    List<Friend> friends = new ArrayList<>();
    private ListView listview;
    private QuickIndexBar quickIndexBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);
        quickIndexBar = (QuickIndexBar) findViewById(R.id.quickindexbar);
        textView = (TextView) findViewById(R.id.tv_text);
        prepareData();
        Collections.sort(friends);
        listview.setAdapter(new myAdapter());
        quickIndexBar.setOnPressLetterListener(new QuickIndexBar.OnPressLetterListener() {
            @Override
            public void getLetter(String letter) {
                for (int i = 0; i < friends.size(); i++) {
                    String substring = friends.get(i).pinYin.substring(0, 1);
                    if (substring.equals(letter)) {
                        listview.setSelection(i);
                    break;
                    }
                    showTextView(letter);
                }
            }

            @Override
            public void release() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView.setVisibility(View.GONE);
                    }
                },500);

            }
        });


    }

    private void showTextView(String letter) {
        textView.setText(letter);
        textView.setVisibility(View.VISIBLE);
    }


    class myAdapter extends BaseAdapter {

        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return friends.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_friend, null);
                viewHolder =  new ViewHolder(view);
                view.setTag(viewHolder);
            }else{
                viewHolder   = (ViewHolder) view.getTag();

            }
            Friend friend = friends.get(i);
            viewHolder.tvName.setText(friend.name);
            String letter = friend.pinYin.substring(0, 1);
            if (i>0){
                String preletter = friends.get(i - 1).pinYin.substring(0, 1);
                if(letter.equals(preletter)){
                    viewHolder.tvLetter.setVisibility(View.GONE);
                }else {
                    viewHolder.tvLetter.setVisibility(View.VISIBLE);
                    viewHolder.tvLetter.setText(letter);
                }

            }else {
                viewHolder.tvLetter.setVisibility(View.VISIBLE);
                viewHolder.tvLetter.setText(letter);
            }

            return view;
        }


    }
    static class ViewHolder {
        @InjectView(R.id.tv_letter)
        TextView tvLetter;
        @InjectView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }


    private void prepareData() {
        friends.add(new Friend("李伟"));
        friends.add(new Friend("张三"));
        friends.add(new Friend("阿三"));
        friends.add(new Friend("阿四"));
        friends.add(new Friend("段誉"));
        friends.add(new Friend("段正淳"));
        friends.add(new Friend("张三丰"));
        friends.add(new Friend("陈坤"));
        friends.add(new Friend("林俊杰1"));
        friends.add(new Friend("陈坤2"));
        friends.add(new Friend("王二a"));
        friends.add(new Friend("林俊杰a"));
        friends.add(new Friend("张四"));
        friends.add(new Friend("林俊杰"));
        friends.add(new Friend("王二"));
        friends.add(new Friend("王二b"));
        friends.add(new Friend("赵四"));
        friends.add(new Friend("杨坤"));
        friends.add(new Friend("赵子龙"));
        friends.add(new Friend("杨坤1"));
        friends.add(new Friend("李伟1"));
        friends.add(new Friend("宋江"));
        friends.add(new Friend("宋江1"));
        friends.add(new Friend("李伟3"));
    }
}

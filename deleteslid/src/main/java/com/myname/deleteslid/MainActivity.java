package com.myname.deleteslid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {
    private SwipDeleteLayout openLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView mlv = (ListView) findViewById(R.id.listview);
        mlv.setAdapter(new myAdapter());

        //监听滑动  滑动就关闭打开的条目
        mlv.setOnScrollListener(new AbsListView.OnScrollListener() {
            // 当 listview 的状态改变的时候调用的方法
//scrollState : listview 的滚动状态
//SCROLL_STATE_IDLE :  静止状态
//SCROLL_STATE_TOUCH_SCROLL :  触摸滚动的状态
//SCROLL_STATE_FLING :  快速滚动的状态
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if(openLayout!=null){
                    openLayout.closeLayout();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    class myAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return Constant.NAMES.length;
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_listview, null);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tvName.setText(Constant.NAMES[i]);
            viewHolder.swipDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this,i+"条目",Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.swipDelete.setOnOpenListener(new SwipDeleteLayout.OnOpenListen() {

            //设置打开条目的时候,如果有条目打开了,要关闭打开的条目,
                //打开条目,保存打开的条目的对象,当再次打开另一个条目的时候调用这个防范,
                //判断是否保存,如果是保存的就关闭

                @Override
                public void isOpen(boolean isOpen, SwipDeleteLayout swipDeleteLayout) {
                    if(isOpen){
                        if(openLayout!=null&&openLayout!=swipDeleteLayout){
                            openLayout.closeLayout();
                        }
                        openLayout = swipDeleteLayout;
                    }else {
                        if(openLayout==swipDeleteLayout){
                            openLayout=null;
                        }
                    }

                }

            });
            return view;
        }


    }
    static class ViewHolder {
        @InjectView(R.id.tv_name)
        TextView tvName;
        @InjectView(R.id.tv_delete)
        TextView tvDelete;
        @InjectView(R.id.swipDelete)
        SwipDeleteLayout swipDelete;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }


}

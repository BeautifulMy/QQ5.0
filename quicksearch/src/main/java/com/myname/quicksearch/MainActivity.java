package com.myname.quicksearch;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText mEditText;
    private ImageView mImageView;
    private TextView mTextView;
    private ListView mListView;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.et);
        mImageView = (ImageView) findViewById(R.id.iv);
        mTextView = (TextView) findViewById(R.id.tv);
        mListView = (ListView) findViewById(R.id.lv);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText("");
                mImageView.setVisibility(View.GONE);
                mListView.setVisibility(View.GONE);
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(mEditText.getText())) {
                    mImageView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() == 0) {
                    mImageView.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                    InputMethodManager inputMethodManager
                            = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

                } else {
                    mImageView.setVisibility(View.VISIBLE);
                    showListView();
                }
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager
                        = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                if (TextUtils.isEmpty(mEditText.getText().toString().trim())) {
                    ToastUtils.ShowStaticToast(MainActivity.this, "请输入要搜索的内容");
                } else {
                    if (cursor != null) {
                        int columnCount = cursor.getColumnCount();
                        if (columnCount == 0) {
                            Toast.makeText(MainActivity.this,"没有要搜索的内容",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });


    }

    private void showListView() {
        mListView.setVisibility(View.VISIBLE);
        String str = mEditText.getText().toString().trim();
        OpenHelper openHelper = new OpenHelper(getApplicationContext());
        SQLiteDatabase db = openHelper.getReadableDatabase();

        cursor = db.rawQuery("select * from lol where name like '%" + str + "%'", null);
        MyListViewAdapter myListViewAdapter = new MyListViewAdapter(MainActivity.this, cursor);
        mListView.setAdapter(myListViewAdapter);

    }

    class MyListViewAdapter extends CursorAdapter {
        public MyListViewAdapter(Context context, Cursor c) {
            super(context, c);


        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            ViewHolder viewHolder = new ViewHolder();
            View view = LayoutInflater.from(context).inflate(R.layout.item, null);
            viewHolder.textView = (TextView) view.findViewById(R.id.itemtv);
            view.setTag(viewHolder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, final Cursor cursor) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            String name = cursor.getString(cursor.getColumnIndex("name"));
            viewHolder.textView.setText(name);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    cursor.moveToPosition(i);
                    String name1 = cursor.getString(cursor.getColumnIndex("name"));
                    ToastUtils.ShowStaticToast(MainActivity.this, name1);
                }
            });

        }

    }

    static class ViewHolder {
        TextView textView;
    }

}

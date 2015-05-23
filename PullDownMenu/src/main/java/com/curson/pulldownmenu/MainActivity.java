package com.curson.pulldownmenu;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private List<String> mStringList = new ArrayList<>();
    private ListView mListView;
    private PopupWindow mPopupWindow;
    private EditText mEditText;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mEditText = (EditText) findViewById(R.id.editText);
        mImageView = (ImageView) findViewById(R.id.down_arrow);

        for (int i = 1; i <= 100; i++) {
            mStringList.add("curson" + i);
        }

        initListView();

        //向下按钮
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow = new PopupWindow(MainActivity.this);
                mPopupWindow.setWidth(mEditText.getWidth());
                mPopupWindow.setHeight(500);
                mPopupWindow.setContentView(mListView);//给PopupWindow设置数据
                mPopupWindow.setOutsideTouchable(true);//点击PopupWindow以外的区域自动关闭
                mPopupWindow.showAsDropDown(mEditText, 0, 0);//设置PopupWindow的弹出位置
            }
        });
    }

    private void initListView() {
        mListView = new ListView(this);
        mListView.setBackgroundResource(R.mipmap.listview_background);
        mListView.setVerticalScrollBarEnabled(false);//关闭滑动条
        mListView.setDivider(null);//分割线
        mListView.setAdapter(new MyListAdapter());
    }

    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mStringList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = android.view.View.inflate(getApplicationContext(), R.layout.list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_msg = (TextView) convertView.findViewById(R.id.tv_list_text);
                viewHolder.img_delete = (ImageView) convertView.findViewById(R.id.delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_msg.setText(mStringList.get(position));
            //删除点击事件
            viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStringList.remove(position);
                    //删除之后刷新界面
                    MyListAdapter.this.notifyDataSetChanged();
                }
            });


            convertView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //把点击的内容设置到EditText上
                            mEditText.setText(mStringList.get(position));
                            if (mPopupWindow != null) {
                                mPopupWindow.dismiss();
                            }
                        }
                    }
            );
            return convertView;
        }
    }

    private class ViewHolder {
        TextView tv_msg;
        ImageView img_delete;
    }


}

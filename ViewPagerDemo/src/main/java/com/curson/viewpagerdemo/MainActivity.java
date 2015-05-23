package com.curson.viewpagerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * ViewPager无限滑动，自动滑动
 */
public class MainActivity extends ActionBarActivity {

    /**
     * 判断是否自动滚动
     */
    private boolean isRunning = false;

    //描述
    private final String[] imageDescriptions = {
            "巩俐不低俗，我就不能低俗",
            "扑树又回来啦！再唱经典老歌引万人大合唱",
            "揭秘北京电影如何升级",
            "乐视网TV版大派送",
            "热血屌丝的反杀"
    };

    //图片
    private final int[] imageIds = {
            R.mipmap.a,
            R.mipmap.b,
            R.mipmap.c,
            R.mipmap.d,
            R.mipmap.e,
    };

    private List<ImageView> mImageViewList;
    private LinearLayout pointGroup;
    private ViewPager mViewPager;
    private TextView imageDesc;

    //上一个页面的位置
    private int lastPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        pointGroup = (LinearLayout) findViewById(R.id.point_group);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        imageDesc = (TextView) findViewById(R.id.image_desc);
        mImageViewList = new ArrayList<>();

        for (int i = 0; i < imageIds.length; i++) {
            //初始化图片资源
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[i]);
            mImageViewList.add(imageView);

            //初始化小点
            ImageView pointImage = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.setMargins(20, 5, 20, 5);
            pointImage.setLayoutParams(layoutParams);
            pointImage.setBackgroundResource(R.drawable.point_bg);

            if (i == 0)
                pointImage.setEnabled(true);
            else
                pointImage.setEnabled(false);

            pointGroup.addView(pointImage);
        }

        mViewPager.setAdapter(new MyPagerAdapter(mImageViewList));

        //实现左右循环.  最大值 / 2  从中间开始左右滑
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % mImageViewList.size()));

        //ViwePager监听
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * 页面正在滑动的时候回调
             * @param position
             * @param positionOffset
             * @param positionOffsetPixels
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * 页面切换后调用
             * @param position 新的页面位置
             */
            @Override
            public void onPageSelected(int position) {
                position = position % mImageViewList.size();
                //设置文字描述
                imageDesc.setText(imageDescriptions[position]);
                //当前指示点设置为true
                pointGroup.getChildAt(position).setEnabled(true);
                //最后指示点设置为false
                pointGroup.getChildAt(lastPosition).setEnabled(false);
                //改变指示点的状态
                lastPosition = position;
            }

            /**
             * 页面状态发生变化的是调用
             * @param state
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        isRunning = true;
        //发送消息到Handler
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //让ViewPager滑动到下一页
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            if (isRunning)
                mHandler.sendEmptyMessageDelayed(0, 2000);
        }
    };

    /**
     * 当程序退出的时候,ViewPager停止滑动
     */
    @Override
    protected void onDestroy() {
        isRunning = false;
        super.onDestroy();
    }
}

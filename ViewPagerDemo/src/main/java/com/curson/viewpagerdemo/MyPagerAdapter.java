package com.curson.viewpagerdemo;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {

    private List<ImageView> mImageViewList;

    public MyPagerAdapter(List<ImageView> imageViewList) {
        mImageViewList = imageViewList;
    }

    /**
     * 获得一共有多少
     *
     * @return
     */
    @Override
    public int getCount() {
        //21亿最大值
        return Integer.MAX_VALUE;
    }

    /**
     * 销毁对应位置上的object
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mImageViewList.get(position % mImageViewList.size()));
    }

    /**
     * 获得响应位置上的view
     *
     * @param container view的容器，其实就是viewpager自身
     * @param position  相应的位置
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mImageViewList.get(position % mImageViewList.size()));
        return mImageViewList.get(position % mImageViewList.size());
    }

    /**
     * 判断view和object的关系
     *
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

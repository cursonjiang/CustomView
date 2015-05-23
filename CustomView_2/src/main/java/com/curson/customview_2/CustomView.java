package com.curson.customview_2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View {

    private int x, y;//位图绘制时左上角的起点坐标

    private Paint mPaint;//画笔
    private Bitmap mBitmap;//位图
    private Context mContext;//上下文


    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        //初始化画笔
        initPaint();

        //初始化资源
        initRes(context);
    }

    /**
     * 初始化资源
     *
     * @param context
     */
    private void initRes(Context context) {
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.psb);

        /**
         * 计算位图绘制时左上角的坐标,使其位于屏幕中心
         * 屏幕坐标 x轴 向左偏移位图一半的宽度
         * 屏幕坐标 y轴 向上偏移位图一半的高度
         */
        x = ScreenUtil.getScreenWidth(mContext) / 2 - mBitmap.getWidth() / 2;
        y = ScreenUtil.getScreenHeight(mContext) / 2 - mBitmap.getHeight() / 2;
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {

        //实例化画笔并打开抗锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制位图
        canvas.drawBitmap(mBitmap, x, y, mPaint);
    }
}

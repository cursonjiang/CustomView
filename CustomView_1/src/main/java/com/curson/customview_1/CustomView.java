package com.curson.customview_1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View implements Runnable {

    private Paint mPaint;

    private Context mContext;

    private int radiu;//圆环半径

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {

        //实例化画笔并打开抗锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        /**
         *画笔的样式分为3种:
         * 1.Paint.Style.STROKE : 描边
         * 2.Paint.Style.FILL_AND_STROKE : 描边并填充
         * 3.Paint.Style.FILL : 填充
         */
        mPaint.setStyle(Paint.Style.STROKE);

        //设置画笔颜色 浅灰色
        mPaint.setColor(Color.LTGRAY);

        /**
         * 设置描边的粗细,单位:像素px
         * 注意:当setStrokeWidth(0)的时候,描边的宽度并不为0,而是占一个像素
         */
        mPaint.setStrokeWidth(10);
    }

    /**
     * 画布
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制圆环
        canvas.drawCircle(
                ScreenUtil.getScreenWidth(mContext) / 2,//获取屏幕宽度
                ScreenUtil.getScreenHeight(mContext) / 2,//获取屏幕高度
                radiu,//圆的半径
                mPaint//画笔
        );
    }

    @Override
    public void run() {
        //确保线程不断执行不断刷新界面
        while (true) {
            try {
                //如果半径小于200则自加
                if (radiu <= 200) {
                    radiu += 10;

                    //主线程更新UI
                    postInvalidate();

                    //否则重置半径值
                } else {
                    radiu = 0;
                }
                //每执行一次暂停40毫秒
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

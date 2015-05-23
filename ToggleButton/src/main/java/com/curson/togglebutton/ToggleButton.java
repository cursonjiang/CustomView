package com.curson.togglebutton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * View对象显示在屏幕上,有几个重要步骤:
 * 1.构造方法,创建对象
 * 2.测量view的大小  onMeasure(int,int)
 * 3.确定view的位置,view自身有一些建议权,决定权在父view手中  onLayout();
 * 4.绘制view的内容  onDraw(Canvas)
 */
public class ToggleButton extends View implements View.OnClickListener {

    /**
     * 滑动图片的资源ID
     */
    private int slideButtonId;

    /**
     * 背景图的资源ID
     */
    private int backgroundId;

    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 作为背景图片
     */
    private Bitmap slideBackgroundBitmap;

    /**
     * 可以滑动的图片
     */
    private Bitmap slideButton;

    /**
     * 滑动按钮的左边界 默认0
     */
    private float slidButton_left;

    /**
     * 在布局文件中声明的view,创建时由系统调用
     *
     * @param context
     * @param attrs
     */
    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获得自定义属性
        @SuppressLint("Recycle")
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToggleButton);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            /**
             * 获取某个属性的ID值
             */
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.ToggleButton_curr_state:
                    currentState = typedArray.getBoolean(index, false);

                case R.styleable.ToggleButton_my_background:
                    backgroundId = typedArray.getResourceId(index, -1);
                    if (backgroundId == -1) {
                        throw new RuntimeException("请设置背景图片");
                    }
                    slideBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundId);
                    break;

                case R.styleable.ToggleButton_my_slideButton:
                    slideButtonId = typedArray.getResourceId(index, -1);
                    if (slideButtonId == -1) {
                        throw new RuntimeException("请设置按钮图片");
                    }
                    slideButton = BitmapFactory.decodeResource(getResources(), slideButtonId);
                    break;
            }
        }
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {

        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//打开抗锯齿

        //添加onClick事件监听
        setOnClickListener(this);

        flushState();
    }

    /**
     * 测量view尺寸的时候调用
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 设置当前view的大小
         *  measureWidth : view的宽度
         *  measureHeight: view的高度
         */
        setMeasuredDimension(slideBackgroundBitmap.getWidth(), slideBackgroundBitmap.getHeight());
    }

    /**
     * 确定位置的时候调用
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 绘制当前view的内容
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        canvas.drawBitmap(slideBackgroundBitmap, 0, 0, mPaint);
        //绘制可滑动的按钮
        canvas.drawBitmap(slideButton, slidButton_left, 0, mPaint);
    }

    /**
     * 当前开关的状态 true为开
     */
    private boolean currentState = false;

    /**
     * 判断是否发生拖动
     * 如果拖动了,就不再响应onClick事件
     */
    private boolean isDrag = false;

    /**
     * onClick事件在view.onTouchEvent中被解析.
     * 只要有down事件,up事件,系统就认为发生了onClick事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        //如果没有拖动,才执行改变的动作
        if (!isDrag) {
            currentState = !currentState;
            flushState();
        }
    }

    /**
     * 刷新当前状态
     */
    private void flushState() {
        //如果开关为true
        if (currentState) {
            slidButton_left = slideBackgroundBitmap.getWidth() - slideButton.getWidth();
        } else {
            slidButton_left = 0;
        }
        //刷新当前view,会导致onDraw方法的执行,必须在主线程调用
        flushView();
    }

    /**
     * down事件时x值
     */
    private int firstX;

    /**
     * touch事件的上一个X值
     */
    private int lastX;

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录按下的位置
                firstX = lastX = (int) event.getX();
                isDrag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                //判断是否发生拖动
                if (Math.abs(event.getX() - firstX) > 5) {
                    isDrag = true;
                }

                //记录手指在屏幕上移动的距离
                int dis = (int) (event.getX() - lastX);

                //将本次的位置赋值给lastX
                lastX = (int) event.getX();

                //根据手指移动的距离,改变slideButton_left的值
                slidButton_left = slidButton_left + dis;
                break;
            case MotionEvent.ACTION_UP:
                //在发生拖动的情况下,根据最后的位置判断当前开关的状态
                if (isDrag) {
                    int maxLeft = slideBackgroundBitmap.getWidth() - slideButton.getWidth();//左边界最大值
                    /**
                     * 根据slidButton_left判断,当前应是什么状态
                     */
                    if (slidButton_left > maxLeft / 2) {
                        //打开
                        currentState = true;
                    } else {
                        currentState = false;
                    }
                    flushState();
                }
                break;
        }
        flushView();
        return true;
    }

    /**
     * 刷新当前view
     */
    private void flushView() {
        //对SlideButton_left的值进行判断,确保其在合理的位置
        //   0 <= slideButton_left <= maxLeft
        int maxLeft = slideBackgroundBitmap.getWidth() - slideButton.getWidth();//左边界最大值

        //确保slideButton_left >= 0
        slidButton_left = (slidButton_left > 0) ? slidButton_left : 0;

        //确保slideButtoon_left <= maxLeft
        slidButton_left = (slidButton_left < maxLeft) ? slidButton_left : maxLeft;

        //刷新当前view,执行onDraw()
        invalidate();
    }
}

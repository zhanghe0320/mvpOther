package com.mvp.activity.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


/**
 *动画效果view
 */
public class AnimatorProgressView extends View {

    private Paint mPaintBehind;//绘制背景
    private Paint mPaintProgress;//绘制
    private int mWidth;//宽度
    private int mHeight;//高度
    private int mRadius;//半径

    public AnimatorProgressView(Context context) {
        super(context);
        initView(context);

    }


    public AnimatorProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public AnimatorProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AnimatorProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化绘制view
     * @param context
     */
    private void initView(Context context) {

    }


    /**
     * 绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaintBehind = new Paint();
        mPaintProgress = new Paint();
        final Rect rect = new Rect();
        rect.set(0, 0, mWidth, mHeight);
        // LinearGradient
        // 第一个参数第二个参数为 起始位置x,y
        // 三四参数为终点位置x,y。
        // 如果x不变则为y轴渐变， y不变则为x轴渐变
        // 第五个参数为颜色渐变，此处为红色渐变为绿色
        // 第七个参数为渐变次数，可repeat
        int count = 0;
        //底色
        Shader mShader2 = new LinearGradient(0, 0, mWidth, mHeight, new int[]{Color.GRAY, Color.CYAN}, null, Shader.TileMode.CLAMP);
        mPaintBehind.setShader(mShader2);
        count = canvas.save();
        Path path2 = new Path();
        path2.moveTo(mWidth-30, mHeight);
        path2.lineTo(mWidth, 0);
        path2.lineTo(0, 0);
        path2.lineTo(0, mHeight);
        path2.close();
        canvas.drawPath(path2, mPaintBehind);
        canvas.restoreToCount(count);

        //动画
        Shader mShader = new LinearGradient(0, 0, mWidth, mHeight, new int[]{Color.WHITE, Color.CYAN}, null, Shader.TileMode.CLAMP);
        mPaintProgress.setShader(mShader);
        count = canvas.save();
        Path path = new Path();
        path.moveTo(mRadius-30, mHeight);//开始
        path.lineTo(mRadius, 0);//终点
        path.lineTo(0, 0);//中调
        path.lineTo(0, mHeight);


//        path.moveTo(0, mHeight);//开始
//        path.lineTo(0, mWidth);//终点
//        path.lineTo(mRadius, mHeight);//中调
//        path.lineTo(0, mHeight);
//        path.moveTo(mWidth, 0);//开始
//        path.lineTo(0, mHeight);//终点
//        path.lineTo(0, mHeight);//中调
//        path.lineTo(mRadius - 10, 0);
        path.close();
        canvas.drawPath(path, mPaintProgress);
        canvas.restoreToCount(count);

        invalidate();
//        mWidth = mHalfWidth - mRadius;
//        mWidth = mHalfWidth + mRadius;
//        mHeight = mHalfHeight - mRadius;
//        mHeight = mHalfHeight + mRadius;
    }

    /**
     * 测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = widthSpecSize;
        mHeight = heightSpecSize;
        setMeasuredDimension(mWidth, mHeight);
        if (mPaintBehind == null) {
            //mPaintBehind = new RectF(0, 0, mWidth, mHeight);
        } else {
           // mPaintBehind.set(0, 0, mWidth, mHeight);
        }
    }
}

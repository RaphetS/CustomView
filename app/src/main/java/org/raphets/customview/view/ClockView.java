package org.raphets.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import org.raphets.customview.R;
import org.raphets.customview.utils.DanWeiUtil;

import java.util.Calendar;

/**
 * 自定义View时钟
 */

public class ClockView extends View {

    private float mTextSize;//刻度值字体大小
    private int mHourNumColor;//刻度值字体颜色
    private float mHourPointerWidth;//时针宽度
    private int mHourPointerColor;//时针颜色
    private float mMinutePointerWidth;//分针宽度
    private int mMinutePointerColor;//分针颜色
    private float mSecondPointerWidth;//秒针宽度
    private int mSecondPointerColor;//秒针颜色
    private int mHourScaleColor;//时刻刻度颜色
    private int mMinuteScaleColor;//分刻刻度颜色
    private Paint mPaint;
    private float mRadius;
    private int mBgColor;

    public ClockView(Context context) {
        this(context,null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        obtainStyledAttrs(attrs);

        init();
    }


    /**
     * 获取自定义属性
     */
    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray ta=getContext().obtainStyledAttributes(attrs, R.styleable.ClockView);
        mTextSize=ta.getDimension(R.styleable.ClockView_hour_num_textsize, DanWeiUtil.sp2px(getContext(),15));
        mHourNumColor= ta.getColor(R.styleable.ClockView_hour_num_color, Color.parseColor("#000000"));
        mHourPointerWidth=ta.getDimension(R.styleable.ClockView_hour_pointer_width,DanWeiUtil.dp2px(getContext(),6));
        mHourPointerColor=ta.getColor(R.styleable.ClockView_hour_pointer_color,Color.parseColor("#000000"));
        mMinutePointerWidth=ta.getDimension(R.styleable.ClockView_minute_pointer_width,DanWeiUtil.dp2px(getContext(),4));
        mMinutePointerColor=ta.getColor(R.styleable.ClockView_minute_pointer_color,Color.parseColor("#000000"));
        mSecondPointerWidth=ta.getDimension(R.styleable.ClockView_second_pointer_width,DanWeiUtil.dp2px(getContext(),2));
        mSecondPointerColor=ta.getColor(R.styleable.ClockView_second_pointer_color,Color.parseColor("#000000"));
        mHourScaleColor=ta.getColor(R.styleable.ClockView_hour_scale_color,Color.parseColor("#000000"));
        mMinuteScaleColor=ta.getColor(R.styleable.ClockView_minute_scale_color,Color.parseColor("#000000"));
        mBgColor=ta.getColor(R.styleable.ClockView_background_color,Color.parseColor("#ffffff"));

        if (ta!=null){
            ta.recycle();
        }
    }

    private void init() {
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int width = 0;
        int height =0;

        if (widthMode==MeasureSpec.UNSPECIFIED&&heightMode==MeasureSpec.UNSPECIFIED){
            width=600;
            height=600;
        }else {

            if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
                width = widthSize;
            }

            if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
                height = heightSize;
            }
        }
        width=Math.min(width,height);

        setMeasuredDimension(width,width);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
       mRadius =Math.min(w-getPaddingRight()-getPaddingLeft(),h-getPaddingBottom()-getPaddingTop())/2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(getWidth()/2,getHeight()/2);

        drawCircle(canvas);

        drawKeDu(canvas);

        drawPointer(canvas);

        canvas.restore();
        //刷新
        postInvalidateDelayed(1000);
    }

    /**
     * 画指针
     * @param canvas
     */
    private void drawPointer(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY); //时
        int minute = calendar.get(Calendar.MINUTE); //分
        int second = calendar.get(Calendar.SECOND); //秒
        int angleHour = (hour % 12) * 360 / 12; //时针转过的角度
        int angleMinute = minute * 360 / 60; //分针转过的角度
        int angleSecond = second * 360 / 60; //秒针转过的角度
        //绘制时针
        canvas.save();
        canvas.rotate(angleHour); //旋转到时针的角度
        RectF rectFHour = new RectF(-mHourPointerWidth / 2, -mRadius * 3 / 7, mHourPointerWidth / 2, mRadius/8);
        mPaint.setColor(mHourPointerColor); //设置指针颜色
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mHourPointerWidth); //设置边界宽度
        canvas.drawRoundRect(rectFHour, 5, 5, mPaint); //绘制时针
        canvas.restore();
        //绘制分针
        canvas.save();
        canvas.rotate(angleMinute);
        RectF rectFMinute = new RectF(-mMinutePointerWidth / 2, -mRadius * 3 / 5, mMinutePointerWidth / 2,  mRadius/8);
        mPaint.setColor(mMinutePointerColor);
        mPaint.setStrokeWidth(mMinutePointerWidth);
        canvas.drawRoundRect(rectFMinute, 5, 5, mPaint);
        canvas.restore();
        //绘制秒针
        canvas.save();
        canvas.rotate(angleSecond);
        RectF rectFSecond = new RectF(-mSecondPointerWidth / 2, -mRadius*6/7, mSecondPointerWidth / 2,  mRadius/8);
        mPaint.setColor(mSecondPointerColor);
        mPaint.setStrokeWidth(mSecondPointerWidth);
        canvas.drawRoundRect(rectFSecond, 5, 5, mPaint);
        canvas.restore();
        //绘制中心小圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mSecondPointerColor);
        canvas.drawCircle(0, 0, mSecondPointerWidth * 4, mPaint);
    }


    /**
     * 绘制表盘
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0,0,mRadius,mPaint);
    }

    /**
     * 画刻度
     * @param canvas
     */
    private void drawKeDu(Canvas canvas) {
        float lineWidth;
        for (int i=0;i<60;i++){
            if (i%5==0){
                lineWidth=mRadius/11;
                mPaint.setStrokeWidth(DanWeiUtil.dp2px(getContext(),2));
                mPaint.setColor(mHourScaleColor);
                /**
                 * 画刻度值
                 */
                mPaint.setTextSize(mTextSize);
                mPaint.setColor(mHourNumColor);
                mPaint.setStyle(Paint.Style.FILL);
                String text=((i/5)==0?12:i/5)+"";
                Rect rect=new Rect();
                mPaint.getTextBounds(text,0,text.length(),rect);
                canvas.save();
                canvas.translate(0, -mRadius + DanWeiUtil.sp2px(getContext(),5) + lineWidth + (rect.bottom - rect.top));
                canvas.rotate(-6 * i);
                canvas.drawText(text, -(rect.right - rect.left) / 2,rect.bottom, mPaint);
                canvas.restore();
            }else {
                lineWidth=mRadius/13;
                mPaint.setStrokeWidth(DanWeiUtil.dp2px(getContext(),1));
                mPaint.setColor(mMinuteScaleColor);
            }
            canvas.drawLine(0,-mRadius+DanWeiUtil.dp2px(getContext(),3),0,-mRadius+lineWidth,mPaint);
            canvas.rotate(6);
        }
    }
}

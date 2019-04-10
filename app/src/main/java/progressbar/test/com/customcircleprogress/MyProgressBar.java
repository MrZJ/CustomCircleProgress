package progressbar.test.com.customcircleprogress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by zhangjian on 2019/4/9 15:45
 */
public class MyProgressBar extends View {
    private int innerbackground = Color.BLUE;
    private int outerbackground = Color.RED;
    private float roundwidth = 10;
    private float progresstextsize = 30;
    private int progresstextcolor = Color.BLACK;
    private int mMax = 4000;
    private int mProgress = 0;
    private Paint innerPaint, outPaint, textPaint;

    public MyProgressBar(Context context) {
        super(context);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyProgressBar);
        innerbackground = array.getColor(R.styleable.MyProgressBar_innerbackground, innerbackground);
        outerbackground = array.getColor(R.styleable.MyProgressBar_outerbackground, outerbackground);
        roundwidth = dip2px((int) array.getDimension(R.styleable.MyProgressBar_roundwidth, roundwidth));
        progresstextsize = sp2px(array.getDimension(R.styleable.MyProgressBar_progresstextsize, progresstextsize));
        progresstextcolor = array.getColor(R.styleable.MyProgressBar_progresstextcolor, progresstextcolor);
        array.recycle();
        innerPaint = new Paint();
        innerPaint.setAntiAlias(true);
        innerPaint.setColor(innerbackground);
        innerPaint.setStrokeWidth(roundwidth);
        innerPaint.setStyle(Paint.Style.STROKE);

        outPaint = new Paint();
        outPaint.setAntiAlias(true);
        outPaint.setColor(outerbackground);
        outPaint.setStrokeWidth(roundwidth);
        outPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(progresstextcolor);
        textPaint.setTextSize(progresstextsize);

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = getWidth() / 2;
        canvas.drawCircle(radius, radius, radius - (roundwidth / 2), outPaint);
        @SuppressLint("DrawAllocation") RectF rectF = new RectF(roundwidth / 2, roundwidth / 2, getWidth() - (roundwidth / 2), getHeight() - (roundwidth / 2));
        //如果进度为0就不绘制
        if (mProgress == 0) {
            return;
        }
        float percent = (float) mProgress / mMax;
        canvas.drawArc(rectF, 270, 360 * percent, false, innerPaint);

        // 画进度文字
        String text = ((int) (percent * 100)) + "%";
        @SuppressLint("DrawAllocation") Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);
        float dx = getWidth() / 2 - rect.width() / 2;
//        @SuppressLint("DrawAllocation") Paint.FontMetricsInt fontMetricsInt = new Paint.FontMetricsInt();
//        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int dy = rect.height() / 2;
        float baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, dx, baseLine, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width, height), Math.min(height, width));
    }

    public synchronized void setMax(int max) {
        if (max < 0) {

        }
        this.mMax = max;
    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
        }
        this.mProgress = progress;
        // 刷新 invalidate
        invalidate();
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private float dip2px(int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

}

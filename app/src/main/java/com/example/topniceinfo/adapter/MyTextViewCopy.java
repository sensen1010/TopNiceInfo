package com.example.topniceinfo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.topniceinfo.R;

@SuppressLint("AppCompatCustomView")
public class MyTextViewCopy extends TextView {
    public final static String TAG = MyTextViewCopy.class.getSimpleName();
    private float textLength = 0f;
    private float viewWidth = 0f;
    private float step = 0f;
    private float y = 0f;
    private float height = 0f;
    private float temp_view_plus_text_length = 0.0f;
    private float temp_view_plus_text_height = 0.0f;
    private float temp_view_plus_two_text_length = 0.0f;
    private float temp_view_plus_two_text_height = 0.0f;
    public boolean isStarting = false;
    private Paint paint = null;
    private String text = "";

    public MyTextViewCopy(Context context, float width, float height, String text) {
        super(context);
        init(width,height,text);
    }

    public MyTextViewCopy(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MyTextViewCopy(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public void init(float width,float height,String text) {
        paint = getPaint();
        //text = getText().toString();
        //textLength = paint.measureText(text);
        paint.setTextSize(50);
        this.text=text;
        textLength = paint.measureText(text);
        System.out.println("view获取到的高度"+textLength);
        //paint.setColor(getTextColors().getDefaultColor());//设置颜色
        paint.setColor(getResources().getColor(R.color.loginBj));//设置颜色

        viewWidth = getWidth();
        System.out.println("view获取到的宽度"+viewWidth);
        if(viewWidth == 0)
        {
         viewWidth =width;
        }
        System.out.println("当前的宽度"+viewWidth);
        step = textLength;
        temp_view_plus_text_length = viewWidth + textLength;
        temp_view_plus_text_height=height+50;
        this.height=height;
        temp_view_plus_two_text_length = viewWidth + textLength * 2;
        temp_view_plus_two_text_height=height+50*2;
        y = getTextSize() + getPaddingTop();
        startScroll();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.step = step;
        ss.isStarting = isStarting;

        return ss;

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());

        step = ss.step;
        isStarting = ss.isStarting;

    }

    public static class SavedState extends BaseSavedState {
        public boolean isStarting = false;
        public float step = 0.0f;
        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBooleanArray(new boolean[]{isStarting});
            out.writeFloat(step);
        }


        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
        };

        private SavedState(Parcel in) {
            super(in);
            boolean[] b = null;
            in.readBooleanArray(b);
            if(b != null && b.length > 0)
                isStarting = b[0];
            step = in.readFloat();
        }
    }

    public void startScroll()
    {
        isStarting = true;
        invalidate();
    }

    public void stopScroll()
    {
        isStarting = false;
        invalidate();
    }

    /**
//     * 绘制跑动效果
//     * @param canvas
//     */
    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawText("1"+text,0,temp_view_plus_text_height-step+50, paint);
        canvas.drawText("2"+text,0,temp_view_plus_text_height-step+50*2, paint);
        canvas.drawText("3"+text,0,temp_view_plus_text_height-step+50*3, paint);
        canvas.drawText("4"+text,0,temp_view_plus_text_height-step+50*4, paint);
        if(!isStarting)
        {
            return;
        }
        //4:为快速
        //2：为中速
        //1：为慢速
        step += 2;//速度控制  可调节快慢
        if(step > (temp_view_plus_two_text_height+50*4))
            step = 50;
//        canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
//        if(!isStarting)
//        {
//            return;
//        }
//        step += 1;//速度控制  可调节快慢
//        if(step > temp_view_plus_two_text_length)
//            step = textLength;
        invalidate();
    }



}

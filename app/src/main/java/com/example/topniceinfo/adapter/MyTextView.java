package com.example.topniceinfo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.topniceinfo.R;

@SuppressLint("AppCompatCustomView")
public class MyTextView extends TextView {
    public final static String TAG = MyTextView.class.getSimpleName();
    private float textLength = 0f;
    private float step = 0f;
    private float y = 0f;
    private float height = 0f;
    //垂直定位
    private float temp_view_plus_text_length = 0.0f;
    private float temp_view_plus_two_text_length = 0.0f;
    //水平定位
    private float temp_view_plus_text_height = 0.0f;
    private float temp_view_plus_two_text_height = 0.0f;
    public boolean isStarting = false;
    private Paint paint = null;
    //字体内容
    private String text = "";
    //容器高度
    private float viewHeight=0f;
    //容器宽度
    private float viewWidth = 0f;
    //字体大小
    private double textSize=0;
    //字体颜色
    private String textColor="";
    //字体背景颜色
    private String bargColor="";
    //滚动方向
    private String direction="";
    //滚动速度
    private double speed=0;

    //参数 1.上下文 2.容器宽度 3.容器高度 4.字体大小 5.字体颜色 6.背景颜色 7.滚动方向 8.速度 9.字体内容
    public MyTextView(Context context,float width,float height,double textSize,String textColor,String bargColor,String direction,String speed,String text) {
        super(context);
        this.viewWidth=width;
        this.viewHeight=height;
        this.textSize=textSize;
        this.textColor=textColor;
        this.bargColor=bargColor;
        this.direction=direction;
        int num=Integer.parseInt(speed);
        if (num<60)this.speed=0.4;
        if (num>60&&num<110)this.speed=1.4;
        if (num>110)this.speed=2;
        this.text=text;
        init();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init() {
        paint = getPaint();
        //设置字体大小
        paint.setTextSize((float) textSize);
        //获取字体最长长度
        String[] textList=text.split("\n");
        int b1=0;
        int b2=0;
        int c=0;
        for (int a=0;textList.length>a;a++){
            b1=textList[a].length();
            if (b1>b2){
                b2=b1;
                c=a;
            }
        }
        textLength = paint.measureText(textList[c]);
        //设置字体颜色
        paint.setColor(Color.parseColor(textColor));

        temp_view_plus_text_length = viewWidth + textLength;
        temp_view_plus_text_height=viewHeight+(float)textSize;

        temp_view_plus_two_text_length = viewWidth + textLength * 2;
        temp_view_plus_two_text_height=viewHeight+(float)textSize*2;

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
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
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
        //绘制背景颜色
        if (!bargColor.equals("")){
            canvas.drawColor(Color.parseColor(bargColor));
        }
        if(!isStarting)
        {
            return;
        }
        if (direction.equals("up")){
            String[] textList=text.split("\n");
            for (int a=0;textList.length>a;a++){
                if (textLength>viewWidth){
                    canvas.drawText(textList[a],0,(float)(temp_view_plus_text_height-step+textSize*(a+1)), paint);
                }else {
                    canvas.drawText(textList[a],(viewWidth-textLength)/2,(float)(temp_view_plus_text_height-step+textSize*(a+1)), paint);
                }
            }
            step += speed;//速度控制  可调节快慢
            if(step > (temp_view_plus_two_text_height+textSize*textList.length))
                step =(float)0;
        }else if (direction.equals("left")){
            String[] textList=text.split("\n");
            for (int a=0;textList.length>a;a++){
              canvas.drawText(textList[a], temp_view_plus_text_length - step,(float) textSize*(a+1), paint);
            }
            //0.4慢速
            //1.3中速
            //2快速
            step +=speed;//速度控制  可调节快慢
            if(step > temp_view_plus_two_text_length)
                step = textLength;
        }
        invalidate();
    }



}

package com.example.topniceinfo;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.example.topniceinfo.adapter.GlideImageLoader;

import com.example.topniceinfo.adapter.MyTextView;
import com.example.topniceinfo.okhttp.OkhttpApi;
import com.example.topniceinfo.server.MyService;
import com.example.topniceinfo.utils.DateUtil;
import com.example.topniceinfo.utils.LinkSharedPreUtil;
import com.example.topniceinfo.utils.LoginSharedPreUtil;
import com.example.topniceinfo.utils.MyApplication;
import com.example.topniceinfo.utils.ProgramSharedPreUtil;
import com.example.topniceinfo.utils.Util;
import com.example.topniceinfo.websocket.WebSocketUtil;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class ProgramHomeActivity extends AppCompatActivity {

    public static ProgramHomeActivity programHomeActivity = null;

    //布局倍数 横向
    double numW=1;
    //布局倍数  纵向
    double numH=1;
    private ArrayList<String> images;
    OrientationUtils orientationUtils;
    //视频播放器
    StandardGSYVideoPlayer videoPlayer;
    //跳转类型
    private final String ACTION_BOOT="BOOT_COMPLETED";
    private final String ACTION_SOCKET="SOCKET_CLIENT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置bar为无、全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_program_home);

        //关闭服务
        Intent server = new Intent(MyApplication.context, MyService.class);
        stopService(server);

        //赋值
        programHomeActivity=this;

        Intent intent=getIntent();
        String startType=intent.getStringExtra("startType");
        //系统开机启动
        if (startType.equals(ACTION_BOOT)){
            Message message=new Message();
            message.what=1;
            handler.sendMessageDelayed(message,200);
            Message message2=new Message();
            message2.what=3;
            handler.sendMessageDelayed(message2,200);
        }
        //系统监听启动
        else if (startType.equals(ACTION_SOCKET)){
            Message message=new Message();
            message.what=1;
            handler.sendMessageDelayed(message,200);
        }
    }
    //该页面只存在一次，新跳转后刷新数据
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String startType=intent.getStringExtra("startType");
        //系统开机启动
        if (startType.equals(ACTION_BOOT)){

        }
        //系统监听启动
        else if (startType.equals(ACTION_SOCKET)){
            Message message=new Message();
            message.what=1;
            handler.sendMessageDelayed(message,200);
        }
    }

    void showView(String date){
        //数据转换
        JSONArray jsonArray1 = JSONArray.parseArray(date);
        //根据z排序 z越大,在后
        JSONArray jsonArray=Util.sortProxyAndCdn(jsonArray1);
        //获取布局
        ConstraintLayout constraintLayout=findViewById(R.id.program_home_constraint);
        //先清空布局
        constraintLayout.removeAllViews();
        //获取屏幕尺寸对象
        DisplayMetrics dm = MyApplication.context.getResources().getDisplayMetrics();
        Util.showToast(MyApplication.context,"宽度:"+dm.widthPixels+"高度："+dm.heightPixels);
        //解析布局
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat=new SimpleDateFormat("mmssSSS");
        for (int i = 0; i < jsonArray.size(); i++) {
            int a=Integer.parseInt(simpleDateFormat.format(new Date()));
            JSONObject infoData = jsonArray.getJSONObject(i);
            //解析背景
             if (infoData.getString("type").equals("bj")){
                 //获取总布局的宽度，与本机宽度。计算倍数
                 double w=Double.parseDouble(infoData.getString("w"));
                 double h=Double.parseDouble(infoData.getString("h"));
                 numW=dm.widthPixels/w;
                 numH=dm.heightPixels/h;
                 //若背景图为空，则设置背景颜色
                 String bargColor=infoData.getString("bargColor");
                 if(bargColor!=null&&!bargColor.equals("")){
                 constraintLayout.setBackgroundColor((Color.parseColor(bargColor)));
                 }else {
                     //获取背景图片地址
                     String bargImg=infoData.getString("bargImg");
                     ConstraintSet constraintSet=new ConstraintSet();
                     Banner mBanner = new Banner(ProgramHomeActivity.this);
                     mBanner.setId(a);
                     constraintLayout.addView(mBanner);
                     images=new ArrayList<>();
                     images.add(LinkSharedPreUtil.getSharePre().imgUrlServer()+bargImg);
                     initBanner(mBanner,0);
                     settingLayoutWH(1,constraintLayout,constraintSet,infoData,mBanner.getId());
                 }
             }
             //解析控件
            if (infoData.getString("x")!=null){
                ConstraintSet constraintSet=new ConstraintSet();
                //解析图片
                if (infoData.getString("type").equals("img")){
                     Banner mBanner = new Banner(ProgramHomeActivity.this);
                     mBanner.setId(a);
                     constraintLayout.addView(mBanner);
                     String fileListData=infoData.getString("fileList");
                     JSONArray fileList = JSONArray.parseArray(fileListData);
                     images=new ArrayList<>();
                     for (int f = 0; f < fileList.size(); f++) {
                         JSONObject file = fileList.getJSONObject(f);
                         images.add(LinkSharedPreUtil.getSharePre().imgUrlServer() +file.getString("src"));
                     }
                     if (fileList.size()<2){
                         initBanner(mBanner,0);
                     }else {
                         String speedTime=infoData.getString("speedTime");
                         int speed=Integer.parseInt(speedTime)*1000;
                         initBanner(mBanner,speed);
                     }
                     settingLayoutWH(0,constraintLayout,constraintSet,infoData,mBanner.getId());
                }
                //时间
                else if (infoData.getString("type").equals("time")){
                    TextView timeView=new TextView(ProgramHomeActivity.this);
                    //设置字体大小
                    //字体大小
                    String textSize=infoData.getString("size").replace("px","");
                    double size;
                    if (numW>numH){
                    size=Integer.parseInt(textSize)*numH;
                    }else {
                    size=Integer.parseInt(textSize)*numW;
                    }

                    timeView.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) size);
                    //设置字体颜色
                    String textColor=infoData.getString("textColor");
                    if (textColor==null||textColor.equals("")){
                        textColor="#ffffff";
                    }
                    timeView.setTextColor(Color.parseColor(textColor));
                    //设置背景颜色
                    String bargColor=infoData.getString("bargColor");
                    if (bargColor!=null&&!bargColor.equals("")){
                        timeView.setBackgroundColor(Color.parseColor(bargColor));
                    }
                    timeView.setGravity(Gravity.CENTER);
                    timeView.setId(a);
                    timeView.setText("");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true){
                                Message message=new Message();
                                message.what=2;
                                message.arg1=timeView.getId();
                                handler.sendMessage(message);
                                try { Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }).start();
                    constraintLayout.addView(timeView);
                    settingLayoutWH(0,constraintLayout,constraintSet,infoData,timeView.getId());

                }
                //文字
                else if (infoData.getString("type").equals("text")){
                    //获取是否滚动 marquee:true 滚动  false 不滚动
                    boolean marquee=infoData.getBoolean("marquee");
                    String marqueeType=infoData.getString("marqueeType");
                    //获取字体内容
                    String context=infoData.getString("context");
                    //字体颜色
                    String textColor=infoData.getString("textColor");
                    if (textColor==null||textColor.equals("")){
                        textColor="#ffffff";
                    }
                    //字体背景颜色
                    String bargColor=infoData.getString("bargColor");
                    //字体大小
                    String textSize=infoData.getString("size").replace("px","");
                    double size;
                    if (numW>numH){
                        size=Integer.parseInt(textSize)*numH;
                    }else {
                        size=Integer.parseInt(textSize)*numW;
                    }
                    //滚动
                    if (marquee&&!marqueeType.equals("1")){
                        //滚动方向
                        String marqueeDirection=infoData.getString("marqueeDirection");
                        //滚动速度
                        String speed=infoData.getString("speed");
                        //容器高度、宽度
                        int h=(int)Math.round(Integer.parseInt(infoData.getString("h"))*numW);
                        int w=(int)Math.round(Integer.parseInt(infoData.getString("w"))*numW);
                        //参数 1.上下文 2.容器宽度 3.容器高度 4.字体大小 5.字体颜色 6.背景颜色 7.滚动方向 8.速度 9.字体内容
                        MyTextView myTextView=new MyTextView(ProgramHomeActivity.this,w,h,size,textColor,bargColor,marqueeDirection,speed,context);
                        myTextView.setId(a);
                        constraintLayout.addView(myTextView);
                        settingLayoutWH(0,constraintLayout,constraintSet,infoData,myTextView.getId());
                    }else {
                    //不滚动
                        TextView textView=new TextView(ProgramHomeActivity.this);
                        //设置字体大小
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) size);
                        textView.setTextColor(Color.parseColor(textColor));
                        //设置背景颜色
                        if (bargColor!=null&&!bargColor.equals("")){
                            textView.setBackgroundColor(Color.parseColor(bargColor));
                        }
                        //设置字体内容
                        textView.setText(context);
                        textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        textView.setId(a);
                        constraintLayout.addView(textView);
                        settingLayoutWH(0,constraintLayout,constraintSet,infoData,textView.getId());
                    }
                }
                //视频
                else if (infoData.getString("type").equals("video")){
                    videoPlayer =new StandardGSYVideoPlayer(ProgramHomeActivity.this);
                    videoPlayer.setId(a);
                    constraintLayout.addView(videoPlayer);
                    initVideo(videoPlayer,LinkSharedPreUtil.getSharePre().imgUrlServer()+infoData.getString("src"));
                    settingLayoutWH(0,constraintLayout,constraintSet,infoData,videoPlayer.getId());
                }
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void  settingLayoutWH(int type,ConstraintLayout constraintLayout,ConstraintSet constraintSet,JSONObject infoData,int viewId){
        constraintSet.clone(constraintLayout);//拷贝布局
        constraintSet.constrainWidth(viewId,(int)Math.round(Integer.parseInt(infoData.getString("w"))*numW));//组件的宽度
        constraintSet.constrainHeight(viewId,(int)Math.round(Integer.parseInt(infoData.getString("h"))*numW));//组件的高度
        if (type==0){
            String w=infoData.getString("x");
            String y=infoData.getString("y");
            constraintSet.setTranslationX(viewId,Math.round(Double.parseDouble(infoData.getString("x"))*numW));//设置x偏移量
            constraintSet.setTranslationY(viewId,Math.round(Double.parseDouble(infoData.getString("y"))*numW));//设置y偏移量
        }else if (type==1){
            constraintSet.setTranslationX(viewId,0);//设置x偏移量
            constraintSet.setTranslationY(viewId,0);//设置y偏移量
        }
        constraintSet.applyTo(constraintLayout);//应用布局
    }

    //视频适配器
    private void initVideo(StandardGSYVideoPlayer videoPlayer,String url) {
        videoPlayer.setUp(url,true,null);
       // videoPlayer.setUp(url, true, null);
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.black_background);
        videoPlayer.setThumbImageView(imageView);
       // videoPlayer.setBottomProgressBarDrawable(null);
        //关闭日志
        IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT);
        //设置底部进度条
        videoPlayer.setBottomProgressBarDrawable(null);
        //设置返回键不可见
        videoPlayer.getBackButton().setVisibility(View.INVISIBLE);
        // 设置触摸显示控制ui的消失时间
        videoPlayer.setDismissControlTime(0);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
        //设置返回键
//        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        videoPlayer.setIsTouchWiget(true);
        videoPlayer.setLooping(true);
        videoPlayer.startPlayLogic();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoPlayer!=null){
            videoPlayer.getCurrentPlayer().release();
        }
    }
    private void initBanner(Banner mBanner,int a) {
        mBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        mBanner.setBannerAnimation(Transformer.ZoomOutSlide);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setViewPagerIsScroll(true);
        if (a>0){
         mBanner.isAutoPlay(true);
         mBanner.setDelayTime(a);
        }
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setImages(images).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String date=Util.readTextFile("pramData");
                            showView(date);
                        }
                    });
                    break;
                case 2:
                    TextView textView=findViewById(msg.arg1);
                    textView.setText(DateUtil.date2TimeStamp(new Date(),"yyyy-MM-dd HH:mm"));
                    break;
                case 3:
                    String enterId= LoginSharedPreUtil.getSharePre().getEnterId();
                    if (enterId!=null&&!enterId.equals("")){
                        WebSocketUtil.getwebSocket().OneClickStart();//开启连接
                    }
                    break;
            }
        }
    };
}

package com.example.topniceinfo;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.topniceinfo.adapter.GlideImageLoader;
import com.example.topniceinfo.utils.MyApplication;
import com.example.topniceinfo.utils.Util;
import com.example.topniceinfo.websocket.WebSocketUtil;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.Random;

public class ProgramHomeActivity extends AppCompatActivity {

    double num=1;


    private ArrayList<String> images;
    OrientationUtils orientationUtils;
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

        Message message=new Message();
        message.what=1;
        handler.sendMessageDelayed(message,1000);

    }


    void showView(){
//        String date=Util.readTextFile("pramData");
//        if (date==null||date.equals("")){
            String  date="[{\"id\":\"1\", \"type\":\"bj\", \"resi\":false, \"drag\":false, \"w\":960, \"h\":540, \"color\":\"\", \"barg\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595913207485&di=99086d5c5dc78689004212475b81a84e&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F4%2F56f62bab021a8.jpg\", \"z\":1}, {\"id\":\"2\", \"type\":\"img\", \"x\":200, \"y\":50, \"style\":\"\", \"resi\":true, \"drag\":true, \"src\":\"\", \"w\":100, \"h\":100, \"z\":3}, {\"id\":\"3\", \"type\":\"video\", \"x\":20, \"y\":50, \"resi\":true, \"drag\":true, \"src\":\"https://easyhtml5video.com/assets/video/new/Penguins_of_Madagascar.m4v\", \"w\":300, \"h\":300, \"z\":2},{\"id\":\"5\", \"type\":\"text\", \"x\":200, \"y\":200, \"w\":200, \"resi\":true, \"drag\":true, \"h\":100, \"z\":2, \"color\":\"#FF1493\", \"textAlign\":\"center\", \"size\":\"16px\"}, {\"id\":6, \"type\":\"text\", \"x\":500, \"y\":60, \"w\":150, \"h\":100, \"z\":6, \"resi\":true, \"drag\":true, \"color\":\"#FF1493\", \"textAlign\":\"center\", \"size\":\"16px\"}]";
            Util.saveToSDCard(ProgramHomeActivity.this,"pramData",date);
       // }
        JSONArray jsonArray = JSONArray.parseArray(date);
        ConstraintLayout constraintLayout=findViewById(R.id.program_home_constraint);

        constraintLayout.setBackgroundColor((Color.parseColor("#A86129")));
        DisplayMetrics dm = MyApplication.context.getResources().getDisplayMetrics();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
            //第一个为背景，根据
             if (jsonObject2.getString("id").equals("1")){
                 System.out.println(dm.widthPixels/Integer.parseInt(jsonObject2.getString("w")));
                 num=dm.widthPixels/Double.parseDouble(jsonObject2.getString("w"));
             }
             //解析控件
            if (jsonObject2.getString("x")!=null){
                //设置ui
                ConstraintSet constraintSet=new ConstraintSet();
                if (jsonObject2.getString("type").equals("img")){
                    Banner mBanner = new Banner(ProgramHomeActivity.this);
                    int a=1001+i;
                    mBanner.setId(a);
                    constraintLayout.addView(mBanner);
                    initData();
                    initView(mBanner,1000);
                    constraintSet.clone(constraintLayout);//拷贝布局
                    constraintSet.constrainWidth(mBanner.getId(),(int)Math.round(Integer.parseInt(jsonObject2.getString("w"))*num));//组件的宽度
                    constraintSet.constrainHeight(mBanner.getId(),(int)Math.round(Integer.parseInt(jsonObject2.getString("h"))*num));//组件的高度
                    constraintSet.setTranslationX(mBanner.getId(),Math.round(Integer.parseInt(jsonObject2.getString("x"))*num));//设置x偏移量
                    constraintSet.setTranslationY(mBanner.getId(),Math.round(Integer.parseInt(jsonObject2.getString("y"))*num));//设置y偏移量
                }
                else if (jsonObject2.getString("type").equals("text")){
                    Banner mBanner = new Banner(ProgramHomeActivity.this);
                    int a=1001+i;
                    mBanner.setId(a);
                    constraintLayout.addView(mBanner);
                    initData2();
                    initView(mBanner,4000);
                    constraintSet.clone(constraintLayout);//拷贝布局
                    constraintSet.constrainWidth(mBanner.getId(),(int)Math.round(Integer.parseInt(jsonObject2.getString("w"))*num));//组件的宽度
                    constraintSet.constrainHeight(mBanner.getId(),(int)Math.round(Integer.parseInt(jsonObject2.getString("h"))*num));//组件的高度
                    constraintSet.setTranslationX(mBanner.getId(),Math.round(Integer.parseInt(jsonObject2.getString("x"))*num));//设置x偏移量
                    constraintSet.setTranslationY(mBanner.getId(),Math.round(Integer.parseInt(jsonObject2.getString("y"))*num));//设置y偏移量
                }
                else if (jsonObject2.getString("type").equals("video")){
                    StandardGSYVideoPlayer videoPlayer =new StandardGSYVideoPlayer(ProgramHomeActivity.this);
                    int a=1001+i;
                    videoPlayer.setId(a);
                    constraintLayout.addView(videoPlayer);
                    initVideo(videoPlayer,jsonObject2.getString("src"));
                    constraintSet.clone(constraintLayout);//拷贝布局
                    constraintSet.constrainWidth(videoPlayer.getId(),(int)Math.round(Integer.parseInt(jsonObject2.getString("w"))*num));//组件的宽度
                    constraintSet.constrainHeight(videoPlayer.getId(),(int)Math.round(Integer.parseInt(jsonObject2.getString("h"))*num));//组件的高度
                    constraintSet.setTranslationX(videoPlayer.getId(),Math.round(Integer.parseInt(jsonObject2.getString("x"))*num));//设置x偏移量
                    constraintSet.setTranslationY(videoPlayer.getId(),Math.round(Integer.parseInt(jsonObject2.getString("y"))*num));//设置y偏移量
                }
                constraintSet.applyTo(constraintLayout);
            }
        }
    }
    //视频适配器
    private void initVideo(StandardGSYVideoPlayer videoPlayer,String url) {
        videoPlayer.setUp(url, true, null);
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.black_background);
        videoPlayer.setThumbImageView(imageView);
       // videoPlayer.setBottomProgressBarDrawable(null);

        videoPlayer.setIsTouchWiget(false);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
        //设置返回键
//        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
//        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
//        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                orientationUtils.resolveByClick();
//            }
//        });
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
//        //设置返回按键功能
//        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        //设置轮播
        videoPlayer.setLooping(true);
        videoPlayer.startPlayLogic();
    }
    private void initView(Banner mBanner,int a) {
        mBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setViewPagerIsScroll(true);
        mBanner.isAutoPlay(true);
        mBanner.setDelayTime(a);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setImages(images) .start();
    }
    private void initData2() {
        //设置图片资源:url或本地资源
        images = new ArrayList<>();
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596105910076&di=5f969bad331b718c095bcc6290b62582&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201312%2F31%2F111859myvyiivetyftfz2n.jpg");
        images.add("https://img.zcool.cn/community/01d3695efabe2ea801215aa04f65f8.jpg@2o.jpg");
        images.add("https://img.zcool.cn/community/0166195efad17fa801206621857308.jpg@1280w_1l_2o_100sh.jpg");

    }
    private void initData() {
        //设置图片资源:url或本地资源
        images = new ArrayList<>();
//        for (int a=1;a<15;a++){
//            images.add("http://192.168.191.1:8080/file/allimg/"+a+".jpg");
//        }
        images.add("https://img.zcool.cn/community/014ae25efaf5b7a801215aa08509f8.jpg@2o.jpg");
        images.add("https://img.zcool.cn/community/01d3695efabe2ea801215aa04f65f8.jpg@2o.jpg");
       images.add("https://img.zcool.cn/community/0166195efad17fa801206621857308.jpg@1280w_1l_2o_100sh.jpg");

    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showView();
//                            //更改UI；
//                            ConstraintLayout constraintLayout=findViewById(R.id.program_home_constraint);
//                            //设置ui
//                            constraintLayout.setBackgroundColor((Color.parseColor("#A86129")));
//                            ConstraintSet constraintSet=new ConstraintSet();
//                            Button new3=new Button(ProgramHomeActivity.this);
//                            new3.setText("3");
//                            new3.setBackgroundColor((Color.parseColor("#f34649")));
//                            int a=1001;
//                            new3.setId(a);
//                            constraintLayout.addView(new3);
//                            constraintSet.clone(constraintLayout);//拷贝布局
//                            constraintSet.constrainWidth(new3.getId(),500);//组件的宽度
//                            constraintSet.constrainHeight(new3.getId(),600);//组件的高度
//
////                          constraintSet.connect(new3.getId(),ConstraintSet.END,  ConstraintSet.PARENT_ID,ConstraintSet.END);
//                            constraintSet.setTranslationX(new3.getId(),200);//设置x偏移量
//                            constraintSet.setTranslationY(new3.getId(),20);//设置y偏移量
//
////                            constraintSet.connect(new3.getId(),ConstraintSet.START, ConstraintSet.PARENT_ID,ConstraintSet.START);
////                            constraintSet.connect(new3.getId(),ConstraintSet.TOP, ConstraintSet.PARENT_ID,ConstraintSet.TOP);
////                            //这个按钮距离底部的margin值为1000
////                            constraintSet.connect(new3.getId(),ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,500);
//
//                            constraintSet.applyTo(constraintLayout);
//                            ConstraintSet constraintSet2=new ConstraintSet();
//                            Button new4=new Button(ProgramHomeActivity.this);
//                            new4.setText("4");
//                            new4.setBackgroundColor((Color.parseColor("#48A6E5")));
//                            int b=1003;
//                            new4.setId(b);
//                            constraintLayout.addView(new4);
//                            constraintSet2.clone(constraintLayout);//拷贝布局
//                            constraintSet2.constrainWidth(new4.getId(),300);//组件的宽度
//                            constraintSet2.constrainHeight(new4.getId(),400);//组件的高度
//                            constraintSet2.connect(new4.getId(),ConstraintSet.END,  ConstraintSet.PARENT_ID,ConstraintSet.END);
//                            constraintSet2.connect(new4.getId(),ConstraintSet.START, ConstraintSet.PARENT_ID,ConstraintSet.START);
//                            //constraintSet.connect(new3.getId(),ConstraintSet.TOP, ConstraintSet.PARENT_ID,ConstraintSet.TOP);
//                            //这个按钮距离底部的margin值为1000
//                            constraintSet2.connect(new4.getId(),ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,200);
//                            constraintSet2.applyTo(constraintLayout);
                        }
                    });
                    break;
                case -1:
                    break;
            }
        }
    };
}

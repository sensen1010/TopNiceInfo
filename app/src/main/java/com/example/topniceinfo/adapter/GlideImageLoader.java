package com.example.topniceinfo.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.topniceinfo.R;
import com.example.topniceinfo.utils.MyApplication;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //imageView.setBackgroundColor(MyApplication.context.getResources().getColor(R.color.colorAccent));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        if (path!=null&&!path.equals("")){
//
//        }else {
//            path="http://www.niwoxuexi.com/statics/images/nougat_bg.png";
//        }
        Glide.with(MyApplication.context)
                .load((String) path)
                .into(imageView);

    }

}

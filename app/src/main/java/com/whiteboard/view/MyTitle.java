package com.whiteboard.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whiteboard.R;


/**
 * Created by LZL on 2016/6/20.
 * 自定义标题栏
 */
public class MyTitle extends LinearLayout {
    private LinearLayout rt_title_view;
    private ImageButton iv_title_back = null;
    private TextView tv_title_text, tv_title_textright = null;

    public MyTitle(Context context) {
        super(context);
    }

    public MyTitle(final Context context, AttributeSet attrs) {
        super(context, attrs);
        View v = LayoutInflater.from(context).inflate(R.layout.mytitle, this);
        rt_title_view =  findViewById(R.id.rt_title_view);
        iv_title_back = (ImageButton) findViewById(R.id.iv_title_back);
        tv_title_text = (TextView)findViewById(R.id.tv_title_text);
        tv_title_textright = (TextView) findViewById(R.id.tv_title_textright);
//        BaseUtil.relativeLayoutnowscale(rt_title_view,95,0);
        // BaseUtil.imageviewnowscale(iv_title_back,88,120);

        //返回按钮
        iv_title_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) context).finish();
            }
        });
    }

    //设置按钮的显示隐藏
    public void setIsHiddenLeftButton(boolean hidden) {
        iv_title_back.setVisibility(hidden ? View.INVISIBLE : View.VISIBLE);
    }

    //设置按钮的显示隐藏
    public void setIsHiddenRightText(boolean hidden) {
        tv_title_textright.setVisibility(hidden ? View.INVISIBLE : View.VISIBLE);
    }


    //设置内容
    public void setRightText(String title) {
        this.tv_title_textright.setText(title);
    }


    //设置标题
    public void setTitle(String title) {
        this.tv_title_text.setText(title);
    }
}

package com.whiteboard.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.whiteboard.R;
import com.whiteboard.view.MyTitle;

public class NursingActivity extends AppCompatActivity {

    private WebView webView_nursing;
    private MyTitle title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing);
        initTitle();
        initView();
        initListener();



    }

    private void initTitle() {
        title =findViewById(R.id.mytitle_nursing);
        title.setTitle("添加护理记录");
        title.setIsHiddenRightText(true);
    }

    private void initView() {
        webView_nursing = findViewById(R.id.webView_nursing);
        webView_nursing.getSettings().setJavaScriptEnabled(true);
        webView_nursing.loadUrl("http://106.14.11.95:9999/haiyang/addNursing.html");
    }


    private void initListener() {

    }
}

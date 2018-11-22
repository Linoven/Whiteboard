package com.whiteboard.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.whiteboard.R;
import com.whiteboard.view.MyTitle;

public class NursingListActivity extends AppCompatActivity {

    private WebView webView_nursinglist;
    private MyTitle title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_list);
        initTitle();
        initView();
        initListener();


    }

    private void initTitle() {
        title =findViewById(R.id.mytitle_nursinglist);
        title.setTitle("护理记录");
        title.setIsHiddenRightText(true);
    }

    private void initView() {
        webView_nursinglist = findViewById(R.id.webView_nursinglist);
        webView_nursinglist.getSettings().setJavaScriptEnabled(true);
        webView_nursinglist.loadUrl("http://106.14.11.95:9999/haiyang/list.html");

    }


    private void initListener() {

    }
}

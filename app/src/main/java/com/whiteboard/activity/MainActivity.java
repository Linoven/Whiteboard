package com.whiteboard.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.whiteboard.Call;
import com.whiteboard.GetRequest_Interface;
import com.whiteboard.R;
import com.whiteboard.view.MyTitle;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView webView;
    private LinearLayout ll_nursing_add, ll_nursing_list,ll_nursing_scan;
    private Intent intent;
    private MyTitle title;
    private static final String TAG = "Rxjava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initTitle();
        initView();
        initData();
        initListener();

        rxJavafixRxjava();

    }


    private void initTitle() {
        title = findViewById(R.id.mytitle_mian);
        title.setTitle("护理中心");
        title.setIsHiddenLeftButton(true);
        title.setIsHiddenRightText(true);
    }

    private void initView() {
        ll_nursing_add = findViewById(R.id.ll_nursing_add);
        ll_nursing_list = findViewById(R.id.ll_nursing_list);
        ll_nursing_scan=findViewById(R.id.ll_nursing_scan);
    }

    private void initData() {
    }

    private void initListener() {
        findViewById(R.id.ll_nursing_add).setOnClickListener(this);
        findViewById(R.id.ll_nursing_list).setOnClickListener(this);
        findViewById(R.id.ll_nursing_scan).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_nursing_add:
                intent = new Intent(MainActivity.this, NursingActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_nursing_list:
                intent = new Intent(MainActivity.this, NursingListActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_nursing_scan:
//                intent = new Intent(MainActivity.this, NursingScanActivity.class);
//                startActivity(intent);

                Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
        }
    }

    private void rxJavafixRxjava() {

        /*
         * 步骤1：采用interval（）延迟发送
         * 注：此处主要展示无限次轮询，若要实现有限次轮询，仅需将interval（）改成intervalRange（）即可
         **/
        Observable.interval(2, 1, TimeUnit.SECONDS)
                // 参数说明：
                // 参数1 = 第1次延迟时间；
                // 参数2 = 间隔时间数字；
                // 参数3 = 时间单位；
                // 该例子发送的事件特点：延迟2s后发送事件，每隔1秒产生1个数字（从0开始递增1，无限个）

                /*
                 * 步骤2：每次发送数字前发送1次网络请求（doOnNext（）在执行Next事件前调用）
                 * 即每隔1秒产生1个数字前，就发送1次网络请求，从而实现轮询需求
                 **/
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long integer) throws Exception {
                        Log.d(TAG, "第 " + integer + " 次轮询");

                        /*
                         * 步骤3：通过Retrofit发送网络请求
                         **/
                        // a. 创建Retrofit对象
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://106.14.11.95:9999/haiyang/") // 设置 网络请求 Url
                                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                                .build();

                        // b. 创建 网络请求接口 的实例
                        GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);

                        // c. 采用Observable<...>形式 对 网络请求 进行封装
                        Observable<Call> observable = request.getCall();
                        // d. 通过线程切换发送网络请求
                        observable.subscribeOn(Schedulers.io())               // 切换到IO线程进行网络请求
                                .observeOn(AndroidSchedulers.mainThread())  // 切换回到主线程 处理请求结果
                                .subscribe(new Observer<Call>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                    }

                                    @Override
                                    public void onNext(Call result) {
                                     System.out.println(result.getMemberName());

                                        int status = result.getCallStatus();
                                        String memberName = result.getMemberName();
                                        String phone = result.getPhone();
                                        Log.d(TAG,"status:"+status +" = "+"memberName:"+memberName+"="+"phone:"+phone);
                                      if(status==1){
                                          playSound(getApplicationContext());

                                      }

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d(TAG, "请求失败");
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });

                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long value) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        });

    }

    private static Ringtone mRingtone;

    //播放自定义的声音
    public synchronized void playSound(Context context) {

        if (mRingtone == null) {
            Log.d(TAG, "----------初始化铃声----------");
            String uri = "android.resource://" + context.getPackageName() + "/" + R.raw.msg;
            Uri no = Uri.parse(uri);
            mRingtone = RingtoneManager.getRingtone(context.getApplicationContext(), no);
        }
        if (!mRingtone.isPlaying()) {
            Log.d(TAG, "--------------播放铃声---------------" + mRingtone.isPlaying());
            mRingtone.play();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && data != null) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Bundle bundle = data.getExtras();
                    String  result = bundle.getString("result");
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}

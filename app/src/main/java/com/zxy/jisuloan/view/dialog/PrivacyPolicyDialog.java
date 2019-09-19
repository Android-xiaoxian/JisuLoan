package com.zxy.jisuloan.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EdgeEffect;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.activity.WebViewActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.SharePrefsUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Create by Fang ShiXian
 * on 2019/8/20
 * 首页提示隐私政策和部分权限的弹窗
 */
public class PrivacyPolicyDialog extends Dialog {

    private Context context;
    private View view1, view2;
    private ViewPager viewPager;
    private ArrayList<View> views;
    private int curPosition = 0;
    private Timer timer;
    private Handler handler;

    public PrivacyPolicyDialog(Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_privacy_policy);
        view1 = findViewById(R.id.view_1);
        view2 = findViewById(R.id.view_2);//viewpager的两个指示点
        findViewById(R.id.btn_privacy_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                SharePrefsUtils.getInstance().putBoolean(Config.AGRESS_WITH_PRIVACY_POLICY, true);
            }
        });
        initViewPager();


    }

    /**
     * 加载viewpager的两个子页
     */
    private void initViewPager() {
        //viewPager部分
        viewPager = findViewById(R.id.vp_privacy);
        LayoutInflater inflater = getLayoutInflater().from(context);
        views = new ArrayList<>();
        //viewpager的两个子页
        View pagerView1 = inflater.inflate(R.layout.item_viewpager_1, null);
        View pagerView2 = inflater.inflate(R.layout.item_viewpager_2, null);
        views.add(pagerView1);
        views.add(pagerView2);
        viewPager.addOnPageChangeListener(new MyPagerListener());
        viewPager.setAdapter(new MyAdapter(views));
        //监听viewpager的触摸事件，pagerView1里面有ScrollView要单独监听触摸事件
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    MyLog.e("test", "viewPager也按下了");
                    timer.cancel();//停止自动滚动
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    timerStart();//开始自动滚动
                    MyLog.e("test", "viewPager也抬起了");
                }
                return false;
            }
        });

        //timer发送message控制viewpager自动滚动的时间
        timerStart();

        //ScrollView部分
        ScrollView scrollView = pagerView1.findViewById(R.id.sv_pp_dialog);
        try {
            //修改头部阴影颜色
            Field topMethod = ScrollView.class.getDeclaredField("mEdgeGlowTop");
            topMethod.setAccessible(true);
            EdgeEffect top = (EdgeEffect) topMethod.get(scrollView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                top.setColor(Color.parseColor("#33000000"));
            }
            //修改底部阴影颜色
            Field bottomMethod = ScrollView.class.getDeclaredField("mEdgeGlowBottom");
            bottomMethod.setAccessible(true);
            EdgeEffect bottom = (EdgeEffect) bottomMethod.get(scrollView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bottom.setColor(Color.parseColor("#33000000"));
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //scrollView的触摸事件要单独处理
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    MyLog.e("test", "按下了");
                    timer.cancel();

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    isLoop = true;
                    timerStart();
                    MyLog.e("test", "抬起了");
                }
                return false;
            }
        });

        //设置pager1中间的TextView
        TextView textView = pagerView1.findViewById(R.id.tv_yinSi_dialog);
        setMidTextView(textView);
    }

    /**
     * viewpager滑动控制
     */
    private void timerStart() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (curPosition == 0) {
                    viewPager.setCurrentItem(1);
                } else {
                    viewPager.setCurrentItem(0);
                }
            }
        }, 3000, 3000);
    }


    private void setMidTextView(TextView textView) {
        SpannableString spanStrStart = new SpannableString("点击同意即表示您已阅读并同意");
        SpannableString spanStrClick = new SpannableString("《用户协议》");
        //改变颜色并设置点击监听
        spanStrClick.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                MyLog.e("test", "《用户协议》被点击了");
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(Config.WEB_URL, Config.USER_AGGREEMENT_URL);
                intent.putExtra(Config.WEB_TITLE, "用户协议");
                context.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(context.getResources().getColor(R.color.colorPrimary)); //设置颜色
                //去掉下划线，默认是带下划线的
                ds.setUnderlineText(false);
                //设置字体背景
//				ds.bgColor = Color.parseColor("#FF0000");
            }
        }, 0, spanStrClick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spanStrClick2 = new SpannableString("《隐私协议》");
        spanStrClick2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                MyLog.e("test", "《隐私》被点击了");
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(Config.WEB_URL, Config.PRIVACY_URL);
                intent.putExtra(Config.WEB_TITLE, "隐私协议");
                context.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(context.getResources().getColor(R.color.colorPrimary)); //设置颜色
                //去掉下划线，默认是带下划线的
                ds.setUnderlineText(false);
                //设置字体背景
//				ds.bgColor = Color.parseColor("#FF0000");
            }
        }, 0, spanStrClick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spanStrMid = new SpannableString("和");
        SpannableString spanStrEnd = new SpannableString("。我们将尽全力保护您的个人信息及合法权益，感谢您的信任。");
        textView.append(spanStrStart);
        textView.append(spanStrClick);
        textView.append(spanStrMid);
        textView.append(spanStrClick2);
        textView.append(spanStrEnd);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        timer.cancel();
                        break;
                    case MotionEvent.ACTION_UP:
                        timerStart();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void show() {
        super.show();
        DialogWindowManger.setDialogWindow(context, this);
        setCancelable(false);
    }


    /**
     * ViewPager数据适配器
     */
    class MyAdapter extends PagerAdapter {
        private ArrayList<View> views;

        public MyAdapter(ArrayList<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {

            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }

    class MyPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (position == 0) {
                        view1.setBackgroundResource(R.drawable.shape_007);
                        view2.setBackgroundResource(R.drawable.shape_008);
                    } else {
                        view1.setBackgroundResource(R.drawable.shape_008);
                        view2.setBackgroundResource(R.drawable.shape_007);
                    }
                }
            });

            curPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        timer.cancel();
    }
}

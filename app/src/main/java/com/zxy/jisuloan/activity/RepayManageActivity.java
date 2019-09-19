package com.zxy.jisuloan.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.gridlayout.widget.GridLayout;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.view.MyChartView;
import com.zxy.jisuloan.view.MyChartView2;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/9/7
 */
public class RepayManageActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.ll_chart)
    LinearLayout ll_chart;
    @BindView(R.id.gl)
    GridLayout gridLayout;
    @BindView(R.id.ll_myChartView)
    LinearLayout ll_myChartView;
    @BindView(R.id.tv_repayment)
    TextView tv_repayment;
    @BindView(R.id.ll_myChartView2)
    LinearLayout ll_myChartView2;


    private MyChartView2 myChartView2;
    private int curItem = 3;
    private int[] months = new int[7];
    private String string = "月应还分析";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_repay_manage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initView2();
    }

    private void initView() {
        tv_title.setText(R.string.str59);
        double[] doubles = new double[]{0, 1, 0, 10, 0, 0, 0};
        MyChartView myChartView = new MyChartView(this, doubles);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        myChartView.setLayoutParams(params);
        myChartView.setBackgroundResource(R.color.white);//不设置背景 不显示折线
        ll_myChartView.addView(myChartView);
        for (int i = 0; i < doubles.length; i++) {
            final int cur = i;
            myChartView.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myChartView.setVisibility(cur);
                    ((TextView) gridLayout.getChildAt(cur)).setTextColor(getResources().getColor(R.color.text_33));
                    ((TextView) gridLayout.getChildAt(curItem)).setTextColor(getResources().getColor(R.color.text_66));
                    curItem = cur;
                    tv_repayment.setText(months[cur] + string);
                    if (cur%2==0){
                        myChartView2.updateView(floats,strings);
                    }else {
                        myChartView2.updateView(floats2,strings2);
                    }
                }
            });
        }
        //获取系统的日期
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);//年
        int month = calendar.get(Calendar.MONTH) + 1;//月
        int year0;
        int month0;
        for (int i = -3; i < 4; i++) {
            if (month + i < 0) {
                year0 = year - 1;
                month0 = 12 - (month + i);
            } else if (month + i > 12) {
                year0 = year + 1;
                month0 = month + i - 12;
            } else {
                year0 = year;
                month0 = month + i;
            }
            months[i + 3] = month0;
            tv_repayment.setText(months[3] + string);
            if (i == 0) {
                ((TextView) gridLayout.getChildAt(i + 3)).setText("当月");
            } else {
                String time = month0 + "月\n" + year0;
                ((TextView) gridLayout.getChildAt(i + 3)).setText(time);
            }
        }
    }

    String[] strings = new String[]{};
    float[] floats = new float[]{};
    String[] strings2 = new String[]{"小赢白条", "有钱花", "及贷", "360贷款", "2345贷款王2345贷款王2345贷款王", "大家花","小赢白条", "有钱花", "及贷", "360贷款", "2345贷款王", "大家花"};
    float[] floats2 = new float[]{100, 300, 100, 200, 400, 150,100, 300, 100, 200, 400, 150};
    private void initView2() {
        myChartView2 = new MyChartView2(this, floats, strings);
        ll_myChartView2.addView(myChartView2);
    }


    @OnClick({R.id.img_title_back,R.id.btn_toRepay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.btn_toRepay:

                break;
            default:
                break;
        }
    }

    @Override
    public void processMessage(Message message) {

    }
}

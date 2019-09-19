package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.adapter.ProvinceAdapter;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.JSONParser;
import com.zxy.jisuloan.utils.ReadFileUtil;
import com.zxy.jisuloan.view.dialog.TipsDialog;
import com.zxy.jisuloan.vo.Cityinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择省市区
 * create by Fang Shixian
 * on 2019/2/14 0014
 */
public class DistinctPickerActivity extends BaseActivity {
    @BindView(R.id.txt_public_title)
    TextView title;
    @BindView(R.id.txt_gps_location)
    TextView txt_gps_location;
    @BindView(R.id.selected_province_text)
    CheckedTextView selected_province_text;
    @BindView(R.id.selected_city_layout)
    LinearLayout selected_city_layout;
    @BindView(R.id.selected_city_text)
    CheckedTextView selected_city_text;
    @BindView(R.id.selected_district_layout)
    LinearLayout selected_district_layout;
    @BindView(R.id.selected_district_text)
    CheckedTextView selected_district_text;
    @BindView(R.id.list_provinces)
    ListView list_provinces;
    @BindView(R.id.list_cities)
    ListView list_cities;
    @BindView(R.id.list_districts)
    ListView list_districts;

    private String province, city, district;//定位所在城市的省份名称、城市名称、区县名称
    private String province_number, city_number;//定位所在城市的省份代码、城市代码
    private String province_seclnum, city_seclnum, district_seclnum;//手动选择的省份代码、城市代码、区县代码
    private int cur_province = -1, cur_city = -1;
    private int province_local = -1, city_local = -1, district_local = -1;
    private List<Cityinfo> province_list = new ArrayList<Cityinfo>();
    private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();
    private HashMap<String, List<Cityinfo>> couny_map = new HashMap<String, List<Cityinfo>>();
    private ProvinceAdapter adapter, adapter2, adapter3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparentStatusBar(this);
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_district_picker;
    }

    @Override
    public void processMessage(Message message) {

    }

    private void initView() {
        title.setText("选择地区");
        Intent intent = getIntent();
        province = intent.getStringExtra(Config.PROVINCE);
        city = intent.getStringExtra("city");
        district = intent.getStringExtra("district");

        if (province == null) {
            txt_gps_location.setText("定位失败");
        } else {
            if (city != null && district != null) {
                txt_gps_location.setText(province + "/" + city + "/" + district);
                selected_province_text.setVisibility(View.VISIBLE);
                selected_city_layout.setVisibility(View.VISIBLE);
                selected_district_layout.setVisibility(View.VISIBLE);
                selected_province_text.setText(province);
                selected_city_text.setText(city);
                selected_district_text.setText(district);
            }
        }

        getaddressinfo();
        adapter = new ProvinceAdapter(this, province_list);
        list_provinces.setAdapter(adapter);
        if (province != null) {
            for (int i = 0; i < province_list.size(); i++) {
                if (province_list.get(i).getCity_name().equals(province)) {
                    list_provinces.setSelection(i);
                    adapter.setSelcetItem(i);
                    province_local = i;
                    province_number = province_list.get(i).getId();
                    province_seclnum = province_list.get(i).getId();
                    break;
                }
            }
        }
        adapter2 = new ProvinceAdapter(this, city_map.get(province_number));
        list_cities.setAdapter(adapter2);
        if (city != null) {
            for (int i = 0; i < city_map.get(province_number).size(); i++) {
                if (city_map.get(province_number).get(i).getCity_name().equals(city)) {
                    list_cities.setSelection(i);
                    adapter2.setSelcetItem(i);
                    city_local = i;
                    city_number = city_map.get(province_number).get(i).getId();
                    city_seclnum = city_map.get(province_number).get(i).getId();
                    break;
                }
            }
        }
        adapter3 = new ProvinceAdapter(this, couny_map.get(city_number), 1);
        list_districts.setAdapter(adapter3);
        if (district != null) {
            for (int i = 0; i < couny_map.get(city_number).size(); i++) {
                if (couny_map.get(city_number).get(i).getCity_name().equals(district)) {
                    list_districts.setSelection(i - 1);
                    adapter3.setSelcetItem(i - 1);
                    district_local = i;
                    district_seclnum = couny_map.get(city_number).get(i).getId();
                    break;
                }
            }
        }
        list_provinces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelcetItem(position);
                if (position == province_local) {
                    province_seclnum = province_number;
                    adapter2.update(city_map.get(province_number));
                    adapter3.update(couny_map.get(city_number), 1);
                    adapter2.setSelcetItem(city_local);
                    adapter3.setSelcetItem(district_local - 1);
                    list_cities.setSelection(city_local);
                    list_districts.setSelection(district_local - 1);
                    selected_province_text.setVisibility(View.VISIBLE);
                    selected_city_layout.setVisibility(View.VISIBLE);
                    selected_district_layout.setVisibility(View.VISIBLE);
                    selected_province_text.setText(province);
                    selected_city_text.setText(city);
                    selected_district_text.setText(district);
                } else {
                    selected_province_text.setVisibility(View.VISIBLE);
                    selected_city_layout.setVisibility(View.GONE);
                    selected_district_layout.setVisibility(View.GONE);
                    selected_province_text.setText(province_list.get(position).getCity_name());
                    province_seclnum = province_list.get(position).getId();
                    adapter2.update(city_map.get(province_seclnum));
                    adapter3.update(null);
                    adapter2.setSelcetItem(-1);
                    adapter3.setSelcetItem(-1);
                    list_cities.setSelection(0);
                    list_districts.setSelection(0);
                }
//                MyLog.e("test", "province_seclnum = " + province_seclnum);
            }
        });
        list_cities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter2.setSelcetItem(position);
                if (position == city_local && province_seclnum.equals(province_number)) {
                    city_seclnum = city_number;
                    adapter3.update(couny_map.get(city_number), 1);
                    adapter3.setSelcetItem(district_local - 1);
                    list_districts.setSelection(district_local - 1);
                    selected_city_layout.setVisibility(View.VISIBLE);
                    selected_district_layout.setVisibility(View.VISIBLE);
                    selected_city_text.setText(city);
                    selected_district_text.setText(district);
                } else {
                    selected_city_layout.setVisibility(View.VISIBLE);
                    selected_district_layout.setVisibility(View.GONE);
                    selected_city_text.setText(city_map.get(province_seclnum).get(position).getCity_name());
                    city_seclnum = city_map.get(province_seclnum).get(position).getId();
                    adapter3.update(couny_map.get(city_seclnum), 1);
                    adapter3.setSelcetItem(-1);
                    list_districts.setSelection(0);
                }
//                MyLog.e("test", "city_seclnum = " + city_seclnum);
            }
        });
        list_districts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter3.setSelcetItem(position);
                selected_district_layout.setVisibility(View.VISIBLE);
                selected_district_text.setText(couny_map.get(city_seclnum).get(position + 1).getCity_name());
                district_seclnum = couny_map.get(city_seclnum).get(position + 1).getId();
//                MyLog.e("test", "district_seclnum = " + district_seclnum);

            }
        });
    }

    // 获取城市信息
    private void getaddressinfo() {
        // TODO Auto-generated method stub
        // 读取城市信息string
        JSONParser parser = new JSONParser();
        String area_str = ReadFileUtil.readAssets(this, "area.json");
        province_list = parser.getJSONParserResult(area_str, "area0");
        city_map = parser.getJSONParserResultArray(area_str, "area1");
        couny_map = parser.getJSONParserResultArray(area_str, "area2");
    }


    @OnClick({R.id.img_title_back, R.id.btn_select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.btn_select:
                setResultAndFinish();
                break;
        }
    }

    private void setResultAndFinish() {
        if (selected_district_layout.getVisibility() == View.GONE) {
            new TipsDialog(this, "请选择完整的省市区").show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("result", selected_province_text.getText().toString() + "1"
                + selected_city_text.getText().toString() + "1"
                + selected_district_text.getText().toString());
        intent.putExtra("areaNo", district_seclnum);
        setResult(Config.RESULT_CODE_1, intent);
        finish();
    }
}

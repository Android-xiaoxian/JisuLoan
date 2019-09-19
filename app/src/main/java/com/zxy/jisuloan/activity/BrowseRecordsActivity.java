package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.adapter.BrowseRecordsAdapter;
import com.zxy.jisuloan.adapter.ProductAdapter;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.DateUtils;
import com.zxy.jisuloan.utils.LoadDialog;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.ToastUtils;
import com.zxy.jisuloan.view.ListViewForScrollView;
import com.zxy.jisuloan.view.dialog.PermissionDialog;
import com.zxy.jisuloan.vo.BaseResponse;
import com.zxy.jisuloan.vo.BrowseRecordsVO;
import com.zxy.jisuloan.vo.ProductVo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/29
 */
public class BrowseRecordsActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.lv_tuisong2)
    ListViewForScrollView listView;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.include_recommend)
    LinearLayout ll_recommend;


    private BrowseRecordsAdapter adapter;
    private ProductAdapter adapter2;
    private List<BrowseRecordsVO.BrowseRecord> list;
    private List<ProductVo.Products> products = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_browse_records;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWhiteStatusBar(this);
        initView();
        initData();

    }


    private void initView() {
        tv_title.setText("浏览记录");
        //添加自定义分割线
//        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        divider.setDrawable(getDrawable(R.drawable.line_fengexian));
//        recyclerView.addItemDecoration(divider);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new BrowseRecordsAdapter(this, null);
        recyclerView.setAdapter(adapter);

        adapter2 = new ProductAdapter(this, null, 1);
        listView.setAdapter(adapter2);

    }

    private String date = "";

    private void initData() {
        LoadDialog.show(this);
        String userId = Config.getUserId();
        BaseApi.request(BaseApi.createApi(IService.class).getBrowseRecord(userId),
                new BaseApi.IResponseListener<BrowseRecordsVO>() {
                    @Override
                    public void onSuccess(BrowseRecordsVO data) {
                        if ("0".equals(data.getError()) && data.getBrowseRecList() != null) {

                            list = data.getBrowseRecList();
                            for (int i = 0; i < list.size(); i++) {
                                BrowseRecordsVO.BrowseRecord browseRecord0 = list.get(i);
                                //获取日期
                                String date1 = browseRecord0.getCreateDate();
                                date1 = DateUtils.timeStamp2Date(Long.valueOf(date1));
                                if (!date.equals(date1)) {//比对日期
                                    BrowseRecordsVO.BrowseRecord browseRecord = new BrowseRecordsVO.BrowseRecord();
                                    browseRecord.setCreateDate(date1);
                                    browseRecord.setItemType(Config.ITEM_TYPE1);
                                    list.add(i, browseRecord);
                                    date = date1;
                                } else {
                                    browseRecord0.setItemType(Config.ITEM_TYPE2);
                                }
                            }
                            date = "";
                            adapter.upDateAdapter(list);
                            ll_no_data.setVisibility(View.GONE);
                            ll_recommend.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                        } else {
                            ll_no_data.setVisibility(View.VISIBLE);
                            ll_recommend.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            initRecommendData();
                        }
                    }

                    @Override
                    public void onFail() {
                        ll_no_data.setVisibility(View.VISIBLE);
                        ll_recommend.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        initRecommendData();
                    }
                });
    }


    @OnClick({R.id.img_title_back, R.id.img_delete, R.id.tv_more_loan,
            R.id.ll_no_data})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.img_delete:
                showDeleteDialog();
                break;
            case R.id.tv_more_loan:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Config.CHANGE_FRAGMENT_BORROW, Config.CHANGE_TO_BORROW);
                startActivity(intent);
                finish();
                break;
            case R.id.ll_no_data:
                initData();
                break;
            default:
                break;
        }
    }

    private void showDeleteDialog() {
        if (adapter.getItemCount() == 0) {
            showTipsDialog("没有浏览记录了");
            return;
        }
        PermissionDialog dialog = new PermissionDialog(this);
        dialog.setOnClickListenerInterface(new PermissionDialog.OnClickListenerInterface() {
            @Override
            public void doConfirm() {
                dialog.dismiss();
                doDeleteRecords();
            }
        });
        dialog.show();
        dialog.setBtnText("取消", "确定");
        dialog.setContent("是否要清空浏览记录？");
        dialog.setTitle("清空浏览记录");
    }

    private void doDeleteRecords() {
        String userId = Config.getUserId();
        BaseApi.request(BaseApi.createApi(IService.class).delBrowseRecords(userId), new BaseApi.IResponseListener<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                if ("0".equals(data.getError())) {
                    MyLog.e("test", "开始清空了");
                    initRecommendData();
                    adapter.deleteAdapter();
                }
            }

            @Override
            public void onFail() {

            }
        });

    }


    private void initRecommendData() {
        LoadDialog.show(this);
        BaseApi.request(BaseApi.createApi(IService.class).getProducts("clickCount", "desc"),
                new BaseApi.IResponseListener<ProductVo>() {
                    @Override
                    public void onSuccess(ProductVo data) {
                        if ("0".equals(data.getError())) {
                            if (data.getProductList().size() > 2) {
                                products.add(data.getProductList().get(0));
                                products.add(data.getProductList().get(1));
                            } else {
                                products = data.getProductList();
                            }
                            adapter2.upDateAdapter(products);
//                            ScrollDisabledListView.getListViewHeight(adapter2, listView);
                            ll_no_data.setVisibility(View.VISIBLE);
                            ll_recommend.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            ToastUtils.showToast(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });

    }

    @Override
    public void processMessage(Message message) {

    }
}

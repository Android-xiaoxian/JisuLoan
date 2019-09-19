package com.zxy.jisuloan.fragmrnt;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.master.permissionhelper.PermissionHelper;
import com.zxy.jisuloan.R;
import com.zxy.jisuloan.activity.HelpCenterActivity;
import com.zxy.jisuloan.activity.MsgCenterActivity;
import com.zxy.jisuloan.adapter.ProductAdapter;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.LocateUtil;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.PermissionUtils;
import com.zxy.jisuloan.utils.SharePrefsUtils;
import com.zxy.jisuloan.utils.ToastUtils;
import com.zxy.jisuloan.view.dialog.PermissionDialog;
import com.zxy.jisuloan.view.dialog.TipsDialog;
import com.zxy.jisuloan.vo.ProductVo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Create by Fang ShiXian
 * on 2019/8/26
 */
//public class HomeLoginedFragment extends BaseFragment {
public class HomeLoginedFragment extends Fragment implements CustomAdapt {


    private static TextView tv_location;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.et)
    EditText et_search;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_idCard)
    TextView tv_idCard;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.lv_home_logined)
    ListView listView;

    private View view;
    private Unbinder unbinder;
    private ProductAdapter adapter;
    private List<ProductVo.Products> list = null;
    private int curPage = 1;
    private int numPerPage = 2;
    private String condition = "clickCount";//clickCount（智能排序） highPassRate（高通过率）interestRate（低利率）
    private String sort = "desc";//desc(降序 高-->低) asc(低-->高)
    private LocateUtil locateUtil;
    private PermissionHelper permissionHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_hone_logined, null);
            unbinder = ButterKnife.bind(this, view);
            initView();
            initData();
        } else {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }


    private void initView() {
        tv_location = view.findViewById(R.id.tv_location);
        locateUtil = new LocateUtil(getActivity());

        String name = SharePrefsUtils.getInstance().getString(Config.SP_USER_REAL_NAME, "");
        String IDCardNO = SharePrefsUtils.getInstance().getString(Config.SP_USER_IDCARD_NO, "");
        String phone = SharePrefsUtils.getInstance().getString(Config.SP_USER_MOBILEPHONE, "");
        tv_name.setText(name);
        tv_idCard.setText(IDCardNO);
        tv_phone.setText(phone);
        et_search.clearFocus();
        adapter = new ProductAdapter(getContext(), list, 1);
        listView.setAdapter(adapter);

        getPermission(1);//静默定位
    }

    private void initData() {
        getData();
    }

    @OnClick({R.id.tv_location, R.id.img_search, R.id.rl_msg, R.id.ll_kefu,
            R.id.tv_hmdjc, R.id.tv_hmdjc_2, R.id.tv_djdzj, R.id.fkyjy, R.id.yv_zshy,
            R.id.tv_bjfx, R.id.tv_cdtjd, R.id.tv_xyk, R.id.tv_look_more, R.id.btn_check_blacklist,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_location:
                //获取权限并开启定位
                getPermission(0);//主动开启定位
                break;
            case R.id.img_search:
                MyLog.e("test", "搜索");
                break;
            case R.id.rl_msg:
                startActivity(new Intent(getContext(), MsgCenterActivity.class));
                MyLog.e("test", "消息");
                break;
            case R.id.ll_kefu:
                MyLog.e("test", "客服");
                startActivity(new Intent(getContext(), HelpCenterActivity.class));
                break;
            case R.id.tv_hmdjc:
                MyLog.e("test", "黑名单1");
                break;
            case R.id.tv_hmdjc_2:
                MyLog.e("test", "黑名单2");
                break;
            case R.id.tv_djdzj:
                MyLog.e("test", "大家都借");
                break;
            case R.id.fkyjy:
                MyLog.e("test", "反馈建议");
                break;
            case R.id.yv_zshy:
                MyLog.e("test", "钻石会员");
                break;
            case R.id.tv_bjfx:
                MyLog.e("test", "被拒分析");
                break;
            case R.id.tv_cdtjd:
                MyLog.e("test", "多头借贷");
                break;
            case R.id.tv_xyk:
                MyLog.e("test", "信用卡");
                break;
            case R.id.tv_look_more:
                changeFragment();
                break;
            case R.id.btn_check_blacklist:
                MyLog.e("test", "按钮");
                break;


        }
    }

    //切换到借钱fragment
    private void changeFragment() {
        Message message = Message.obtain();
        message.what = Config.CHANGE_TO_BORROW;
        BaseActivity.sendMsg(message);
    }

    private void getData() {
//        LoadDialog.show(getContext());
        BaseApi.request(BaseApi.createApi(IService.class).getProducts(condition, sort),
                new BaseApi.IResponseListener<ProductVo>() {
                    @Override
                    public void onSuccess(ProductVo data) {
                        if (isDestroy) {
                            MyLog.e("test", "网络加载完成，页面已经不存在");
                            return;
                        }
                        if ("0".equals(data.getError())) {
                            list = data.getProductList();
                            adapter.upDateAdapter(list);
                            scrollView.scrollTo(0, 0);
                        } else {
                            ToastUtils.showToast(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
    }

    /**
     * 获取定位权限并开始定位
     */
    public static boolean locating = false;//开始定位到页面跳转有时间延迟，防止多次定位

    private void getPermission(final int type) {
        if (locating) {
            return;
        }
        locating = true;
        permissionHelper = new PermissionHelper(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, Config.PERMISSION_REQUEST_CODE);
        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                //定位后的回调在fragment中onActivityResult不响应
                //在mainActivity中处理

                locateUtil.startLoacte(type);
            }

            @Override
            public void onIndividualPermissionGranted(String[] grantedPermission) {

            }

            @Override
            public void onPermissionDenied() {
                new TipsDialog(getContext(), "您拒绝了定位权限申请").show();
            }

            @Override
            public void onPermissionDeniedBySystem() {
                locating = false;
                if (locateUtil != null) {
                    locateUtil.unRegister();
                }
                final PermissionDialog dialog = new PermissionDialog(getContext());
                dialog.show();
                dialog.setTitle("极速闪贷想要访问定位权限");
                dialog.setContent("您已经关闭定位权限并不再提醒,请手动打开定位权限");
                dialog.setOnClickListenerInterface(new PermissionDialog.OnClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        PermissionUtils.toSelfSetting(getActivity());
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    /**
     * 申请权限回调处理
     * 使用权限插件implementation 'com.master.android:permissionhelper:1.3'
     * permissionHelper类内部处理权限问题
     *
     * @param requestCode  请求权限时传入的参数，前后对应
     * @param permissions  请求权限时传入的权限数组，可同时请求多个权限
     * @param grantResults 对应每个权限的返回结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            MyLog.e("test", "权限回调");
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static void setArea(String area) {
        tv_location.setText(area);
    }


    private boolean isDestroy = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        unbinder.unbind();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locateUtil != null) {
            locateUtil.unRegister();
        }
    }

    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 375;
    }
}

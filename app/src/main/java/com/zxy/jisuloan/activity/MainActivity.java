package com.zxy.jisuloan.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.master.permissionhelper.PermissionHelper;
import com.zxy.jisuloan.R;
import com.zxy.jisuloan.fragmrnt.BorrowFragMent;
import com.zxy.jisuloan.fragmrnt.CreditFragment;
import com.zxy.jisuloan.fragmrnt.HomeFragMent;
import com.zxy.jisuloan.fragmrnt.HomeLoginedFragment;
import com.zxy.jisuloan.fragmrnt.MineFragMent;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.ActivityList;
import com.zxy.jisuloan.utils.AppUtils;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.PermissionUtils;
import com.zxy.jisuloan.utils.SharePrefsUtils;
import com.zxy.jisuloan.utils.StatusBarUtils;
import com.zxy.jisuloan.utils.ToastUtils;
import com.zxy.jisuloan.view.dialog.PermissionDialog;
import com.zxy.jisuloan.view.dialog.PrivacyPolicyDialog;
import com.zxy.jisuloan.view.dialog.UpdateDialog;
import com.zxy.jisuloan.vo.AppInfoVO;
import com.zxy.jisuloan.vo.ProductVo;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloadConfiguration;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.internal.CustomAdapt;


public class MainActivity extends BaseActivity implements CustomAdapt {
    @BindView(R.id.index_img_home)
    ImageView index_img_home;
    @BindView(R.id.index_img_borrow)
    ImageView index_img_borrow;
    @BindView(R.id.index_img_credit)
    ImageView index_img_credit;
    @BindView(R.id.index_img_mine)
    ImageView index_img_mine;
    @BindView(R.id.index_txt_home)
    TextView index_txt_home;
    @BindView(R.id.index_txt_borrow)
    TextView index_txt_borrow;
    @BindView(R.id.index_txt_credit)
    TextView index_txt_credit;
    @BindView(R.id.index_txt_mine)
    TextView index_txt_mine;
    private TextView[] tvs;
    private ImageView[] imgs;
    private int[] img_sel = {R.mipmap.tab_icon_sy_xz, R.mipmap.tab_icon_jq_xz, R.mipmap.tab_icon_xy_xz, R.mipmap.tab_icon_wd_xz};
    private int[] img_unSel = {R.mipmap.tab_icon_sy_wxz, R.mipmap.tab_icon_jq_wxz, R.mipmap.tab_icon_xy_wxz, R.mipmap.tab_icon_wd_wxz};
    private int current = -1;
    private boolean isLogin;
    public static ProductVo.Products products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.with(this).init();
        initData();
        Intent intent = getIntent();
        if (Config.CHANGE_TO_BORROW == intent.getIntExtra(Config.CHANGE_FRAGMENT_BORROW, 0)) {
            changeFragment(new BorrowFragMent(), 1);
        }
        initDownLoad();
        queryNewVersion();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Config.CHANGE_TO_BORROW == intent.getIntExtra(Config.CHANGE_FRAGMENT_BORROW, 0)) {
            changeFragment(new BorrowFragMent(), 1);
        }
        if (intent.getBooleanExtra("isLogin", false)) {
            changeFragment(new HomeLoginedFragment(), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLogin();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void processMessage(Message message) {
        if (message.what == Config.CHANGE_TO_BORROW) {
            changeFragment(new BorrowFragMent(), 1);
        }
    }


    private void initData() {
        index_txt_borrow = findViewById(R.id.index_txt_borrow);
        tvs = new TextView[]{index_txt_home, index_txt_borrow, index_txt_credit, index_txt_mine};
        imgs = new ImageView[]{index_img_home, index_img_borrow, index_img_credit, index_img_mine};
        isLogin = SharePrefsUtils.getInstance().getBoolean(Config.SP_IS_LOGIN, false);
        if (isLogin) {
            changeFragment(new HomeLoginedFragment(), 0);
        } else {
            changeFragment(new HomeFragMent(), 0);
        }

        boolean agree = SharePrefsUtils.getInstance().getBoolean(Config.AGRESS_WITH_PRIVACY_POLICY, false);
        if (!agree) {
            new PrivacyPolicyDialog(this).show();
        }
    }

    @OnClick({R.id.index_home, R.id.index_borrow, R.id.index_credit, R.id.index_mine,})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.index_home:
                isLogin = SharePrefsUtils.getInstance().getBoolean(Config.SP_IS_LOGIN, false);
                if (isLogin) {
                    changeFragment(new HomeLoginedFragment(), 0);
                } else {
                    changeFragment(new HomeFragMent(), 0);
                }
                break;
            case R.id.index_borrow:
                changeFragment(new BorrowFragMent(), 1);
                break;
            case R.id.index_credit:
                changeFragment(new CreditFragment(), 2);
                break;
            case R.id.index_mine:
                changeFragment(new MineFragMent(), 3);
                break;
            default:
                break;
        }
    }

    /**
     * 切换Fragment
     * 并修改底部菜单栏颜色、图片
     *
     * @param targetFragment
     */
    private void changeFragment(Fragment targetFragment, int j) {
        if (current != j) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.index_fragment, targetFragment, "fragment")
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            if (current == -1) {
                current++;
                return;
            }
            tvs[j].setTextColor(getResources().getColor(R.color.text_first));
            tvs[current].setTextColor(getResources().getColor(R.color.text_99));
            imgs[j].setImageResource(img_sel[j]);
            imgs[current].setImageResource(img_unSel[current]);
            current = j;
        }
    }


    /**
     * 判断是否已经登录
     */
    public boolean isLogin() {
        isLogin = SharePrefsUtils.getInstance().getBoolean(Config.SP_IS_LOGIN, false);
        if (isLogin) {//已登录
            long last_login_time = SharePrefsUtils.getInstance().getLong(Config.SP_LAST_LOGIN_TIME, 0);
            if (System.currentTimeMillis() - last_login_time > 7 * 24 * 60 * 60 * 1000) {
                //距离上次使用时间超过7天
                SharePrefsUtils.getInstance().putBoolean(Config.SP_IS_LOGIN, false);
                SharePrefsUtils.getInstance().putLong(Config.SP_LAST_LOGIN_TIME, 0L);
                return false;
            } else {
                SharePrefsUtils.getInstance().putLong(Config.SP_LAST_LOGIN_TIME, System.currentTimeMillis());
                return true;
            }
        } else {//未登录
            return false;
        }
    }


    public void queryNewVersion() {
        BaseApi.request(BaseApi.createApi(IService.class).download(), new BaseApi.IResponseListener<AppInfoVO>() {
            @Override
            public void onSuccess(final AppInfoVO data) {
                if (data != null) {
                    update(data);
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    private String url;
    private UpdateDialog dialog;

    /**
     * 是否升级
     */
    public void update(final AppInfoVO appInfoVO) {
        int version = AppUtils.getLocalVersion(this);
        if (appInfoVO.getVersioncode() > version) {
            dialog = new UpdateDialog(this, appInfoVO.getDescription());
            dialog.setOnClickListenerInterface(new UpdateDialog.OnClickListenerInterface() {
                @Override
                public void doCancel() {
                    dialog.dismiss();
                }

                @Override
                public void doUpdate() {
                    downloadApp(appInfoVO.getDownloadurl());
                    ToastUtils.showToast("正在升级");

                }
            });
            dialog.show();
        }
    }


    private PermissionHelper permissionHelper;

    private void downloadApp(final String urlStr) {
        url = urlStr;
        permissionHelper = new PermissionHelper(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                Config.WRITE_EXTERNAL_STORAGE_CODE);
        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {

                FileDownloader.start(urlStr);
            }

            @Override
            public void onIndividualPermissionGranted(String[] grantedPermission) {

            }

            @Override
            public void onPermissionDenied() {
                showTipsDialog("您拒绝了读写文件的权限");
            }

            @Override
            public void onPermissionDeniedBySystem() {
                final PermissionDialog dialog = new PermissionDialog(MainActivity.this);
                dialog.show();
                dialog.setTitle("极速闪贷想要读写文件权限");
                dialog.setContent("您已经读写文件权限并不再提醒,请手动打开定位权限");
                dialog.setOnClickListenerInterface(new PermissionDialog.OnClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        PermissionUtils.toSelfSetting(MainActivity.this);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 定位页返回后获取位置并显示
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MyLog.e("test", "onActivityResult");
        if (resultCode == Config.RESULT_CODE_1 && requestCode == Config.REQUEST_CODE_1) {
            String area = data.getStringExtra("result");
            area = area.replaceAll("1市辖区1", "")
                    .replaceAll("1县1", "");
//                    .replaceAll("1", "");
            String regex = "1(.*?)1";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(area);
            if (m.find()) {
                HomeLoginedFragment.setArea(m.group().replaceAll("1", ""));
            } else {
                HomeLoginedFragment.setArea(area.substring(0, 3));
            }

        } else if (requestCode == Config.SELF_PERMISSION_REQUEST_CODE) {
            boolean b = permissionHelper.checkSelfPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
            if (b) {
                MyLog.e("test", "有权限了");
                FileDownloader.start(url);
            } else {
                MyLog.e("test", "还是没有权限了");
            }
        } else if (requestCode == Config.REQUEST_CODE_UNKNOWN_APP) {//获取到
            MyLog.e("test", resultCode + "");
            startInstall();
        } else if (requestCode == Config.APP_INSTALL) {
            MyLog.e("test", "resultCode = " + resultCode);
            MyLog.e("test", "用户取消安装");
            ToastUtils.showToast("用户取消安装");
        }

    }

    private DownloadFileInfo mDownloadFileInfo;

    public void initDownLoad() {
        // 1.create FileDownloadConfiguration.Builder
        FileDownloadConfiguration.Builder builder = new FileDownloadConfiguration.Builder(this);

        // 2.config FileDownloadConfiguration.Builder
        builder.configFileDownloadDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                "FileDownloader"); // config the download path
        // allow 3 download tasks at the same time, if not config, default is 2
        builder.configDownloadTaskSize(3);
        // config retry download times when failed, if not config, default is 0
        builder.configRetryDownloadTimes(5);
        // enable debug mode, if not config, default is false
        builder.configDebugMode(true);
        // config connect timeout, if not config, default is 15s
        builder.configConnectTimeout(25000); // 25s

        // 3.init FileDownloader with the configuration
        FileDownloadConfiguration configuration = builder.build(); // build FileDownloadConfiguration with the builder
        FileDownloader.init(configuration);
        FileDownloader.registerDownloadStatusListener(new OnFileDownloadStatusListener() {
            @Override
            public void onFileDownloadStatusWaiting(DownloadFileInfo downloadFileInfo) {

            }

            @Override
            public void onFileDownloadStatusPreparing(DownloadFileInfo downloadFileInfo) {
                MyLog.e("ABD", "onFileDownloadStatusPreparing" + downloadFileInfo.getFileDir());
            }

            @Override
            public void onFileDownloadStatusPrepared(DownloadFileInfo downloadFileInfo) {

            }

            @Override
            public void onFileDownloadStatusDownloading(DownloadFileInfo downloadFileInfo, float downloadSpeed, long remainingTime) {
                MyLog.e("MainActivity", downloadFileInfo.getDownloadedSizeLong() + "");
                int downProgress = (int) (downloadFileInfo.getDownloadedSizeLong() * 100 / downloadFileInfo.getFileSizeLong());
                if (dialog.isShowing()) {
                    dialog.setProgress(downProgress);
                }

            }

            @Override
            public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) {

            }

            @Override
            public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                mDownloadFileInfo = downloadFileInfo;
                if (Build.VERSION.SDK_INT >= 26) {
                    boolean b = getPackageManager().canRequestPackageInstalls();
                    if (b) {
                        startInstall();
                    } else {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                        startActivityForResult(intent, Config.REQUEST_CODE_UNKNOWN_APP);
                        ToastUtils.showToast("请允许极速闪贷安装未知来源应用", 10000);
                    }
                } else {
                    startInstall();
                }
            }

            @Override
            public void onFileDownloadStatusFailed(String url, DownloadFileInfo downloadFileInfo, FileDownloadStatusFailReason failReason) {
                if (downloadFileInfo != null) {
                    MyLog.e("ABD", "onFileDownloadStatusFailed" + downloadFileInfo.getFileDir());
                }
                ToastUtils.showToast("安装包下载失败");
            }
        });
    }

    private void startInstall() {
        File saveFile = new File(mDownloadFileInfo.getFileDir() + File.separator + mDownloadFileInfo.getFileName());
        MyLog.e("tets", mDownloadFileInfo.getFileDir() + mDownloadFileInfo.getFileName());

        Intent install = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) { //判断版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(getApplicationContext(),
                    "com.zxy.kingloan.fileProvider", saveFile);//在AndroidManifest中的android:authorities值
            install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");

        } else {
            install.setDataAndType(Uri.fromFile(saveFile), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivityForResult(install, Config.APP_INSTALL);
    }

    private long lastTime = 0;

    @Override
    public void onBackPressed() {
        MyLog.e("test", "返回键触发了");
        long curTime = System.currentTimeMillis();
        if (curTime - lastTime > 2000) {
            ToastUtils.showToast("再点一次退出应用");
            lastTime = curTime;
        } else {
            ActivityList.tuichu();
            ToastUtils.cancel();
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


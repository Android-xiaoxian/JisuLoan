package com.zxy.jisuloan.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * create by Fang Shixian
 * on 2018/8/20 0020
 * 各类权限判断工具类 ，Android6.0之后对敏感权限做了升级
 * 权限不能在安装时就授权，使用前要请求权限，
 * 为防止用户在请求权限的时候禁止了权限，使用前还要判断是否有权限，
 * 防止无权限导致崩溃
 */
public class PermissionUtils {
    private static PackageManager pm;


    /**
     * 判断相机是否可用
     * 返回true 表示可以使用  返回false表示不可以使用
     */
    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open(0);
            mCamera.setDisplayOrientation(90);
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    /**
     * 判断麦克风是否可用
     */
    public static boolean isHasAudioRecordPermission() {
        // 音频获取源
        int audioSource = MediaRecorder.AudioSource.MIC;
        // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
        int sampleRateInHz = 44100;
        // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
        int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
        // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        // 缓冲区字节大小
        int bufferSizeInBytes = 0;
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                channelConfig, audioFormat);
        AudioRecord audioRecord = new AudioRecord(audioSource, sampleRateInHz,
                channelConfig, audioFormat, bufferSizeInBytes);
        //开始录制音频
        try {
            // 防止某些手机崩溃，例如联想
            audioRecord.startRecording();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        /**
         * 根据开始录音判断是否有录音权限
         */
        if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
            return false;
        }
        audioRecord.stop();
        audioRecord.release();
        audioRecord = null;
        return true;

    }

    /**
     * 判断是否有读取联系人权限
     * 有权限返回true
     * 无权限返回false
     */
    public static boolean isHsaReadContactsPermission(Context context) {
        pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.READ_CONTACTS", context.getPackageName()));
        return permission;
    }

    /**
     * 判断是否有 读取短信 权限
     * 有权限返回true
     * 无权限返回false
     */
    public static boolean isHsaReadSMSPermission(Context context) {
        pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.READ_SMS", context.getPackageName()));
        return permission;
    }

    /**
     * 判断是否有读写文件系统权限
     */
    public static boolean isHsaWriteSDCardPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
    }


    // 100 表示只请求文件系统读写权限
    // 101 请求相机权限和读写文件系统权限
    // 102 请求麦克风权限和文件系统读写权限
    // 103 请求读取联系人权限

    /**
     * 请求读写SD卡权限
     *
     * @param activity
     */
    public static void getWriteSDCardPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                100);
    }


    /**
     * 请求相机权限和读写文件系统权限
     */
    public static void getCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                101);
    }

    /**
     * 请求麦克风权限和文件系统读写权限
     */
    public static void getAudioRecordPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                102);
    }

    /**
     * 请求读取联系人权限
     */
    public static void getReadContacts(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_CONTACTS},
                103);
    }


    /**
     * 获取权限失败跳转到应用信息界面，让用户手动打开权限
     *
     * @param activity
     */
    public static void toSelfSetting(Activity activity) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        mIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
//        context.startActivity(mIntent);
        activity.startActivityForResult(mIntent,Config.SELF_PERMISSION_REQUEST_CODE);
    }
}

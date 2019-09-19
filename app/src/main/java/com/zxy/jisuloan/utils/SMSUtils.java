package com.zxy.jisuloan.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Create by Fang ShiXian
 * on 2019/8/23
 */
public class SMSUtils {


    public static String getSmsInPhone0(Context context) {
        final String SMS_URI_ALL = "content://sms/"; // 所有短信
        Uri SMS_INBOX = Uri.parse(SMS_URI_ALL);
        String[] projection = new String[]{"_id", "address", "person",
                "body", "date", "type",};
        Cursor cur0 = context.getContentResolver().query(SMS_INBOX, projection, null, null, "date desc");
        JSONArray array = new JSONArray();
        JSONObject object0;
        if (null == cur0) {
            Log.i("ooc", "************cur == null");
            return array.toString();
        }

        while (cur0.moveToNext()) {
            String number = cur0.getString(cur0.getColumnIndex("address"));//手机号
            String name = cur0.getString(cur0.getColumnIndex("person"));//联系人姓名列表
            int nameInt = cur0.getInt(cur0.getColumnIndex("person"));//联系人姓名列表
            String body = cur0.getString(cur0.getColumnIndex("body"));//短信内容
            String date = cur0.getString(cur0.getColumnIndex("date"));//日期
            String type = cur0.getString(cur0.getColumnIndex("type"));//发送或接收
            //至此就获得了短信的相关的内容, 以下是把短信加入map中，构建listview,非必要。
            object0 = new JSONObject();
            try {
                object0.put("address", number);
                object0.put("person", name);
                object0.put("personInt", nameInt);
                object0.put("body", body);
                object0.put("date", date);
                object0.put("type", type);
                array.add(object0);
                object0 = null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!cur0.isClosed()) {
            cur0.close();
            cur0 = null;
        }
        return array.toString();
    }

    public static JSONArray getSmsInPhone(Context context) {
        final String SMS_URI_ALL = "content://sms/"; // 所有短信
        final String SMS_URI_INBOX = "content://sms/inbox"; // 收件箱
        final String SMS_URI_SEND = "content://sms/sent"; // 已发送
        final String SMS_URI_DRAFT = "content://sms/draft"; // 草稿
        final String SMS_URI_OUTBOX = "content://sms/outbox"; // 发件箱
        final String SMS_URI_FAILED = "content://sms/failed"; // 发送失败
        final String SMS_URI_QUEUED = "content://sms/queued"; // 待发送列表
        JSONArray array = new JSONArray();
        JSONObject object;

        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type",};
            Cursor cur = context.getContentResolver().query(uri, projection, null,
                    null, "date desc"); // 获取手机内部短信
            // Cursor cur = getContentResolver().query(uri, projection,
            // "read = ?", new String[]{"0"}, "date desc");
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");//联系人号码
//                int index_Person = cur.getColumnIndex("person");//收（发）件人
                int index_Body = cur.getColumnIndex("body");//内容
                int index_Date = cur.getColumnIndex("date");//日期
                int index_Type = cur.getColumnIndex("type");//短信类型
                int i = 0;
                do {
                    String strAddress = cur.getString(index_Address);
//                    int intPerson = cur.getInt(index_Person);
//                    String person = cur.getString(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int intType = cur.getInt(index_Type);

                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(longDate);
                    String strDate = dateFormat.format(d);

                    String strType = "";
                    if (intType == 1) {
                        strType = "接收";
                    } else if (intType == 2) {
                        strType = "发送";
                    } else if (intType == 3) {
                        strType = "草稿";
                    } else if (intType == 4) {
                        strType = "发件箱";
                    } else if (intType == 5) {
                        strType = "发送失败";
                    } else if (intType == 6) {
                        strType = "待发送列表";
                    } else if (intType == 0) {
                        strType = "所有短信";
                    } else {
                        strType = "null";
                    }

//                    SMSVO smsvo = new SMSVO();
//                    smsvo.setAddress(strAddress);
//                    smsvo.setBody(strbody);
//                    smsvo.setDate(strDate);
//                    smsvo.setType(strType);
//                    MyLog.e("test", smsvo.toString());
                    object = new JSONObject();
                    object.put("address", strAddress);
//                    object.put("person", intPerson);
//                    object.put("personName", person);
                    object.put("body", strbody);
                    object.put("date", strDate);
                    object.put("type", strType);
                    array.add(object);
//                    i++;
//                    if (i == 3) {
//                        break;
//                    }
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            }

        } catch (SQLiteException ex) {
            MyLog.e("SQLiteException in getSmsInPhone", ex.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String listStr = array.toString();
        MyLog.e("test", "list = " + listStr);
        return array;
    }
}

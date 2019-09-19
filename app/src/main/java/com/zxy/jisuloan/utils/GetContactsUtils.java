package com.zxy.jisuloan.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zxy.jisuloan.view.dialog.TipsDialog;
import com.zxy.jisuloan.vo.PhoneInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Fang ShiXian
 * on 2019/8/20
 */
public class GetContactsUtils {

    /**
     * 获取全部通讯录
     *
     * @param context
     * @return
     */
    public static JSONArray getPhoneNumberFromMobile(Context context) {
        // TODO Auto-generated constructor stub
        List<PhoneInfo> list1 = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{"display_name", "sort_key", "contact_id",
                        "data1"}, null, null, null);
//        moveToNext方法返回的是一个boolean类型的数据
        JSONArray array = new JSONArray();
        JSONObject object = null;
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            int Id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            String Sortkey = getSortkey(cursor.getString(1));
            if (name.contains(" ")) {
                name = name.replaceAll(" ", "");
            }
            if (number.contains(" ")) {
                number = number.replaceAll(" ", "");
            }
//            PhoneInfo phoneInfo = new PhoneInfo(name, number, Sortkey, Id);
//            list1.add(phoneInfo);
            object = new JSONObject();
            try {
                object.put("name", name);
                object.put("phoneNo", number);
                array.add(object);
                object = null;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //只取300条
//            if (list1.size() > 300) {
//                cursor.close();
//            }
        }
        cursor.close();

        //将list1转换成json字符串
//        JSONArray array = new JSONArray();
//        JSONObject object = null;
//        for (int i = 0; i < list1.size(); i++) {
//            try {
//                object = new JSONObject();
//                object.put("name", list1.get(i).getName());
//                object.put("phoneNo", list1.get(i).getPhoneNo());
////                for (int j = 0; j < 200; j++) {
////                    array.put(object);
////                }
//                object = null;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        MyLog.e("test", "长度 = " + array.size());
        String dataStr = array.toString();

        MyLog.e("test", "通讯录 = " + dataStr);

        return array;
    }


    private static String getSortkey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        } else
            return "#";
    }

    /**
     * 判断是否表情符号
     *
     * @param codePoint
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }


    /**
     * 获取单个联系人姓名和号码
     *
     * @param context
     * @param data
     * @return
     */
    public static String[] getSigleContact(Context context, Intent data) {

        if (data == null) {
            return null;
        }
        Uri contactData = data.getData();
        if (contactData == null) {
            return null;
        }
        Cursor cursor = context.getContentResolver().query(contactData, null, null, null, null);
        String name = "";
        String number = "";
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            if (hasPhone.equalsIgnoreCase("1")) {
                hasPhone = "true";
            } else {
                hasPhone = "flase";
            }
            if (Boolean.parseBoolean(hasPhone)) {
                Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                        null, null);
                if (phones.moveToFirst()) {
                    number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            .replaceAll(" ", "")
                            .replaceAll("-", "")
                            .replace("+86", "");
                    if (!AccountValidatorUtils.isMobile(number)) {
                        new TipsDialog(context, "未获取到有效手机号码").show();
                        return null;
                    }
                }
                phones.close();
            } else {
                new TipsDialog(context, "未获取到手机号码").show();
                return null;
            }
        }
        String[] strings = new String[]{name,number};
        return strings;
    }
}
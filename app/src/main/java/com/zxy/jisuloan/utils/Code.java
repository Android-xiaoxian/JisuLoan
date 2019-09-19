package com.zxy.jisuloan.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * 图形验证码工具类
 */
public class Code {

    //随机数数组
    private static final char[] CHARS = {
            '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'
//            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm',  
//            'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',  
//            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',  
//            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'  
    };

    private static Code bmpCode;

    public static Code getInstance() {
        if (bmpCode == null)
            bmpCode = new Code();
        return bmpCode;
    }

    //default settings  
    //验证码默认随机数的个数  
    private static final int DEFAULT_CODE_LENGTH = 4;
    //默认字体大小  
    private static final int DEFAULT_FONT_SIZE = ViewUtil.dp2px(20);
    //默认线条的条数  
    private static final int DEFAULT_LINE_NUMBER = 2;
    //padding值  用dp转换成px，直接用px回在低像素手机偏移过大
    private static final int BASE_PADDING_LEFT = ViewUtil.dp2px(10),
            RANGE_PADDING_LEFT = ViewUtil.dp2px(10),
            BASE_PADDING_TOP = ViewUtil.dp2px(15),
            RANGE_PADDING_TOP = ViewUtil.dp2px(7);
    //验证码的默认宽高  
    private static final int DEFAULT_WIDTH = ViewUtil.dp2px(90), DEFAULT_HEIGHT = ViewUtil.dp2px(23);

    //settings decided by the layout xml  
    //canvas width and height  
    private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;

    //random word space and pading_top  
    private int base_padding_left = BASE_PADDING_LEFT, range_padding_left = RANGE_PADDING_LEFT,
            base_padding_top = BASE_PADDING_TOP, range_padding_top = RANGE_PADDING_TOP;

    //number of chars, lines; font size  
    private int codeLength = DEFAULT_CODE_LENGTH, line_number = DEFAULT_LINE_NUMBER, font_size = DEFAULT_FONT_SIZE;

    //variables  
    private String code;
    private int padding_left, padding_top;
    private Random random = new Random();

    //验证码图片
    public Bitmap createBitmap() {
        //每次刷新验证码把padding_left初始值改回来，
        // 上一次生成验证码的时候padding_left的值已经叠加上去
        padding_left = ViewUtil.dp2px(5);
        Bitmap bp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas c = new Canvas(bp);

        code = createCode();
        //画布背景色
        c.drawColor(Color.TRANSPARENT);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(font_size);
        //画验证码  
        for (int i = 0; i < code.length(); i++) {
            randomTextStyle(paint);
            randomPadding();
            //padding_left文字距离左侧长度  padding_top
            c.drawText(code.charAt(i) + "", padding_left, padding_top, paint);
//            if (i == 0){
//                //测试文字位置，y偏移量在15-22dp之间
//                c.drawText(code.charAt(i) + "", 0, ViewUtil.dp2px(22), paint);
//            }

        }
        //画线条  
        for (int i = 0; i < line_number; i++) {
            drawLine(c, paint);
        }

//        c.save( Canvas.ALL_SAVE_FLAG );//保存
        c.save();//保存
        c.restore();//
        return bp;
    }

    public String getCode() {
        return code;
    }

    //生成验证码  
    private String createCode() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }

    //画干扰线  
    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = random.nextInt(width);
        int startY = random.nextInt(height);
        int stopX = random.nextInt(width);
        int stopY = random.nextInt(height);
        paint.setStrokeWidth(2);
//        paint.setColor(color);
        paint.setColor(Color.parseColor("#1C1C1C"));
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    //生成随机颜色  
    private int randomColor() {
        return randomColor(1);
    }

    private int randomColor(int rate) {
        int red = random.nextInt(256) / rate;
        int green = random.nextInt(256) / rate;
        int blue = random.nextInt(256) / rate;
        return Color.rgb(red, green, blue);
    }

    //随机生成文字样式，颜色，粗细，倾斜度  
    private void randomTextStyle(Paint paint) {
//        int color = randomColor();//随机颜色
//        paint.setColor(color);
        paint.setColor(Color.parseColor("#1314C4"));
//        paint.setFakeBoldText(random.nextBoolean());  //true为粗体，false为非粗体
        paint.setFakeBoldText(true);  //true为粗体，false为非粗体
        float skewX = random.nextInt(11) / 10;
        skewX = random.nextBoolean() ? skewX : -skewX;
//        paint.setTextSkewX(skewX); //float类型参数，负数表示右斜，整数左斜
        //paint.setUnderlineText(true); //true为下划线，false为非下划线  
        //paint.setStrikeThruText(true); //true为删除线，false为非删除线  
    }

    //随机生成padding值  
    private void randomPadding() {
        padding_left += base_padding_left + random.nextInt(range_padding_left);
        padding_top = base_padding_top + random.nextInt(range_padding_top);
        MyLog.e("test", "padding_left = " + padding_left + "   padding_top = " + padding_top);
    }

}

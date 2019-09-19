package com.zxy.jisuloan.aatest;

/**
 * Create by Fang ShiXian
 * on 2019/8/23
 */
public class textjava {

    public static void main(String[] args) {
        String s = "String";
        String s1 = "s";
        int a = 100, b = 1000;
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println("a = " + a + "\nb = " + b);
    }

}

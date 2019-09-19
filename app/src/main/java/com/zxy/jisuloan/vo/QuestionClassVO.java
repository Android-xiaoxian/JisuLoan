package com.zxy.jisuloan.vo;

import java.util.List;

/**
 * Create by Fang ShiXian
 * on 2019/9/6
 */
public class QuestionClassVO {

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "QuestionClassVO{" +
                "data=" + data +
                '}';
    }

    public static class Data{
        private String classname;
        private String icon;
        private String id;

        public String getClassname() {
            return classname;
        }

        public String getIcon() {
            return icon;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "classname='" + classname + '\'' +
                    ", icon='" + icon + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }
}

package com.zxy.jisuloan.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Create by Fang ShiXian
 * on 2019/9/9
 */
public class QuestionVO implements Serializable{

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "QuestionVO{" +
                "data=" + data +
                '}';
    }


    public static class Data implements Serializable {
        private String answer;
        private String classname;
        private String problem;
        private String hot;
        private String id;

        public String getAnswer() {
            return answer;
        }

        public String getClassname() {
            return classname;
        }

        public String getProblem() {
            return problem;
        }

        public String getHot() {
            return hot;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "answer='" + answer + '\'' +
                    ", classname='" + classname + '\'' +
                    ", problem='" + problem + '\'' +
                    ", hot='" + hot + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }
}

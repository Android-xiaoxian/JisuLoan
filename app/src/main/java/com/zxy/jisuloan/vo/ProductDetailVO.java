package com.zxy.jisuloan.vo;

/**
 * Create by Fang ShiXian
 * on 2019/9/12
 */
public class ProductDetailVO {
    private Data data;

    public Data getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ProductDetailVO{" +
                "data=" + data +
                '}';
    }

    public class Data {
        private String conditions;
        private String materials;
        private String explains;
        private String organization;

//    "conditions": "申请条件",
////            "materials": "所需材料",
////            "explains": "说明",
////            "organization": "机构信息"

        public String getConditions() {
            return conditions;
        }

        public String getMaterials() {
            return materials;
        }

        public String getExplains() {
            return explains;
        }

        public String getOrganization() {
            return organization;
        }

        @Override
        public String toString() {
            return "ProductDetailVO{" +
                    "conditions='" + conditions + '\'' +
                    ", materials='" + materials + '\'' +
                    ", explains='" + explains + '\'' +
                    ", organization='" + organization + '\'' +
                    '}';
        }
    }
}

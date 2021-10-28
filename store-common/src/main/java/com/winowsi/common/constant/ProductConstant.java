package com.winowsi.common.constant;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/11 17:47
 */

public class ProductConstant {
    public enum AttrEnum{
        /**
         * 规格参数
         *
         */
        ATTR_TYPE_BASE("base",1),
        /**
         *  销售属性
         */
        ATTR_TYPE_SALE("sale",0);

        private String meg;
        private Integer code;

        AttrEnum(String meg, Integer code) {
            this.meg = meg;
            this.code = code;
        }

        public String getMeg() {
            return meg;
        }

        public void setMeg(String meg) {
            this.meg = meg;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }
    }
    public enum StatusEnum{
        /**
         * 新建商品
         *
         */
        NEW_SPU("新建商品",0),
        /**
         *  上架商品
         */
        UP_SPU("上架商品",1),
        /**
         *  下架商品
         */
        DOWN_SPU("下架商品",2);


        private String meg;
        private Integer code;

        StatusEnum(String meg, Integer code) {
            this.meg = meg;
            this.code = code;
        }

        public String getMeg() {
            return meg;
        }

        public void setMeg(String meg) {
            this.meg = meg;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }
    }
}

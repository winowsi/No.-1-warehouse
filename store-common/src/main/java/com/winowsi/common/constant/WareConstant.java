package com.winowsi.common.constant;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/11 17:47
 */

public class WareConstant {
    public enum purchaseStatusEnum{
        /**
         * 新建
         *
         */
        CREATED("新建",0),
        /**
         *  已分配
         */
        ASSIGNED("已分配",1),
        /**
         *  已领取
         */
        RECEIVE("已领取",2),
        /**
         *  已完成
         */
        FINISH("已完成",3),
        /**
         *  异常
         */
        HAS_ERROR("异常",4);


        private String meg;
        private Integer code;

        purchaseStatusEnum(String meg, Integer code) {
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
    public enum purchaseDetailEnum{
        /**
         * 新建
         *
         */
        CREATED("新建",0),
        /**
         *  已分配
         */
        ASSIGNED("已分配",1),
        /**
         *  已领取
         */
        BUYING("正在采购",2),
        /**
         *  已完成
         */
        FINISH("已完成",3),
        /**
         *  异常
         */
        HAS_ERROR("采购失败",4);


        private String meg;
        private Integer code;

        purchaseDetailEnum(String meg, Integer code) {
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

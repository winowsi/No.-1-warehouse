/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.56.128_3306
 Source Server Type    : MySQL
 Source Server Version : 50735
 Source Host           : 192.168.56.128:3306
 Source Schema         : store_wms

 Target Server Type    : MySQL
 Target Server Version : 50735
 File Encoding         : 65001

 Date: 19/10/2021 17:12:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wms_purchase
-- ----------------------------
DROP TABLE IF EXISTS `wms_purchase`;
CREATE TABLE `wms_purchase`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '采购单id',
  `assignee_id` bigint(20) NULL DEFAULT NULL COMMENT '采购人id',
  `assignee_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '采购人名',
  `phone` char(13) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '联系方式',
  `priority` int(4) NULL DEFAULT NULL COMMENT '优先级',
  `status` int(4) NULL DEFAULT NULL COMMENT '状态',
  `ware_id` bigint(20) NULL DEFAULT NULL COMMENT '仓库id',
  `amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '总金额',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '采购信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_purchase
-- ----------------------------
INSERT INTO `wms_purchase` VALUES (5, 1, 'admin', '13612345678', 1, 2, NULL, NULL, '2021-10-18 10:11:58', '2021-10-18 10:12:48');

-- ----------------------------
-- Table structure for wms_purchase_detail
-- ----------------------------
DROP TABLE IF EXISTS `wms_purchase_detail`;
CREATE TABLE `wms_purchase_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `purchase_id` bigint(20) NULL DEFAULT NULL COMMENT '采购单id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT '采购商品id',
  `sku_num` int(11) NULL DEFAULT NULL COMMENT '采购数量',
  `sku_price` decimal(18, 4) NULL DEFAULT NULL COMMENT '采购金额',
  `ware_id` bigint(20) NULL DEFAULT NULL COMMENT '仓库id',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态[0新建，1已分配，2正在采购，3已完成，4采购失败]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_purchase_detail
-- ----------------------------
INSERT INTO `wms_purchase_detail` VALUES (7, 5, 1, 1, NULL, 1, 1);
INSERT INTO `wms_purchase_detail` VALUES (8, 5, 2, 2, NULL, 1, 1);

-- ----------------------------
-- Table structure for wms_ware_info
-- ----------------------------
DROP TABLE IF EXISTS `wms_ware_info`;
CREATE TABLE `wms_ware_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '仓库名',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '仓库地址',
  `areacode` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '区域编码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '仓库信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_ware_info
-- ----------------------------
INSERT INTO `wms_ware_info` VALUES (1, '1号仓', '成都', '366441');

-- ----------------------------
-- Table structure for wms_ware_order_task
-- ----------------------------
DROP TABLE IF EXISTS `wms_ware_order_task`;
CREATE TABLE `wms_ware_order_task`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT 'order_id',
  `order_sn` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'order_sn',
  `consignee` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '收货人',
  `consignee_tel` char(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '收货人电话',
  `delivery_address` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '配送地址',
  `order_comment` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '订单备注',
  `payment_way` tinyint(1) NULL DEFAULT NULL COMMENT '付款方式【 1:在线付款 2:货到付款】',
  `task_status` tinyint(2) NULL DEFAULT NULL COMMENT '任务状态',
  `order_body` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '订单描述',
  `tracking_no` char(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '物流单号',
  `create_time` datetime NULL DEFAULT NULL COMMENT 'create_time',
  `ware_id` bigint(20) NULL DEFAULT NULL COMMENT '仓库id',
  `task_comment` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工作单备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '库存工作单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_ware_order_task
-- ----------------------------

-- ----------------------------
-- Table structure for wms_ware_order_task_detail
-- ----------------------------
DROP TABLE IF EXISTS `wms_ware_order_task_detail`;
CREATE TABLE `wms_ware_order_task_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'sku_id',
  `sku_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'sku_name',
  `sku_num` int(11) NULL DEFAULT NULL COMMENT '购买个数',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '工作单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '库存工作单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_ware_order_task_detail
-- ----------------------------

-- ----------------------------
-- Table structure for wms_ware_sku
-- ----------------------------
DROP TABLE IF EXISTS `wms_ware_sku`;
CREATE TABLE `wms_ware_sku`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'sku_id',
  `ware_id` bigint(20) NULL DEFAULT NULL COMMENT '仓库id',
  `stock` int(11) NULL DEFAULT NULL COMMENT '库存数',
  `sku_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'sku_name',
  `stock_locked` int(11) NULL DEFAULT NULL COMMENT '锁定库存',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '商品库存' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_ware_sku
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

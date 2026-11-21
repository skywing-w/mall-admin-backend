-- ============================================================
-- 电商后台管理系统 数据库初始化脚本
-- 适用：MySQL 8.x
-- 数据库：mall_admin
-- 说明：本脚本包含 14 张表 + 索引，幂等（先 DROP 再 CREATE）
-- ============================================================

DROP DATABASE IF EXISTS mall_admin;
CREATE DATABASE mall_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE mall_admin;

-- ------------------------------------------------------------
-- 1. 后台操作员 / 角色 / 部门（RBAC）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    VARCHAR(50)  NOT NULL COMMENT '登录名',
    `password`    VARCHAR(100) NOT NULL COMMENT '密码（MD5）',
    `name`        VARCHAR(50)  DEFAULT NULL COMMENT '姓名',
    `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar`      VARCHAR(255) DEFAULT NULL COMMENT '头像 URL',
    `role_id`     BIGINT       DEFAULT NULL COMMENT '角色 ID',
    `dept_id`     BIGINT       DEFAULT NULL COMMENT '部门 ID',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    `remark`      VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='后台操作员';

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(50)  NOT NULL COMMENT '角色名称',
    `label`       VARCHAR(50)  NOT NULL COMMENT '权限标识（如 admin / operator）',
    `remark`      VARCHAR(255) DEFAULT NULL,
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_label` (`label`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='角色';

DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(50)  NOT NULL,
    `parent_id`   BIGINT       NOT NULL DEFAULT 0 COMMENT '父部门 ID，0 表示顶级',
    `sort`        INT          NOT NULL DEFAULT 0,
    `status`      TINYINT      NOT NULL DEFAULT 1,
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='部门（树形）';

-- ------------------------------------------------------------
-- 2. 单表业务：分类 / 商品 / 轮播图 / 优惠券
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(50)  NOT NULL,
    `parent_id`   BIGINT       NOT NULL DEFAULT 0,
    `icon`        VARCHAR(255) DEFAULT NULL,
    `sort`        INT          NOT NULL DEFAULT 0,
    `status`      TINYINT      NOT NULL DEFAULT 1,
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='商品分类（树形）';

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
    `id`          BIGINT        NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(100)  NOT NULL COMMENT '商品名称',
    `category_id` BIGINT        NOT NULL COMMENT '分类 ID',
    `cover`       VARCHAR(255)  DEFAULT NULL COMMENT '封面图',
    `price`       DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '售价',
    `stock`       INT           NOT NULL DEFAULT 0 COMMENT '库存',
    `sales`       INT           NOT NULL DEFAULT 0 COMMENT '累计销量',
    `status`      TINYINT       NOT NULL DEFAULT 1 COMMENT '0-下架 1-上架',
    `description` TEXT          COMMENT '商品描述',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='商品';

DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `title`       VARCHAR(100) NOT NULL,
    `image`       VARCHAR(255) NOT NULL,
    `link`        VARCHAR(255) DEFAULT NULL,
    `sort`        INT          NOT NULL DEFAULT 0,
    `status`      TINYINT      NOT NULL DEFAULT 1,
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='首页轮播图';

DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon` (
    `id`          BIGINT        NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(100)  NOT NULL COMMENT '优惠券名称',
    `type`        TINYINT       NOT NULL COMMENT '1-满减 2-折扣',
    `discount`    DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '满减金额 / 折扣（如 0.85）',
    `min_amount`  DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '使用门槛',
    `total`       INT           NOT NULL DEFAULT 0 COMMENT '发行量',
    `used`        INT           NOT NULL DEFAULT 0 COMMENT '已使用',
    `start_time`  DATETIME      NOT NULL,
    `end_time`    DATETIME      NOT NULL,
    `status`      TINYINT       NOT NULL DEFAULT 1,
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='优惠券';

-- ------------------------------------------------------------
-- 3. 多表业务：C 端用户 / 收货地址 / 购物车 / 订单 / 订单项 / 评价
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `c_user`;
CREATE TABLE `c_user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'C 端买家 ID',
    `open_id`     VARCHAR(64)  DEFAULT NULL COMMENT '微信 openId',
    `nickname`    VARCHAR(50)  NOT NULL,
    `phone`       VARCHAR(20)  DEFAULT NULL,
    `avatar`      VARCHAR(255) DEFAULT NULL,
    `gender`      TINYINT      NOT NULL DEFAULT 0 COMMENT '0-未知 1-男 2-女',
    `status`      TINYINT      NOT NULL DEFAULT 1,
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_phone` (`phone`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='C 端买家';

DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `user_id`     BIGINT       NOT NULL,
    `receiver`    VARCHAR(50)  NOT NULL,
    `phone`       VARCHAR(20)  NOT NULL,
    `province`    VARCHAR(50)  NOT NULL,
    `city`        VARCHAR(50)  NOT NULL,
    `district`    VARCHAR(50)  NOT NULL,
    `detail`      VARCHAR(255) NOT NULL,
    `is_default`  TINYINT      NOT NULL DEFAULT 0,
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='收货地址';

DROP TABLE IF EXISTS `cart_item`;
CREATE TABLE `cart_item` (
    `id`          BIGINT   NOT NULL AUTO_INCREMENT,
    `user_id`     BIGINT   NOT NULL,
    `product_id`  BIGINT   NOT NULL,
    `quantity`    INT      NOT NULL DEFAULT 1,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_product` (`user_id`, `product_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='购物车';

DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
    `id`           BIGINT        NOT NULL AUTO_INCREMENT,
    `order_no`     VARCHAR(32)   NOT NULL COMMENT '订单号',
    `user_id`      BIGINT        NOT NULL,
    `address_id`   BIGINT        NOT NULL,
    `total_amount` DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    `pay_amount`   DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    `status`       TINYINT       NOT NULL DEFAULT 0 COMMENT '0-待支付 1-待发货 2-待收货 3-已完成 4-已取消 5-退款中 6-已退款',
    `pay_time`     DATETIME      DEFAULT NULL,
    `remark`       VARCHAR(500)  DEFAULT NULL,
    `create_time`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='订单';

DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
    `id`            BIGINT        NOT NULL AUTO_INCREMENT,
    `order_id`      BIGINT        NOT NULL,
    `product_id`    BIGINT        NOT NULL,
    `product_name`  VARCHAR(100)  NOT NULL COMMENT '冗余商品名',
    `product_image` VARCHAR(255)  DEFAULT NULL,
    `price`         DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    `quantity`      INT           NOT NULL DEFAULT 1,
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_product_id` (`product_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='订单项';

DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `user_id`     BIGINT       NOT NULL,
    `product_id`  BIGINT       NOT NULL,
    `order_id`    BIGINT       NOT NULL,
    `rating`      TINYINT      NOT NULL DEFAULT 5 COMMENT '1-5 星',
    `content`     VARCHAR(500) DEFAULT NULL,
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_product_id` (`product_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='评价';

-- ------------------------------------------------------------
-- 4. 操作日志
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `operate_log`;
CREATE TABLE `operate_log` (
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `user_id`    BIGINT       DEFAULT NULL,
    `username`   VARCHAR(50)  DEFAULT NULL,
    `module`     VARCHAR(50)  DEFAULT NULL COMMENT '模块',
    `type`       VARCHAR(50)  DEFAULT NULL COMMENT '操作类型',
    `class_name` VARCHAR(255) DEFAULT NULL,
    `method`     VARCHAR(255) DEFAULT NULL,
    `params`     TEXT         COMMENT '请求参数',
    `result`     TEXT         COMMENT '返回结果',
    `cost_time`  BIGINT       DEFAULT NULL COMMENT '耗时 ms',
    `ip`         VARCHAR(50)  DEFAULT NULL,
    `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='操作日志';

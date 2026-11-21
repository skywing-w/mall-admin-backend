-- ============================================================
-- 种子数据脚本
-- 适用：MySQL 8.x
-- 说明：包含 1 个管理员、1 个角色、2 个部门、5 个分类、20 个商品、
--      3 个 C 端用户、3 个收货地址、10 个订单（含订单项）、3 条评价
-- 默认管理员账号：admin / 123456（密码 = MD5("admin123456")）
-- ============================================================

USE mall_admin;

-- -------------------- 后台基础 --------------------
INSERT INTO `role` (id, name, label, remark) VALUES
    (1, '平台管理员', 'admin', '拥有全部权限'),
    (2, '运营', 'operator', '负责商品 / 订单');

INSERT INTO `dept` (id, name, parent_id, sort, status) VALUES
    (1, '总公司', 0, 1, 1),
    (2, '运营部', 1, 1, 1),
    (3, '技术部', 1, 2, 1);

-- 默认密码 123456 的 MD5 = e10adc3949ba59abbe56e057f20f883e
INSERT INTO `user` (id, username, password, name, phone, email, role_id, dept_id, status) VALUES
    (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', '13800000000', 'admin@mall.com', 1, 1, 1),
    (2, 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '张三', '13800000001', 'zs@mall.com', 2, 2, 1);

-- -------------------- 商品分类（树形） --------------------
INSERT INTO `category` (id, name, parent_id, sort, status) VALUES
    (1, '手机数码', 0, 1, 1),
    (2, '手机',     1, 1, 1),
    (3, '笔记本',   0, 2, 1),
    (4, '服装鞋帽', 0, 3, 1),
    (5, '家用电器', 0, 4, 1);

-- -------------------- 商品 --------------------
INSERT INTO `product` (id, name, category_id, cover, price, stock, sales, status, description) VALUES
    (1,  'iPhone 15 Pro',       2, 'https://picsum.photos/seed/p1/200/200', 7999.00, 100, 50, 1, 'Apple 最新旗舰手机'),
    (2,  '华为 Mate60',         2, 'https://picsum.photos/seed/p2/200/200', 5999.00, 200, 80, 1, '华为旗舰'),
    (3,  '小米 14',             2, 'https://picsum.photos/seed/p3/200/200', 3999.00, 300, 120, 1, '小米年度旗舰'),
    (4,  'MacBook Pro 14',      3, 'https://picsum.photos/seed/p4/200/200', 14999.00, 50, 20, 1, 'M3 Pro 芯片'),
    (5,  'ThinkPad X1',         3, 'https://picsum.photos/seed/p5/200/200', 12999.00, 80, 30, 1, '商务笔记本'),
    (6,  '戴尔 XPS 15',         3, 'https://picsum.photos/seed/p6/200/200', 13999.00, 60, 25, 1, '轻薄高性能'),
    (7,  'Nike 跑步鞋',         4, 'https://picsum.photos/seed/p7/200/200', 699.00, 500, 200, 1, '舒适透气'),
    (8,  'Adidas T 恤',         4, 'https://picsum.photos/seed/p8/200/200', 199.00, 800, 350, 1, '纯棉舒适'),
    (9,  '优衣库 牛仔裤',       4, 'https://picsum.photos/seed/p9/200/200', 299.00, 600, 220, 1, '经典款'),
    (10, '海澜之家 衬衫',       4, 'https://picsum.photos/seed/p10/200/200', 259.00, 400, 150, 1, '商务休闲'),
    (11, '美的 空调',           5, 'https://picsum.photos/seed/p11/200/200', 2999.00, 150, 60, 1, '1.5 匹一级能效'),
    (12, '格力 空调',           5, 'https://picsum.photos/seed/p12/200/200', 3199.00, 120, 50, 1, '变频冷暖'),
    (13, '海尔 洗衣机',         5, 'https://picsum.photos/seed/p13/200/200', 1999.00, 200, 80, 1, '10 公斤大容量'),
    (14, '西门子 冰箱',         5, 'https://picsum.photos/seed/p14/200/200', 4999.00, 80, 30, 1, '双开门'),
    (15, '小米电视 65 寸',      5, 'https://picsum.photos/seed/p15/200/200', 2599.00, 150, 70, 1, '4K 全面屏'),
    (16, 'iPad Air',            2, 'https://picsum.photos/seed/p16/200/200', 4799.00, 100, 40, 1, 'M2 芯片'),
    (17, 'AirPods Pro 2',       2, 'https://picsum.photos/seed/p17/200/200', 1899.00, 300, 150, 1, '主动降噪'),
    (18, 'Apple Watch',         2, 'https://picsum.photos/seed/p18/200/200', 3199.00, 200, 80, 1, 'S9 芯片'),
    (19, 'Puma 运动裤',         4, 'https://picsum.photos/seed/p19/200/200', 399.00, 350, 100, 1, '透气速干'),
    (20, '海尔 热水器',         5, 'https://picsum.photos/seed/p20/200/200', 1299.00, 180, 60, 1, '60 升电热水器');

-- -------------------- 轮播图 --------------------
INSERT INTO `banner` (title, image, link, sort, status) VALUES
    ('双 11 大促',  'https://picsum.photos/seed/b1/1200/400', '/activity/double11', 1, 1),
    ('新品首发',    'https://picsum.photos/seed/b2/1200/400', '/product/new',       2, 1),
    ('品牌特卖',    'https://picsum.photos/seed/b3/1200/400', '/brand/sale',        3, 1);

-- -------------------- 优惠券 --------------------
INSERT INTO `coupon` (name, type, discount, min_amount, total, used, start_time, end_time, status) VALUES
    ('满 1000 减 100', 1, 100.00, 1000.00, 1000, 50, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 1),
    ('满 500 减 50',   1, 50.00,  500.00,  2000, 200, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 1),
    ('9 折优惠券',     2, 0.90,   0.00,    500,  30,  '2026-01-01 00:00:00', '2026-12-31 23:59:59', 1);

-- -------------------- C 端用户 --------------------
INSERT INTO `c_user` (id, nickname, phone, avatar, gender, status) VALUES
    (1, '小明',   '13900000001', 'https://picsum.photos/seed/u1/100/100', 1, 1),
    (2, '小红',   '13900000002', 'https://picsum.photos/seed/u2/100/100', 2, 1),
    (3, '小李',   '13900000003', 'https://picsum.photos/seed/u3/100/100', 1, 1);

-- -------------------- 收货地址 --------------------
INSERT INTO `address` (user_id, receiver, phone, province, city, district, detail, is_default) VALUES
    (1, '王明', '13900000001', '北京市', '北京市', '朝阳区', '建国路 88 号', 1),
    (2, '李红', '13900000002', '上海市', '上海市', '浦东新区', '世纪大道 100 号', 1),
    (3, '赵丽', '13900000003', '广东省', '深圳市', '南山区', '科技园路 1 号', 1);

-- -------------------- 订单 + 订单项（10 单） --------------------
-- 为保证统计效果，订单分布于不同日期、不同状态
INSERT INTO `order` (id, order_no, user_id, address_id, total_amount, pay_amount, status, pay_time, remark, create_time) VALUES
    (1,  'OD202608010001', 1, 1, 7999.00,  7899.00, 3, '2026-08-01 10:30:00', '尽快发货', '2026-08-01 10:00:00'),
    (2,  'OD202608010002', 2, 2, 14999.00, 14899.00, 3, '2026-08-01 14:00:00', NULL, '2026-08-01 13:30:00'),
    (3,  'OD202608020001', 1, 1, 699.00,   649.00,   3, '2026-08-02 09:00:00', NULL, '2026-08-02 08:30:00'),
    (4,  'OD202608020002', 3, 3, 2999.00,  2949.00,  2, '2026-08-02 11:00:00', NULL, '2026-08-02 10:30:00'),
    (5,  'OD202608020003', 2, 2, 399.00,   399.00,   1, '2026-08-02 15:00:00', NULL, '2026-08-02 14:30:00'),
    (6,  'OD202608020004', 1, 1, 1899.00,  1849.00,  0, NULL, NULL, '2026-08-02 16:00:00'),
    (7,  'OD202608020005', 3, 3, 199.00,   199.00,   4, NULL, '暂时不需要', '2026-08-02 17:00:00'),
    (8,  'OD202608020006', 2, 2, 4799.00,  4799.00,  0, NULL, NULL, '2026-08-02 18:00:00'),
    (9,  'OD202608020007', 1, 1, 5999.00,  5999.00,  1, '2026-08-02 19:00:00', NULL, '2026-08-02 18:30:00'),
    (10, 'OD202608020008', 3, 3, 1299.00,  1299.00,  2, '2026-08-02 20:00:00', NULL, '2026-08-02 19:30:00');

INSERT INTO `order_item` (order_id, product_id, product_name, product_image, price, quantity) VALUES
    (1, 1,  'iPhone 15 Pro',     'https://picsum.photos/seed/p1/200/200', 7999.00, 1),
    (2, 4,  'MacBook Pro 14',    'https://picsum.photos/seed/p4/200/200', 14999.00, 1),
    (3, 7,  'Nike 跑步鞋',       'https://picsum.photos/seed/p7/200/200', 699.00, 1),
    (4, 11, '美的 空调',         'https://picsum.photos/seed/p11/200/200', 2999.00, 1),
    (5, 19, 'Puma 运动裤',       'https://picsum.photos/seed/p19/200/200', 399.00, 1),
    (6, 17, 'AirPods Pro 2',     'https://picsum.photos/seed/p17/200/200', 1899.00, 1),
    (7, 8,  'Adidas T 恤',       'https://picsum.photos/seed/p8/200/200', 199.00, 1),
    (8, 16, 'iPad Air',          'https://picsum.photos/seed/p16/200/200', 4799.00, 1),
    (9, 2,  '华为 Mate60',       'https://picsum.photos/seed/p2/200/200', 5999.00, 1),
    (10, 20, '海尔 热水器',       'https://picsum.photos/seed/p20/200/200', 1299.00, 1);

-- -------------------- 评价 --------------------
INSERT INTO `review` (user_id, product_id, order_id, rating, content) VALUES
    (1, 1, 1, 5, '非常好用，物流也很快！'),
    (2, 4, 2, 4, '性能强劲，发货速度一般'),
    (3, 11, 4, 5, '制冷效果很好，安静');

-- -------------------- 购物车 --------------------
INSERT INTO `cart_item` (user_id, product_id, quantity) VALUES
    (1, 3, 1),
    (1, 18, 1),
    (2, 5, 1),
    (3, 13, 2);

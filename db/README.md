# SQL 脚本使用说明

## 文件说明

| 文件 | 说明 |
|------|------|
| `01-schema.sql` | 建表脚本，包含 14 张表（幂等，可重复执行） |
| `02-seed.sql`   | 种子数据脚本，含管理员、商品、订单等演示数据 |

## 执行顺序

1. 先执行 `01-schema.sql`（会自动创建 `mall_admin` 库）
2. 再执行 `02-seed.sql`（导入种子数据）

```bash
mysql -u root -p < 01-schema.sql
mysql -u root -p < 02-seed.sql
```

或者在 MySQL 客户端（如 Navicat、DataGrip）中依次打开并执行。

## 默认账号

| 账号 | 密码 | 角色 |
|------|------|------|
| admin | 123456 | 平台管理员 |
| zhangsan | 123456 | 运营 |

> 种子脚本中演示了简单的 MD5 加密（仅 `username + "123456"`），实际项目请使用 BCrypt。

## 表结构概览

- **后台基础**：`user`、`role`、`dept`（RBAC）
- **单表业务**：`category`、`product`、`banner`、`coupon`
- **多表业务**：`c_user`、`address`、`cart_item`、`order`、`order_item`、`review`
- **审计**：`operate_log`

## 订单状态枚举

| 值 | 含义 |
|----|------|
| 0 | 待支付 |
| 1 | 待发货 |
| 2 | 待收货 |
| 3 | 已完成 |
| 4 | 已取消 |
| 5 | 退款中 |
| 6 | 已退款 |

# 电商后台管理系统 — 接口文档

> 基于 `mall-admin.openapi.json` 生成
> 版本：1.0.0 ｜ 后端：Spring Boot 3.5 + Java 17 + MyBatis-Plus + JWT

---

## 目录

1. [基础说明](#一基础说明)
2. [通用约定](#二通用约定)
3. [数据模型](#三数据模型)
4. [接口列表](#四接口列表)
  - [4.1 登录](#41-登录)
  - [4.2 操作员](#42-操作员)
  - [4.3 角色](#43-角色)
  - [4.4 部门](#44-部门)
  - [4.5 商品分类](#45-商品分类)
  - [4.6 商品](#46-商品)
  - [4.7 轮播图](#47-轮播图)
  - [4.8 优惠券](#48-优惠券)
  - [4.9 C 端用户](#49-c-端用户)
  - [4.10 收货地址](#410-收货地址)
  - [4.11 购物车](#411-购物车)
  - [4.12 订单](#412-订单)
  - [4.13 评价](#413-评价)
  - [4.14 数据概览](#414-数据概览)
  - [4.15 统计](#415-统计)
  - [4.16 操作日志](#416-操作日志)
  - [4.17 文件](#417-文件)
5. [附录：错误码](#五附录错误码)
6. [附录：枚举](#六附录枚举)

---

## 一、基础说明

### 服务器地址

| 环境 | 地址 |
|------|------|
| 本地开发（直连 Spring Boot） | `http://localhost:8080/api` |
| Nginx 反代 | `/api`（由 nginx 反代到 8080） |
| Vite dev proxy | `http://localhost:5173/api`（前端开发期） |

### 默认账号

| 账号 | 密码 | 角色 |
|------|------|------|
| `admin` | `123456` | 平台管理员（拥有全部权限） |
| `zhangsan` | `123456` | 运营 |

---

## 二、通用约定

### 1. 鉴权

- 除明确标注「无需 token」的接口外，**所有接口都需要在 Header 中传递 token**：

```
token: eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MS...
```

- token 由 `/login` 接口返回，类型为 JWT，载荷含 `id`、`username`，有效期 12 小时。
- 缺失 / 无效 / 过期 → 返回 `code: 401`。

### 2. 统一响应结构

```json
{
 "code": 1,
 "msg": "success",
 "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `code` | int | `1` = 成功，`0` = 失败，其他值 = 业务错误 |
| `msg`  | string | 提示信息（成功时为 `success`） |
| `data` | object / array / null | 业务数据 |

### 3. 分页接口约定

`data` 形如：

```json
{
 "total": 86,
 "rows": [ ... ]
}
```

分页请求参数（除特殊说明外，所有分页接口都支持）：

| 参数 | 类型 | 默认 | 说明 |
|------|------|------|------|
| `page` | int | 1 | 当前页（从 1 开始） |
| `pageSize` | int | 10 | 每页大小 |

### 4. HTTP 方法说明

| 方法 | 用途 |
|------|------|
| `GET` | 查询（带 query string） |
| `POST` | 新增（body 为 JSON） |
| `PUT` | 修改（body 为 JSON）或路径触发状态变更 |
| `DELETE` | 删除（路径传 id） |

### 5. 字符集

- 请求 / 响应统一 `UTF-8`
- 数据库连接 `useUnicode=true&characterEncoding=utf8`

---

## 三、数据模型

### 3.1 通用响应 `Result`

| 字段 | 类型 | 说明 |
|------|------|------|
| `code` | int | `1`=成功 `0`=失败 |
| `msg`  | string | 提示信息 |
| `data` | any | 业务数据 |

### 3.2 通用分页 `PageResult`

| 字段 | 类型 | 说明 |
|------|------|------|
| `total` | long | 总记录数 |
| `rows`  | array | 当前页数据列表 |

### 3.3 登录请求 `LoginDTO`

| 字段 | 必填 | 说明 |
|------|------|------|
| `username` |  | 用户名 |
| `password` |  | 密码（明文，传输时建议 HTTPS） |

### 3.4 登录响应 `LoginVO`

| 字段 | 类型 | 说明 |
|------|------|------|
| `userId` | long | 用户 ID |
| `username` | string | 用户名 |
| `name` | string | 姓名 |
| `avatar` | string | 头像 URL |
| `roleLabel` | string | 角色权限标识（如 `admin`） |
| `token` | string | JWT Token |
| `permissions` | string[] | 权限列表 |

### 3.5 操作员 `User`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 主键 |
| `username` | string | 登录名（唯一） |
| `password` | string | 密码（MD5，建议改为 BCrypt） |
| `name` | string | 姓名 |
| `phone` | string | 手机号 |
| `email` | string | 邮箱 |
| `avatar` | string | 头像 URL |
| `roleId` | long | 角色 ID |
| `deptId` | long | 部门 ID |
| `status` | int | `0`=禁用 `1`=启用 |
| `remark` | string | 备注 |
| `createTime` | datetime | 创建时间 |
| `updateTime` | datetime | 更新时间 |
| `roleName` | string | 角色名（关联展示，不存数据库） |
| `deptName` | string | 部门名（关联展示） |

### 3.6 角色 `Role`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 主键 |
| `name` | string | 角色名称 |
| `label` | string | 权限标识（如 `admin`、`operator`，唯一） |
| `remark` | string | 备注 |
| `createTime` | datetime | 创建时间 |
| `updateTime` | datetime | 更新时间 |

### 3.7 部门 `Dept`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 主键 |
| `name` | string | 部门名称 |
| `parentId` | long | 父部门 ID，`0` 表示顶级 |
| `sort` | int | 排序 |
| `status` | int | `0`=禁用 `1`=启用 |
| `createTime` | datetime | 创建时间 |
| `updateTime` | datetime | 更新时间 |
| `children` | Dept[] | 子部门（树形响应） |

### 3.8 商品分类 `Category`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 主键 |
| `name` | string | 分类名称 |
| `parentId` | long | 父分类 ID，`0`=顶级 |
| `icon` | string | 图标 URL |
| `sort` | int | 排序 |
| `status` | int | `0`=禁用 `1`=启用 |
| `createTime` | datetime | 创建时间 |
| `updateTime` | datetime | 更新时间 |
| `children` | Category[] | 子分类（树形响应） |

### 3.9 商品 `Product`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 主键 |
| `name` | string | 商品名称 |
| `categoryId` | long | 分类 ID |
| `categoryName` | string | 分类名（关联展示） |
| `cover` | string | 封面图 URL |
| `price` | decimal | 售价 |
| `stock` | int | 库存 |
| `sales` | int | 累计销量 |
| `status` | int | `0`=下架 `1`=上架 |
| `description` | string | 商品描述 |
| `createTime` | datetime | 创建时间 |
| `updateTime` | datetime | 更新时间 |

### 3.10 轮播图 `Banner`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 主键 |
| `title` | string | 标题 |
| `image` | string | 图片 URL |
| `link` | string | 跳转链接 |
| `sort` | int | 排序 |
| `status` | int | `0`=禁用 `1`=启用 |
| `createTime` | datetime | 创建时间 |
| `updateTime` | datetime | 更新时间 |

### 3.11 优惠券 `Coupon`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 主键 |
| `name` | string | 优惠券名称 |
| `type` | int | `1`=满减 `2`=折扣 |
| `discount` | decimal | 满减金额 / 折扣（如 0.85 表示 85 折） |
| `minAmount` | decimal | 使用门槛（满减才有） |
| `total` | int | 发行量 |
| `used` | int | 已使用数量 |
| `startTime` | datetime | 有效期开始 |
| `endTime` | datetime | 有效期结束 |
| `status` | int | `0`=禁用 `1`=启用 |
| `createTime` | datetime | 创建时间 |
| `updateTime` | datetime | 更新时间 |

### 3.12 C 端用户 `CUser`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 主键 |
| `openId` | string | 微信 openId |
| `nickname` | string | 昵称 |
| `phone` | string | 手机号 |
| `avatar` | string | 头像 URL |
| `gender` | int | `0`=未知 `1`=男 `2`=女 |
| `status` | int | `0`=禁用 `1`=启用 |
| `createTime` | datetime | 注册时间 |
| `updateTime` | datetime | 更新时间 |

### 3.13 收货地址 `Address`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 主键 |
| `userId` | long | C 端用户 ID |
| `receiver` | string | 收货人 |
| `phone` | string | 收货人手机号 |
| `province` | string | 省 |
| `city` | string | 市 |
| `district` | string | 区 |
| `detail` | string | 详细地址 |
| `isDefault` | int | `0`=否 `1`=默认 |
| `createTime` | datetime | 创建时间 |
| `updateTime` | datetime | 更新时间 |

### 3.14 购物车 `CartItem`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 主键 |
| `userId` | long | C 端用户 ID |
| `productId` | long | 商品 ID |
| `quantity` | int | 数量 |
| `createTime` | datetime | 加入时间 |
| `updateTime` | datetime | 更新时间 |
| `productName` | string | 商品名（关联） |
| `productImage` | string | 商品图（关联） |
| `productPrice` | decimal | 商品单价（关联） |
| `nickname` | string | 买家昵称（关联） |

### 3.15 订单 `Order`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 主键 |
| `orderNo` | string | 订单号 |
| `userId` | long | C 端用户 ID |
| `addressId` | long | 收货地址 ID |
| `totalAmount` | decimal | 订单金额 |
| `payAmount` | decimal | 实付金额 |
| `status` | int | 状态（详见枚举） |
| `payTime` | datetime | 支付时间 |
| `remark` | string | 备注 |
| `createTime` | datetime | 下单时间 |
| `updateTime` | datetime | 更新时间 |
| `nickname` | string | 买家昵称（分页列表） |
| `phone` | string | 买家手机号（分页列表） |
| `items` | OrderItem[] | 订单项（详情） |
| `user` | CUser | 买家信息（详情） |
| `address` | Address | 收货地址（详情） |

### 3.16 订单项 `OrderItem`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 主键 |
| `orderId` | long | 订单 ID |
| `productId` | long | 商品 ID |
| `productName` | string | 商品名（冗余） |
| `productImage` | string | 商品图（冗余） |
| `price` | decimal | 单价 |
| `quantity` | int | 数量 |

### 3.17 评价 `Review`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 主键 |
| `userId` | long | C 端用户 ID |
| `productId` | long | 商品 ID |
| `orderId` | long | 订单 ID |
| `rating` | int | 评分 `1~5` |
| `content` | string | 评价内容 |
| `createTime` | datetime | 评价时间 |
| `updateTime` | datetime | 更新时间 |
| `nickname` | string | 买家昵称（关联） |
| `productName` | string | 商品名（关联） |

### 3.18 数据概览 `OverviewVO`

| 字段 | 类型 | 说明 |
|------|------|------|
| `todaySales` | decimal | 今日销售额 |
| `todayOrders` | long | 今日订单数 |
| `todayNewUsers` | long | 今日新增用户 |
| `pendingShipOrders` | long | 待发货订单数 |
| `totalOrders` | long | 累计订单数 |
| `totalUsers` | long | 累计用户数 |
| `totalSales` | decimal | 累计销售额 |
| `onSaleProducts` | long | 在售商品数 |

### 3.19 操作日志 `OperateLog`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 主键 |
| `userId` | long | 操作人 ID |
| `username` | string | 操作人用户名 |
| `module` | string | 模块 |
| `type` | string | 操作类型（新增 / 修改 / 删除 …） |
| `className` | string | 类名 |
| `method` | string | 方法名 |
| `params` | text | 请求参数（JSON 字符串） |
| `result` | text | 返回结果（JSON 字符串） |
| `costTime` | long | 耗时（ms） |
| `ip` | string | IP 地址 |
| `createTime` | datetime | 时间 |

---

## 四、接口列表

> 接口按模块分组。所有接口基础路径：`/api`

---

### 4.1 登录

#### 4.1.1 登录

- **方法 / 路径**：`POST /login`
- **鉴权**：无需 token
- **请求体**（`LoginDTO`）：

```json
{
 "username": "admin",
 "password": "123456"
}
```

- **响应**（`data: LoginVO`）：

```json
{
 "code": 1,
 "msg": "success",
 "data": {
  "userId": 1,
  "username": "admin",
  "name": "超级管理员",
  "avatar": null,
  "roleLabel": "admin",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MS...",
  "permissions": ["*"]
 }
}
```

#### 4.1.2 退出登录

- **方法 / 路径**：`POST /users/logout`
- **鉴权**：需 token
- **请求体**：无
- **响应**：`{ "code": 1, "msg": "success", "data": null }`

---

### 4.2 操作员

#### 4.2.1 分页查询操作员

- **方法 / 路径**：`GET /users`
- **查询参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `page` | int | 否 | 默认 1 |
| `pageSize` | int | 否 | 默认 10 |
| `name` | string | 否 | 姓名（模糊） |
| `username` | string | 否 | 用户名（模糊） |
| `phone` | string | 否 | 手机号（模糊） |
| `deptId` | long | 否 | 部门 ID |
| `status` | int | 否 | `0`=禁用 `1`=启用 |

- **响应**：`data: PageResult<User>`，password 字段为 null。

#### 4.2.2 查询所有操作员（不分页）

- **方法 / 路径**：`GET /users/all`
- **响应**：`data: User[]`

#### 4.2.3 查询当前登录用户信息

- **方法 / 路径**：`GET /users/info`
- **响应**：`data: LoginVO`（无 token 字段）

#### 4.2.4 查询操作员

- **方法 / 路径**：`GET /users/{id}`
- **路径参数**：`id` long
- **响应**：`data: User`

#### 4.2.5 新增操作员

- **方法 / 路径**：`POST /users`
- **请求体**（`User`，password 留空则使用默认 `123456`）：

```json
{
 "username": "lisi",
 "password": "",
 "name": "李四",
 "phone": "13800000002",
 "email": "lisi@mall.com",
 "roleId": 2,
 "deptId": 2,
 "status": 1
}
```

- **响应**：`{ "code": 1, "msg": "success", "data": null }`

#### 4.2.6 修改操作员

- **方法 / 路径**：`PUT /users`
- **请求体**（`User`，必须带 id，password 留空表示不修改密码）：

```json
{
 "id": 2,
 "name": "李四（修改）",
 "phone": "13800000099",
 "status": 1
}
```

#### 4.2.7 删除操作员

- **方法 / 路径**：`DELETE /users/{id}`
- **路径参数**：`id` long
- **响应**：`{ "code": 1, "msg": "success", "data": null }`

---

### 4.3 角色

#### 4.3.1 分页查询角色

- **方法 / 路径**：`GET /roles`
- **查询参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `page` | int | 否 | 默认 1 |
| `pageSize` | int | 否 | 默认 10 |
| `name` | string | 否 | 角色名称（模糊） |
| `label` | string | 否 | 权限标识（模糊） |

- **响应**：`data: PageResult<Role>`

#### 4.3.2 查询所有角色（不分页）

- **方法 / 路径**：`GET /roles/all`
- **响应**：`data: Role[]`

#### 4.3.3 查询角色

- **方法 / 路径**：`GET /roles/{id}`
- **路径参数**：`id` long
- **响应**：`data: Role`

#### 4.3.4 新增角色

- **方法 / 路径**：`POST /roles`
- **请求体**：

```json
{
 "name": "客服",
 "label": "service",
 "remark": "处理售后"
}
```

#### 4.3.5 修改角色

- **方法 / 路径**：`PUT /roles`
- **请求体**：同新增，必须带 `id`

#### 4.3.6 删除角色

- **方法 / 路径**：`DELETE /roles/{id}`
- **路径参数**：`id` long

---

### 4.4 部门

#### 4.4.1 分页查询部门

- **方法 / 路径**：`GET /depts`
- **查询参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `page` | int | 否 | 默认 1 |
| `pageSize` | int | 否 | 默认 10 |
| `name` | string | 否 | 部门名称（模糊） |

- **响应**：`data: PageResult<Dept>`

#### 4.4.2 查询所有部门（不分页）

- **方法 / 路径**：`GET /depts/all`
- **响应**：`data: Dept[]`

#### 4.4.3 查询部门树

- **方法 / 路径**：`GET /depts/tree`
- **响应**：`data: Dept[]`（递归 children 字段）

```json
[
 {
  "id": 1,
  "name": "总公司",
  "parentId": 0,
  "sort": 1,
  "status": 1,
  "children": [
   { "id": 2, "name": "运营部", "parentId": 1, "children": [] },
   { "id": 3, "name": "技术部", "parentId": 1, "children": [] }
  ]
 }
]
```

#### 4.4.4 查询部门

- **方法 / 路径**：`GET /depts/{id}`
- **路径参数**：`id` long

#### 4.4.5 新增部门

- **方法 / 路径**：`POST /depts`
- **请求体**：

```json
{
 "parentId": 1,
 "name": "市场部",
 "sort": 3,
 "status": 1
}
```

#### 4.4.6 修改部门

- **方法 / 路径**：`PUT /depts`
- **请求体**：同新增，必须带 `id`

#### 4.4.7 删除部门

- **方法 / 路径**：`DELETE /depts/{id}`
- **路径参数**：`id` long

---

### 4.5 商品分类

#### 4.5.1 分页查询分类

- **方法 / 路径**：`GET /categories`
- **查询参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `page` | int | 否 | 默认 1 |
| `pageSize` | int | 否 | 默认 10 |
| `name` | string | 否 | 分类名称（模糊） |
| `parentId` | long | 否 | 父分类 ID |

- **响应**：`data: PageResult<Category>`

#### 4.5.2 查询所有分类（不分页）

- **方法 / 路径**：`GET /categories/all`
- **响应**：`data: Category[]`

#### 4.5.3 查询分类树

- **方法 / 路径**：`GET /categories/tree`
- **响应**：`data: Category[]`（嵌套 children）

#### 4.5.4 查询分类

- **方法 / 路径**：`GET /categories/{id}`
- **路径参数**：`id` long

#### 4.5.5 新增分类

- **方法 / 路径**：`POST /categories`
- **请求体**：

```json
{
 "parentId": 1,
 "name": "智能手表",
 "icon": "https://example.com/icon.png",
 "sort": 1,
 "status": 1
}
```

#### 4.5.6 修改分类

- **方法 / 路径**：`PUT /categories`
- **请求体**：必须带 `id`

#### 4.5.7 删除分类

- **方法 / 路径**：`DELETE /categories/{id}`
- **路径参数**：`id` long

---

### 4.6 商品

#### 4.6.1 分页查询商品

- **方法 / 路径**：`GET /products`
- **查询参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `page` | int | 否 | 默认 1 |
| `pageSize` | int | 否 | 默认 10 |
| `name` | string | 否 | 商品名称（模糊） |
| `categoryId` | long | 否 | 分类 ID |
| `status` | int | 否 | `0`=下架 `1`=上架 |

- **响应**：`data: PageResult<Product>`（含 `categoryName`）

#### 4.6.2 查询商品

- **方法 / 路径**：`GET /products/{id}`
- **路径参数**：`id` long
- **响应**：`data: Product`

#### 4.6.3 新增商品

- **方法 / 路径**：`POST /products`
- **请求体**：

```json
{
 "name": "小米 14 Ultra",
 "categoryId": 2,
 "cover": "https://picsum.photos/seed/p100/200/200",
 "price": 6499.00,
 "stock": 100,
 "status": 1,
 "description": "徕卡光学 Summilux 镜头"
}
```

#### 4.6.4 修改商品

- **方法 / 路径**：`PUT /products`
- **请求体**：必须带 `id`

#### 4.6.5 删除商品

- **方法 / 路径**：`DELETE /products/{id}`
- **路径参数**：`id` long

#### 4.6.6 商品上下架

- **方法 / 路径**：`PUT /products/status/{id}/{status}`
- **路径参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | long | 商品 ID |
| `status` | int | `0`=下架 `1`=上架 |

---

### 4.7 轮播图

#### 4.7.1 分页查询轮播图

- **方法 / 路径**：`GET /banners`
- **查询参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `page` | int | 否 | 默认 1 |
| `pageSize` | int | 否 | 默认 10 |
| `title` | string | 否 | 标题（模糊） |

- **响应**：`data: PageResult<Banner>`

#### 4.7.2 查询所有启用轮播图

- **方法 / 路径**：`GET /banners/all`
- **响应**：`data: Banner[]`（仅 status=1，按 sort 排序）

#### 4.7.3 新增轮播图

- **方法 / 路径**：`POST /banners`
- **请求体**：

```json
{
 "title": "双 11 大促",
 "image": "https://picsum.photos/seed/b1/1200/400",
 "link": "/activity/double11",
 "sort": 1,
 "status": 1
}
```

#### 4.7.4 修改轮播图

- **方法 / 路径**：`PUT /banners`
- **请求体**：必须带 `id`

#### 4.7.5 删除轮播图

- **方法 / 路径**：`DELETE /banners/{id}`
- **路径参数**：`id` long

---

### 4.8 优惠券

#### 4.8.1 分页查询优惠券

- **方法 / 路径**：`GET /coupons`
- **查询参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `page` | int | 否 | 默认 1 |
| `pageSize` | int | 否 | 默认 10 |
| `name` | string | 否 | 名称（模糊） |
| `type` | int | 否 | `1`=满减 `2`=折扣 |
| `status` | int | 否 | `0`=禁用 `1`=启用 |

- **响应**：`data: PageResult<Coupon>`

#### 4.8.2 查询优惠券

- **方法 / 路径**：`GET /coupons/{id}`
- **路径参数**：`id` long

#### 4.8.3 新增优惠券

- **方法 / 路径**：`POST /coupons`
- **请求体**：

```json
{
 "name": "满 1000 减 100",
 "type": 1,
 "discount": 100.00,
 "minAmount": 1000.00,
 "total": 1000,
 "used": 0,
 "startTime": "2026-08-01 00:00:00",
 "endTime": "2026-12-31 23:59:59",
 "status": 1
}
```

#### 4.8.4 修改优惠券

- **方法 / 路径**：`PUT /coupons`
- **请求体**：必须带 `id`

#### 4.8.5 删除优惠券

- **方法 / 路径**：`DELETE /coupons/{id}`
- **路径参数**：`id` long

---

### 4.9 C 端用户

#### 4.9.1 分页查询 C 端用户

- **方法 / 路径**：`GET /c-users`
- **查询参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `page` | int | 否 | 默认 1 |
| `pageSize` | int | 否 | 默认 10 |
| `nickname` | string | 否 | 昵称（模糊） |
| `phone` | string | 否 | 手机号（模糊） |
| `status` | int | 否 | `0`=禁用 `1`=启用 |

- **响应**：`data: PageResult<CUser>`

#### 4.9.2 查询 C 端用户

- **方法 / 路径**：`GET /c-users/{id}`
- **路径参数**：`id` long
- **响应**：`data: CUser`

---

### 4.10 收货地址

#### 4.10.1 查询用户的所有地址

- **方法 / 路径**：`GET /addresses/by-user/{userId}`
- **路径参数**：`userId` long
- **响应**：`data: Address[]`（按默认优先 + 创建时间倒序）

---

### 4.11 购物车

#### 4.11.1 分页查询购物车

- **方法 / 路径**：`GET /cart-items`
- **查询参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `page` | int | 否 | 默认 1 |
| `pageSize` | int | 否 | 默认 10 |

- **响应**：`data: PageResult<CartItem>`（含 `productName`、`productImage`、`productPrice`、`nickname` 关联字段）

---

### 4.12 订单

#### 4.12.1 分页查询订单

- **方法 / 路径**：`GET /orders`
- **查询参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `page` | int | 否 | 默认 1 |
| `pageSize` | int | 否 | 默认 10 |
| `orderNo` | string | 否 | 订单号（模糊） |
| `phone` | string | 否 | 买家手机号（模糊） |
| `status` | int | 否 | 订单状态 |
| `startDate` | date | 否 | 起始日期（`yyyy-MM-dd`） |
| `endDate` | date | 否 | 结束日期（`yyyy-MM-dd`） |

- **响应**：`data: PageResult<Order>`（含 `nickname`、`phone`）

#### 4.12.2 订单详情

- **方法 / 路径**：`GET /orders/{id}`
- **路径参数**：`id` long
- **响应**：`data: Order`（含完整 `items`、`user`、`address`）

```json
{
 "code": 1,
 "msg": "success",
 "data": {
  "id": 1,
  "orderNo": "OD202608010001",
  "userId": 1,
  "addressId": 1,
  "totalAmount": 7999.00,
  "payAmount": 7899.00,
  "status": 3,
  "payTime": "2026-08-01T10:30:00",
  "remark": "尽快发货",
  "createTime": "2026-08-01T10:00:00",
  "items": [
   {
    "id": 1,
    "orderId": 1,
    "productId": 1,
    "productName": "iPhone 15 Pro",
    "productImage": "https://picsum.photos/seed/p1/200/200",
    "price": 7999.00,
    "quantity": 1
   }
  ],
  "user": {
   "id": 1,
   "nickname": "小明",
   "phone": "13900000001",
   ...
  },
  "address": {
   "id": 1,
   "receiver": "王明",
   "phone": "13900000001",
   "province": "北京市",
   "city": "北京市",
   "district": "朝阳区",
   "detail": "建国路 88 号",
   ...
  }
 }
}
```

#### 4.12.3 订单发货

- **方法 / 路径**：`PUT /orders/ship/{id}`
- **路径参数**：`id` long
- **业务规则**：仅「待发货 (status=1)」可执行，会变为「待收货 (status=2)」

#### 4.12.4 取消订单

- **方法 / 路径**：`PUT /orders/cancel/{id}`
- **路径参数**：`id` long
- **业务规则**：仅「待支付 (0)」或「待发货 (1)」可取消 → 变为「已取消 (4)」

#### 4.12.5 订单退款

- **方法 / 路径**：`PUT /orders/refund/{id}`
- **路径参数**：`id` long
- **业务规则**：仅「待收货 (2)」或「已完成 (3)」可退款 → 变为「已退款 (6)」

---

### 4.13 评价

#### 4.13.1 分页查询评价

- **方法 / 路径**：`GET /reviews`
- **查询参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `page` | int | 否 | 默认 1 |
| `pageSize` | int | 否 | 默认 10 |
| `productId` | long | 否 | 商品 ID |
| `rating` | int | 否 | 评分（1~5） |

- **响应**：`data: PageResult<Review>`（含 `nickname`、`productName`）

#### 4.13.2 查询评价

- **方法 / 路径**：`GET /reviews/{id}`
- **路径参数**：`id` long
- **响应**：`data: Review`

#### 4.13.3 删除评价

- **方法 / 路径**：`DELETE /reviews/{id}`
- **路径参数**：`id` long

---

### 4.14 数据概览

#### 4.14.1 首页数据概览

- **方法 / 路径**：`GET /stats/overview`
- **响应**：`data: OverviewVO`

```json
{
 "code": 1,
 "msg": "success",
 "data": {
  "todaySales": 4897.0,
  "todayOrders": 8,
  "todayNewUsers": 3,
  "pendingShipOrders": 2,
  "totalOrders": 10,
  "totalUsers": 3,
  "totalSales": 27695.0,
  "onSaleProducts": 20
 }
}
```

---

### 4.15 统计

#### 4.15.1 销售趋势（按日统计销售总价和总数量）

- **方法 / 路径**：`GET /stats/sales-trend`
- **查询参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `startDate` | date | 否 | 起始日期 |
| `endDate` | date | 否 | 结束日期 |

- **响应**：`data: { date: string, amount: decimal, count: long }[]`
- **业务规则**：仅统计已完成 (status=2/3) 的订单

```java
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "date": "2026-08-01",
      "amount": 22798.00,
      "count": 2
    },
    {
      "date": "2026-08-02",
      "amount": 10896.00,
      "count": 4
    }
  ]
}
```



#### 4.15.2 商品销量排行

- **方法 / 路径**：`GET /stats/product-ranking`
- **查询参数**：

| 参数 | 类型 | 必填 | 默认 | 说明 |
|------|------|------|------|------|
| `topN` | int | 否 | 10 | TOP N |

- **响应**：`data: { productId, productName, productImage, totalQty, totalAmount }[]`

例如:

```java
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "productId": 11,
      "productName": "美的 空调",
      "productImage": "https://picsum.photos/seed/p11/200/200",
      "totalQty": 1,
      "totalAmount": 2999.00
    },
    {
      "productId": 2,
      "productName": "华为 Mate60",
      "productImage": "https://picsum.photos/seed/p2/200/200",
      "totalQty": 1,
      "totalAmount": 5999.00
    },
    {
      "productId": 20,
      "productName": "海尔 热水器",
      "productImage": "https://picsum.photos/seed/p20/200/200",
      "totalQty": 1,
      "totalAmount": 1299.00
    },
    {
      "productId": 1,
      "productName": "iPhone 15 Pro",
      "productImage": "https://picsum.photos/seed/p1/200/200",
      "totalQty": 1,
      "totalAmount": 7999.00
    },
    {
      "productId": 4,
      "productName": "MacBook Pro 14",
      "productImage": "https://picsum.photos/seed/p4/200/200",
      "totalQty": 1,
      "totalAmount": 14999.00
    },
    {
      "productId": 7,
      "productName": "Nike 跑步鞋",
      "productImage": "https://picsum.photos/seed/p7/200/200",
      "totalQty": 1,
      "totalAmount": 699.00
    }
  ]
}
```



#### 4.15.3 分类销售占比

- **方法 / 路径**：`GET /stats/category-share`
- **响应**：`data: { categoryId, name, amount }[]`

例如:

```java
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "categoryId": 3,
      "name": "笔记本",
      "amount": 14999.00
    },
    {
      "categoryId": 2,
      "name": "手机",
      "amount": 13998.00
    },
    {
      "categoryId": 5,
      "name": "家用电器",
      "amount": 4298.00
    },
    {
      "categoryId": 4,
      "name": "服装鞋帽",
      "amount": 699.00
    }
  ]
}
```



#### 4.15.4 订单状态分布

- **方法 / 路径**：`GET /stats/order-status`

- **响应**：`data: { status: int, name: string, count: long }[]`

  

例如:

```java
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "status": 0,
      "name": "待支付",
      "count": 2
    },
    {
      "status": 1,
      "name": "待发货",
      "count": 1
    },
    {
      "status": 2,
      "name": "待收货",
      "count": 3
    },
    {
      "status": 3,
      "name": "已完成",
      "count": 3
    },
    {
      "status": 4,
      "name": "已取消",
      "count": 1
    }
  ]
}
```



---

### 4.16 操作日志

#### 4.16.1 分页查询操作日志

- **方法 / 路径**：`GET /operate-logs`
- **查询参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `page` | int | 否 | 默认 1 |
| `pageSize` | int | 否 | 默认 10 |
| `username` | string | 否 | 操作人（模糊） |
| `module` | string | 否 | 模块（模糊） |

- **响应**：`data: PageResult<OperateLog>`
- **业务说明**：通过 `@LogOperation` 注解自动写入，支持 controller 方法粒度

---

### 4.17 文件

#### 4.17.1 文件上传

- **方法 / 路径**：`POST /upload`
- **鉴权**：无需 token（已在拦截器白名单）
- **请求**：`multipart/form-data`，字段名 `file`
- **响应**：`data: string`（文件路径，如 `/uploads/2026/08/02/abc.jpg`）
- **存储位置**：`D:/mall-admin/uploads/yyyy/MM/dd/`
- **访问 URL**：`http://localhost:8080/api/uploads/yyyy/MM/dd/abc.jpg`（WebConfig 静态映射）

---

## 五、附录：错误码

| code | 含义 | 说明 |
|------|------|------|
| 1 | 成功 | 业务处理成功 |
| 0 | 业务失败 | 见 `msg` 字段 |
| 401 | 未登录 / token 无效 | 缺失或过期的 JWT |
| 500 | 系统异常 | 见 `msg` 字段（含异常 message） |

---

## 六、附录：枚举

### 6.1 订单状态

| 值 | 名称 | 说明 |
|----|------|------|
| 0 | 待支付 | 等待用户付款 |
| 1 | 待发货 | 已支付，待商家发货 |
| 2 | 待收货 | 已发货，待用户确认 |
| 3 | 已完成 | 用户已确认收货 |
| 4 | 已取消 | 订单被取消 |
| 5 | 退款中 | 申请了退款 |
| 6 | 已退款 | 退款完成 |

### 6.2 用户/商品状态

| 值 | 含义 |
|----|------|
| 0 | 禁用 / 下架 |
| 1 | 启用 / 上架 |

### 6.3 优惠券类型

| 值 | 含义 | discount 字段含义 |
|----|------|-------------------|
| 1 | 满减 | 满 `minAmount` 减 `discount` 元 |
| 2 | 折扣 | 按 `discount` 比例打折（如 0.85 = 85 折） |

### 6.4 评价评分

| 值 | 含义 |
|----|------|
| 1 | 差评 |
| 2 | 不满意 |
| 3 | 一般 |
| 4 | 满意 |
| 5 | 非常满意 |

### 6.5 性别

| 值 | 含义 |
|----|------|
| 0 | 未知 |
| 1 | 男 |
| 2 | 女 |

### 6.6 部门/分类 parentId

| 值 | 含义 |
|----|------|
| 0 | 顶级（无父级） |
| >0 | 子级，值为父级 ID |

---

## 附：接口总览表

| # | 模块 | 方法 | 路径 | 鉴权 |
|---|------|------|------|------|
| 1 | 登录 | POST | `/login` | |
| 2 | 登录 | POST | `/users/logout` | |
| 3 | 操作员 | GET | `/users` | |
| 4 | 操作员 | GET | `/users/all` | |
| 5 | 操作员 | GET | `/users/info` | |
| 6 | 操作员 | GET | `/users/{id}` | |
| 7 | 操作员 | POST | `/users` | |
| 8 | 操作员 | PUT | `/users` | |
| 9 | 操作员 | DELETE | `/users/{id}` | |
| 10 | 角色 | GET | `/roles` | |
| 11 | 角色 | GET | `/roles/all` | |
| 12 | 角色 | GET | `/roles/{id}` | |
| 13 | 角色 | POST | `/roles` | |
| 14 | 角色 | PUT | `/roles` | |
| 15 | 角色 | DELETE | `/roles/{id}` | |
| 16 | 部门 | GET | `/depts` | |
| 17 | 部门 | GET | `/depts/all` | |
| 18 | 部门 | GET | `/depts/tree` | |
| 19 | 部门 | GET | `/depts/{id}` | |
| 20 | 部门 | POST | `/depts` | |
| 21 | 部门 | PUT | `/depts` | |
| 22 | 部门 | DELETE | `/depts/{id}` | |
| 23 | 分类 | GET | `/categories` | |
| 24 | 分类 | GET | `/categories/all` | |
| 25 | 分类 | GET | `/categories/tree` | |
| 26 | 分类 | GET | `/categories/{id}` | |
| 27 | 分类 | POST | `/categories` | |
| 28 | 分类 | PUT | `/categories` | |
| 29 | 分类 | DELETE | `/categories/{id}` | |
| 30 | 商品 | GET | `/products` | |
| 31 | 商品 | GET | `/products/{id}` | |
| 32 | 商品 | POST | `/products` | |
| 33 | 商品 | PUT | `/products` | |
| 34 | 商品 | DELETE | `/products/{id}` | |
| 35 | 商品 | PUT | `/products/status/{id}/{status}` | |
| 36 | 轮播图 | GET | `/banners` | |
| 37 | 轮播图 | GET | `/banners/all` | |
| 38 | 轮播图 | POST | `/banners` | |
| 39 | 轮播图 | PUT | `/banners` | |
| 40 | 轮播图 | DELETE | `/banners/{id}` | |
| 41 | 优惠券 | GET | `/coupons` | |
| 42 | 优惠券 | GET | `/coupons/{id}` | |
| 43 | 优惠券 | POST | `/coupons` | |
| 44 | 优惠券 | PUT | `/coupons` | |
| 45 | 优惠券 | DELETE | `/coupons/{id}` | |
| 46 | C 端用户 | GET | `/c-users` | |
| 47 | C 端用户 | GET | `/c-users/{id}` | |
| 48 | 收货地址 | GET | `/addresses/by-user/{userId}` | |
| 49 | 购物车 | GET | `/cart-items` | |
| 50 | 订单 | GET | `/orders` | |
| 51 | 订单 | GET | `/orders/{id}` | |
| 52 | 订单 | PUT | `/orders/ship/{id}` | |
| 53 | 订单 | PUT | `/orders/cancel/{id}` | |
| 54 | 订单 | PUT | `/orders/refund/{id}` | |
| 55 | 评价 | GET | `/reviews` | |
| 56 | 评价 | GET | `/reviews/{id}` | |
| 57 | 评价 | DELETE | `/reviews/{id}` | |
| 58 | 数据概览 | GET | `/stats/overview` | |
| 59 | 统计 | GET | `/stats/sales-trend` | |
| 60 | 统计 | GET | `/stats/product-ranking` | |
| 61 | 统计 | GET | `/stats/category-share` | |
| 62 | 统计 | GET | `/stats/order-status` | |
| 63 | 操作日志 | GET | `/operate-logs` | |
| 64 | 文件 | POST | `/upload` | |

**合计 64 个接口**（其中需鉴权 62 个，无需鉴权 2 个）。